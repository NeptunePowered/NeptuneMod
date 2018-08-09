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
package org.neptunepowered.vanilla.mixin.core.server.management;

import com.mojang.authlib.GameProfile;
import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.Translator;
import net.canarymod.api.ConfigurationManager;
import net.canarymod.api.PlayerListAction;
import net.canarymod.api.PlayerListData;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.bansystem.Ban;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.config.Configuration;
import net.canarymod.config.ServerConfiguration;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.PlayerListHook;
import net.canarymod.hook.player.PreConnectionHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;
import org.apache.logging.log4j.Logger;
import org.neptunepowered.vanilla.util.helper.StatisticsHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(ServerConfigurationManager.class)
@Implements(@Interface(iface = ConfigurationManager.class, prefix = "config$"))
public abstract class MixinServerConfigurationManager implements ConfigurationManager {

    @Shadow @Final public static Logger logger;
    @Shadow @Final private static SimpleDateFormat dateFormat;

    @Shadow @Final private MinecraftServer mcServer;
    @Shadow @Final public List<EntityPlayerMP> playerEntityList;
    @Shadow @Final private Map<UUID, StatisticsFile> playerStatFiles;
    @Shadow protected int maxPlayers;

    @Shadow public abstract int getCurrentPlayerCount();
    @Shadow public abstract List<EntityPlayerMP> getPlayerList();
    @Shadow public abstract EntityPlayerMP getPlayerByUsername(String username);
    @Shadow public abstract int getMaxPlayers();

    /**
     * @author jamierocks - 8th May 2016
     * @reason Complete overwrite to use Canary internals.
     */
    @Overwrite
    public String allowUserToConnect(SocketAddress address, GameProfile profile) {
        final ServerConfiguration srv = Configuration.getServerConfig();
        final String ip = ((InetSocketAddress) address).getAddress().getHostAddress();

        final PreConnectionHook hook = (PreConnectionHook) new PreConnectionHook(ip, profile.getName(), profile.getId(),
                DimensionType.NORMAL, Canary.getServer().getDefaultWorldName()).call();

        if (hook.getKickReason() != null) {
            return hook.getKickReason();
        }

        if (Canary.bans().isBanned(profile.getId().toString())) {
            final Ban ban = Canary.bans().getBan(profile.getId().toString());

            if (ban.getExpiration() != -1) {
                return ban.getReason() + ", " +
                        srv.getBanExpireDateMessage() + ToolBox.formatTimestamp(ban.getExpiration());
            }
            return ban.getReason();
        }

        if (Canary.bans().isIpBanned(ip)) {
            return Translator.translate(srv.getDefaultBannedMessage());
        }

        if (!Canary.whitelist().isWhitelisted(profile.getId().toString())
                && Configuration.getServerConfig().isWhitelistEnabled()) {
            return srv.getWhitelistMessage();
        }

        if (this.playerEntityList.size() >= this.maxPlayers) {
            if (Canary.reservelist().isSlotReserved(profile.getId().toString())
                    && Configuration.getServerConfig().isReservelistEnabled()) {
                return null;
            }

            return srv.getServerFullMessage();
        }

        return null;
    }

    /**
     * @author jamierocks - 28th October 2016
     * @reason Use the global stats directory
     */
    @Overwrite
    public StatisticsFile getPlayerStatsFile(EntityPlayer playerIn) {
        final UUID uuid = playerIn.getUniqueID();
        StatisticsFile statisticsFile = this.playerStatFiles.get(uuid);

        if (statisticsFile == null) {
            statisticsFile = StatisticsHelper.getStatisticsFile(playerIn.getUniqueID(), playerIn.getName());
            this.playerStatFiles.put(uuid, statisticsFile);
        }

        return statisticsFile;
    }

    @Redirect(method = "playerLoggedOut", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;"
                    + "sendPacketToAllPlayers(Lnet/minecraft/network/Packet;)V"
    ))
    private void firePlayerListData(ServerConfigurationManager manager, net.minecraft.network.Packet packetIn, EntityPlayerMP playerIn) {
        PlayerListData playerListData = ((Player) playerIn).getPlayerListData(PlayerListAction.REMOVE_PLAYER);
        for (EntityPlayerMP playerMP : manager.playerEntityList) {
            PlayerListHook playerListHook = new PlayerListHook(playerListData.copy(), (Player) playerMP);
            if (!playerListHook.call().isCanceled()) {
                S38PacketPlayerListItem packet = new S38PacketPlayerListItem();
                packet.action = S38PacketPlayerListItem.Action.valueOf(PlayerListAction.REMOVE_PLAYER.name());
                WorldSettings.GameType gameType =
                        WorldSettings.GameType.getByID(playerListHook.getData().getMode().getId());
                IChatComponent iChatComponent = playerListHook.getData().displayNameSet() ?
                        (IChatComponent) playerListHook.getData().getDisplayName() : null;
                packet.players.add(packet.new AddPlayerData(playerListHook.getData()
                        .getProfile(), playerListHook.getData().getPing(), gameType, iChatComponent));
                playerMP.playerNetServerHandler.sendPacket(packet);
            }
        }
    }

    @Inject(method = "initializeConnectionToPlayer", at = @At("RETURN"))
    private void sendMOTD(NetworkManager netManager, EntityPlayerMP playerIn, CallbackInfo info) {
        Canary.motd().sendMOTD((MessageReceiver) playerIn);
    }

    @Redirect(method = "initializeConnectionToPlayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;"
                    + "sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"
    ))
    private void fireConnectionHook(ServerConfigurationManager manager, IChatComponent component, NetworkManager netManager, EntityPlayerMP playerIn) {
        ConnectionHook hook = (ConnectionHook)
                new ConnectionHook(
                        (Player) playerIn, component.getUnformattedTextForChat(), false // TODO: check first time
                ).call();
        if (!hook.isHidden()) {
            manager.sendChatMsg(component);
        }
    }

    @Override
    public void sendPacketToAllInWorld(String world, Packet packet) {

    }

    @Override
    public int getNumPlayersOnline() {
        return this.getCurrentPlayerCount();
    }

    @Override
    public Player getPlayerByName(String name) {
        return (Player) this.getPlayerByUsername(name);
    }

    @Override
    public List<Player> getAllPlayers() {
        return (List) this.getPlayerList();
    }

    @Intrinsic
    public int config$getMaxPlayers() {
        return this.getMaxPlayers();
    }

    @Override
    public void markBlockNeedsUpdate(int x, int y, int z, DimensionType dimension, String world) {

    }

    @Override
    public void switchDimension(Player player, World world, boolean createPortal) {

    }

}
