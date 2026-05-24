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
 * Represents the audio output hardware mode returned by getNewAudioOutputHardwareMode.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioOutputMode {

    @JsonProperty("hardware")
    private String hardware;
    @JsonProperty("source")
    private String source;
    @JsonProperty("audiocast")
    private String audiocast;

    public HardwareMode getHardwareMode() {
        try {
            return HardwareMode.fromCode(Integer.parseInt(hardware));
        } catch (Exception e) {
            return HardwareMode.UNKNOWN;
        }
    }

    public boolean isBtSourceActive() {
        return "1".equals(source);
    }

    public boolean isAudiocastActive() {
        return "1".equals(audiocast);
    }

    public String getHardware() {
        return hardware;
    }

    public String getSource() {
        return source;
    }

    public String getAudiocast() {
        return audiocast;
    }

    @Override
    public String toString() {
        return "AudioOutputMode{hardware=" + getHardwareMode() +
                ", btSource=" + isBtSourceActive() +
                ", audiocast=" + isAudiocastActive() + '}';
    }

    public enum HardwareMode {
        SPDIF(1), AUX(2), COAX(3), UNKNOWN(-1);

        private final int code;

        HardwareMode(int code) {
            this.code = code;
        }

        public static HardwareMode fromCode(int code) {
            for (HardwareMode m : values()) {
                if (m.code == code) return m;
            }
            return UNKNOWN;
        }

        public int getCode() {
            return code;
        }
    }
}
