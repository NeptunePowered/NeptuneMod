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
import net.canarymod.api.PlayerReference;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.config.Configuration;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.hook.player.KickHook;
import net.canarymod.hook.player.PlayerIdleHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetHandlerPlayServer;
import org.neptunepowered.vanilla.util.helper.NetHandlerPlayServerHelper;
import org.neptunepowered.vanilla.chat.NeptuneChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.SocketAddress;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements NetServerHandler, IMixinNetHandlerPlayServer {

    @Shadow @Final private static Logger logger;

    @Shadow @Final public NetworkManager netManager;
    @Shadow @Final private MinecraftServer serverController;
    @Shadow public EntityPlayerMP playerEntity;
    @Shadow private int chatSpamThresholdCount;

    @Shadow
    public abstract void sendPacket(final net.minecraft.network.Packet packetIn);

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;"
            + "kickPlayerFromServer(Ljava/lang/String;)V"))
    public void handlePlayerIdleHook(NetHandlerPlayServer playServer, String reason) {
        final long timeIdle = MinecraftServer.getCurrentTimeMillis() - playServer.playerEntity.getLastActiveTime();
        if (!((Player) playServer.playerEntity).canIgnoreRestrictions()) {
            PlayerIdleHook idleHook = (PlayerIdleHook) new PlayerIdleHook((Player) playServer.playerEntity, timeIdle).call();
            if (!idleHook.isCanceled()) {
                this.kickPlayerFromServer("You have been idle for too long!");
            }
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

    /**
     * Overwrite to fire the DisconnectHook.
     *
     * @author jamierocks
     */
    @Overwrite
    public void onDisconnect(IChatComponent reason) {
        logger.info(this.playerEntity.getName() + " lost connection: " + reason);
        this.serverController.refreshStatusNextTick();
        ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.left",
                new Object[]{this.playerEntity.getDisplayName()});
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);

        // Neptune - start
        DisconnectionHook hook = (DisconnectionHook) new DisconnectionHook(
                (Player) this.playerEntity,
                reason.getUnformattedText(),
                chatcomponenttranslation.getUnformattedText()).call();

        if (!hook.isHidden()) {
            this.serverController.getConfigurationManager().sendChatMsg(chatcomponenttranslation);
        }
        // Neptune - end

        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);

        if (this.serverController.isSinglePlayer() &&
                this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            logger.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }

    /**
     * Overwrite to pass chat to Canary.
     *
     * @author jamierocks
     */
    @Overwrite
    public void processChatMessage(C01PacketChatMessage packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (NetHandlerPlayServer) (Object) this, this.playerEntity.getServerForPlayer());

        if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            this.sendPacket(new S02PacketChat(chatcomponenttranslation));
            return;
        }

        this.chatSpamThresholdCount += 20;

        final boolean op = Canary.ops().isOpped((PlayerReference) this.playerEntity);
        final boolean ignore = ((Player) this.playerEntity).canIgnoreRestrictions();
        final String spamProtectionLevel = Configuration.getServerConfig().getSpamProtectionLevel();

        if (spamProtectionLevel.equalsIgnoreCase("all") || (spamProtectionLevel.equalsIgnoreCase("default") && !(op || ignore))) {
            if (this.chatSpamThresholdCount > 200) {
                this.kickPlayerFromServer("disconnect.spam");
                return;
            }
        }
        this.playerEntity.markPlayerActive();
        ((Player) this.playerEntity).chat(packetIn.getMessage());
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
        return (Player) this.playerEntity;
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
