package com.netflix.exhibitor.core.config.openstack;


import com.google.api.client.http.HttpResponseException;
import com.google.api.services.storage.model.StorageObject;
import com.netflix.exhibitor.core.config.PseudoLockBase;
import com.netflix.exhibitor.core.gcs.GcsClient;
import com.netflix.exhibitor.core.openstack.OpenstackClient;

import java.util.ArrayList;
import java.util.List;

public class OpenstackPseudoLock extends PseudoLockBase
{
    private final OpenstackClient client;
    private final String bucketName;

    public static final int HTTP_NOT_FOUND = 404;

    public OpenstackPseudoLock(OpenstackClient client, String bucketName, String lockPrefix, int timeoutMs, int pollingMs, int settlingMs) {
        super(lockPrefix, timeoutMs, pollingMs, settlingMs);
        this.client = client;
        this.bucketName = bucketName;
    }

    @Override
    protected void createFile(String objectName, byte[] contents) throws Exception {
        client.putObject(contents, bucketName, objectName);
    }

    @Override
    protected void deleteFile(String objectName) throws Exception {
        try {
            client.deleteObject(bucketName, objectName);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() != HTTP_NOT_FOUND) {
                throw e;
            }
        }
    }

    @Override
    protected List<String> getFileNames(String lockPrefix) throws Exception {
        List<String> storageObjects = client.getObjectList(bucketName, lockPrefix);

        List<String> fileNames = new ArrayList<String>();
        for (String storageObject : storageObjects) {
            fileNames.add(storageObject);
        }

        return fileNames;
    }
}