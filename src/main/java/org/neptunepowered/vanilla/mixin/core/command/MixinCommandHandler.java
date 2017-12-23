/*
 * This file is part of NeptuneVanilla, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015-2017, Jamie Mansfield <https://github.com/jamierocks>
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
package org.neptunepowered.vanilla.mixin.core.command;

import com.google.common.collect.Lists;
import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(CommandHandler.class)
public abstract class MixinCommandHandler {

    @Shadow @Final private Map<String, ICommand> commandMap;
    @Shadow @Final private Set<ICommand> commandSet;

    /**
     * @author jamierocks - 15th May 2015
     * @reason Add Canary tab-completion results
     */
    @Overwrite
    public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
        final String[] commandSplit = input.split(" ", -1);
        final String commandName = commandSplit[0];

        if (commandSplit.length == 1) {
            final List<String> matches = Lists.newArrayList();

            // Neptune - Add Canary command matches
            matches.addAll(Canary.commands().matchCommandNames((MessageReceiver) sender, commandName, false));
            // Neptune - end

            for (final Map.Entry<String, ICommand> alias : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(commandName, alias.getKey()) && alias.getValue().canCommandSenderUseCommand(sender)) {
                    matches.add(alias.getKey());
                }
            }

            return matches;
        } else {
            if (commandSplit.length > 1) {
                // Neptune - Tab complete through Canary if possible
                final List<String> options = Canary.commands().tabComplete((MessageReceiver) sender, commandName, CommandHandler.dropFirstString(commandSplit));
                if (options != null) {
                    return options;
                }
                // Neptune - end

                final ICommand command = this.commandMap.get(commandName);
                if (command != null && command.canCommandSenderUseCommand(sender)) {
                    return command.addTabCompletionOptions(sender, CommandHandler.dropFirstString(commandSplit), pos);
                }
            }

            return null;
        }
    }

}
