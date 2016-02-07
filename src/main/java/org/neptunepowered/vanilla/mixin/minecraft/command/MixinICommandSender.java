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
package org.neptunepowered.vanilla.mixin.minecraft.command;

import net.canarymod.api.Server;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.ChatFormat;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.chat.ReceiverType;
import net.canarymod.exceptions.InvalidInstanceException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ICommandSender.class)
@Implements(@Interface(iface = MessageReceiver.class, prefix = "messagereceiver$"))
public interface MixinICommandSender extends ICommandSender, MessageReceiver {

    @Intrinsic
    default String messagereceiver$getName() {
        return getName();
    }

    @Override
    default void notice(String message) {
        message(ChatFormat.RED + message);
    }

    @Override
    default void notice(CharSequence message) {
        notice(message.toString());
    }

    @Override
    default void notice(CharSequence... messages) {
        for (CharSequence message : messages) {
            notice(message);
        }
    }

    @Override
    default void notice(Iterable<? extends CharSequence> messages) {
        for (CharSequence message : messages) {
            notice(message);
        }
    }

    @Override
    default void message(String message) {
        addChatMessage(new ChatComponentText(message));
    }

    @Override
    default void message(CharSequence message) {
        message(message.toString());
    }

    @Override
    default void message(CharSequence... messages) {
        for (CharSequence message : messages) {
            message(message);
        }
    }

    @Override
    default void message(Iterable<? extends CharSequence> messages) {
        for (CharSequence message : messages) {
            message(message);
        }
    }

    @Override
    default void message(ChatComponent... chatComponents) {
        for (ChatComponent message : chatComponents) {
            message(message.getText());
        }
    }

    @Override
    default boolean hasPermission(String node) {
        //PermissionCheckHook hook = (PermissionCheckHook) new PermissionCheckHook(node, this, false).call();
        //return hook.getResult();
        return true; // Testing
    }

    @Override
    default boolean safeHasPermission(String permission) {
        return true; // Testing
    }

    @Override
    default ReceiverType getReceiverType() {
        if (this instanceof Player) {
            return ReceiverType.PLAYER;
        } else if (this instanceof net.canarymod.api.CommandBlockLogic) {
            return ReceiverType.COMMANDBLOCK;
        } else {
            return ReceiverType.SERVER;
        }
    }

    @Override
    default Player asPlayer() {
        if (this instanceof Player) {
            return (Player) this;
        }
        throw new InvalidInstanceException("This is not a MessageReceiver of the type: PLAYER");
    }

    @Override
    default Server asServer() {
        if (this instanceof Server) {
            return (Server) this;
        }
        throw new InvalidInstanceException("This is not a MessageReceiver of the type: SERVER");
    }

    @Override
    default net.canarymod.api.CommandBlockLogic asCommandBlock() {
        if (this instanceof net.canarymod.api.CommandBlockLogic) {
            return (net.canarymod.api.CommandBlockLogic) this;
        }
        throw new InvalidInstanceException("This is not a MessageReceiver of the type: COMMANDBLOCK");
    }

    @Override
    default String getLocale() {
        return null;
    }

}
