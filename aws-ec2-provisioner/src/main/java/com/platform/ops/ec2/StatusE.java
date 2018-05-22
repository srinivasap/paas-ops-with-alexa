package com.platform.ops.ec2;

/**
 * Host status indicator
 */
public enum StatusE {
    BUILD,
    ACTIVE,
    RUNNING,
    MIGRATING,
    PAUSED,
    SUSPENDED,
    REBOOT,
    HARD_REBOOT,
    SHUTOFF,
    SHUTDOWN,
    DELETED,
    SOFT_DELETED,
    UNKNOWN,
    UNRECOGNIZED,
    ERROR;
}
