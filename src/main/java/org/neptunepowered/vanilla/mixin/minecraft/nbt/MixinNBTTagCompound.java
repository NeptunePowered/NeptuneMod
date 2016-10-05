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
package org.neptunepowered.vanilla.mixin.minecraft.nbt;

import net.canarymod.api.nbt.BaseTag;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.nbt.ListTag;
import net.canarymod.api.nbt.NBTTagType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Mixin(NBTTagCompound.class)
@Implements(@Interface(iface = CompoundTag.class, prefix = "tag$"))
public abstract class MixinNBTTagCompound extends MixinNBTBase<CompoundTag> implements CompoundTag {

    @Shadow private Map<String, NBTBase> tagMap;

    @Shadow public abstract int getInteger(String key);
    @Shadow public abstract void setTag(String key, NBTBase value);
    @Shadow public abstract void setByte(String key, byte value);
    @Shadow public abstract void setShort(String key, short value);
    @Shadow public abstract void setInteger(String key, int value);
    @Shadow public abstract void setLong(String key, long value);
    @Shadow public abstract void setFloat(String key, float value);
    @Shadow public abstract void setDouble(String key, double value);
    @Shadow public abstract void setString(String key, String value);
    @Shadow public abstract void setByteArray(String key, byte[] value);
    @Shadow public abstract void setIntArray(String key, int[] value);
    @Shadow public abstract void setBoolean(String key, boolean value);
    @Shadow public abstract NBTBase getTag(String key);
    @Shadow public abstract boolean hasKey(String key, int type);
    @Shadow public abstract NBTTagCompound shadow$getCompoundTag(String key);
    @Shadow public abstract byte getByte(String key);
    @Shadow public abstract short getShort(String key);
    @Shadow public abstract long getLong(String key);
    @Shadow public abstract float getFloat(String key);
    @Shadow public abstract double getDouble(String key);
    @Shadow public abstract String getString(String key);
    @Shadow public abstract byte[] getByteArray(String key);
    @Shadow public abstract int[] getIntArray(String key);
    @Shadow public abstract boolean getBoolean(String key);

    @Override
    public Collection<BaseTag> values() {
        return (Collection) this.tagMap.values();
    }

    @Override
    public Set<String> keySet() {
        return this.tagMap.keySet();
    }

    @Override
    public void put(String key, BaseTag value) {
        this.setTag(key, (NBTBase) value);
    }

    @Override
    public void put(String key, byte value) {
        this.setByte(key, value);
    }

    @Override
    public void put(String key, short value) {
        this.setShort(key, value);
    }

    @Override
    public void put(String key, int value) {
        this.setInteger(key, value);
    }

    @Override
    public void put(String key, long value) {
        this.setLong(key, value);
    }

    @Override
    public void put(String key, float value) {
        this.setFloat(key, value);
    }

    @Override
    public void put(String key, double value) {
        this.setDouble(key, value);
    }

    @Override
    public void put(String key, String value) {
        this.setString(key, value);
    }

    @Override
    public void put(String key, byte[] value) {
        this.setByteArray(key, value);
    }

    @Override
    public void put(String key, int[] value) {
        this.setIntArray(key, value);
    }

    @Override
    public void put(String key, CompoundTag value) {
        this.setTag(key, (NBTTagCompound) value);
    }

    @Override
    public void put(String key, boolean value) {
        this.setBoolean(key, value);
    }

    @Override
    public BaseTag get(String key) {
        return (BaseTag) this.getTag(key);
    }

    @Override
    public boolean containsKey(String key) {
        return this.tagMap.containsKey(key);
    }

    @Override
    public boolean containsKey(String key, NBTTagType type) {
        return type != NBTTagType.UNKNOWN && hasKey(key, type == NBTTagType.ANY_NUMERIC ? 99 : type.ordinal());
    }

    @Intrinsic
    public byte tag$getByte(String key) {
        return this.getByte(key);
    }

    @Intrinsic
    public short tag$getShort(String key) {
        return this.getShort(key);
    }

    @Override
    public int getInt(String key) {
        return this.getInteger(key);
    }

    @Intrinsic
    public long tag$getLong(String key) {
        return this.getLong(key);
    }

    @Intrinsic
    public float tag$getFloat(String key) {
        return this.getFloat(key);
    }

    @Intrinsic
    public double tag$getDouble(String key) {
        return this.getDouble(key);
    }

    @Intrinsic
    public String tag$getString(String key) {
        return this.getString(key);
    }

    @Intrinsic
    public byte[] tag$getByteArray(String key) {
        return this.getByteArray(key);
    }

    @Intrinsic
    public int[] tag$getIntArray(String key) {
        return this.getIntArray(key);
    }

    @Intrinsic
    public CompoundTag key$getCompoundTag(String key) {
        return (CompoundTag) this.shadow$getCompoundTag(key);
    }

    @Override
    public <E extends BaseTag> ListTag<E> getListTag(String key) {
        return null;
    }

    @Intrinsic
    public boolean tag$getBoolean(String key) {
        return this.getBoolean(key);
    }

    @Override
    public void remove(String key) {
        this.tagMap.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return this.tagMap.isEmpty();
    }

}
