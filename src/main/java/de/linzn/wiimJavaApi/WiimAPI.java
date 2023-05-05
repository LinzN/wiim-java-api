/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */

package de.linzn.wiimJavaApi;

import de.linzn.wiimJavaApi.exceptions.WiimAPIGeneralException;

public class WiimAPI {
    private String ipAddress;
    private DeviceInformation deviceInformation;
    private PlayerStatus playerStatus;
    private boolean sslCheck;

    public WiimAPI(String ipAddress) {
        this(ipAddress, true);
    }

    public WiimAPI(String ipAddress, boolean sslCheck) {
        this.ipAddress = ipAddress;
        this.sslCheck = sslCheck;
    }

    public void connect() {
        this.deviceInformation = new DeviceInformation(this);
        this.playerStatus = new PlayerStatus(this);
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

    public DeviceInformation getDeviceInformation() {
        if (this.deviceInformation != null) {
            return this.deviceInformation;
        }
        throw new WiimAPIGeneralException("WiimAPI is not connected yet. Use connect() to connect to the Wiim device!");
    }

    public PlayerStatus getPlayerStatus() {
        if (playerStatus != null) {
            return this.playerStatus;
        }
        throw new WiimAPIGeneralException("WiimAPI is not connected yet. Use connect() to connect to the Wiim device!");
    }
}
