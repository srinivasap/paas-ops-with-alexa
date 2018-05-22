package com.platform.ops.ec2.exception;

/**
 * Indicates failure with host provisioning request.
 *
 */
public class HostProvisioningFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public HostProvisioningFailedException() {
        super();
    }

    /**
     * @param message
     */
    public HostProvisioningFailedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public HostProvisioningFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public HostProvisioningFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public HostProvisioningFailedException(String message, Throwable cause,
                                           boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
