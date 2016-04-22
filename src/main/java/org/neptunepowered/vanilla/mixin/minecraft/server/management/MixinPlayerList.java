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
package org.neptunepowered.vanilla.mixin.minecraft.server.management;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.canarymod.Canary;
import net.canarymod.api.ConfigurationManager;
import net.canarymod.api.PlayerListAction;
import net.canarymod.api.PlayerListData;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.PlayerListHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.Logger;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList implements ConfigurationManager {

    @Shadow private static Logger logger;

    @Shadow public List playerEntityList;
    @Shadow public Map uuidToPlayerMap;
    @Shadow private MinecraftServer mcServer;
    @Shadow private Map playerStatFiles;

    @Shadow
    public abstract NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn);

    @Shadow
    protected abstract void setPlayerGameTypeBasedOnOther(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_,
            net.minecraft.world.World worldIn);

    @Shadow
    protected abstract void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn);

    @Shadow
    public abstract void sendChatMsg(ITextComponent component);

    @Shadow
    public abstract void playerLoggedIn(EntityPlayerMP playerIn);

    @Shadow
    public abstract void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn);

    @Shadow
    protected abstract void writePlayerData(EntityPlayerMP playerIn);

    @Shadow
    public abstract int getCurrentPlayerCount();

    @Shadow
    public abstract MinecraftServer getServerInstance();

    @Shadow
    public abstract void updatePermissionLevel(EntityPlayerMP p_187243_1_);

    @Shadow
    public abstract List getPlayerList();

    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn) {
        GameProfile gameprofile = playerIn.getGameProfile();
        PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
        GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();
        playerprofilecache.addEntry(gameprofile);
        NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
        playerIn.interactionManager.setWorld((WorldServer) playerIn.worldObj);
        String s1 = "local";

        if (netManager.getRemoteAddress() != null) {
            s1 = netManager.getRemoteAddress().toString();
        }

        logger.info(playerIn.getName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at ("
                + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
        WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        WorldInfo worldinfo = worldserver.getWorldInfo();
        BlockPos blockpos = worldserver.getSpawnPoint();
        this.setPlayerGameTypeBasedOnOther(playerIn, (EntityPlayerMP) null, worldserver);
        NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
        nethandlerplayserver.sendPacket(
                new SPacketJoinGame(playerIn.getEntityId(), playerIn.interactionManager.getGameType(),
                        worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionType().getId(),
                        worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getTerrainType(),
                        worldserver.getGameRules().getBoolean("reducedDebugInfo")));
        nethandlerplayserver.sendPacket(new SPacketCustomPayload("MC|Brand",
                (new PacketBuffer(Unpooled.buffer())).writeString(this.getServerInstance().getServerModName())));
        nethandlerplayserver
                .sendPacket(new SPacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new SPacketSpawnPosition(blockpos));
        nethandlerplayserver.sendPacket(new SPacketPlayerAbilities(playerIn.capabilities));
        nethandlerplayserver.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
        this.updatePermissionLevel(playerIn);
        playerIn.getStatFile().func_150877_d();
        playerIn.getStatFile().sendAchievements(playerIn);
        this.sendScoreboard((ServerScoreboard) worldserver.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();
        TextComponentTranslation textcomponenttranslation;

        if (!playerIn.getName().equalsIgnoreCase(s)) {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined.renamed",
                    new Object[]{playerIn.getDisplayName(), s});
        } else {
            textcomponenttranslation =
                    new TextComponentTranslation("multiplayer.player.joined", new Object[]{playerIn.getDisplayName()});
        }

        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);

        // Neptune: start
        ConnectionHook hook = (ConnectionHook)
                new ConnectionHook(
                        (Player) playerIn, textcomponenttranslation.getUnformattedText(), false
                ).call();
        if (!hook.isHidden()) {
            this.sendChatMsg(textcomponenttranslation);
        }

        // Neptune: Called above
        //this.sendChatMsg(textcomponenttranslation);
        // Neptune: end

        this.playerLoggedIn(playerIn);
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw,
                playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver);

        if (!this.mcServer.getResourcePackUrl().isEmpty()) {
            playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }

        for (PotionEffect potioneffect : playerIn.getActivePotionEffects()) {
            nethandlerplayserver.sendPacket(new SPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }

        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("RootVehicle", 10)) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RootVehicle");
                Entity entity2 =
                        AnvilChunkLoader.readWorldEntity(nbttagcompound1.getCompoundTag("Entity"), worldserver, true);

                if (entity2 != null) {
                    UUID uuid = nbttagcompound1.getUniqueId("Attach");

                    if (entity2.getUniqueID().equals(uuid)) {
                        playerIn.startRiding(entity2, true);
                    } else {
                        for (Entity entity : entity2.getRecursivePassengers()) {
                            if (entity.getUniqueID().equals(uuid)) {
                                playerIn.startRiding(entity, true);
                                break;
                            }
                        }
                    }

                    if (!playerIn.isRiding()) {
                        logger.warn("Couldn\'t reattach entity to player");
                        worldserver.removePlayerEntityDangerously(entity2);

                        for (Entity entity3 : entity2.getRecursivePassengers()) {
                            worldserver.removePlayerEntityDangerously(entity3);
                        }
                    }
                }
            } else if (nbttagcompound.hasKey("Riding", 10)) {
                Entity entity1 =
                        AnvilChunkLoader.readWorldEntity(nbttagcompound.getCompoundTag("Riding"), worldserver, true);

                if (entity1 != null) {
                    playerIn.startRiding(entity1, true);
                }
            }
        }

        playerIn.addSelfToInternalCraftingInventory();

        // Neptune: start
        Canary.motd().sendMOTD((MessageReceiver) playerIn);
        // Neptune: end
    }

    public void playerLoggedOut(EntityPlayerMP playerIn) {
        WorldServer worldserver = playerIn.getServerWorld();
        playerIn.addStat(StatList.leaveGame);
        this.writePlayerData(playerIn);

        if (playerIn.isRiding()) {
            Entity entity = playerIn.getLowestRidingEntity();

            if (entity.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
                logger.debug("Removing player mount");
                playerIn.dismountRidingEntity();
                worldserver.removePlayerEntityDangerously(entity);

                for (Entity entity1 : entity.getRecursivePassengers()) {
                    worldserver.removePlayerEntityDangerously(entity1);
                }

                worldserver.getChunkFromChunkCoords(playerIn.chunkCoordX, playerIn.chunkCoordZ).setChunkModified();
            }
        }

        worldserver.removeEntity(playerIn);
        worldserver.getPlayerChunkMap().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        UUID uuid = playerIn.getUniqueID();
        EntityPlayerMP entityplayermp = (EntityPlayerMP) this.uuidToPlayerMap.get(uuid);

        if (entityplayermp == playerIn) {
            this.uuidToPlayerMap.remove(uuid);
            this.playerStatFiles.remove(uuid);
        }

        // Neptune: Start
        PlayerListData playerListData = ((Player) playerIn).getPlayerListData(PlayerListAction.REMOVE_PLAYER);
        for (int i = 0; i < playerEntityList.size(); i++) {
            EntityPlayerMP playerMP = (EntityPlayerMP) this.playerEntityList.get(i);
            PlayerListHook playerListHook = new PlayerListHook(playerListData.copy(), (Player) playerMP);

            ITextComponent displayName = playerListHook.getData().displayNameSet() ? ((NeptuneChatComponent)
                    playerListHook.getData().getDisplayName()).getHandle() : null;
            WorldSettings.GameType gameType =
                    WorldSettings.GameType.getByID(playerListHook.getData().getMode().getId());

            SPacketPlayerListItem packet =
                    new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER);
            packet.players.add(packet.new AddPlayerData(
                    playerListHook.getData().getProfile(),
                    playerListHook.getData().getPing(),
                    gameType,
                    displayName));
        }

        // Neptune: Replaced by above code
        //this.sendPacketToAllPlayers(new SPacketPlayerListItem(net.minecraft.network.play.server
        //        .SPacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[]{playerIn}));
        // Neptune: end
    }

    @Override
    public void sendPacketToAllInWorld(String world, Packet packet) {

    }

    @Override
    public int getNumPlayersOnline() {
        return getCurrentPlayerCount();
    }

    @Override
    public Player getPlayerByName(String name) {
        return null;
    }

    @Override
    public List<Player> getAllPlayers() {
        return (List<Player>) this.getPlayerList();
    }

    @Override
    @Shadow
    public abstract int getMaxPlayers();

    @Override
    public void markBlockNeedsUpdate(int x, int y, int z, DimensionType dimension, String world) {

    }

    @Override
    public void switchDimension(Player player, World world, boolean createPortal) {

    }
}
