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
package org.neptunepowered.common.mixin.minecraft.entity.projectile;

import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.entity.throwable.EntityThrowable;
import net.minecraft.entity.EntityLivingBase;
import org.neptunepowered.common.mixin.minecraft.entity.MixinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.entity.projectile.EntityThrowable.class)
public abstract class MixinEntityThrowable extends MixinEntity implements EntityThrowable {

    protected float gravity = 0.3f;
    @Shadow private EntityLivingBase thrower;

    @Overwrite
    protected float getGravityVelocity() {
        return this.gravity;
    }

    @Override
    public LivingBase getThrower() {
        return (LivingBase) this.thrower;
    }

    @Override
    public float getGravity() {
        return getGravityVelocity();
    }

    @Override
    public void setGravity(float velocity) {
        this.gravity = velocity;
    }
}
