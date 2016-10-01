/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.EvictingQueue;
import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.config.Configuration;
import net.canarymod.plugin.Plugin;
import org.neptunepowered.lib.config.TimingsConfiguration;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class NeptuneTimingsFactory implements TimingsFactory {

    private final int MAX_HISTORY_FRAMES = 12;
    public final Timing NULL_HANDLER = new NullTimingHandler();
    private boolean timingsEnabled = true;
    private boolean verboseEnabled = true;
    private int historyInterval = -1;
    private int historyLength = -1;

    public void init() {
        final TimingsConfiguration config = Configuration.getTimingsConfig();
        setVerboseTimingsEnabled(config.isVerbose());
        setTimingsEnabled(config.isEnabled());
        setHistoryInterval(config.getHistoryInterval());
        setHistoryLength(config.getHistoryLength());

        Canary.log.debug("Neptune Timings: " + this.timingsEnabled +
                         " - Verbose: " + this.verboseEnabled +
                         " - Interval: " + timeSummary(this.historyInterval / 20) +
                         " - Length: " + timeSummary(this.historyLength / 20));
    }

    private static String timeSummary(int seconds) {
        String time = "";
        if (seconds > 60 * 60) {
            time += TimeUnit.SECONDS.toHours(seconds) + "h";
            seconds /= 60;
        }

        if (seconds > 0) {
            time += TimeUnit.SECONDS.toMinutes(seconds) + "m";
        }
        return time;
    }

    private static Plugin checkPlugin(Object plugin) {
        checkArgument(plugin instanceof Plugin, "Provided object is not a plugin!");
        return (Plugin) plugin;
    }

    @Override
    public Timing of(Object pluginObj, String name, @Nullable Timing groupHandler) {
        Plugin plugin = checkPlugin(pluginObj);
        return TimingsManager.getHandler(plugin.getName().toLowerCase(), name, groupHandler, true);
    }

    @Override
    public boolean isTimingsEnabled() {
        return this.timingsEnabled;
    }

    @Override
    public void setTimingsEnabled(boolean enabled) {
        this.timingsEnabled = enabled;
        reset();
    }

    @Override
    public boolean isVerboseTimingsEnabled() {
        return this.verboseEnabled;
    }

    @Override
    public void setVerboseTimingsEnabled(boolean enabled) {
        this.verboseEnabled = enabled;
        TimingsManager.needsRecheckEnabled = true;
    }

    @Override
    public int getHistoryInterval() {
        return this.historyInterval;
    }

    @Override
    public void setHistoryInterval(int interval) {
        this.historyInterval = Math.max(20 * 60, interval);
        // Recheck the history length with the new Interval
        if (this.historyLength != -1) {
            setHistoryLength(this.historyLength);
        }
    }

    @Override
    public int getHistoryLength() {
        return this.historyLength;
    }

    @Override
    public void setHistoryLength(int length) {
        // Cap at 12 History Frames, 1 hour at 5 minute frames.
        int maxLength = this.historyInterval * this.MAX_HISTORY_FRAMES;
        // For special cases of servers with special permission to bypass the
        // max.
        // This max helps keep data file sizes reasonable for processing on
        // Aikar's Timing parser side.
        // Setting this will not help you bypass the max unless Aikar has added
        // an exception on the API side.
        if (System.getProperty("timings.bypassMax") != null) {
            maxLength = Integer.MAX_VALUE;
        }
        this.historyLength = Math.max(Math.min(maxLength, length), this.historyInterval);
        Queue<TimingHistory> oldQueue = TimingsManager.HISTORY;
        int frames = (getHistoryLength() / getHistoryInterval());
        if (length > maxLength) {
            Canary.log.warn(
                    "Timings Length too high. Requested " + length + ", max is " + maxLength
                            + ". To get longer history, you must increase your interval. Set Interval to "
                            + Math.ceil(length / this.MAX_HISTORY_FRAMES)
                            + " to achieve this length.");
        }
        TimingsManager.HISTORY = EvictingQueue.create(frames);
        TimingsManager.HISTORY.addAll(oldQueue);
    }

    @Override
    public void reset() {
        TimingsManager.reset();
    }

    @Override
    public void generateReport(MessageReceiver sender) {
        if (sender == null) {
            sender = Canary.getServer();
        }
        TimingsExport.reportTimings(sender);
    }

    public static long getCost() {
        return TimingsExport.getCost();
    }

    public static TimingHandler ofSafe(String name) {
        return ofSafe(null, name, null);
    }

    public static Timing ofSafe(Plugin plugin, String name) {
        return ofSafe(plugin != null ? plugin.getName() : "Minecraft - Invalid Plugin", name);
    }

    public static TimingHandler ofSafe(String name, Timing groupHandler) {
        return ofSafe(null, name, groupHandler);
    }

    public static TimingHandler ofSafe(String groupName, String name) {
        return TimingsManager.getHandler(groupName, name, null, false);
    }

    public static TimingHandler ofSafe(String groupName, String name, Timing groupHandler) {
        return TimingsManager.getHandler(groupName, name, groupHandler, false);
    }

}