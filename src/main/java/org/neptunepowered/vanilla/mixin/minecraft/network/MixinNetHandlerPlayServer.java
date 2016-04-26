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
package org.neptunepowered.vanilla.mixin.minecraft.network;

import net.canarymod.Canary;
import net.canarymod.api.NetServerHandler;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketRespawn;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatComponent;
import org.neptunepowered.vanilla.wrapper.packet.NeptunePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements NetServerHandler {

    @Shadow public NetworkManager netManager;
    @Shadow public EntityPlayerMP playerEntity;

    @Shadow
    public abstract void sendPacket(final net.minecraft.network.Packet packetIn);

    @Override
    public void sendPacket(Packet packet) {
        sendPacket(((NeptunePacket) packet).getHandle());
    }

    @Override
    public void handleChat(Packet chatPacket) {
        if (!(((NeptunePacket) chatPacket).getHandle() instanceof SPacketChat)) {
            return;
        }

        sendPacket(chatPacket);
    }

    @Override
    public void handleCommand(String[] command) {
        getUser().executeCommand(command);
    }

    @Override
    public void handleRespawn(Packet respawnPacket) {
        if (!(((NeptunePacket) respawnPacket).getHandle() instanceof SPacketRespawn)) {
            return;
        }

        sendPacket(respawnPacket);
    }

    @Override
    public Player getUser() {
        return (Player) playerEntity;
    }

    @Override
    public void sendMessage(String message) {
        sendMessage(Canary.factory().getChatComponentFactory().compileChatComponent(message));
    }

    @Override
    public void sendMessage(ChatComponent chatComponent) {
        sendPacket(new SPacketChat(((NeptuneChatComponent) chatComponent).getHandle()));
    }

    @Override
    public SocketAddress getSocketAdress() {
        return netManager.getRemoteAddress();
    }
}
