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
import net.canarymod.hook.player.KickHook;
import net.canarymod.hook.player.PlayerIdleHook;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.server.MinecraftServer;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetHandlerPlayServer;
import org.neptunepowered.vanilla.util.helper.NetHandlerPlayServerHelper;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements NetServerHandler, IMixinNetHandlerPlayServer {

    @Shadow public NetworkManager netManager;
    @Shadow public EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer serverController;
    @Shadow private int networkTickCount;
    @Shadow private boolean field_147366_g;
    @Shadow private int field_147378_h;
    @Shadow private long lastPingTime;
    @Shadow private long lastSentPingPacket;
    @Shadow private int chatSpamThresholdCount;
    @Shadow private int itemDropThreshold;

    @Shadow
    public abstract void sendPacket(final net.minecraft.network.Packet packetIn);

    @Shadow
    public abstract long currentTimeMillis();

    /**
     * Overwrite to fire {@link PlayerIdleHook}.
     *
     * @author jamierocks
     */
    @Overwrite
    public void update() {
        this.field_147366_g = false;
        ++this.networkTickCount;
        this.serverController.theProfiler.startSection("keepAlive");

        if ((long) this.networkTickCount - this.lastSentPingPacket > 40L) {
            this.lastSentPingPacket = (long) this.networkTickCount;
            this.lastPingTime = this.currentTimeMillis();
            this.field_147378_h = (int) this.lastPingTime;
            this.sendPacket(new S00PacketKeepAlive(this.field_147378_h));
        }

        this.serverController.theProfiler.endSection();

        if (this.chatSpamThresholdCount > 0) {
            --this.chatSpamThresholdCount;
        }

        if (this.itemDropThreshold > 0) {
            --this.itemDropThreshold;
        }

        long timeIdle = MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime();
        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0
                && timeIdle > (long) (this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60)
                && !((Player) this.playerEntity).canIgnoreRestrictions()) { // Neptune - check if player is immune
            // Neptune - start
            PlayerIdleHook idleHook = (PlayerIdleHook) new PlayerIdleHook((Player) this.playerEntity, timeIdle).call();
            if (!idleHook.isCanceled()) {
                this.kickPlayerFromServer("You have been idle for too long!");
            }
            // Neptune - end
        }
    }

    /**
     * Overwrite to fire the KickHook.
     *
     * @author jamierocks
     */
    @Overwrite
    public void kickPlayerFromServer(String reason) {
        // Fire KickHook
        new KickHook((Player) this.playerEntity, Canary.getServer(), reason).call();

        // Kick player
        this.kickPlayerFromServerWithoutHook(reason);
    }

    @Override
    public void sendPacket(Packet packet) {
        this.sendPacket((net.minecraft.network.Packet) packet);
    }

    @Override
    public void handleChat(Packet chatPacket) {
        if (!(chatPacket instanceof S02PacketChat)) {
            return;
        }

        this.sendPacket(chatPacket);
    }

    @Override
    public void handleCommand(String[] command) {
        this.getUser().executeCommand(command);
    }

    @Override
    public void handleRespawn(Packet respawnPacket) {
        if (!(respawnPacket instanceof S07PacketRespawn)) {
            return;
        }

        this.sendPacket(respawnPacket);
    }

    @Override
    public Player getUser() {
        return (Player) playerEntity;
    }

    @Override
    public void sendMessage(String message) {
        this.sendMessage(Canary.factory().getChatComponentFactory().compileChatComponent(message));
    }

    @Override
    public void sendMessage(ChatComponent chatComponent) {
        this.sendPacket(new S02PacketChat(((NeptuneChatComponent) chatComponent).getHandle()));
    }

    @Override
    public SocketAddress getSocketAdress() {
        return this.netManager.getRemoteAddress();
    }

    @Override
    public void kickPlayerFromServerWithoutHook(String reason) {
        NetHandlerPlayServerHelper.kickPlayerFromServer(this.playerEntity, reason);
    }
}
