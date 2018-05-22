package com.platform.ops;

import com.platform.ops.ec2.EC2HostProvisionImpl;
import com.platform.ops.ec2.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The factory that returns the specific HostProvision implementation referred
 * by constant.
 */
public enum HostProvisionImplFactory {

    EC2;

    private static final Logger LOG = LoggerFactory.getLogger(HostProvisionImplFactory.class);

    private HostProvisionImplFactory() {
    }

    /**
     * Returns new client implementation instance of PAAS provider.
     *
     * @return the HostProvision implementation instance
     * @throws InvalidRequestException when no HostProvision implementation is found for constant.
     */
    public EC2HostProvisionImpl getInstance(String accessKey, String secretKey) throws InvalidRequestException {

        EC2HostProvisionImpl instance = null;

        switch (this) {
            case EC2:
                instance = new EC2HostProvisionImpl();
                instance.init(accessKey, secretKey);
                break;
            default:
                LOG.error("Invalid host provisioning implementation requested <"+this.name()+">");
                throw new InvalidRequestException("No HostProvision implementation found for paas provider <"+this.name()+">");
        }

        return instance;
    }

}
