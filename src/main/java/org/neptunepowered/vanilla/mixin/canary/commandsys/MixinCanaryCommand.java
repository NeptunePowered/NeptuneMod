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
package org.neptunepowered.vanilla.mixin.canary.commandsys;

import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.CanaryCommand;
import net.canarymod.commandsys.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.List;

@Mixin(CanaryCommand.class)
public abstract class MixinCanaryCommand implements ICommand {

    @Shadow(remap = false) public Command meta;

    @Shadow(remap = false)
    protected abstract boolean parseCommand(MessageReceiver caller, String[] parameters);

    @Shadow(remap = false)
    public abstract boolean canUse(MessageReceiver msgrec);

    @Shadow(remap = false)
    protected abstract List<String> tabComplete(MessageReceiver msgrec, String[] args);

    @Shadow(remap = false)
    public abstract String getLocaleDescription();

    @Override
    public String getCommandName() {
        return this.meta.aliases()[0];
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return this.getLocaleDescription();
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList(this.meta.aliases());
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings)
            throws CommandException {
        this.parseCommand((MessageReceiver) iCommandSender, strings);
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return this.canUse((MessageReceiver) iCommandSender);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer minecraftServer, ICommandSender iCommandSender,
            String[] strings, BlockPos blockPos) {
        return this.tabComplete((MessageReceiver) iCommandSender, strings);
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    @Shadow
    public abstract int compareTo(ICommand o);
}
