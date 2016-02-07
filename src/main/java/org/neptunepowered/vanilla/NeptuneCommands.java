/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
package org.neptunepowered.vanilla;

import net.canarymod.Canary;
import net.canarymod.chat.ChatFormat;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;

public class NeptuneCommands implements CommandListener {

    @Command(aliases = { "neptune" },
            description = "Neptune command",
            permissions = { "neptune.command.base" },
            toolTip = "/neptune [info]")
    public void baseCommand(MessageReceiver caller, String[] args) {
        this.infoCommand(caller, args);
    }

    @Command(aliases = { "info" },
            parent = "neptune",
            description = "info subcommand",
            permissions = { "neptune.command.info" },
            toolTip = "/neptune info")
    public void infoCommand(MessageReceiver caller, String[] args) {
        caller.message(String.format("%s%s=== %s %s ===",
                ChatFormat.BOLD, ChatFormat.BLUE, Canary.getImplementationTitle(), Canary.getImplementationVersion()));
        caller.message("Website: https://www.neptunepowered.org/");
        caller.message("Lead Developer: Jamie Mansfield");
    }
}
