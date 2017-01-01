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
package org.neptunepowered.vanilla.mixin.minecraft.tileentity;

import com.google.common.collect.Lists;
import net.canarymod.api.MobSpawnerEntry;
import net.canarymod.api.MobSpawnerLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import org.neptunepowered.vanilla.NeptuneMobSpawnerEntry;
import org.neptunepowered.vanilla.util.NbtConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.List;

@Mixin(MobSpawnerBaseLogic.class)
public abstract class MixinMobSpawnerBaseLogic implements MobSpawnerLogic {

    @Shadow private String mobID;
    @Shadow private int spawnDelay;
    @Shadow private int minSpawnDelay;
    @Shadow private int maxSpawnDelay;
    @Shadow private int spawnCount;
    @Shadow private int maxNearbyEntities;
    @Shadow private int activatingRangeFromPlayer;
    @Shadow private int spawnRange;

    @Shadow public abstract void writeToNBT(NBTTagCompound nbt);
    @Shadow public abstract void readFromNBT(NBTTagCompound nbt);

    @Override
    public String getSpawnId() {
        return this.mobID;
    }

    @Override
    public void setSpawnId(String s) {
        this.mobID = s;
    }

    @Override
    public int getDelay() {
        return this.spawnDelay;
    }

    @Override
    public void setDelay(int i) {
        this.spawnDelay = i;
    }

    @Override
    public int getMinDelay() {
        return this.minSpawnDelay;
    }

    @Override
    public void setMinDelay(int i) {
        this.minSpawnDelay = i;
    }

    @Override
    public int getMaxDelay() {
        return this.maxSpawnDelay;
    }

    @Override
    public void setMaxDelay(int i) {
        this.maxSpawnDelay = i;
    }

    @Override
    public int getSpawnCount() {
        return this.spawnCount;
    }

    @Override
    public void setSpawnCount(int i) {
        this.spawnCount = i;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int i) {
        this.maxNearbyEntities = i;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.activatingRangeFromPlayer;
    }

    @Override
    public void setRequiredPlayerRange(int i) {
        this.activatingRangeFromPlayer = i;
    }

    @Override
    public int getSpawnRange() {
        return this.spawnRange;
    }

    @Override
    public void setSpawnRange(int i) {
        this.spawnRange = i;
    }

    @Override
    public String[] getSpawns() {
        final List<String> spawns = Lists.newArrayList();
        final NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);

        if (tagCompound.hasKey(NbtConstants.SPAWN_POTENTIALS)) {
            final NBTTagList tagList = tagCompound.getTagList(NbtConstants.SPAWN_POTENTIALS, NbtConstants.TAG_COMPOUND);

            for (int i = 0; i < tagList.tagCount(); i++) {
                spawns.add(tagList.getCompoundTagAt(i).getString(NbtConstants.ENTITY_TYPE));
            }
        }

        return spawns.toArray(new String[spawns.size()]);
    }

    @Override
    public void setSpawnedEntities(MobSpawnerEntry... mobSpawnerEntries) {
        final NBTTagList spawnPotentials = new NBTTagList();

        for (MobSpawnerEntry spawnerEntry : mobSpawnerEntries) {
            spawnPotentials.appendTag((NBTTagCompound) spawnerEntry.getSpawnPotentialsTag());
        }

        final NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);

        if (tagCompound.hasKey(NbtConstants.SPAWN_POTENTIALS)) {
            tagCompound.removeTag(NbtConstants.SPAWN_POTENTIALS);
        }
        tagCompound.setTag(NbtConstants.SPAWN_POTENTIALS, spawnPotentials);

        this.readFromNBT(tagCompound);
    }

    @Override
    public void addSpawnedEntities(MobSpawnerEntry... mobSpawnerEntries) {
        final List<MobSpawnerEntry> entries = Lists.newArrayList();
        entries.addAll(Arrays.asList(this.getSpawnedEntities()));
        entries.addAll(Arrays.asList(mobSpawnerEntries));
        this.setSpawnedEntities(entries.toArray(new MobSpawnerEntry[entries.size()]));
    }

    @Override
    public MobSpawnerEntry[] getSpawnedEntities() {
        final List<MobSpawnerEntry> entries = Lists.newArrayList();
        final NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);

        if (tagCompound.hasKey(NbtConstants.SPAWN_POTENTIALS)) {
            final NBTTagList tagList = tagCompound.getTagList(NbtConstants.SPAWN_POTENTIALS, NbtConstants.TAG_COMPOUND);

            for (int i = 0; i < tagList.tagCount(); i++) {
                entries.add(new NeptuneMobSpawnerEntry(tagList.getCompoundTagAt(i)));
            }
        }

        return entries.toArray(new MobSpawnerEntry[entries.size()]);
    }

}
