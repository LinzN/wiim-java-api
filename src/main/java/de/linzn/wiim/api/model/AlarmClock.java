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
 * Represents alarm clock data returned by getAlarmClock.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmClock {

    @JsonProperty("enable")
    private String enable;
    @JsonProperty("trigger")
    private String trigger;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("date")
    private String date;
    @JsonProperty("week_day")
    private String weekDay;
    @JsonProperty("day")
    private String day;
    @JsonProperty("time")
    private String time;
    @JsonProperty("path")
    private String path;

    public boolean isEnabled() {
        return "1".equals(enable);
    }

    public Trigger getTriggerMode() {
        try {
            return Trigger.fromCode(Integer.parseInt(trigger));
        } catch (Exception e) {
            return Trigger.CANCEL;
        }
    }

    public Operation getOperation() {
        try {
            return Operation.fromCode(Integer.parseInt(operation));
        } catch (Exception e) {
            return Operation.STOP_PLAYBACK;
        }
    }

    public String getEnable() {
        return enable;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getOperationRaw() {
        return operation;
    }

    public String getDate() {
        return date;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "AlarmClock{enabled=" + isEnabled() +
                ", trigger=" + getTriggerMode() +
                ", time='" + time + '\'' +
                ", path='" + path + '\'' + '}';
    }

    public enum Trigger {
        CANCEL(0), ONCE(1), EVERY_DAY(2), EVERY_WEEK_DAY(3), EVERY_WEEK_BITMASK(4), EVERY_MONTH(5);

        private final int code;

        Trigger(int code) {
            this.code = code;
        }

        public static Trigger fromCode(int code) {
            for (Trigger t : values()) {
                if (t.code == code) return t;
            }
            return CANCEL;
        }

        public int getCode() {
            return code;
        }
    }

    public enum Operation {
        SHELL(0), PLAYBACK_OR_RING(1), STOP_PLAYBACK(2);

        private final int code;

        Operation(int code) {
            this.code = code;
        }

        public static Operation fromCode(int code) {
            for (Operation o : values()) {
                if (o.code == code) return o;
            }
            return STOP_PLAYBACK;
        }

        public int getCode() {
            return code;
        }
    }
}
