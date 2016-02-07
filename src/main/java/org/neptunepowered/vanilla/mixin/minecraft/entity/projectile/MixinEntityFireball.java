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
package org.neptunepowered.vanilla.mixin.minecraft.entity.projectile;

import net.canarymod.api.entity.Fireball;
import net.canarymod.api.entity.living.LivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import org.neptunepowered.vanilla.mixin.minecraft.entity.MixinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityFireball.class)
public abstract class MixinEntityFireball extends MixinEntity implements Fireball {

    @Shadow private int ticksAlive;
    @Shadow private int ticksInAir;
    @Shadow public double accelerationX;
    @Shadow public double accelerationY;
    @Shadow public double accelerationZ;
    @Shadow public EntityLivingBase shootingEntity;
    private float motionFactor = 0.95F;

    @Override
    public int getTicksAlive() {
        return this.ticksAlive;
    }

    @Override
    public void setTicksAlive(int ticks) {
        this.ticksAlive = ticks;
    }

    @Override
    public int getTicksInAir() {
        return this.ticksInAir;
    }

    @Override
    public void setTicksInAir(int ticks) {
        this.ticksInAir = ticks;
    }

    @Override
    public double getAccelerationX() {
        return this.accelerationX;
    }

    @Override
    public void setAccelerationX(double accelX) {
        this.accelerationX = accelX;
    }

    @Override
    public double getAccelerationY() {
        return this.accelerationY;
    }

    @Override
    public void setAccelerationY(double accelY) {
        this.accelerationY = accelY;
    }

    @Override
    public double getAccelerationZ() {
        return this.accelerationZ;
    }

    @Override
    public void setAccelerationZ(double accelZ) {
        this.accelerationZ = accelZ;
    }

    @Override
    @Overwrite
    public float getMotionFactor() {
        return this.motionFactor;
    }

    @Override
    public void setMotionFactor(float factor) {
        this.motionFactor = factor;
    }

    @Override
    public LivingBase getOwner() {
        return (LivingBase) this.shootingEntity;
    }

    @Override
    public String getFqName() {
        return "Fireball";
    }
}
