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

package de.linzn.wiim.api;

import de.linzn.wiim.api.exception.WiiMApiException;
import de.linzn.wiim.api.model.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

/**
 * Java client for the WiiM HTTP API (v1.2).
 *
 * <p>Usage:
 * <pre>{@code
 *   WiiMClient client = new WiiMClient("192.168.1.42");
 *   DeviceStatus status = client.getDeviceStatus();
 *   client.setVolume(50);
 *   client.play("http://stream.example.com/audio.mp3");
 * }</pre>
 *
 * <p>All methods throw {@link WiiMApiException} on error.
 * The client disables SSL certificate verification by default because WiiM devices
 * use self-signed certificates — only use on a trusted local network.
 */
public class WiiMClient {

    private static final String BASE_PATH = "/httpapi.asp?command=";
    private static final DateTimeFormatter TIME_SYNC_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final String host;
    private final HttpClient http;
    private final ObjectMapper mapper;


    /**
     * Creates a client with a 5-second timeout (SSL verification disabled).
     *
     * @param host IP address or hostname of the WiiM device, e.g. {@code "192.168.1.42"}
     */
    public WiiMClient(String host) {
        this(host, 5);
    }

    /**
     * Creates a client with a custom timeout (SSL verification disabled).
     *
     * @param host           IP address or hostname of the WiiM device
     * @param timeoutSeconds HTTP request timeout in seconds
     */
    public WiiMClient(String host, int timeoutSeconds) {
        Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        this.host = host;
        this.mapper = new ObjectMapper();
        this.http = buildTrustAllClient(timeoutSeconds);
    }


    private static void validateAlarmIndex(int n) {
        if (n < 0 || n > 2) {
            throw new WiiMApiException("Alarm index must be 0–2, got: " + n);
        }
    }


    /**
     * Converts a string to uppercase hex — used for URLs in playlist/play commands.
     */
    private static String hexEncode(String s) {
        StringBuilder sb = new StringBuilder();
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private static String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }


