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
package org.neptunepowered.vanilla;

import com.google.common.collect.Lists;
import net.canarymod.Canary;
import net.canarymod.api.GameMode;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.PlayerInventory;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.statistics.Achievement;
import net.canarymod.api.statistics.Achievements;
import net.canarymod.api.statistics.Stat;
import net.canarymod.api.statistics.Statistics;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Position;
import net.canarymod.permissionsystem.PermissionProvider;
import net.canarymod.user.Group;
import net.canarymod.user.UserAndGroupsProvider;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.world.WorldSettings;
import org.neptunepowered.vanilla.util.NbtConstants;
import org.neptunepowered.vanilla.util.PermissionConstants;
import org.neptunepowered.vanilla.util.helper.StatisticsHelper;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NeptuneOfflinePlayer implements OfflinePlayer {

    private final String name;
    private final UUID id;
    private final NBTTagCompound tag;
    private final PermissionProvider permissions;
    private final StatisticsFile statisticsFile;
    private final InventoryPlayer inventory;
    private List<Group> groups;
    private String prefix;
    private boolean isMuted;

    public NeptuneOfflinePlayer(String name, UUID id, NBTTagCompound tag) {
        this.name = name;
        this.id = id;
        this.tag = tag;

        final UserAndGroupsProvider provider = Canary.usersAndGroups();
        final String uuid = this.getUUIDString();

        this.permissions = Canary.permissionManager().getPlayerProvider(this.name, this.getWorld().getFqName());
        final String[] data = provider.getPlayerData(uuid);
        final Group[] subs = provider.getModuleGroupsForPlayer(uuid);

        this.groups = Lists.newLinkedList();
        this.groups.add(Canary.usersAndGroups().getGroup(data[1]));
        for (Group group : subs) {
            if (group != null) {
                this.groups.add(group);
            }
        }

        this.prefix = data[0];
        this.isMuted = Boolean.parseBoolean(data[2]);

        this.statisticsFile = StatisticsHelper.getStatisticsFile(id, name);
        this.inventory = new InventoryPlayer(null);
        this.inventory.readFromNBT(this.tag.getTagList(NbtConstants.INVENTORY, NbtConstants.TAG_COMPOUND));
    }

    @Override
    public CompoundTag getNBT() {
        return (CompoundTag) this.tag;
    }

    public NBTTagCompound getMetadata() {
        return this.tag.hasKey(NbtConstants.CANARY_TAG) ? this.tag.getCompoundTag(NbtConstants.CANARY_TAG) : null;
    }

    @Override
    public void save() {
        if (this.isOnline()) {
            Canary.log.warn("Attempted to save an online player! (" + this.name + ")");
            return;
        }

        this.tag.setTag(NbtConstants.INVENTORY, this.inventory.writeToNBT(new NBTTagList()));
        // TODO: Save NBT
    }

    @Override
    public PermissionProvider getPermissionProvider() {
        return this.permissions;
    }

    @Override
    public Group getGroup() {
        return this.groups.get(0);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public boolean isOnline() {
        return Canary.getServer().getPlayer(this.name) != null;
    }

    @Override
    public boolean hasPermission(String path) {
        return this.permissions.queryPermission(path);
    }

    @Override
    public void setGroup(Group group) {
        this.groups.set(0, group);
        Canary.usersAndGroups().addOrUpdateOfflinePlayer(this);
    }

    @Override
    public void addGroup(Group group) {
        if (!this.groups.contains(group)) {
            this.groups.add(group);
            Canary.usersAndGroups().addOrUpdateOfflinePlayer(this);
        }
    }

    @Override
    public boolean removeGroup(Group group) {
        return false;
    }

    @Override
    public boolean removeGroup(String s) {
        return false;
    }

    @Override
    public boolean isInGroup(Group group, boolean b) {
        return false;
    }

    @Override
    public boolean isInGroup(String s, boolean b) {
        return false;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        Canary.usersAndGroups().addOrUpdateOfflinePlayer(this);
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUUID() {
        return this.id;
    }

    @Override
    public String getUUIDString() {
        return this.id.toString();
    }

    @Override
    public boolean isMuted() {
        return this.isMuted;
    }

    @Override
    public void setMuted(boolean muted) {
        this.isMuted = muted;
        Canary.usersAndGroups().addOrUpdateOfflinePlayer(this);
    }

    @Override
    public Group[] getPlayerGroups() {
        return this.groups.toArray(new Group[this.groups.size()]);
    }

    @Override
    public String getFirstJoined() {
        if (this.getMetadata() != null && this.getMetadata().hasKey(NbtConstants.FIRST_JOINED)) {
            return this.getMetadata().getString(NbtConstants.FIRST_JOINED);
        }
        return "NEVER";
    }

    @Override
    public long getTimePlayed() {
        if (this.getMetadata() != null && this.getMetadata().hasKey(NbtConstants.TIME_PLAYED)) {
            return this.getMetadata().getLong(NbtConstants.TIME_PLAYED);
        }
        return 0;
    }

    @Override
    public GameMode getMode() {
        return GameModeConverter.of(WorldSettings.GameType.getByID(this.getModeId()));
    }

    @Override
    public int getModeId() {
        if (this.getNBT() != null && this.getNBT().containsKey(NbtConstants.GAME_TYPE)) {
            return this.getNBT().getInt(NbtConstants.GAME_TYPE);
        }
        return 0;
    }

    @Override
    public void setMode(GameMode gameMode) {
        this.setModeId(GameModeConverter.of(gameMode).getID());
    }

    @Override
    public void setModeId(int mode) {
        if (this.getNBT() != null) {
            this.getNBT().put(NbtConstants.GAME_TYPE, mode);
            this.save();
        }
    }

    @Override
    public boolean isOperator() {
        return Canary.ops().isOpped(this);
    }

    @Override
    public boolean isAdmin() {
        return this.isOperator() || this.hasPermission(PermissionConstants.Super.ADMINISTRATOR);
    }

    @Override
    public boolean canBuild() {
        return this.isAdmin() || this.hasPermission(PermissionConstants.World.BUILD);
    }

    @Override
    public void setCanBuild(boolean canModify) {
        this.permissions.addPermission(PermissionConstants.World.BUILD, canModify);
    }

    @Override
    public boolean canIgnoreRestrictions() {
        return this.isAdmin() || this.hasPermission(PermissionConstants.Super.IGNORE_RESTRICTIONS);
    }

    @Override
    public void setCanIgnoreRestrictions(boolean canIgnore) {
        this.permissions.addPermission(PermissionConstants.Super.IGNORE_RESTRICTIONS, canIgnore, -1);
    }

    @Override
    public void addExhaustion(float v) {

    }

    @Override
    public void setExhaustion(float v) {

    }

    @Override
    public float getExhaustionLevel() {
        return 0;
    }

    @Override
    public void addSaturation(float v) {

    }

    @Override
    public void setSaturation(float v) {

    }

    @Override
    public float getSaturationLevel() {
        return 0;
    }

    @Override
    public void setHunger(int i) {

    }

    @Override
    public int getHunger() {
        return 0;
    }

    @Override
    public void addExperience(int i) {

    }

    @Override
    public void removeExperience(int i) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperience(int i) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int i) {

    }

    @Override
    public void addLevel(int i) {

    }

    @Override
    public void removeLevel(int i) {

    }

    @Override
    public void setHome(Location location) {

    }

    @Override
    public Location getHome() {
        return null;
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
        if (this.getMetadata() != null && this.getMetadata().hasKey(NbtConstants.PREVIOUS_IP)) {
            return this.getMetadata().getString(NbtConstants.PREVIOUS_IP);
        }
        return "UNKNOWN";
    }

    @Override
    public String getLastJoined() {
        if (this.getMetadata() != null && this.getMetadata().hasKey(NbtConstants.LAST_JOINED)) {
            return this.getMetadata().getString(NbtConstants.LAST_JOINED);
        }
        return "NEVER";
    }

    @Override
    public float getHealth() {
        return 0;
    }

    @Override
    public void setHealth(float v) {

    }

    @Override
    public void increaseHealth(float v) {

    }

    @Override
    public void setStat(Stat stat, int value) {
        this.statisticsFile.unlockAchievement(null, (StatBase) stat, value);
    }

    @Override
    public void setStat(Statistics statistics, int value) {
        this.setStat(statistics.getInstance(), value);
    }

    @Override
    public void increaseStat(Stat stat, int value) {
        if (value < 0) return;
        this.statisticsFile.increaseStat(null, (StatBase) stat, value);
    }

    @Override
    public void increaseStat(Statistics statistics, int value) {
        this.increaseStat(statistics.getInstance(), value);
    }

    @Override
    public void decreaseStat(Stat stat, int value) {
        if (value < 0) return;
        this.setStat(stat, this.getStat(stat) - value);
    }

    @Override
    public void decreaseStat(Statistics statistics, int value) {
        this.decreaseStat(statistics.getInstance(), value);
    }

    @Override
    public int getStat(Stat stat) {
        return this.statisticsFile.readStat((StatBase) stat);
    }

    @Override
    public int getStat(Statistics statistics) {
        return this.getStat(statistics.getInstance());
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return this.statisticsFile.hasAchievementUnlocked((net.minecraft.stats.Achievement) achievement);
    }

    @Override
    public boolean hasAchievement(Achievements achievements) {
        return this.hasAchievement(achievements.getInstance());
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        // Ensure all children achievements are removed
        Arrays.stream(Achievements.values())
                .map(Achievements::getInstance).map(Achievement::getParent)
                .filter(achievement::equals).filter(this::hasAchievement)
                .forEach(this::removeAchievement);

        this.statisticsFile.unlockAchievement(null, (StatBase) achievement, 0);
    }

    @Override
    public void removeAchievement(Achievements achievements) {
        this.removeAchievement(achievements.getInstance());
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        // Ensure all parent achievements are awarded
        if (!this.hasAchievement(achievement.getParent())) {
            this.awardAchievement(achievement.getParent());
        }

        this.statisticsFile.unlockAchievement(null, (StatBase) achievement, 1);
    }

    @Override
    public void awardAchievement(Achievements achievements) {
        this.awardAchievement(achievements.getInstance());
    }

    @Override
    public PlayerInventory getInventory() {
        return (PlayerInventory) this.inventory;
    }

    @Override
    public Inventory getEnderChestInventory() {
        return null;
    }

}
