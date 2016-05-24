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
package org.neptunepowered.vanilla.mixin.minecraft.entity.player;

import com.mojang.authlib.GameProfile;
import net.canarymod.api.GameMode;
import net.canarymod.api.NetServerHandler;
import net.canarymod.api.PlayerListAction;
import net.canarymod.api.PlayerListData;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.statistics.Achievement;
import net.canarymod.api.statistics.Achievements;
import net.canarymod.api.statistics.Stat;
import net.canarymod.api.statistics.Statistics;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.api.world.position.Direction;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.permissionsystem.PermissionProvider;
import net.canarymod.user.Group;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsFile;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetHandlerPlayServer;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerMP.class)
@Implements(@Interface(iface = Player.class, prefix = "player$"))
public abstract class MixinEntityPlayerMP extends MixinEntityPlayer implements Player {

    @Shadow public int ping;
    @Shadow private StatisticsFile statsFile;
    @Shadow public NetHandlerPlayServer playerNetServerHandler;
    @Shadow public ItemInWorldManager theItemInWorldManager;

    @Override
    public void initPlayerData() {

    }

    @Override
    public void chat(String message) {

    }

    @Override
    public Location getSpawnPosition() {
        return null;
    }

    @Override
    public void setSpawnPosition(Location spawn) {

    }

    @Override
    public boolean executeCommand(String[] command) {
        return false;
    }

    @Override
    public void sendPacket(Packet packet) {
        this.getNetServerHandler().sendPacket(packet);
    }

    @Override
    public NetServerHandler getNetServerHandler() {
        return (NetServerHandler) this.playerNetServerHandler;
    }

    @Override
    public void teleportTo(Location location, TeleportHook.TeleportCause cause) {

    }

    @Override
    public void kick(String reason) {
        this.playerNetServerHandler.kickPlayerFromServer(reason);
    }

    @Override
    public void kickNoHook(String reason) {
        ((IMixinNetHandlerPlayServer) this.playerNetServerHandler).kickPlayerFromServerWithoutHook(reason);
    }

    @Override
    public Direction getCardinalDirection() {
        return null;
    }

    @Override
    public int getPing() {
        return this.ping;
    }

    @Override
    public PlayerListData getPlayerListData(PlayerListAction action) {
        return null;
    }

    @Override
    public void sendPlayerListData(PlayerListData data) {

    }

    @Override
    public void refreshCreativeMode() {

    }

    @Override
    public void updateCapabilities() {

    }

    @Override
    public void openInventory(Inventory inventory) {

    }

    @Override
    public void createAndOpenWorkbench() {

    }

    @Override
    public void createAndOpenAnvil() {

    }

    @Override
    public void createAndOpenEnchantmentTable(int bookshelves) {

    }

    @Override
    public void openSignEditWindow(Sign sign) {

    }

    @Override
    public void openBook(Item writtenbook) {

    }

    @Override
    public void closeWindow() {

    }

    @Override
    public void sendChatComponent(ChatComponent chatComponent) {

    }

    @Override
    public String getPreviousIP() {
        return null;
    }

    @Override
    public void hidePlayer(Player player) {

    }

    @Override
    public void hideFrom(Player player) {

    }

    @Override
    public void hidePlayerGlobal() {

    }

    @Override
    public void hideFromAll() {

    }

    @Override
    public void showPlayer(Player player) {

    }

    @Override
    public void showTo(Player player) {

    }

    @Override
    public void showPlayerGlobal() {

    }

    @Override
    public void showToAll() {

    }

    @Override
    public boolean isPlayerHidden(Player player, Player isHidden) {
        return false;
    }

    @Override
    public boolean isHiddenFrom(Player player) {
        return false;
    }

    @Override
    public boolean isHiddenFromAll() {
        return false;
    }

    @Override
    public void setCompassTarget(int x, int y, int z) {

    }

    @Intrinsic
    public GameProfile player$getGameProfile() {
        return this.getGameProfile();
    }

    @Override
    public ChatComponent getDisplayNameComponent() {
        return null;
    }

    @Override
    public void setDisplayNameComponent(ChatComponent component) {

    }

    @Override
    public Inventory getSecondInventory() {
        return null;
    }

