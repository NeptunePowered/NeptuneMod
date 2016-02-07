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
package org.neptunepowered.common.wrapper.factory;

import net.canarymod.api.factory.NBTFactory;
import net.canarymod.api.nbt.BaseTag;
import net.canarymod.api.nbt.ByteArrayTag;
import net.canarymod.api.nbt.ByteTag;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.nbt.DoubleTag;
import net.canarymod.api.nbt.FloatTag;
import net.canarymod.api.nbt.IntArrayTag;
import net.canarymod.api.nbt.IntTag;
import net.canarymod.api.nbt.ListTag;
import net.canarymod.api.nbt.LongTag;
import net.canarymod.api.nbt.NBTTagType;
import net.canarymod.api.nbt.ShortTag;
import net.canarymod.api.nbt.StringTag;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class NeptuneNBTFactory implements NBTFactory {

    @Override
    public CompoundTag newCompoundTag(String name) {
        return (CompoundTag) new NBTTagCompound();
    }

    @Override
    public ByteTag newByteTag(byte value) {
        return (ByteTag) new NBTTagByte(value);
    }

    @Override
    public ByteArrayTag newByteArrayTag(byte[] value) {
        return (ByteArrayTag) new NBTTagByteArray(value);
    }

    @Override
    public DoubleTag newDoubleTag(double value) {
        return (DoubleTag) new NBTTagDouble(value);
    }

    @Override
    public FloatTag newFloatTag(float value) {
        return (FloatTag) new NBTTagFloat(value);
    }

    @Override
    public IntTag newIntTag(int value) {
        return (IntTag) new NBTTagInt(value);
    }

    @Override
    public IntArrayTag newIntArrayTag(int[] value) {
        return (IntArrayTag) new NBTTagIntArray(value);
    }

    @Override
    public <E extends BaseTag> ListTag<E> newListTag() {
        return (ListTag<E>) new NBTTagList();
    }

    @Override
    public LongTag newLongTag(long value) {
        return (LongTag) new NBTTagLong(value);
    }

    @Override
    public ShortTag newShortTag(short value) {
        return (ShortTag) new NBTTagShort(value);
    }

    @Override
    public StringTag newStringTag(String value) {
        return (StringTag) new NBTTagString(value);
    }

    @Override
    public BaseTag newTagFromType(NBTTagType type, Object value) {
        switch (type) {
            case BYTE:
                return newByteTag((Byte) value);
            case BYTE_ARRAY:
                return newByteArrayTag((byte[]) value);
            case COMPOUND:
                return newCompoundTag((String) value);
            case DOUBLE:
                return newDoubleTag((Double) value);
            case FLOAT:
                return newFloatTag((Float) value);
            case INT:
                return newIntTag((Integer) value);
            case INT_ARRAY:
                return newIntArrayTag((int[]) value);
            case LIST:
                return newListTag();
            case LONG:
                return newLongTag((Long) value);
            case SHORT:
                return newShortTag((Short) value);
            case STRING:
                return newStringTag((String) value);
        }
        return null;
    }

    @Override
    public BaseTag newTagFromJSON(String json) {
        try {
            return (BaseTag) JsonToNBT.getTagFromJson(json);
        } catch (NBTException e) {
            return null;
        }
    }
}
