/*
 * Copyright (c) 2026 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.wiim.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the full device status returned by getStatusEx.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceStatus {

    @JsonProperty("DeviceName")
    private String deviceName;

    @JsonProperty("GroupName")
    private String groupName;

    @JsonProperty("ssid")
    private String ssid;

    @JsonProperty("firmware")
    private String firmware;

    @JsonProperty("build")
    private String build;

    @JsonProperty("project")
    private String project;

    @JsonProperty("Release")
    private String release;

    @JsonProperty("group")
    private String group;

    @JsonProperty("internet")
    private String internet;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("MAC")
    private String mac;

    @JsonProperty("BT_MAC")
    private String btMac;

    @JsonProperty("apcli0")
    private String ipAddress;

    @JsonProperty("hardware")
    private String hardware;

    @JsonProperty("VersionUpdate")
    private String versionUpdate;

    @JsonProperty("NewVer")
    private String newVersion;

    @JsonProperty("RSSI")
    private String rssi;

    @JsonProperty("BSSID")
    private String bssid;

    @JsonProperty("WifiChannel")
    private String wifiChannel;

    @JsonProperty("battery")
    private String battery;

    @JsonProperty("battery_percent")
    private String batteryPercent;

    @JsonProperty("preset_key")
    private String presetKey;

    @JsonProperty("volume_control")
    private String volumeControl;

    @JsonProperty("privacy_mode")
    private String privacyMode;

    /**
     * @return true if the device is a slave in a group
     */
    public boolean isSlave() {
        return "1".equals(group);
    }

    /**
     * @return true if connected to the internet
     */
    public boolean isOnline() {
        return "1".equals(internet);
    }

    /**
     * @return true if a firmware update is available
     */
    public boolean isUpdateAvailable() {
        return "1".equals(versionUpdate);
    }

    // --- Getters ---

    public String getDeviceName() {
        return deviceName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSsid() {
        return ssid;
    }

    public String getFirmware() {
        return firmware;
    }

    public String getBuild() {
        return build;
    }

    public String getProject() {
        return project;
    }

    public String getRelease() {
        return release;
    }

    public String getGroup() {
        return group;
    }

    public String getInternet() {
        return internet;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMac() {
        return mac;
    }

    public String getBtMac() {
        return btMac;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getHardware() {
        return hardware;
    }

    public String getVersionUpdate() {
        return versionUpdate;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public String getRssi() {
        return rssi;
    }

    public String getBssid() {
        return bssid;
    }

    public String getWifiChannel() {
        return wifiChannel;
    }

    public String getBattery() {
        return battery;
    }

    public String getBatteryPercent() {
        return batteryPercent;
    }

    public String getPresetKey() {
        return presetKey;
    }

    public String getVolumeControl() {
        return volumeControl;
    }

    public String getPrivacyMode() {
        return privacyMode;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "deviceName='" + deviceName + '\'' +
                ", firmware='" + firmware + '\'' +
                ", uuid='" + uuid + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", rssi='" + rssi + '\'' +
                '}';
    }
}
