/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.wiimJavaApi;

import de.linzn.wiimJavaApi.exceptions.WiimAPIDataFetchException;
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

public class HttpRequestApi {

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

    HttpRequestApi(String httpAPIRequest, WiimAPI wiimAPI) {
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

    public Date getLastPullDate() {
        return this.lastPull;
    }
}