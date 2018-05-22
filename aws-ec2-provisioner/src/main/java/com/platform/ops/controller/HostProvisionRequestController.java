package com.platform.ops.controller;


import com.platform.ops.HostProvision;
import com.platform.ops.HostProvisionImplFactory;
import com.platform.ops.model.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller that handles provisioning request.
 *
 * @author Srinivasa Prasad Sunnapu
 */
@RestController
public class HostProvisionRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(HostProvisionRequestController.class);

    @Autowired
    private Host host;

    private HostProvision hostProvision;

    @Value("${aws.creds.access.key:xxxx}")
    private String accessKey;

    @Value("${aws.creds.secret.key:yyyy}")
    private String secretKey;

    @RequestMapping(
            value = "/provision/hosts/create",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String createHosts(
            @RequestParam(value="image_tag", defaultValue="app") String imageTag,
            @RequestParam(value="count", defaultValue="1") int count) {

        String message = "successfully created "+count+" instances using image "+imageTag;

        host.setImageTag(imageTag);
        host.setCount(count);

        try {
            hostProvision = HostProvisionImplFactory.EC2.getInstance(accessKey, secretKey);
            hostProvision.createInstance(host);
        } catch (Exception e) {
            LOG.error("failed to create "+count+" instances using image "+imageTag, e);
            message = "failed to create "+count+" instances using image "+imageTag+
                      ", do you wan retry? You can say yes or no.";
        }

        return message;
    }

    // action - start, stop, restart, terminate
    @RequestMapping(
            value = "/provision/hosts/state",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String hostsStateAction(
            @RequestParam(value="host_tag", defaultValue="app") String hostTag,
            @RequestParam(value="action", defaultValue="") String action,
            @RequestParam(value="count", defaultValue="") int count) {

        String message = "successfully "+action+"ed instances with tag "+hostTag;

        host.setInstanceTag(hostTag);
        host.setAction(action);
        host.setCount(count);

        try {
            hostProvision = HostProvisionImplFactory.EC2.getInstance(accessKey, secretKey);
            switch (action.toLowerCase()) {
                case "start":
                    hostProvision.startInstance(host);
                    message = host.getMessage();
                    break;
                case "stop":
                    hostProvision.stopInstance(host);
                    message = host.getMessage();
                    break;
                case "restart":
                    hostProvision.restartInstance(host, false);
                    message = host.getMessage();;
                    break;
                case "terminate":
                    hostProvision.destroyInstance(host);
                    message = host.getMessage();
                    break;
                case "resize":
                    hostProvision.resizeCluster(host);
                    message = host.getMessage();
                    break;
                default:
                    message = "unsupported action "+action+", supported actions are start, stop, restart, terminate.";
            }
        } catch (Exception e) {
            LOG.error("failed to "+action+" instances with tag "+hostTag, e);
            message = "failed to "+action+" instances with tag "+hostTag+
                      ", would you like to retry?";
        }

        return message;
    }

    @RequestMapping(
            value = "/provision/hosts/info",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String getHostsInfo(@RequestParam(value="host_tag", defaultValue="app") String hostTag,
                               @RequestParam(value="filter", defaultValue="") String filter) {

        String message = "invoked instance information query operation";
        host.setInstanceTag(hostTag);

        try {
            hostProvision = HostProvisionImplFactory.EC2.getInstance(accessKey, secretKey);
            hostProvision.listInstances(host, "pending".equalsIgnoreCase(filter));
            message = host.getMessage();
        } catch (Exception e) {
            LOG.error("failed to obtain information about instances with tag "+hostTag, e);
            message = "failed to obtain information about instances with tag "+hostTag;
        }

        return message;
    }

    // param - cpu
    @RequestMapping(
            value = "/provision/hosts/query",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String queryHostsAttributes(
            @RequestParam(value="host_tag", defaultValue="app") String hostTag,
            @RequestParam(value="param", defaultValue="cpu") String param) {

        String message = "invoked instance metrics query operation";
        host.setInstanceTag(hostTag);
        try {
            hostProvision = HostProvisionImplFactory.EC2.getInstance(accessKey, secretKey);
            hostProvision.queryMetrics(host, param);
            message = host.getMessage();
        } catch (Exception e) {
            LOG.error("failed to query for metrics "+param+" for instances tagged as "+hostTag, e);
            message = "failed to query for metrics "+param+" for instances tagged as "+hostTag;
        }

        return message;
    }

}
