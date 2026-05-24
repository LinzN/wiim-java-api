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
 * Wraps the metaData object returned by getMetaInfo.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackMetadata {

    @JsonProperty("metaData")
    private MetaData metaData;

    public MetaData getMetaData() {
        return metaData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaData {
        @JsonProperty("album")
        private String album;

        @JsonProperty("title")
        private String title;

        @JsonProperty("artist")
        private String artist;

        @JsonProperty("albumArtURI")
        private String albumArtUri;

        @JsonProperty("sampleRate")
        private String sampleRate;

        @JsonProperty("bitDepth")
        private String bitDepth;

        public String getAlbum() {
            return album;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }

        public String getAlbumArtUri() {
            return albumArtUri;
        }

        public String getSampleRate() {
            return sampleRate;
        }

        public String getBitDepth() {
            return bitDepth;
        }

        @Override
        public String toString() {
            return artist + " – " + title + " [" + album + "]";
        }
    }
}
