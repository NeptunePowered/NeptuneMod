/*
 * This file is part of NeptuneVanilla, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015-2016, Jamie Mansfield <https://github.com/jamierocks>
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
package org.neptunepowered.vanilla.server;

import com.google.common.collect.Lists;
import net.canarymod.Canary;
import net.visualillusionsent.utils.TaskManager;

import java.util.List;

/**
 * A manager for server timers.
 */
public final class ServerTimerManager {

    private static final List<String> timers = Lists.newArrayList();

    public static void setTimer(String timerName, int duration) {
        if (ServerTimerManager.timers.contains(timerName)) {
            Canary.log.info("A timer with the name: " + timerName + " is already running!");
            return;
        }

        TaskManager.scheduleDelayedTaskInSeconds(new ServerTimer(timerName), duration);
        ServerTimerManager.timers.add(timerName);
    }

    public static boolean hasTimerFinished(String timerName) {
        return !ServerTimerManager.timers.contains(timerName);
    }

    private static class ServerTimer implements Runnable {

        private final String name;

        ServerTimer(String name) {
            this.name = name;
        }

        @Override
        public synchronized void run() {
            ServerTimerManager.timers.remove(this.name);
        }

    }

}
