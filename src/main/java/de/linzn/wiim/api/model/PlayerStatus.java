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
 * Represents the playback status returned by getPlayerStatus.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerStatus {

    @JsonProperty("type")
    private String type;
    @JsonProperty("ch")
    private String channel;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("loop")
    private String loop;
    @JsonProperty("eq")
    private String eq;
    @JsonProperty("status")
    private String status;
    @JsonProperty("curpos")
    private String currentPosition;
    @JsonProperty("totlen")
    private String totalLength;
    @JsonProperty("vol")
    private String volume;
    @JsonProperty("mute")
    private String mute;
    @JsonProperty("plicount")
    private String playlistCount;
    @JsonProperty("plicurr")
    private String playlistCurrent;

    public boolean isMaster() {
        return "0".equals(type);
    }

    public boolean isPlaying() {
        return "play".equals(status);
    }

    // --- Convenience methods ---

    public boolean isPaused() {
        return "pause".equals(status);
    }

    public boolean isStopped() {
        return "stop".equals(status);
    }

    public boolean isLoading() {
        return "loading".equals(status);
    }

    public boolean isMuted() {
        return "1".equals(mute);
    }

    public int getVolumeInt() {
        try {
            return Integer.parseInt(volume);
        } catch (Exception e) {
            return 0;
        }
    }

    public long getCurrentPositionMs() {
        try {
            return Long.parseLong(currentPosition);
        } catch (Exception e) {
            return 0;
        }
    }

    public long getTotalLengthMs() {
        try {
            return Long.parseLong(totalLength);
        } catch (Exception e) {
            return 0;
        }
    }

    public PlaybackMode getPlaybackMode() {
        try {
            return PlaybackMode.fromCode(Integer.parseInt(mode));
        } catch (Exception e) {
            return PlaybackMode.UNKNOWN;
        }
    }

    public LoopMode getLoopMode() {
        try {
            return LoopMode.fromCode(Integer.parseInt(loop));
        } catch (Exception e) {
            return LoopMode.LOOP_ALL;
        }
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    // --- Getters ---

    public String getMode() {
        return mode;
    }

    public String getLoop() {
        return loop;
    }

    public String getEq() {
        return eq;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getTotalLength() {
        return totalLength;
    }

    public String getVolume() {
        return volume;
    }

    public String getMute() {
        return mute;
    }

    public String getPlaylistCount() {
        return playlistCount;
    }

    public String getPlaylistCurrent() {
        return playlistCurrent;
    }

    @Override
    public String toString() {
        return "PlayerStatus{" +
                "status='" + status + '\'' +
                ", mode=" + getPlaybackMode() +
                ", volume=" + volume +
                ", muted=" + isMuted() +
                ", posMs=" + getCurrentPositionMs() +
                '}';
    }

    public enum PlaybackMode {
        NONE(0), AIRPLAY(1), DLNA(2),
        WIIMU_PLAYLIST(10), USB_PLAYLIST(11), TF_PLAYLIST(16),
        SPOTIFY(31), TIDAL(32),
        AUX_IN(40), BLUETOOTH(41), EXTERNAL_STORAGE(42), OPTICAL_IN(43),
        MIRROR(50), VOICE_MAIL(60), SLAVE(99), UNKNOWN(-1);

        private final int code;

        PlaybackMode(int code) {
            this.code = code;
        }

        public static PlaybackMode fromCode(int code) {
            for (PlaybackMode m : values()) {
                if (m.code == code) return m;
            }
            if (code >= 10 && code <= 19) return WIIMU_PLAYLIST;
            return UNKNOWN;
        }

        public int getCode() {
            return code;
        }
    }

    public enum LoopMode {
        LOOP_ALL(0), SINGLE_LOOP(1), SHUFFLE_LOOP(2), SHUFFLE_NO_LOOP(3), NO_SHUFFLE_NO_LOOP(4), SEQUENCE_LOOP(-1);

        private final int code;

        LoopMode(int code) {
            this.code = code;
        }

        public static LoopMode fromCode(int code) {
            for (LoopMode m : values()) {
                if (m.code == code) return m;
            }
            return LOOP_ALL;
        }

        public int getCode() {
            return code;
        }
    }
}
