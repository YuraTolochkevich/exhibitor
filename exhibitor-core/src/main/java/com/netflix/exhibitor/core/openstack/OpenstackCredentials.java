package com.netflix.exhibitor.core.openstack;

public interface OpenstackCredentials {
    public String getTenant();
    public String getUsername();
    public String getPassword();
    public String getAuthserver();
}