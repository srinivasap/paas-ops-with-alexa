package com.platform.ops.ec2.exception;

/**
 * Indicates paas end-point in inconsistent state to fulfill host provisioning request.
 *
 */
public class InvalidServerStateException extends HostProvisioningFailedException {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public InvalidServerStateException() {
        super();
    }

    /**
     * @param message
     */
    public InvalidServerStateException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidServerStateException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidServerStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidServerStateException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
