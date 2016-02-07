/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Mixin(NBTTagCompound.class)
@Implements(@Interface(iface = CompoundTag.class, prefix = "tag$"))
public abstract class MixinNBTTagCompound extends NBTBase {

    @Shadow private Map tagMap;

    @Shadow
    public abstract int getInteger(String key);

    @Shadow
    public abstract void setTag(String key, NBTBase value);

    @Shadow
    public abstract void setByte(String key, byte value);

    @Shadow
    public abstract void setShort(String key, short value);

    @Shadow
    public abstract void setInteger(String key, int value);

    @Shadow
    public abstract void setLong(String key, long value);

    @Shadow
    public abstract void setFloat(String key, float value);

    @Shadow
    public abstract void setDouble(String key, double value);

    @Shadow
    public abstract void setString(String key, String value);

    @Shadow
    public abstract void setByteArray(String key, byte[] value);

    @Shadow
    public abstract void setIntArray(String key, int[] value);

    @Shadow
    public abstract void setBoolean(String key, boolean value);

    @Shadow
    public abstract NBTBase getTag(String key);

    @Shadow
    public abstract boolean hasKey(String key, int type);

    @Shadow
    public abstract NBTTagCompound getCompoundTag(String key);

    public Collection<BaseTag> tag$values() {
        Collection<BaseTag> values = new ArrayList<BaseTag>();
        for (NBTBase tag : (Collection<NBTBase>) tagMap) {
            values.add((BaseTag) tag);
        }
        return values;
    }

    public Set<String> tag$keySet() {
        return tagMap.keySet();
    }

    public void tag$put(String key, BaseTag value) {
        setTag(key, (NBTBase) value);
    }

    public void tag$put(String key, byte value) {
        setByte(key, value);
    }

    public void tag$put(String key, short value) {
        setShort(key, value);
    }

    public void tag$put(String key, int value) {
        setInteger(key, value);
    }

    public void tag$put(String key, long value) {
        setLong(key, value);
    }

    public void tag$put(String key, float value) {
        setFloat(key, value);
    }

    public void tag$put(String key, double value) {
        setDouble(key, value);
    }

    public void tag$put(String key, String value) {
        setString(key, value);
    }

    public void tag$put(String key, byte[] value) {
        setByteArray(key, value);
    }

    public void tag$put(String key, int[] value) {
        setIntArray(key, value);
    }

    public void tag$put(String key, CompoundTag value) {
        setTag(key, (NBTTagCompound) value);
    }

    public void tag$put(String key, boolean value) {
        setBoolean(key, value);
    }

    public BaseTag tag$get(String key) {
        return (BaseTag) getTag(key);
    }

    public boolean tag$containsKey(String key) {
        return this.tagMap.containsKey(key);
    }

    public boolean tag$containsKey(String key, NBTTagType type) {
        return type != NBTTagType.UNKNOWN && hasKey(key, type == NBTTagType.ANY_NUMERIC ? 99 : type.ordinal());
    }

    @Shadow(prefix = "tag$")
    public abstract byte tag$getByte(String key);

    @Shadow(prefix = "tag$")
    public abstract short tag$getShort(String key);

    public int tag$getInt(String key) {
        return getInteger(key);
    }

    @Shadow(prefix = "tag$")
    public abstract long tag$getLong(String key);

    @Shadow(prefix = "tag$")
    public abstract float tag$getFloat(String key);

    @Shadow(prefix = "tag$")
    public abstract double tag$getDouble(String key);

    @Shadow(prefix = "tag$")
    public abstract String tag$getString(String key);

    @Shadow(prefix = "tag$")
    public abstract byte[] tag$getByteArray(String key);

    @Shadow(prefix = "tag$")
    public abstract int[] tag$getIntArray(String key);

    public CompoundTag tag$getCompoundTag(String key) {
        return (CompoundTag) getCompoundTag(key);
    }

    public <E extends BaseTag> ListTag<E> tag$getListTag(String key) {
        return null;
    }

    @Shadow(prefix = "tag$")
    public abstract boolean tag$getBoolean(String key);

    public void tag$remove(String key) {
        tagMap.remove(key);
    }

    public boolean tag$isEmpty() {
        return tagMap.isEmpty();
    }
}
