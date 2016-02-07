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

import com.mojang.authlib.GameProfile;
import net.canarymod.hook.system.ServerListPingHook;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerStatusServer;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetworkManager;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetSocketAddress;
import java.util.Arrays;

@Mixin(NetHandlerStatusServer.class)
public class MixinNetHandlerStatusServer {

    @Shadow private MinecraftServer server;
    @Shadow private NetworkManager networkManager;

    @Overwrite
    public void processServerQuery(C00PacketServerQuery packetIn) {
        ServerListPingHook hook =
                (ServerListPingHook) new ServerListPingHook((InetSocketAddress) networkManager.getRemoteAddress(),
                        ((IMixinNetworkManager) networkManager).getProtocolVersion(),
                        ((IMixinNetworkManager) networkManager).getHostnamePinged(),
                        ((IMixinNetworkManager) networkManager).getPortPinged(),
                        new NeptuneChatComponent(server.getServerStatusResponse().getServerDescription()),
                        server.getServerStatusResponse().getPlayerCountData().getOnlinePlayerCount(),
                        server.getServerStatusResponse().getPlayerCountData().getMaxPlayers(),
                        server.getServerStatusResponse().getFavicon(),
                        Arrays.asList(server.getServerStatusResponse().getPlayerCountData().getPlayers())).call();
        if (hook.isCanceled()) {
            networkManager.closeChannel(null);
            return;
        }

        ServerStatusResponse response = new ServerStatusResponse();
        response.setProtocolVersionInfo(server.getServerStatusResponse().getProtocolVersionInfo());
        ServerStatusResponse.PlayerCountData playerCountData = new ServerStatusResponse.PlayerCountData(hook
                .getMaxPlayers(), hook.getCurrentPlayers());
        playerCountData.setPlayers(hook.getProfiles().toArray(new GameProfile[hook.getProfiles().size()]));
        response.setPlayerCountData(playerCountData);
        response.setServerDescription(((NeptuneChatComponent) hook.getMotd()).getHandle());
        response.setFavicon(hook.getFavicon());

        networkManager.sendPacket(new S00PacketServerInfo(response));
    }
}
