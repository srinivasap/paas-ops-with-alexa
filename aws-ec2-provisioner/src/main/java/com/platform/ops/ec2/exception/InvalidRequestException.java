package com.platform.ops.ec2.exception;

/**
 * Indicates host provisioning request is invalid.
 *
 * @author Srinivasa Prasad Sunnapu
 *
 */
public class InvalidRequestException extends HostProvisioningFailedException {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public InvalidRequestException() {
        super();
    }

    /**
     * @param message
     */
    public InvalidRequestException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidRequestException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
