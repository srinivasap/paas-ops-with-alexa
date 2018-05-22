package com.platform.ops.ec2.exception;

/**
 * Indicates resource request is not found.
 *
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * @param message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ResourceNotFoundException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
