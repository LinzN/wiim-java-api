/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.wiimJavaApi;


import de.linzn.wiimJavaApi.exceptions.WiimAPIDataFetchException;
import de.linzn.wiimJavaApi.exceptions.WiimAPIGeneralException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WiimAPI {
    IWiimLogger logger;
    int standByTimer;
    TimeUnit standByTimerTimeUnit;
    int pullInterval;
    TimeUnit pullIntervalTimeUnit;
    private String ipAddress;
    private DeviceInformation deviceInformation;
    private WiimPlayer wiimPlayer;
    private boolean sslCheck;
    private boolean hasAPIError;

    public WiimAPI(String ipAddress) {
        this(ipAddress, true);
    }

    public WiimAPI(String ipAddress, boolean sslCheck) {
        this.hasAPIError = false;
        this.logger = new DefaultWiimLogger();
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
        this.pull_data();
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                pullIntervalTimeUnit.sleep(pullInterval);
                this.pull_data();
            }
        });
    }

    private void pull_data() {
        boolean apiErrorCheck = false;
        try {
            JSONObject deviceInformationData = deviceInformation.requestDataSetUpdate();
            if (deviceInformationData != null) {
                deviceInformation.processNewData(deviceInformationData);
            } else {
                apiErrorCheck = true;
            }
            JSONObject wiimPlayerData = wiimPlayer.requestDataSetUpdate();
            if (wiimPlayerData != null) {
                wiimPlayer.processNewData(wiimPlayerData);
            } else {
                apiErrorCheck = true;

            }
        } catch (WiimAPIDataFetchException e) {
            apiErrorCheck = true;
            this.logger.error(e);
        }
        if (apiErrorCheck) {
            if (!this.hasAPIError) {
                this.wiimPlayer.wiimAPI.logger.error("Invalid json data from wiim device!");
            }
        } else {
            if (this.hasAPIError) {
                this.wiimPlayer.wiimAPI.logger.info("Valid json data received from wiim device!");
            }
        }

        this.hasAPIError = apiErrorCheck;
    }

    public void setWiimLogger(IWiimLogger wiimLogger) {
        this.logger = wiimLogger;
        this.logger.info("Register custom logger interface!");
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

    public boolean hasAPIError() {
        return hasAPIError;
    }
}
