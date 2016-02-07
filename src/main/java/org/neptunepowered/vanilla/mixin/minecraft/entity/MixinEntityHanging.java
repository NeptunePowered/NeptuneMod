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
package org.neptunepowered.vanilla.mixin.minecraft.entity;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.hanging.HangingEntity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.EnumFacing;
import org.neptunepowered.vanilla.mixin.minecraft.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityHanging.class)
public abstract class MixinEntityHanging extends org.neptunepowered.common.mixin.minecraft.entity.MixinEntity implements HangingEntity {

    @Shadow private int tickCounter1;
    @Shadow public EnumFacing facingDirection;

    @Shadow
    public abstract boolean onValidSurface();

    @Override
    public int getHangingDirection() {
        return this.facingDirection.getIndex();
    }

    @Override
    public void setHangingDirection(int direction) {
        this.facingDirection = EnumFacing.getFront(direction);
    }

    @Override
    public boolean isOnValidSurface() {
        return this.onValidSurface();
    }

    @Override
    public int getTickCounter() {
        return this.tickCounter1;
    }

    @Override
    public void setTickCounter(int ticks) {
        this.tickCounter1 = ticks;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.GENERIC_HANGING;
    }
}
