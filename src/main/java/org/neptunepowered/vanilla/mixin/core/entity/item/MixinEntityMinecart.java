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
package org.neptunepowered.vanilla.mixin.core.entity.item;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.vehicle.Minecart;
import net.minecraft.entity.item.EntityMinecart;
import org.neptunepowered.vanilla.mixin.core.entity.MixinEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityMinecart.class)
@Implements(@Interface(iface = Minecart.class, prefix = "minecart$"))
public abstract class MixinEntityMinecart extends MixinEntity implements Minecart {

    @Shadow private boolean isInReverse;

    @Shadow public abstract void setRollingAmplitude(int p_70497_1_);
    @Shadow public abstract int getRollingAmplitude();
    @Shadow public abstract void setRollingDirection(int p_70494_1_);
    @Shadow public abstract int getRollingDirection();

    @Override
    public boolean isInReverse() {
        return this.isInReverse;
    }

    @Intrinsic
    public void minecart$setRollingAmplitude(int i) {
        this.setRollingAmplitude(i);
    }

    @Intrinsic
    public int minecart$getRollingAmplitude() {
        return this.getRollingDirection();
    }

    @Intrinsic
    public void minecart$setRollingDirection(int i) {
        this.setRollingDirection(i);
    }

    @Intrinsic
    public int minecart$getRollingDirection() {
        return this.getRollingDirection();
    }

    @Override
    public Entity getPassenger() {
        return null;
    }

    @Override
    public boolean isBoat() {
        return false;
    }

    @Override
    public boolean isMinecart() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
