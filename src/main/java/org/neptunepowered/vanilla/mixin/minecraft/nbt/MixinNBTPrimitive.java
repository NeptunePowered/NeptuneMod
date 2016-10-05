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

import net.canarymod.api.nbt.PrimitiveTag;
import net.minecraft.nbt.NBTBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NBTBase.NBTPrimitive.class)
public abstract class MixinNBTPrimitive<T> extends MixinNBTBase<T> implements PrimitiveTag<T> {

    @Shadow public abstract long getLong();
    @Shadow public abstract int getInt();
    @Shadow public abstract short getShort();
    @Shadow public abstract byte getByte();
    @Shadow public abstract double getDouble();
    @Shadow public abstract float getFloat();

    @Override
    public long getLongValue() {
        return this.getLong();
    }

    @Override
    public int getIntValue() {
        return this.getInt();
    }

    @Override
    public short getShortValue() {
        return this.getShort();
    }

    @Override
    public byte getByteValue() {
        return this.getByte();
    }

    @Override
    public double getDoubleValue() {
        return this.getDouble();
    }

    @Override
    public float getFloatValue() {
        return this.getFloat();
    }

}