    private static HttpClient buildTrustAllClient(int timeoutSeconds) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                    //.hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new WiiMApiException("Failed to create HTTP client: " + e.getMessage(), e);
        }
    }

    /**
     * Returns full device information (firmware, MAC, IP, group membership, …).
     */
    public DeviceStatus getDeviceStatus() {
        return getJson("getStatusEx", DeviceStatus.class);
    }

    /**
     * Returns the Wi-Fi connection state as a plain string:
     * {@code "OK"}, {@code "PROCESS"}, {@code "PAIRFAIL"}, or {@code "FAIL"}.
     */
    public String getWlanConnectionState() {
        return getText("wlanGetConnectState").trim();
    }

    /**
     * Returns {@code true} if the device is connected to Wi-Fi.
     */
    public boolean isConnected() {
        return "OK".equalsIgnoreCase(getWlanConnectionState());
    }

    /**
     * Returns the current playback status.
     */
    public PlayerStatus getPlayerStatus() {
        return getJson("getPlayerStatus", PlayerStatus.class);
    }

    /**
     * Starts playing an audio stream URL.
     *
     * @param url audio stream URL (http or https)
     */
    public void play(String url) {
        String encoded = hexEncode(url);
        requireOk(getText("setPlayerCmd:play:" + encoded));
    }

    /**
     * Starts playing a playlist (M3U / ASX) from the given URL.
     *
     * @param playlistUrl URL of the M3U/ASX playlist
     * @param startIndex  zero-based start index
     */
    public void playPlaylist(String playlistUrl, int startIndex) {
        String encoded = hexEncode(playlistUrl);
        requireOk(getText("setPlayerCmd:hex_playlist:" + encoded + ":" + startIndex));
    }

    /**
     * Pauses playback.
     */
    public void pause() {
        requireOk(getText("setPlayerCmd:pause"));
    }

    /**
     * Resumes paused playback.
     */
    public void resume() {
        requireOk(getText("setPlayerCmd:resume"));
    }

    /**
     * Toggles between play and pause.
     */
    public void togglePause() {
        requireOk(getText("setPlayerCmd:onepause"));
    }

    /**
     * Skips to the previous track.
     */
    public void previous() {
        requireOk(getText("setPlayerCmd:prev"));
    }

    /**
     * Skips to the next track.
     */
    public void next() {
        requireOk(getText("setPlayerCmd:next"));
    }

    /**
     * Seeks to a position in the current track.
     *
     * @param positionSeconds position in seconds (0 = beginning)
     */
    public void seek(int positionSeconds) {
        requireOk(getText("setPlayerCmd:seek:" + positionSeconds));
    }

    /**
     * Stops playback.
     */
    public void stop() {
        requireOk(getText("setPlayerCmd:stop"));
    }

    /**
     * Sets the volume.
     *
     * @param volume value between 0 and 100
     */
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new WiiMApiException("Volume must be between 0 and 100, got: " + volume);
        }
        requireOk(getText("setPlayerCmd:vol:" + volume));
    }

    /**
     * Mutes or unmutes the device.
     *
     * @param mute {@code true} to mute, {@code false} to unmute
     */
    public void setMute(boolean mute) {
        requireOk(getText("setPlayerCmd:mute:" + (mute ? "1" : "0")));
    }

    /**
     * Sets the loop mode.
     *
     * @param mode one of the {@link PlayerStatus.LoopMode} values
     */
    public void setLoopMode(PlayerStatus.LoopMode mode) {
        requireOk(getText("setPlayerCmd:loopmode:" + mode.getCode()));
    }

    /**
     * Enables the equalizer.
     */
    public void eqOn() {
        requireStatusOk(getJson("EQOn", StatusResponse.class));
    }

    /**
     * Disables the equalizer.
     */
    public void eqOff() {
        requireStatusOk(getJson("EQOff", StatusResponse.class));
    }

    /**
     * Returns {@code true} if the EQ is currently enabled.
     */
    public boolean isEqOn() {
        EqStatResponse r = getJson("EQGetStat", EqStatResponse.class);
        return "On".equalsIgnoreCase(r.eqStat);
    }

    /**
     * Returns the list of available EQ preset names.
     */
    public List<String> getEqList() {
        return getJson("EQGetList", new TypeReference<List<String>>() {
        });
    }

    /**
     * Loads a named EQ preset.
     *
     * @param eqName one of the names returned by {@link #getEqList()}
     */
    public void loadEq(String eqName) {
        requireStatusOk(getJson("EQLoad:" + urlEncode(eqName), StatusResponse.class));
    }

    /**
     * Reboots the device.
     */
    public void reboot() {
        requireStatusOk(getJson("reboot", StatusResponse.class));
    }

    /**
     * Schedules a shutdown.
     *
     * @param seconds seconds until shutdown; {@code 0} = immediate; {@code -1} = cancel
     */
    public void shutdown(int seconds) {
        requireStatusOk(getJson("setShutdown:" + seconds, StatusResponse.class));
    }

    /**
     * Shuts down the device immediately.
     */
    public void shutdownNow() {
        shutdown(0);
    }

    /**
     * Cancels a previously scheduled shutdown.
     */
    public void cancelShutdown() {
        shutdown(-1);
    }

    /**
     * Returns the remaining seconds until shutdown, or {@code -1} if no timer is set.
     */
    public int getShutdownTimer() {
        try {
            return Integer.parseInt(getText("getShutdown").trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Synchronises the device clock with the provided time (UTC).
     *
     * @param dateTime UTC date/time to sync
     */
    public void syncTime(LocalDateTime dateTime) {
        requireOk(getText("timeSync:" + dateTime.format(TIME_SYNC_FORMAT)));
    }

    /**
     * Synchronises the device clock with the current UTC time.
     */
    public void syncTimeNow() {
        syncTime(LocalDateTime.now());
    }

    /**
     * Returns alarm clock configuration for slot {@code n} (0–2).
     */
    public AlarmClock getAlarm(int n) {
        validateAlarmIndex(n);
        return getJson("getAlarmClock:" + n, AlarmClock.class);
    }

    /**
     * Sets an alarm to ring once at a specific UTC date/time.
     *
     * @param n    alarm slot (0–2)
     * @param time UTC time string {@code "HHmmss"}
     * @param date UTC date string {@code "YYYYmmdd"}
     * @param url  playback URL or shell path (max 256 bytes)
     */
    public void setAlarmOnce(int n, String time, String date, String url) {
        validateAlarmIndex(n);
        // trig=1, op=1 (playback), time, day=YYYYMMDD, url
        getText("setAlarmClock:" + n + ":1:1:" + time + ":" + date + ":" + hexEncode(url));
    }

    /**
     * Sets an alarm that fires every day at a given UTC time.
     *
     * @param n    alarm slot (0–2)
     * @param time UTC time string {@code "HHmmss"}
     * @param url  playback URL or shell path
     */
    public void setAlarmEveryDay(int n, String time, String url) {
        validateAlarmIndex(n);
        getText("setAlarmClock:" + n + ":2:1:" + time + ":" + hexEncode(url));
    }

    /**
     * Cancels an alarm.
     *
     * @param n alarm slot (0–2)
     */
    public void cancelAlarm(int n) {
        validateAlarmIndex(n);
        getText("setAlarmClock:" + n + ":0");
    }

    /**
     * Stops the currently ringing alarm.
     */
    public void stopAlarm() {
        getText("alarmStop");
    }

    /**
     * Switches the active source input.
     *
     * @param source the desired input source
     */
    public void switchSource(SourceInput source) {
        requireOk(getText("setPlayerCmd:switchmode:" + source.getCommand()));
    }

    /**
     * Plays a preset by its number.
     *
     * @param preset preset number (1–12)
     */
    public void playPreset(int preset) {
        if (preset < 1 || preset > 12) {
            throw new WiiMApiException("Preset number must be between 1 and 12, got: " + preset);
        }
        getText("MCUKeyShortClick:" + preset);
    }

    /**
     * Returns the list of configured presets.
     */
    public PresetList getPresetList() {
        return getJson("getPresetInfo", PresetList.class);
    }

    /**
     * Returns metadata for the currently playing track.
     */
    public TrackMetadata.MetaData getTrackMetadata() {
        TrackMetadata wrapper = getJson("getMetaInfo", TrackMetadata.class);
        return wrapper != null ? wrapper.getMetaData() : null;
    }

    /**
     * Returns the current audio output hardware mode.
     */
    public AudioOutputMode getAudioOutputMode() {
        return getJson("getNewAudioOutputHardwareMode", AudioOutputMode.class);
    }

    /**
     * Sets the audio output hardware mode.
     *
     * @param mode the desired hardware output mode
     */
    public void setAudioOutputMode(AudioOutputMode.HardwareMode mode) {
        requireOk(getText("setAudioOutputHardwareMode:" + mode.getCode()));
    }

    private String buildUrl(String command) {
        return "https://" + host + BASE_PATH + command;
    }

    private String getText(String command) {
        String url = buildUrl(command);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new WiiMApiException("HTTP " + response.statusCode() + " for " + url, response.statusCode());
            }
            return response.body();
        } catch (WiiMApiException e) {
            throw e;
        } catch (Exception e) {
            throw new WiiMApiException("Request failed for " + url + ": " + e.getMessage(), e);
        }
    }

    private <T> T getJson(String command, Class<T> type) {
        String body = getText(command);
        return mapper.readValue(body, type);
    }

    private <T> T getJson(String command, TypeReference<T> typeRef) {
        String body = getText(command);
        return mapper.readValue(body, typeRef);
    }

    private void requireOk(String response) {
        if (!"OK".equalsIgnoreCase(response.trim())) {
            throw new WiiMApiException("Device returned error response: " + response);
        }
    }

    private void requireStatusOk(StatusResponse sr) {
        if (sr == null || !"OK".equalsIgnoreCase(sr.status)) {
            throw new WiiMApiException("Device returned status: " + (sr == null ? "null" : sr.status));
        }
    }

    public enum SourceInput {
        LINE_IN("line-in"),
        BLUETOOTH("bluetooth"),
        OPTICAL("optical"),
        USB_DISK("udisk"),
        WIFI("wifi");

        private final String command;

        SourceInput(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }


    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private static class StatusResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("status")
        public String status;
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private static class EqStatResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("EQStat")
        public String eqStat;
    }
}
