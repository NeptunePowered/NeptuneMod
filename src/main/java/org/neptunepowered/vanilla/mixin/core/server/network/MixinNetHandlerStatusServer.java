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
package org.neptunepowered.vanilla.mixin.core.server.network;

import com.mojang.authlib.GameProfile;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.hook.system.ServerListPingHook;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerStatusServer;
import net.minecraft.util.IChatComponent;
import org.neptunepowered.vanilla.bridge.core.network.BridgeNetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.InetSocketAddress;
import java.util.Arrays;

@Mixin(NetHandlerStatusServer.class)
public class MixinNetHandlerStatusServer {

    @Shadow @Final private static IChatComponent EXIT_MESSAGE;
    @Shadow @Final private MinecraftServer server;

    @Redirect(method = "processServerQuery", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"
    ))
    private void handleSeverListPingHook(NetworkManager networkManager, Packet packetIn) {
        ServerListPingHook hook =
                (ServerListPingHook) new ServerListPingHook((InetSocketAddress) networkManager.getRemoteAddress(),
                        ((BridgeNetworkManager) networkManager).bridge$getProtocolVersion(),
                        ((BridgeNetworkManager) networkManager).bridge$getHostnamePinged(),
                        ((BridgeNetworkManager) networkManager).bridge$getPortPinged(),
                        (ChatComponent) this.server.getServerStatusResponse().getServerDescription(),
                        this.server.getServerStatusResponse().getPlayerCountData().getOnlinePlayerCount(),
                        this.server.getServerStatusResponse().getPlayerCountData().getMaxPlayers(),
                        this.server.getServerStatusResponse().getFavicon(),
                        Arrays.asList(this.server.getServerStatusResponse().getPlayerCountData().getPlayers())).call();
        if (hook.isCanceled()) {
            networkManager.closeChannel(null);
            return;
        }

        ServerStatusResponse response = new ServerStatusResponse();
        response.setProtocolVersionInfo(this.server.getServerStatusResponse().getProtocolVersionInfo());
        ServerStatusResponse.PlayerCountData playerCountData = new ServerStatusResponse.PlayerCountData(hook
                .getMaxPlayers(), hook.getCurrentPlayers());
        playerCountData.setPlayers(hook.getProfiles().toArray(new GameProfile[hook.getProfiles().size()]));
        response.setPlayerCountData(playerCountData);
        response.setServerDescription((IChatComponent) hook.getMotd());
        response.setFavicon(hook.getFavicon());

        networkManager.sendPacket(new S00PacketServerInfo(response));
    }

}
