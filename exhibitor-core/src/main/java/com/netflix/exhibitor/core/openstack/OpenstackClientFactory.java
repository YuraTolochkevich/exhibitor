package com.netflix.exhibitor.core.openstack;

import com.netflix.exhibitor.core.gcs.GcsClient;

public interface OpenstackClientFactory {
    public OpenstackClient makeNewClient(OpenstackCredentials credentials) throws Exception;
}
