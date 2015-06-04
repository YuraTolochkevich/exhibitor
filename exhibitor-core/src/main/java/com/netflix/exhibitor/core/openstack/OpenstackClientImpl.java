package com.netflix.exhibitor.core.openstack;

import com.google.api.client.http.ByteArrayContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OpenstackClientImpl implements OpenstackClient {

    public OpenstackClientImpl(OpenstackCredentials credentials) {
        this.TOKEN_URI = String.format("http://%s/v2.0/tokens", credentials.getAuthserver());
    }

    private String TOKEN_URI;
    private String userName;
    private String password;
    private String tenant;
    private CloseableHttpClient httpclient = HttpClients.createDefault();

    private StorageToken getCredentials() throws IOException {
        HttpPost tokenRequest = new HttpPost(TOKEN_URI);
        String authParam = String.format("{\"auth\": {\"tenantName\": %s, \"passwordCredentials\":" +
                " {\"username\": %s, \"password\": %s}}}", tenant, userName, password);
        tokenRequest.setEntity(new StringEntity(authParam));
        tokenRequest.addHeader("Content-Type", "application/json");

        CloseableHttpResponse httpResponse = httpclient.execute(tokenRequest);

        String content =  EntityUtils.toString(httpResponse.getEntity());
        if (StringUtils.isEmpty(content)) {
            throw new IOException("Failed to write entity content.");
        }
        JSONObject obj = new JSONObject(content);
        String token = obj.getString("access_token");
        String storageURL = obj.getString("publicURL");
        return new StorageToken(token, storageURL);
    }


    public InputStream getObject(String container, String object) throws IOException {
        StorageToken storageToken = getCredentials();
        HttpGet getRequest = new HttpGet(String.format("%s/%s/%s",storageToken.getStorageUrl(),container, object));

        getRequest.addHeader("X-Auth-Token", storageToken.getToken());
        CloseableHttpResponse httpResponse = httpclient.execute(getRequest);

        return httpResponse.getEntity().getContent();
    }

    public Header[] getObjectMeta(String container, String object) throws IOException {
        StorageToken storageToken = getCredentials();
        HttpGet getRequest = new HttpGet(String.format("%s/%s",storageToken.getStorageUrl(),container, object));

        getRequest.addHeader("X-Auth-Token", storageToken.getToken());
        CloseableHttpResponse httpResponse = httpclient.execute(getRequest);

        return httpResponse.getAllHeaders();
    }

    @Override
    public void deleteObject(String container, String object) throws Exception {
        StorageToken storageToken = getCredentials();
        HttpDelete getRequest = new HttpDelete(String.format("%s/%s/%s",storageToken.getStorageUrl(),container, object));

        getRequest.addHeader("X-Auth-Token", storageToken.getToken());
        CloseableHttpResponse httpResponse = httpclient.execute(getRequest);

        return;

    }

    @Override
    public List<String> getObjectList(String container, String lockPrefix) throws Exception {
        StorageToken storageToken = getCredentials();
        HttpGet getRequest = new HttpGet(String.format("%s/%s/%s",storageToken.getStorageUrl(),container));

        getRequest.addHeader("X-Auth-Token", storageToken.getToken());
        CloseableHttpResponse httpResponse = httpclient.execute(getRequest);

        String responselist = EntityUtils.toString( httpResponse.getEntity(), "UTF-8");
        if (responselist.length() > 0) {
            ArrayList<String> objectList  = new ArrayList<String>();
            String [] ogjects =responselist.split("\n");
            for (String s: ogjects) {
                if (s.startsWith(lockPrefix)){
                    objectList.add(s);
                }
            }

            return objectList;
        }
        return null;
    }

    @Override
    public void putObject(byte[] bytes, String container, String objectName) throws Exception {
        StorageToken storageToken = getCredentials();

        HttpPut putRequest = new HttpPut(String.format("%s/%s/%s", storageToken.getStorageUrl(), container,objectName));
        putRequest.addHeader("X-Auth-Token", storageToken.getToken());
        HttpEntity putEntity =  new ByteArrayEntity(bytes);
        putRequest.setEntity(putEntity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(putRequest);
        } finally {
            response.close();
        }
    }


    class StorageToken {
        private String token;
        private String storageUrl;

        public StorageToken(String token, String storageUrl) {
            this.token = token;
            this.storageUrl = storageUrl;
        }

        public String getStorageUrl() {
            return storageUrl;
        }

        public String getToken() {
            return token;
        }

    }

    @Override
    public void close() throws IOException {

    }
}
