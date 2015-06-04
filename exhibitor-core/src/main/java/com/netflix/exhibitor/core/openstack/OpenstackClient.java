package com.netflix.exhibitor.core.openstack;

import org.apache.http.Header;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface OpenstackClient extends Closeable {

    void putObject(byte[] bytes, String container, String objectName) throws Exception;
    InputStream getObject(String container, String objectName) throws IOException;
    Header[] getObjectMeta(String container, String object) throws IOException;
    void deleteObject(String bucketName, String objectName) throws Exception;
    List<String> getObjectList(java.lang.String container, String lockPrefix) throws Exception;

}
