/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */

package de.linzn.wiimJavaApi;


import de.linzn.wiimJavaApi.exceptions.WiimAPIDataFetchException;
import de.linzn.wiimJavaApi.exceptions.WiimAPIGeneralException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WiimAPI {
    private String ipAddress;
    private DeviceInformation deviceInformation;
    private WiimPlayer wiimPlayer;
    private boolean sslCheck;
    int standByTimer;
    TimeUnit standByTimerTimeUnit;
    int pullInterval;
    TimeUnit pullIntervalTimeUnit;

    public WiimAPI(String ipAddress) {
        this(ipAddress, true);
    }

    public WiimAPI(String ipAddress, boolean sslCheck) {
        this.ipAddress = ipAddress;
        this.sslCheck = sslCheck;
        this.pullInterval = 1000;
        this.pullIntervalTimeUnit = TimeUnit.MILLISECONDS;
        this.standByTimer = 3;
        this.standByTimerTimeUnit = TimeUnit.MINUTES;

    }

    public void connect() {
        this.deviceInformation = new DeviceInformation(this);
        this.wiimPlayer = new WiimPlayer(this);
        this.pull_scheduler();
    }

    private void pull_scheduler() {
        try {
            JSONObject deviceInformationData = deviceInformation.requestDataSetUpdate();
            deviceInformation.processNewData(deviceInformationData);
            JSONObject wiimPlayerData = wiimPlayer.requestDataSetUpdate();
            wiimPlayer.processNewData(wiimPlayerData);
        } catch (WiimAPIDataFetchException ignored) {
        }

        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    pullIntervalTimeUnit.sleep(pullInterval);
                    JSONObject deviceInformationData = deviceInformation.requestDataSetUpdate();
                    deviceInformation.processNewData(deviceInformationData);
                    JSONObject wiimPlayerData = wiimPlayer.requestDataSetUpdate();
                    wiimPlayer.processNewData(wiimPlayerData);
                } catch (InterruptedException | WiimAPIDataFetchException ignored) {
                }
            }
        });
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean hasSSLCheck() {
        return sslCheck;
    }

    public void setSslCheck(boolean sslCheck) {
        this.sslCheck = sslCheck;
    }

    public void setPullInterval(int pullInterval, TimeUnit timeUnit) {
        this.pullInterval = pullInterval;
        this.pullIntervalTimeUnit = timeUnit;
    }

    public void setStandByTimer(int standByTimer, TimeUnit timeUnit) {
        this.standByTimer = standByTimer;
        this.standByTimerTimeUnit = timeUnit;
    }

    public DeviceInformation getDeviceInformation() {
        if (this.deviceInformation != null) {
            return this.deviceInformation;
        }
        throw new WiimAPIGeneralException("WiimAPI is not connected yet. Use connect() to connect to the Wiim device!");
    }

    public WiimPlayer getWiimPlayer() {
        if (this.wiimPlayer != null) {
            return this.wiimPlayer;
        }
        throw new WiimAPIGeneralException("WiimAPI is not connected yet. Use connect() to connect to the Wiim device!");
    }
}
