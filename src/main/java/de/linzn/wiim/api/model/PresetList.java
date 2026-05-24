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

import java.util.List;

/**
 * Represents the list of presets returned by getPresetInfo.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PresetList {

    @JsonProperty("preset_num")
    private int presetCount;
    @JsonProperty("preset_list")
    private List<Preset> presets;

    public int getPresetCount() {
        return presetCount;
    }

    public List<Preset> getPresets() {
        return presets;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Preset {
        @JsonProperty("number")
        private int number;

        @JsonProperty("name")
        private String name;

        @JsonProperty("url")
        private String url;

        @JsonProperty("source")
        private String source;

        @JsonProperty("picurl")
        private String pictureUrl;

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getSource() {
            return source;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        @Override
        public String toString() {
            return "[" + number + "] " + name + " (" + source + ")";
        }
    }
}
