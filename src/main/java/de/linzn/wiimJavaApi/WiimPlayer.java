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


import de.linzn.wiimJavaApi.exceptions.WiimAPIDataPushException;
import de.linzn.wiimJavaApi.exceptions.WiimAPINoDataException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class WiimPlayer extends HttpAPIAccess {
    private final AtomicBoolean isStandby;
    private Date standbyTimer = null;

    WiimPlayer(WiimAPI wiimAPI) {
        super("getPlayerStatus", wiimAPI);
        this.isStandby = new AtomicBoolean(false);
    }

    /**
     * Check if device is in standby mode or not.
     * This will not get the information from the device itself its more a software solution indicated on play/stop status
     *
     * @return If the device is in standby mode or not
     */
    public boolean isStandby() {
        return this.isStandby.get();
    }

    public int get_type() {
        if (this.dataSet.has("type")) {
            return this.dataSet.getInt("type");
        }
        throw new WiimAPINoDataException();
    }

    public int get_ch() {
        if (this.dataSet.has("ch")) {
            return this.dataSet.getInt("ch");
        }
        throw new WiimAPINoDataException();
    }

    public int get_mode() {
        if (this.dataSet.has("mode")) {
            return this.dataSet.getInt("mode");
        }
        throw new WiimAPINoDataException();
    }

    public int get_loop() {
        if (this.dataSet.has("loop")) {
            return this.dataSet.getInt("loop");
        }
        throw new WiimAPINoDataException();
    }

    public int get_eq() {
        if (this.dataSet.has("eq")) {
            return this.dataSet.getInt("eq");
        }
        throw new WiimAPINoDataException();
    }

    public String get_status() {
        if (this.dataSet.has("status")) {
            return this.dataSet.getString("status");
        }
        throw new WiimAPINoDataException();
    }

    public long get_curpos() {
        if (this.dataSet.has("curpos")) {
            return this.dataSet.getLong("curpos");
        }
        throw new WiimAPINoDataException();
    }

    public long get_offset_pts() {
        if (this.dataSet.has("offset_pts")) {
            return this.dataSet.getLong("offset_pts");
        }
        throw new WiimAPINoDataException();
    }

    public int get_totlen() {
        if (this.dataSet.has("totlen")) {
            return this.dataSet.getInt("totlen");
        }
        throw new WiimAPINoDataException();
    }

    public int get_alarmflag() {
        if (this.dataSet.has("alarmflag")) {
            return this.dataSet.getInt("alarmflag");
        }
        throw new WiimAPINoDataException();
    }

    public int get_plicount() {
        if (this.dataSet.has("plicount")) {
            return this.dataSet.getInt("plicount");
        }
        throw new WiimAPINoDataException();
    }

    public int get_plicurr() {
        if (this.dataSet.has("plicurr")) {
            return this.dataSet.getInt("plicurr");
        }
        throw new WiimAPINoDataException();
    }

    public int get_vol() {
        if (this.dataSet.has("vol")) {
            return this.dataSet.getInt("vol");
        }
        throw new WiimAPINoDataException();
    }

    public void set_vol(int volume) throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:vol:" + volume);
    }

    public int get_mute() {
        if (this.dataSet.has("mute")) {
            return this.dataSet.getInt("mute");
        }
        throw new WiimAPINoDataException();
    }

    public void set_mute(int mute) throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:mute:" + mute);
    }

    public void play_url(String url) throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:play:" + url);
    }

    public void playlist_url(String url, int index) throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:playlist:" + url + ":" + index);
    }

    public void set_pause() throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:pause");
    }

    public void set_resume() throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:resume");
    }

    public void set_onepause() throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:onepause");
    }

    public void set_prev() throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:prev");
    }

    public void set_next() throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:next");
    }

    public void set_stop() throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:stop");
    }

    public void set_loopmode(int loopmode) throws WiimAPIDataPushException {
        this.pushDataUpdate("setPlayerCmd:loopmode:" + loopmode);
    }

    @Override
    void processNewData(JSONObject jsonObject) {
        try {
            String oldStatus = null;
            if (this.dataSet.has("status")) {
                oldStatus = this.dataSet.getString("status");
            }
            String status = jsonObject.getString("status");

            if (oldStatus != null && oldStatus.equalsIgnoreCase(status)) {
                if (status.equalsIgnoreCase("stop") || status.equalsIgnoreCase("none")) {
                    if (!this.isStandby.get() && this.standbyTimer.getTime() + this.wiimAPI.standByTimerTimeUnit.toMillis(this.wiimAPI.standByTimer) < new Date().getTime()) {
                        this.isStandby.set(true);
                    }
                }
            } else {
                this.standbyTimer = new Date();
                this.isStandby.set(false);
            }

        } catch (Exception e) {
            this.wiimAPI.logger.error(e);
        }
        this.dataSet = jsonObject;
        this.lastPull = new Date();
    }
}
