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
package org.neptunepowered.vanilla.wrapper;

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

import java.util.UUID;

public class NeptuneOfflinePlayer implements OfflinePlayer {

    private final String name;
    private final UUID id;
    private final CompoundTag tag;

    public NeptuneOfflinePlayer(String name, UUID id, CompoundTag tag) {
        this.name = name;
        this.id = id;
        this.tag = tag;
    }

    @Override
    public CompoundTag getNBT() {
        return this.tag;
    }

    @Override
    public void save() {

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
    public String getPrefix() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public boolean hasPermission(String s) {
        return false;
    }

    @Override
    public void setGroup(Group group) {

    }

    @Override
    public void addGroup(Group group) {

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
    public void setPrefix(String s) {

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
        return false;
    }

    @Override
    public void setMuted(boolean b) {

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
        return null;
    }

    @Override
    public int getModeId() {
        return 0;
    }

    @Override
    public void setMode(GameMode gameMode) {

    }

    @Override
    public void setModeId(int i) {

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
    public void setCanBuild(boolean b) {

    }

    @Override
    public boolean canIgnoreRestrictions() {
        return false;
    }

    @Override
    public void setCanIgnoreRestrictions(boolean b) {

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
        return null;
    }

    @Override
    public String getLastJoined() {
        return null;
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
    public void setStat(Stat stat, int i) {

    }

    @Override
    public void setStat(Statistics statistics, int i) {

    }

    @Override
    public void increaseStat(Stat stat, int i) {

    }

    @Override
    public void increaseStat(Statistics statistics, int i) {

    }

    @Override
    public void decreaseStat(Stat stat, int i) {

    }

    @Override
    public void decreaseStat(Statistics statistics, int i) {

    }

    @Override
    public int getStat(Stat stat) {
        return 0;
    }

    @Override
    public int getStat(Statistics statistics) {
        return 0;
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return false;
    }

    @Override
    public boolean hasAchievement(Achievements achievements) {
        return false;
    }

    @Override
    public void removeAchievement(Achievement achievement) {

    }

    @Override
    public void removeAchievement(Achievements achievements) {

    }

    @Override
    public void awardAchievement(Achievement achievement) {

    }

    @Override
    public void awardAchievement(Achievements achievements) {

    }

    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    @Override
    public Inventory getEnderChestInventory() {
        return null;
    }
}
