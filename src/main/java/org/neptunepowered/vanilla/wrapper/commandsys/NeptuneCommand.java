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
package org.neptunepowered.vanilla.wrapper.commandsys;

import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.CanaryCommand;
import net.canarymod.commandsys.DynamicCommandAnnotation;
import net.canarymod.commandsys.TabCompleteDispatch;
import net.canarymod.commandsys.TabCompleteException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import org.neptunepowered.vanilla.Neptune;
import org.neptunepowered.vanilla.NeptuneVanilla;

import java.util.List;

public class NeptuneCommand extends CanaryCommand {

    private final ICommand command;

    public NeptuneCommand(final ICommand command) {
        super(new DynamicCommandAnnotation(
                        command.getCommandAliases().toArray(new String[command.getCommandAliases().size()]),
                        new String[0], "", "", "", "", new String[0], 0, 0, "", 2), Neptune.minecraftCommandOwner, null,
                new TabCompleteDispatch() {
                    @Override
                    public List<String> complete(MessageReceiver msgrec, String[] args) throws TabCompleteException {
                        return command.getTabCompletionOptions(NeptuneVanilla.getServer(), (ICommandSender) msgrec, args, BlockPos.ORIGIN);
                    }
                });
        this.command = command;
    }

    @Override
    protected void execute(MessageReceiver caller, String[] parameters) {
        try {
            this.command.execute(NeptuneVanilla.getServer(), (ICommandSender) caller, parameters);
        } catch (CommandException e) {
            Canary.log.error("Eh, something has broken :(", e);
        }
    }
}
