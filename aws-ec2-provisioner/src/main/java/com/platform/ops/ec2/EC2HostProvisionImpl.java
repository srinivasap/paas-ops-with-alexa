package com.platform.ops.ec2;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.platform.ops.HostProvision;
import com.platform.ops.ec2.exception.HostProvisioningFailedException;
import com.platform.ops.model.Host;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Manages the host provisioning requests.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public class EC2HostProvisionImpl implements HostProvision {

    private AWSCredentials awsCreds;
    private AmazonEC2 ec2Client;
    AmazonCloudWatch cloudWatch;

    public void init(String accessKey, String secretKey) {
        awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        ec2Client = AmazonEC2ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.US_WEST_2)
                    .build();
        cloudWatch = AmazonCloudWatchClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.US_WEST_2)
                    .build();
    }

    @Override
    public Host createInstance(Host host) throws HostProvisioningFailedException {
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
                .withImageId(host.getImageId())
                .withInstanceType(host.getInstanceType())
                .withMinCount(host.getCount())
                .withMaxCount(host.getCount())
                .withKeyName(host.getKeypair())
                .withSecurityGroups(host.getSecurityGroup());

        RunInstancesResult runInstancesResult = ec2Client.runInstances(runInstancesRequest);

        for (Instance instance : runInstancesResult.getReservation().getInstances()) {
            String instanceId = instance.getInstanceId();
            // tag instance
            CreateTagsRequest createTagsRequest = new CreateTagsRequest()
                    .withResources(instanceId)
                    .withTags(new Tag("Name", host.getImageTag()));
            ec2Client.createTags(createTagsRequest);
            // start instance
            startInstance(instanceId);
        }

        return host;
    }

    @Override
    public Host resizeCluster(Host host) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        List<Instance> instances = new ArrayList<>();
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                // 16 -- running
                if (instance.getState().getCode() == 16) {
                    instances.add(instance);
                }
            }
        }
        if (instances.size() > host.getCount()) {
            int tobeTerminatedCount = instances.size() - host.getCount();
            for (int index = 0; index < instances.size() && tobeTerminatedCount > 0; index++, tobeTerminatedCount--) {
                TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest().withInstanceIds(instances.get(index).getInstanceId());
                ec2Client.terminateInstances(terminateInstancesRequest);
            }
            host.setMessage("successfully terminated "+(instances.size() - host.getCount())+
                            " "+pluralify("instance", instances.size() - host.getCount())+
                            " tagged as "+host.getInstanceTag()+" to bring cluster size to "+host.getCount());
        } else if (instances.size() < host.getCount()) {
            int tobeAddedCount = host.getCount() - instances.size();
            host.setCount(tobeAddedCount);
            createInstance(host);
            host.setMessage("successfully added "+tobeAddedCount+" "+pluralify("instance", tobeAddedCount)+" tagged as "+host.getInstanceTag()+" to bring cluster size to "+host.getCount());
        } else {
            host.setMessage(host.getInstanceTag()+" cluster is already at "+host.getCount()+", no action was taken");
        }

        return host;
    }

    @Override
    public Host destroyInstance(Host host) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        int count = host.getCount();
        int terminatedCount = 0;
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                // 48 -- terminated
                if (instance.getState().getCode() != 48 && count > 0) {
                    count--;
                    terminatedCount++;
                    TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest().withInstanceIds(instance.getInstanceId());
                    ec2Client.terminateInstances(terminateInstancesRequest);
                }
            }
        }
        host.setMessage("successfully terminated "+terminatedCount+" "+pluralify("instance", count)+" tagged as "+host.getInstanceTag());
        host.setCount(count);
        return host;
    }

    @Override
    public Host listInstances(Host host, boolean filterPending) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        List<Instance> instances = new ArrayList();
        Map<String, Integer> instancesByState = new HashMap<>();
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                if (instancesByState.get(instance.getState().getName()) == null) {
                    instancesByState.put(instance.getState().getName(), 0);
                }
                int instanceCount = instancesByState.get(instance.getState().getName()).intValue();
                instancesByState.put(instance.getState().getName(), ++instanceCount);
                instances.add(instance);
            }
        }
        String message = "";
        if (filterPending) {
            String details = "";
            for (Map.Entry<String, Integer> entry : instancesByState.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("pending")
                        || entry.getKey().equalsIgnoreCase("shutting-down")
                        || entry.getKey().equalsIgnoreCase("stopping")) {
                    details += entry.getValue()+" in "+entry.getKey()+ " state, ";
                }
            }
            if (details.isEmpty()) {
                message += "found no instance in pending state";
            } else {
                message += "found "+details;
            }
        } else {
            message += "total "+instances.size()+" "+pluralify("instance", instances.size())+
                      " tagged as " + host.getInstanceTag() + " with flavor "+ host.getInstanceType() +
                      " in region " + host.getAvailabilityZone();
            if (instances.size() > 0) {
                message += ", of which ";
                for (Map.Entry<String, Integer> entry : instancesByState.entrySet()) {
                    message += entry.getValue() + " in "+ entry.getKey() + " state, ";
                }
            }
        }

        host.setMessage(message);
        return host;
    }

    @Override
    public Host stopInstance(Host host) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        int count = 0;
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                // 16 -- running
                if (instance.getState().getCode() == 16) {
                    count++;
                    stopInstane(instance.getInstanceId());
                }
            }
        }
        host.setMessage("successfully stopped "+count+" "+pluralify("instance", count)+" tagged as "+host.getInstanceTag());
        host.setCount(count);
        return host;
    }

    private boolean stopInstane(String instanceId) {
        StopInstancesRequest stopInstanceRequest = new StopInstancesRequest().withInstanceIds(instanceId);
        ec2Client.stopInstances(stopInstanceRequest);
        return true;
    }

    @Override
    public Host startInstance(Host host) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        int count = 0;
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                // 80 -- stopped
                if (instance.getState().getCode() == 80) {
                    count++;
                    startInstance(instance.getInstanceId());
                }
            }
        }
        host.setMessage("successfully started "+count+" "+pluralify("instance", count)+" tagged as "+host.getInstanceTag());
        host.setCount(count);
        return host;
    }

    private boolean startInstance(String instanceId) {
        StartInstancesRequest startInstancesRequest = new StartInstancesRequest().withInstanceIds(instanceId);
        ec2Client.startInstances(startInstancesRequest);
        return true;
    }

    @Override
    public Host restartInstance(Host host, boolean hard) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        int count = 0;
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                // reboot only instances in stopped or running state
                if (instance.getState().getCode() == 80 || instance.getState().getCode() == 16) {
                    count++;
                    RebootInstancesRequest rebootInstancesRequest = new RebootInstancesRequest().withInstanceIds(instance.getInstanceId());
                    ec2Client.rebootInstances(rebootInstancesRequest);
                }

            }
        }
        host.setMessage("successfully restarted "+count+" "+pluralify("instance", count)+" tagged as "+host.getInstanceTag());
        host.setCount(count);
        return host;
    }

    @Override
    public Host queryMetrics(Host host, String param) throws HostProvisioningFailedException {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> instanceTags = new ArrayList<>();
        instanceTags.add(host.getInstanceTag());
        Filter filter = new Filter("tag:Name", instanceTags);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));
        List<Reservation> reservations = result.getReservations();
        int count = 0;
        Instance someRandomInstance = null;
        for (Reservation reservation : reservations) {
            for (Instance instance : reservation.getInstances()) {
                // 16 -- running
                if (instance.getState().getCode() == 16) {
                    count++;
                    someRandomInstance = instance;
                    MonitorInstancesRequest request1 = new MonitorInstancesRequest().withInstanceIds(instance.getInstanceId());
                    ec2Client.monitorInstances(request1);
                }
            }
        }

        if (someRandomInstance == null) {
            host.setMessage("found no instance in running state that is tagged as "+host.getInstanceTag()+" to query "+param);
            host.setCount(count);
            return host;
        }

        if ("size".equalsIgnoreCase(param)) {
            host.setMessage(host.getInstanceTag()+" cluster size is "+count);
            return host;
        } else if ("type".equalsIgnoreCase(param)) {
            host.setMessage(host.getInstanceTag()+" cluster is built on "+someRandomInstance.getInstanceType());
            return host;
        } else if (!"cpu".equalsIgnoreCase(param)) {
            host.setMessage("querying param "+param+
                            " is currently not supported, instead you may query for cluster size, instance type or instance cpu utilization");
            return host;
        }

        final long sinceLastDay = 1000 * 60 * 60 * 24;
        final int oneHrPeriod = 60 * 60;
        GetMetricStatisticsRequest metricStatisticsRequest =
                new GetMetricStatisticsRequest()
                        .withStartTime(new Date(new Date().getTime() - sinceLastDay))
                        .withNamespace("AWS/EC2")
                        .withPeriod(oneHrPeriod)
                        .withDimensions(new Dimension().withName("InstanceId").withValue(someRandomInstance.getInstanceId()))
                        .withMetricName("CPUUtilization")
                        .withStatistics("Average", "Maximum")
                        .withEndTime(new Date());

        GetMetricStatisticsResult metricStatisticsResult = cloudWatch.getMetricStatistics(metricStatisticsRequest);
        for (Datapoint dataPoint : metricStatisticsResult.getDatapoints()) {
            DecimalFormat decimalFormat = new DecimalFormat(".##");
            host.setMessage("Average CPU utilization of an instance tagged as "+host.getInstanceTag()+
                            " is "+decimalFormat.format(dataPoint.getAverage()));
        }

        host.setCount(count);
        return host;
    }

    private String pluralify(String word, int count) {
        String utterance = word;
        if (count > 1) {
            utterance = word+"s";
        }
        return utterance;
    }

}
