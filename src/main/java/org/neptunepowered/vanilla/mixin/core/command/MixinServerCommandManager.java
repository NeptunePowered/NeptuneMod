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

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerCommandManager.class)
public abstract class MixinServerCommandManager extends MixinCommandHandler {

    /**
     * @author jamierocks - 1st September 2016
     * @reason Log command feedback
     */
    @Overwrite
    public void notifyOperators(ICommandSender sender, ICommand command, int flags, String msgFormat, Object... msgParams) {
        final MinecraftServer server = MinecraftServer.getServer();

        final IChatComponent feedback =
                new ChatComponentTranslation("chat.type.admin", sender.getName(), new ChatComponentTranslation(msgFormat, msgParams));
        feedback.getChatStyle().setColor(EnumChatFormatting.GRAY);
        feedback.getChatStyle().setItalic(true);

        if (sender.sendCommandFeedback()) {
            for (final EntityPlayer player : server.getConfigurationManager().getPlayerList()) {
                if (player != sender && server.getConfigurationManager().canSendCommands(player.getGameProfile()) && command
                        .canCommandSenderUseCommand(sender)) {
                    final boolean broadcastConsoleToOps = sender instanceof MinecraftServer &&
                            MinecraftServer.getServer().shouldBroadcastConsoleToOps();
                    final boolean broadcastRconToOps = sender instanceof RConConsoleSource &&
                            MinecraftServer.getServer().shouldBroadcastRconToOps();

                    if (broadcastConsoleToOps || broadcastRconToOps || !(sender instanceof RConConsoleSource) && !(sender instanceof MinecraftServer)) {
                        player.addChatMessage(feedback);
                    }
                }
            }
        }

        // Neptune - Always log command feedback
        //if (sender != server && server.worldServers[0].getGameRules().getBoolean("logAdminCommands")) {
        server.addChatMessage(feedback);
        //}
        // Neptune - end

        final boolean shouldSendCommandFeedback = sender instanceof CommandBlockLogic ?
                ((CommandBlockLogic) sender).shouldTrackOutput() :
                server.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");

        if ((flags & 1) != 1 && shouldSendCommandFeedback || sender instanceof MinecraftServer) {
            sender.addChatMessage(new ChatComponentTranslation(msgFormat, msgParams));
        }
    }

}
