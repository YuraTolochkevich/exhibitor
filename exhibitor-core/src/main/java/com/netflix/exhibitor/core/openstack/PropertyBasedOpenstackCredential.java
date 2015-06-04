package com.netflix.exhibitor.core.openstack;

import org.apache.curator.utils.CloseableUtils;

import java.io.*;
import java.util.Properties;

public class PropertyBasedOpenstackCredential implements OpenstackCredentials {


    private final String tenant;
    private final String username;
    private final String password;
    private final String authServer;

    public static final String PROPERTY_OPENSTACK_TENANT = "com.netflix.exhibitor.openstack.tenant";
    public static final String PROPERTY_OPENSTACK_USERNAME = "com.netflix.exhibitor.openstack.username";
    public static final String PROPERTY_OPENSTACK_PASSWORD = "com.netflix.exhibitor.openstack.password";
    public static final String AUTH_SERVER = "com.netflix.exhibitor.openstack.authSever";

    public PropertyBasedOpenstackCredential(File propertiesFile) throws IOException {
        this(loadProperties(propertiesFile));
    }

    public PropertyBasedOpenstackCredential(Properties properties) {
        tenant = properties.getProperty(PROPERTY_OPENSTACK_TENANT);
        username = properties.getProperty(PROPERTY_OPENSTACK_USERNAME);
        password = properties.getProperty(PROPERTY_OPENSTACK_PASSWORD);
        authServer = properties.getProperty(AUTH_SERVER);
    }



    private static Properties loadProperties(File propertiesFile) throws IOException {
        Properties properties = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(propertiesFile));
        try {
            properties.load(in);
        } finally {
            CloseableUtils.closeQuietly(in);
        }
        return properties;
    }

    @Override
    public String getTenant() {
        return tenant;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getAuthserver() {
        return authServer;
    }
}
