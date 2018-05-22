package com.platform.ops.ec2;

/**
 * Allowed set of provisioning actions on open-stack host instances.
 *
 * @author Srinivasa Prasad Sunnapu
 *
 */
public enum HostProvisionActionE {

    CREATE_INSTANCE,
    CREATE_SNAPSHOT,
    ASSOCIATE_FLOATING_IP,
    DISASSOCIATE_FLOATING_IP,
    UPDATE_INSTANCE,
    UPDATE_SECURITY_GROUPS,
    UPDATE_SIZE,
    LIST_INSTANCES,
    CONSOLE_LOGS,
    PAUSE_INSTANCE,
    SUSPEND_INSTANCE,
    RESUME_INSTANCE,
    SORT_REBOOT,
    HARD_REBOOT,
    SHUTDOWN_INSTANCE,
    START_INSTANCE,
    REBUILD_INSTANCE,
    TERMINATE_INSTANCE;

}
