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

import net.canarymod.Canary;
import net.canarymod.api.MobSpawnerEntry;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.nbt.CompoundTag;
import net.minecraft.nbt.NBTTagCompound;

public class NeptuneMobSpawnerEntry implements MobSpawnerEntry {

    private final NBTTagCompound properties;
    private int weight;
    private Entity entity;

    public NeptuneMobSpawnerEntry(NBTTagCompound tagCompound) {
        this.properties = tagCompound.getCompoundTag("Properties");
        this.weight = tagCompound.getInteger("Weight");
        this.entity = Canary.factory().getEntityFactory().newEntity(tagCompound.getString("Type"));
        ((net.minecraft.entity.Entity) this.entity).readFromNBT(this.properties);
    }

    @Override
    public void setWeight(int i) {
        this.weight = i;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean isValid() {
        return this.entity != null;
    }

    @Override
    public CompoundTag getSpawnPotentialsTag() {
        if (!this.isValid()) {
            return null;
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setTag("Properties", this.properties);
        nbttagcompound.setString("Type", this.entity.getFqName());
        nbttagcompound.setInteger("Weight", this.weight);
        return null;
    }

}
