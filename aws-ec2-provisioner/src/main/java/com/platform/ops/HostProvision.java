package com.platform.ops;

import com.platform.ops.ec2.exception.HostProvisioningFailedException;
import com.platform.ops.model.Host;

/**
 * Service contract for host provisioning implementor.
 *
 */
public interface HostProvision {

    Host createInstance(Host host) throws HostProvisioningFailedException;
    Host resizeCluster(Host hosts) throws HostProvisioningFailedException;
    Host destroyInstance(Host hosts) throws HostProvisioningFailedException;
    Host listInstances(Host host, boolean filterPending) throws HostProvisioningFailedException;
    Host stopInstance(Host host) throws HostProvisioningFailedException;
    Host startInstance(Host host) throws HostProvisioningFailedException;
    Host restartInstance(Host host, boolean hard) throws HostProvisioningFailedException;
    Host queryMetrics(Host host, String param) throws HostProvisioningFailedException;
}
