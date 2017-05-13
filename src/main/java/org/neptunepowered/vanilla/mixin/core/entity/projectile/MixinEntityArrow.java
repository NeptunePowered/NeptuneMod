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
package org.neptunepowered.vanilla.mixin.core.entity.projectile;

import net.canarymod.api.entity.Arrow;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityType;
import net.minecraft.entity.projectile.EntityArrow;
import org.neptunepowered.vanilla.mixin.core.entity.MixinEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityArrow.class)
@Implements(@Interface(iface = Arrow.class, prefix = "arrow$"))
public abstract class MixinEntityArrow extends MixinEntity implements Arrow {

    @Shadow public net.minecraft.entity.Entity shootingEntity;
    @Shadow public int canBePickedUp;
    @Shadow private boolean inGround;
    @Shadow private int knockbackStrength;
    @Shadow private int ticksInGround;
    @Shadow private int ticksInAir;

    @Shadow public abstract boolean getIsCritical();
    @Shadow public abstract double getDamage();
    @Shadow public abstract void setDamage(double damage);
    @Shadow public abstract void setIsCritical(boolean critical);

    @Override
    public boolean canPickUp() {
        return this.canBePickedUp == 1;
    }

    @Override
    public void setCanPickUp(boolean canPickUp) {
        this.canBePickedUp = canPickUp ? 1 : 0;
    }

    @Intrinsic
    public double arrow$getDamage() {
        return this.getDamage();
    }

    @Intrinsic
    public void arrow$setDamage(double damage) {
        this.setDamage(damage);
    }

    @Override
    public boolean isCritical() {
        return this.getIsCritical();
    }

    @Intrinsic
    public void arrow$setIsCritical(boolean critical) {
        this.setIsCritical(critical);
    }

    @Override
    public Entity getOwner() {
        return (Entity) this.shootingEntity;
    }

    @Override
    public boolean isInGround() {
        return this.inGround;
    }

    @Override
    public int getTicksInAir() {
        return this.ticksInAir;
    }

    @Override
    public int getTicksInGround() {
        return this.ticksInGround;
    }

    @Override
    public int getKnockbackStrength() {
        return this.knockbackStrength;
    }

    @Override
    public void setKnockbackStrenth(int knockback) {
        this.knockbackStrength = knockback;
    }

    @Override
    public String getFqName() {
        return "Arrow";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ARROW;
    }

}
