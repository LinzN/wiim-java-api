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


import de.linzn.wiim.api.model.PlayerStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Monitors a WiiM device and calls placeholder methods whenever the player
 * state changes.
 *
 * <p>Polling runs every {@value #POLL_INTERVAL_MILLISECONDS} seconds on a background
 * daemon thread. If the device remains stopped for longer than
 * {@value #STANDBY_TIMEOUT_MINUTES} minutes, {@link #onStandby()} is invoked.
 *
 * <h3>Extend and override</h3>
 * <pre>{@code
 * WiiMClient client = new WiiMClient("192.168.1.42");
 *
 * DeviceMonitor monitor = new DeviceMonitor(client) {
 *
 *     @Override protected void onStartedPlaying(PlayerStatus status) {
 *         System.out.println("▶ Now playing – vol " + status.getVolumeInt());
 *     }
 *
 *     @Override protected void onStopped(PlayerStatus status) {
 *         System.out.println("■ Stopped");
 *     }
 *
 *     @Override protected void onStandby() {
 *         System.out.println("💤 Going to standby");
 *         // e.g. dim a display, cut an amplifier relay, …
 *     }
 * };
 *
 * monitor.start();
 * }</pre>
 */
public class DeviceMonitor {

    // ── Configuration constants ────────────────────────────────────────────

    private static final int POLL_INTERVAL_MILLISECONDS = 1000;
    private static final int STANDBY_TIMEOUT_MINUTES = 2;
    private static final Logger LOG = Logger.getLogger(DeviceMonitor.class.getName());

    // ── State ──────────────────────────────────────────────────────────────

    private final WiiMClient client;
    private final ScheduledExecutorService scheduler;

    private volatile PlayerStatus lastStatus = null;
    private volatile ScheduledFuture<?> pollTask = null;
    private volatile boolean running = false;

    /**
     * Set to the moment playback stopped; null while not stopped.
     */
    private volatile Instant stoppedSince = null;
    /**
     * True once onStandby() has been called for the current stopped stretch.
     */
    private volatile boolean standbyTriggered = false;

    // ── Constructor ────────────────────────────────────────────────────────

    public DeviceMonitor(WiiMClient client) {
        this.client = client;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "wiim-device-monitor");
            t.setDaemon(true);
            return t;
        });
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────

    private static boolean eq(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    /**
     * Starts polling. Safe to call on an already-running monitor (no-op).
     */
    public synchronized void start(int poolMilliseconds) {
        if (running) return;
        running = true;
        pollTask = scheduler.scheduleAtFixedRate(
                this::poll, 0, poolMilliseconds, TimeUnit.MILLISECONDS);
        LOG.info("DeviceMonitor started.");
    }

    public synchronized void start(){
        start(POLL_INTERVAL_MILLISECONDS);
    }

    /**
     * Stops polling. The monitor can be restarted via {@link #start()}.
     */
    public synchronized void stop() {
        if (!running) return;
        running = false;
        if (pollTask != null) {
            pollTask.cancel(false);
            pollTask = null;
        }
        LOG.info("DeviceMonitor stopped.");
    }

    // ── Core polling loop ──────────────────────────────────────────────────

    /**
     * Returns {@code true} while the monitor is actively polling.
     */
    public boolean isRunning() {
        return running;
    }

    // ── Change detection & dispatch ────────────────────────────────────────

    private void poll() {
        try {
            PlayerStatus current = client.getPlayerStatus();
            PlayerStatus previous = lastStatus;
            lastStatus = current;

            if (previous == null) {
                // First successful poll – record initial state, no diff yet.
                initStopTimer(current);
                return;
            }

            dispatchChanges(previous, current);
            updateStopTimer(current);

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "DeviceMonitor: poll error", ex);
            onPollError(ex);
        }
    }

    // ── Standby timer ──────────────────────────────────────────────────────

    private void dispatchChanges(PlayerStatus prev, PlayerStatus curr) {

        boolean statusChanged = !eq(prev.getStatus(), curr.getStatus());
        boolean volumeChanged = !eq(prev.getVolume(), curr.getVolume());
        boolean muteChanged = !eq(prev.getMute(), curr.getMute());
        boolean modeChanged = !eq(prev.getMode(), curr.getMode());
        boolean trackChanged = !eq(prev.getTitle(), curr.getTitle());
        boolean loopChanged = !eq(prev.getLoop(), curr.getLoop());

        // ── Playback state transitions ──────────────────────────────────
        if (statusChanged) {
            onPlaybackStatusChanged(prev, curr);
            if (prev.isPlaying() && curr.isPaused()) onPaused(curr);
            if (prev.isPaused() && curr.isPlaying()) onResumed(curr);
            if (!prev.isStopped() && curr.isStopped()) onStopped(curr);
            if (prev.isStopped() && curr.isPlaying()) onStartedPlaying(curr);
            if (!prev.isLoading() && curr.isLoading()) onLoading(curr);
        }

        // ── Volume / mute ────────────────────────────────────────────────
        if (volumeChanged) onVolumeChanged(prev.getVolumeInt(), curr.getVolumeInt());
        if (muteChanged) {
            if (curr.isMuted()) onMuted();
            else onUnmuted();
        }

        // ── Source / mode ────────────────────────────────────────────────
        if (modeChanged) onModeChanged(prev.getPlaybackMode(), curr.getPlaybackMode());

        // ── Track ────────────────────────────────────────────────────────
        if (trackChanged) onTrackChanged(curr);

        // ── Loop ─────────────────────────────────────────────────────────
        if (loopChanged) onLoopModeChanged(curr.getLoopMode());
    }

    private void initStopTimer(PlayerStatus status) {
        if (status.isStopped()) {
            stoppedSince = Instant.now();
            standbyTriggered = false;
        }
    }

    // ── Placeholder callbacks (override these) ─────────────────────────────

    private void updateStopTimer(PlayerStatus current) {
        if (current.isStopped()) {
            // Device is stopped – start or continue the timer.
            if (stoppedSince == null) {
                stoppedSince = Instant.now();
                standbyTriggered = false;
            } else if (!standbyTriggered) {
                long minutesStopped = Duration.between(stoppedSince, Instant.now()).toMinutes();
                if (minutesStopped >= STANDBY_TIMEOUT_MINUTES) {
                    standbyTriggered = true;
                    safeCall(this::onStandby, "onStandby");
                }
            }
        } else {
            // Device is not stopped – reset the timer.
            stoppedSince = null;
            standbyTriggered = false;
        }
    }

    /**
     * Called whenever the raw playback status string changes
     * (play → pause, pause → stop, etc.).
     *
     * @param previous previous status snapshot
     * @param current  new status snapshot
     */
    protected void onPlaybackStatusChanged(PlayerStatus previous, PlayerStatus current) {
        LOG.fine(() -> "onPlaybackStatusChanged: " + previous.getStatus()
                + " → " + current.getStatus());
    }

    /**
     * Called when the device starts playing
     * (from stopped, paused, or loading).
     */
    protected void onStartedPlaying(PlayerStatus status) {
        LOG.fine(() -> "onStartedPlaying: vol=" + status.getVolumeInt()
                + " mode=" + status.getPlaybackMode());
    }

    /**
     * Called when playback is paused.
     */
    protected void onPaused(PlayerStatus status) {
        LOG.fine(() -> "onPaused: pos=" + status.getCurrentPositionMs() + "ms");
    }

    /**
     * Called when paused playback resumes.
     */
    protected void onResumed(PlayerStatus status) {
        LOG.fine(() -> "onResumed: pos=" + status.getCurrentPositionMs() + "ms");
    }

    /**
     * Called when playback stops completely.
     * Note: after {@value #STANDBY_TIMEOUT_MINUTES} minutes in this state,
     * {@link #onStandby()} is also called.
     */
    protected void onStopped(PlayerStatus status) {
        LOG.fine("onStopped");
    }

    /**
     * Called when the device is buffering / loading a new stream.
     */
    protected void onLoading(PlayerStatus status) {
        LOG.fine("onLoading");
    }

    /**
     * Called when the device has been stopped for
     * {@value #STANDBY_TIMEOUT_MINUTES} minutes without resuming.
     *
     * <p>Override this to cut power relays, dim displays, notify a home
     * automation system, etc.
     */
    protected void onStandby() {
        LOG.info("onStandby: device idle for " + STANDBY_TIMEOUT_MINUTES + " minutes.");
    }

    /**
     * Called when the volume level changes.
     *
     * @param previous volume before the change (0–100)
     * @param current  new volume (0–100)
     */
    protected void onVolumeChanged(int previous, int current) {
        LOG.fine(() -> "onVolumeChanged: " + previous + " → " + current);
    }

    /**
     * Called when the device is muted.
     */
    protected void onMuted() {
        LOG.fine("onMuted");
    }

    /**
     * Called when the device is unmuted.
     */
    protected void onUnmuted() {
        LOG.fine("onUnmuted");
    }

    /**
     * Called when the playback source/mode changes
     * (e.g. Bluetooth → Spotify, Wi-Fi → optical in).
     *
     * @param previous previous playback mode
     * @param current  new playback mode
     */
    protected void onModeChanged(PlayerStatus.PlaybackMode previous,
                                 PlayerStatus.PlaybackMode current) {
        LOG.fine(() -> "onModeChanged: " + previous + " → " + current);
    }

    /**
     * Called when the active track in a playlist changes.
     */
    protected void onTrackChanged(PlayerStatus status) {
        LOG.fine(() -> "onTrackChanged: index=" + status.getPlaylistCurrent());
    }

    /**
     * Called when the loop/shuffle mode changes.
     */
    protected void onLoopModeChanged(PlayerStatus.LoopMode newMode) {
        LOG.fine(() -> "onLoopModeChanged: " + newMode);
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Called when the polling request fails (network error, device unreachable).
     *
     * @param error the exception that was thrown
     */
    protected void onPollError(Exception error) {
        LOG.log(Level.WARNING, "onPollError: " + error.getMessage());
    }

    /**
     * Calls a Runnable and logs any exception thrown by an overridden callback.
     */
    private void safeCall(Runnable r, String name) {
        try {
            r.run();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Exception in " + name, ex);
        }
    }
}