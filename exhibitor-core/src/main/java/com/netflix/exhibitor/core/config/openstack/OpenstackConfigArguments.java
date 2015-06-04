package com.netflix.exhibitor.core.config.openstack;

public class OpenstackConfigArguments {
    private final String containerName;
    private final String objectName;
    private final OpenstackConfigAutoManageLockArguments lockArguments;

    public OpenstackConfigArguments(String bucketName, String objectName, OpenstackConfigAutoManageLockArguments lockArguments) {
        this.containerName = bucketName;
        this.objectName = objectName;
        this.lockArguments = lockArguments;
    }

    public String getBucketName() {
        return containerName;
    }

    public String getObjectName() {
        return objectName;
    }

    public OpenstackConfigAutoManageLockArguments getLockArguments() {
        return lockArguments;
    }
}