/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */

package de.linzn.wiimJavaApi;

import de.linzn.wiimJavaApi.exceptions.WiimAPINoDataException;

public class PlayerStatus extends HttpRequestApi {
    PlayerStatus(WiimAPI wiimAPI) {
        super("getPlayerStatus", wiimAPI);
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

    public int get_mute() {
        if (this.dataSet.has("mute")) {
            return this.dataSet.getInt("mute");
        }
        throw new WiimAPINoDataException();
    }
}
