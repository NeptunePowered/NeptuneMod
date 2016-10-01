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
package org.neptunepowered.vanilla.mixin.minecraft.command;

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

@Mixin(CommandHandler.class)
public class MixinCommandHandler {

    @Shadow @Final private Map<String, ICommand> commandMap;

    /**
     * @author jamierocks - 15th May 2015
     * @reason Add Canary tab-completion results
     */
    @Overwrite
    public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
        String[] astring = input.split(" ", -1);
        String s = astring[0];

        if (astring.length == 1) {
            List<String> list = Lists.newArrayList();

            // Neptune - Add Canary command matches
            list.addAll(Canary.commands().matchCommandNames((MessageReceiver) sender, s, false));
            // Neptune - end

            for (Map.Entry<String, ICommand> entry : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey()) && entry.getValue().canCommandSenderUseCommand(sender)) {
                    list.add(entry.getKey());
                }
            }

            return list;
        } else {
            if (astring.length > 1) {
                // Neptune - Tab complete through Canary if possible
                List<String> options = Canary.commands().tabComplete((MessageReceiver) sender, s, CommandHandler.dropFirstString(astring));
                if (options != null) {
                    return options;
                }
                // Neptune - end

                ICommand icommand = this.commandMap.get(s);

                if (icommand != null && icommand.canCommandSenderUseCommand(sender)) {
                    return icommand.addTabCompletionOptions(sender, CommandHandler.dropFirstString(astring), pos);
                }
            }

            return null;
        }
    }

}
