package com.platform.ops.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Model object representing host attributes.
 *
 * @author Srinivasa Prasad Sunnapu
 */
@Component
@ConfigurationProperties(prefix = "aws")
public class Host {

    private String instanceId;

    @Value("${aws.ec2.instance.type:t2.medium}")
    private String instanceType;

    private String instanceState;

    private String instanceStateReason;

    private String instanceTag;

    @Value("${aws.ec2.instance.keypair:srinivasaprasad-aws}")
    private String keypair;

    @Value("${aws.ec2.instance.security.group:app-sg}")
    private String securityGroup;

    private Date launchTime;

    private int count;

    @Value("${aws.ec2.image.id:ami-a73741df}")
    private String imageId;

    @Value("${aws.ec2.image.tag:app}")
    private String imageTag;

    @Value("${aws.ec2.availability.zone:us-west-2}")
    private String availabilityZone;

    private double cpu;

    private double tps;

    private String message;

    private String action;

    public Host() {}

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(String instanceState) {
        this.instanceState = instanceState;
    }

    public String getInstanceStateReason() {
        return instanceStateReason;
    }

    public void setInstanceStateReason(String instanceStateReason) {
        this.instanceStateReason = instanceStateReason;
    }

    public String getInstanceTag() {
        return instanceTag;
    }

    public void setInstanceTag(String instanceTag) {
        this.instanceTag = instanceTag;
    }

    public String getKeypair() {
        return keypair;
    }

    public void setKeypair(String keypair) {
        this.keypair = keypair;
    }

    public String getSecurityGroup() {
        return securityGroup;
    }

    public void setSecurityGroup(String securityGroup) {
        this.securityGroup = securityGroup;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getTps() {
        return tps;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
