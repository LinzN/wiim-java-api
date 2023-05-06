/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */

package de.linzn.wiimJavaApi;


import de.linzn.wiimJavaApi.exceptions.WiimAPIDataFetchException;
import de.linzn.wiimJavaApi.exceptions.WiimAPIDataPushException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class HttpAPIAccess {

    private final String httpAPIRequest;
    private final WiimAPI wiimAPI;
    protected JSONObject dataSet;
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
    private Date lastPull;

    HttpAPIAccess(String httpAPIRequest, WiimAPI wiimAPI) {
        this.httpAPIRequest = httpAPIRequest;
        this.wiimAPI = wiimAPI;
        this.lastPull = null;
        this.dataSet = new JSONObject();
        try {
            requestDataSetUpdate();
        } catch (WiimAPIDataFetchException e) {
            e.printStackTrace();
        }
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    requestDataSetUpdate();
                } catch (WiimAPIDataFetchException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestDataSetUpdate() throws WiimAPIDataFetchException {
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
            this.dataSet = new JSONObject(inline.toString());
            this.lastPull = new Date();
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
}
