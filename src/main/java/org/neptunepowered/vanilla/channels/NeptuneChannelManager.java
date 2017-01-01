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
package org.neptunepowered.vanilla.channels;

import io.netty.buffer.Unpooled;
import net.canarymod.Canary;
import net.canarymod.api.NetServerHandler;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.channels.ChannelManager;
import net.canarymod.channels.CustomPayloadChannelException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

public class NeptuneChannelManager extends ChannelManager {

    @Override
    public boolean sendCustomPayloadToAllPlayers(String channel, byte[] bytes) {
        boolean toRet = false;

        try {
            if (bytes == null) {
                throw new CustomPayloadChannelException("Invalid Custom Payload: Byte Array is null.");
            }
            if (channel == null || channel.trim().equals("") || channel.equalsIgnoreCase("REGISTER") || channel.equalsIgnoreCase("UNREGISTER")) {
                throw new CustomPayloadChannelException(String.format("Invalid Custom Payload: Invalid channel name of '%s'", channel));
            }
            if (channel.length() > 20) {
                throw new CustomPayloadChannelException(String.format("Invalid Custom Payload: Channel Name too long '%s'", channel));
            }

            if (this.clients.containsKey(channel)) {
                for (NetServerHandler handler : this.clients.get(channel)) {
                    PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                    packetbuffer.writeByteArray(bytes);
                    handler.sendPacket((Packet) new S3FPacketCustomPayload(channel, packetbuffer));
                    toRet = true;
                }
            }
        } catch (CustomPayloadChannelException e) {
            Canary.log.error(e.getMessage(), e);
        }

        return toRet;
    }

    @Override
    public boolean sendCustomPayloadToPlayer(String channel, byte[] bytes, Player player) {
        try {
            if (bytes == null) {
                throw new CustomPayloadChannelException("Invalid Custom Payload: Byte Array is null.");
            }
            if (channel == null || channel.trim().equals("") || channel.equalsIgnoreCase("REGISTER") || channel.equalsIgnoreCase("UNREGISTER")) {
                throw new CustomPayloadChannelException(String.format("Invalid Custom Payload: Invalid channel name of '%s'", channel));
            }
            if (channel.length() > 20) {
                throw new CustomPayloadChannelException(String.format("Invalid Custom Payload: Channel Name too long '%s'", channel));
            }
            if (player == null) {
                throw new CustomPayloadChannelException("Invalid Custom Payload: Player is null.");
            }

            if (this.clients.containsKey(channel)) {
                for (NetServerHandler handler : this.clients.get(channel)) {
                    if (handler.getUser().equals(player)) {
                        final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                        packetbuffer.writeByteArray(bytes);
                        handler.sendPacket((Packet) new S3FPacketCustomPayload(channel, packetbuffer));
                        return true;
                    }
                }
            }
        } catch (CustomPayloadChannelException e) {
            Canary.log.error(e.getMessage(), e);
        }

        return false;
    }

}
