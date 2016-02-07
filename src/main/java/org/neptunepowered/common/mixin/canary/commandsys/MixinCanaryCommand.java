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
package org.neptunepowered.common.mixin.canary.commandsys;

import com.google.common.collect.Lists;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.CanaryCommand;
import net.canarymod.commandsys.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
    public String getCommandUsage(ICommandSender sender) {
        return this.getLocaleDescription();
    }

    @Override
    public List getCommandAliases() {
        return Lists.newArrayList(this.meta.aliases());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        this.parseCommand((MessageReceiver) sender, args);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return this.canUse((MessageReceiver) sender);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return this.tabComplete((MessageReceiver) sender, args);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
