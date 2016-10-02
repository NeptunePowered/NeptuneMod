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
package org.neptunepowered.vanilla.command;

import co.aikar.timings.NeptuneTimingsFactory;
import co.aikar.timings.Timings;
import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.commandsys.TabComplete;
import net.canarymod.commandsys.TabCompleteHelper;

import java.util.List;

public final class TimingsCommand implements CommandListener {

    @Command(
            aliases = "timings",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings [reset/report/on/off/verbon/verboff/cost]")
    public void timingsCommand(MessageReceiver caller, String[] args) {
        Canary.help().getHelp(caller, "timings");
    }

    @Command(
            parent = "timings",
            aliases = "reset",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings reset")
    public void resetCommand(MessageReceiver caller, String[] args) {
        if (!Timings.isTimingsEnabled()) {
            caller.message("Please enable timings by typing /timings on");
            return;
        }
        Timings.reset();
        caller.message("Timings reset");
    }

    @Command(
            parent = "timings",
            aliases = {"report", "paste"},
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings report")
    public void reportCommand(MessageReceiver caller, String[] args) {
        if (!Timings.isTimingsEnabled()) {
            caller.message("Please enable timings by typing /timings on");
            return;
        }
        Timings.generateReport(caller);
    }

    @Command(
            parent = "timings",
            aliases = "on",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings on")
    public void onCommand(MessageReceiver caller, String[] args) {
        Timings.setTimingsEnabled(true);
        caller.message("Enabled Timings & Reset");
    }

    @Command(
            parent = "timings",
            aliases = "off",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings off")
    public void offCommand(MessageReceiver caller, String[] args) {
        Timings.setTimingsEnabled(false);
        caller.message("Disabled Timings");
    }

    @Command(
            parent = "timings",
            aliases = "verbon",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings verbon")
    public void verbonCommand(MessageReceiver caller, String[] args) {
        if (!Timings.isTimingsEnabled()) {
            caller.message("Please enable timings by typing /timings on");
            return;
        }
        Timings.setVerboseTimingsEnabled(true);
        caller.message("Enabled Verbose Timings");
    }

    @Command(
            parent = "timings",
            aliases = "verboff",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings verboff")
    public void verboffCommand(MessageReceiver caller, String[] args) {
        if (!Timings.isTimingsEnabled()) {
            caller.message("Please enable timings by typing /timings on");
            return;
        }
        Timings.setVerboseTimingsEnabled(false);
        caller.message("Disabled Verbose Timings");
    }

    @Command(
            parent = "timings",
            aliases = "cost",
            permissions = "neptune.command.timings",
            description = "Manages Neptune Timings data to see performance of the server.",
            toolTip = "/timings cost")
    public void costCommand(MessageReceiver caller, String[] args) {
        if (!Timings.isTimingsEnabled()) {
            caller.message("Please enable timings by typing /timings on");
            return;
        }
        caller.message("Timings cost: " + NeptuneTimingsFactory.getCost());
    }

    @TabComplete(commands = { "timings" })
    public List<String> tabComplete(MessageReceiver caller, String[] args) {
        if (args.length == 1) {
            return TabCompleteHelper.matchTo(args, new String[] { "reset", "report", "paste", "on", "off", "verbon", "verboff", "cost" });
        }
        return null;
    }
    
}
