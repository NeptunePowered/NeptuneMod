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
package org.neptunepowered.vanilla;

import net.canarymod.Canary;
import net.canarymod.chat.ChatFormat;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.commandsys.TabComplete;
import net.canarymod.commandsys.TabCompleteHelper;

import java.util.List;

public final class NeptuneCommands implements CommandListener {

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
        caller.message(String.format("%s%s---- %s %s ----",
                ChatFormat.BOLD, ChatFormat.BLUE, Canary.getImplementationTitle(), Canary.getImplementationVersion()));
        caller.message(ChatFormat.GOLD + "Website:" + ChatFormat.WHITE + " https://www.neptunepowered.org/");
        caller.message(ChatFormat.GOLD + "Lead Developer:" + ChatFormat.WHITE + " Jamie Mansfield");
        caller.message(ChatFormat.GOLD + "With Contributions By:" + ChatFormat.WHITE + " 14mRh4X0r, Minecrell");
        caller.message(ChatFormat.RED + "Copyright (c) 2015-2016, Jamie Mansfield");
        caller.message(ChatFormat.RED + "Licensed under the MIT license.");
    }

    @TabComplete(commands = { "neptune" })
    public List<String> tabComplete(MessageReceiver caller, String[] args) {
        if (args.length == 1) {
            return TabCompleteHelper.matchTo(args, new String[] { "info" });
        }
        return null;
    }
}
