package com.netflix.exhibitor.core.openstack;



public class OpenstackClientfactoryImpl implements OpenstackClientFactory {


    @Override
    public OpenstackClient makeNewClient(OpenstackCredentials credentials) throws Exception {
        return new OpenstackClientImpl(credentials);
    }
}