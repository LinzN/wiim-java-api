/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */

package de.linzn.wiimJavaApi;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.linzn.wiimJavaApi.exceptions.WiimAPIDataFetchException;
import de.linzn.wiimJavaApi.exceptions.WiimAPIDataPushException;
import de.linzn.wiimJavaApi.exceptions.WiimAPIInvalidDataException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Scanner;

public abstract class HttpAPIAccess {

    protected final WiimAPI wiimAPI;
    private final String httpAPIRequest;
    protected JSONObject dataSet;
    protected Date lastPull;
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
    };
    HostnameVerifier allHostsValid = (hostname, session) -> true;

    HttpAPIAccess(String httpAPIRequest, WiimAPI wiimAPI) {
        this.httpAPIRequest = httpAPIRequest;
        this.wiimAPI = wiimAPI;
        this.lastPull = null;
        this.dataSet = new JSONObject();
    }

    JSONObject requestDataSetUpdate() throws WiimAPIDataFetchException, WiimAPIInvalidDataException {
        try {
            URL url = new URL("https://" + wiimAPI.getIpAddress() + "/httpapi.asp?command=" + httpAPIRequest);
            /* Ignore SSL */
            if (!this.wiimAPI.hasSSLCheck()) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            }

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();
            String data = inline.toString();
            if (this.isValidJson(data)) {
                return new JSONObject(data);
            } else {
                throw new WiimAPIInvalidDataException();
            }
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            throw new WiimAPIDataFetchException(e);
        }
    }

    protected void pushDataUpdate(String update) throws WiimAPIDataPushException {
        try {
            URL url = new URL("https://" + wiimAPI.getIpAddress() + "/httpapi.asp?command=" + update);
            /* Ignore SSL */
            if (!this.wiimAPI.hasSSLCheck()) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            }

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();
            // Response will be ignored at the moment because its always "OK"
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            throw new WiimAPIDataPushException(e);
        }
    }

    public Date getLastPullDate() {
        return this.lastPull;
    }

    abstract void processNewData(JSONObject jsonObject);

    protected boolean isValidJson(String json) {
        ObjectMapper mapper = new ObjectMapper()
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            mapper.readTree(json);
        } catch (JacksonException e) {
            return false;
        }
        return true;
    }
}
