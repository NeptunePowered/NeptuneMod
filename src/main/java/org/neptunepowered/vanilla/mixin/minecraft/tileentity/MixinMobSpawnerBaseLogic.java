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
package org.neptunepowered.vanilla.mixin.minecraft.tileentity;

import com.google.common.collect.Lists;
import net.canarymod.api.MobSpawnerEntry;
import net.canarymod.api.MobSpawnerLogic;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(MobSpawnerBaseLogic.class)
public abstract class MixinMobSpawnerBaseLogic implements MobSpawnerLogic {

    @Shadow @Final private List<MobSpawnerBaseLogic.WeightedRandomMinecart> minecartToSpawn;
    @Shadow private String mobID;
    @Shadow private int spawnDelay;
    @Shadow private int minSpawnDelay;
    @Shadow private int maxSpawnDelay;
    @Shadow private int spawnCount;
    @Shadow private int maxNearbyEntities;
    @Shadow private int activatingRangeFromPlayer;
    @Shadow private int spawnRange;

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
        this.minecartToSpawn.stream()
                .map(MobSpawnerBaseLogic.WeightedRandomMinecart::toNBT).map(n -> n.getTagList("SpawnPotentials", 8))
                .forEach(l -> {
                    for (int i = 0; i < l.tagCount(); i++) {
                        spawns.add(l.getCompoundTagAt(i).getString("id"));
                    }
                });
        return spawns.toArray(new String[spawns.size()]);
    }

    @Override
    public void setSpawnedEntities(MobSpawnerEntry... mobSpawnerEntries) {

    }

    @Override
    public void addSpawnedEntities(MobSpawnerEntry... mobSpawnerEntries) {

    }

    @Override
    public MobSpawnerEntry[] getSpawnedEntities() {
        return new MobSpawnerEntry[0];
    }

}