    @Override
    public void showTitle(ChatComponent title) {

    }

    @Override
    public void showTitle(ChatComponent title, ChatComponent subtitle) {

    }

    @Override
    public PermissionProvider getPermissionProvider() {
        return null;
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public void setGroup(Group group) {

    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void addGroup(Group group) {

    }

    @Override
    public boolean removeGroup(Group g) {
        return false;
    }

    @Override
    public boolean removeGroup(String g) {
        return false;
    }

    @Override
    public boolean isInGroup(Group group, boolean parents) {
        return false;
    }

    @Override
    public boolean isInGroup(String group, boolean parents) {
        return false;
    }

    @Override
    public String getUUIDString() {
        return this.getUUID().toString();
    }

    @Override
    public boolean isMuted() {
        return false;
    }

    @Override
    public void setMuted(boolean muted) {

    }

    @Override
    public Group[] getPlayerGroups() {
        return new Group[0];
    }

    @Override
    public String getFirstJoined() {
        return null;
    }

    @Override
    public long getTimePlayed() {
        return 0;
    }

    @Override
    public GameMode getMode() {
        return GameModeConverter.of(this.theItemInWorldManager.getGameType());
    }

    @Override
    public void setMode(GameMode mode) {
        this.theItemInWorldManager.setGameType(GameModeConverter.of(mode));
    }

    @Override
    public int getModeId() {
        return 0;
    }

    @Override
    public void setModeId(int mode) {

    }

    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean canBuild() {
        return false;
    }

    @Override
    public void setCanBuild(boolean canModify) {

    }

    @Override
    public boolean canIgnoreRestrictions() {
        return false;
    }

    @Override
    public void setCanIgnoreRestrictions(boolean canIgnore) {

    }

    @Override
    public void addExhaustion(float exhaustion) {

    }

    @Override
    public void setExhaustion(float exhaustion) {

    }

    @Override
    public float getExhaustionLevel() {
        return 0;
    }

    @Override
    public void addSaturation(float saturation) {

    }

    @Override
    public void setSaturation(float saturation) {

    }

    @Override
    public float getSaturationLevel() {
        return 0;
    }

    @Override
    public int getHunger() {
        return 0;
    }

    @Override
    public void setHunger(int hunger) {

    }

    @Override
    public void addExperience(int experience) {

    }

    @Override
    public void removeExperience(int experience) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperience(int xp) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void addLevel(int level) {

    }

    @Override
    public void removeLevel(int level) {

    }

    @Override
    public Location getHome() {
        return null;
    }

    @Override
    public void setHome(Location loc) {

    }

    @Override
    public boolean hasHome() {
        return false;
    }

    @Override
    public String[] getAllowedIPs() {
        return new String[0];
    }

    @Override
    public String getIP() {
        return null;
    }

    @Override
    public String getLastJoined() {
        return null;
    }

    @Override
    public void setStat(Stat stat, int value) {

    }

    @Override
    public void setStat(Statistics stat, int value) {

    }

    @Override
    public void increaseStat(Stat stat, int value) {

    }

    @Override
    public void increaseStat(Statistics stat, int value) {

    }

    @Override
    public void decreaseStat(Stat stat, int value) {

    }

    @Override
    public void decreaseStat(Statistics stat, int value) {

    }

    @Override
    public int getStat(Stat stat) {
        return this.statsFile.readStat((StatBase) stat);
    }

    @Override
    public int getStat(Statistics stat) {
        return getStat(stat.getInstance());
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return this.statsFile.hasAchievementUnlocked((net.minecraft.stats.Achievement) achievement);
    }

    @Override
    public boolean hasAchievement(Achievements achievement) {
        return hasAchievement(achievement.getInstance());
    }

    @Override
    public void removeAchievement(Achievement achievement) {

    }

    @Override
    public void removeAchievement(Achievements achievement) {

    }

    @Override
    public void awardAchievement(Achievement achievement) {

    }

    @Override
    public void awardAchievement(Achievements achievement) {

    }

    @Override
    public Inventory getEnderChestInventory() {
        return null;
    }

    @Override
    public String getFqName() {
        return "Player";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }
}
