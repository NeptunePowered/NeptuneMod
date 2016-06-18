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
package org.neptunepowered.vanilla.mixin.minecraft.server.network;

import com.google.gson.Gson;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.canarymod.config.Configuration;
import net.canarymod.hook.system.ServerListPingHook;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.server.network.NetHandlerStatusServer;
import net.minecraft.util.ChatComponentText;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetSocketAddress;

@Mixin(NetHandlerHandshakeTCP.class)
public abstract class MixinNetHandlerHandshakeTCP {

    private static final Gson gson = new Gson();

    @Shadow private MinecraftServer server;
    @Shadow private NetworkManager networkManager;

    /**
     * Overwrite to allow for the {@link ServerListPingHook} and Bungeecord support.
     *
     * @author jamierocks
     */
    @Overwrite
    public void processHandshake(C00Handshake packetIn) {
        IMixinNetworkManager info = (IMixinNetworkManager) this.networkManager;
        info.setProtocolVersion(packetIn.getProtocolVersion());
        info.setHostnamePinged(packetIn.ip);
        info.setPortPinged(packetIn.port);

        switch (packetIn.getRequestedState()) {
            case LOGIN:
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);

                if (packetIn.getProtocolVersion() > 47) {
                    ChatComponentText chatcomponenttext = new ChatComponentText("Outdated server! I'm still on 1.8.9");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
                    this.networkManager.closeChannel(chatcomponenttext);
                } else if (packetIn.getProtocolVersion() < 47) {
                    ChatComponentText chatcomponenttext1 = new ChatComponentText("Outdated client! Please use 1.8.9");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext1));
                    this.networkManager.closeChannel(chatcomponenttext1);
                } else {
                    this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));

                    // Neptune - start (Bungeecord support)
                    if (Configuration.getServerConfig().getBungeecordSupport()) {
                        String[] split = packetIn.ip.split("\00");
                        if (split.length >= 3) {
                            packetIn.ip = split[0];
                            ((IMixinNetworkManager) this.networkManager)
                                    .setRemoteAddress(new InetSocketAddress(split[1],
                                            ((InetSocketAddress) this.networkManager.getRemoteAddress()).getPort()));
                            ((IMixinNetworkManager) this.networkManager)
                                    .setSpoofedUUID(UUIDTypeAdapter.fromString(split[2]));

                            if (split.length == 4) {
                                ((IMixinNetworkManager) this.networkManager)
                                        .setSpoofedProfile(gson.fromJson(split[3], Property[].class));
                            }
                        }
                        else {
                            ChatComponentText chatcomponenttext =
                                    new ChatComponentText("If you wish to use IP forwarding, please enable it in your"
                                            + " BungeeCord config as well!");
                            this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
                            this.networkManager.closeChannel(chatcomponenttext);
                        }
                    }
                    // Neptune - end
                }

                break;
            case STATUS:
                this.networkManager.setConnectionState(EnumConnectionState.STATUS);
                this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
                break;
            default:
                throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
        }
    }
}
