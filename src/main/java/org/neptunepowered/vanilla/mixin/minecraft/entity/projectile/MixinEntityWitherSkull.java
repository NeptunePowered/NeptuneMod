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
package org.neptunepowered.vanilla.mixin.minecraft.entity.projectile;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.WitherSkull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import org.neptunepowered.vanilla.util.NbtConstants;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityWitherSkull.class)
@Implements(@Interface(iface = WitherSkull.class, prefix = "skull$"))
public abstract class MixinEntityWitherSkull extends MixinEntityFireball implements WitherSkull {

    private boolean damageWorld = true;
    private boolean damageEntity = true;
    private float power = 1.0f;

    @Shadow public abstract boolean isInvulnerable();
    @Shadow public abstract void setInvulnerable(boolean invulnerable);

    @Intrinsic
    public boolean skull$isInvulnerable() {
        return this.isInvulnerable();
    }

    @Intrinsic
    public void skull$setInvulnerable(boolean b) {
        this.setInvulnerable(b);
    }

    @Override
    public void setCanDamageWorld(boolean b) {
        this.damageWorld = b;
    }

    @Override
    public boolean canDamageWorld() {
        return this.damageWorld;
    }

    @Override
    public void setCanDamageEntities(boolean b) {
        this.damageEntity = b;
    }

    @Override
    public boolean canDamageEntities() {
        return this.damageEntity;
    }

    @Override
    public float getPower() {
        return this.power;
    }

    @Override
    public void setPower(float v) {
        this.power = v;
    }

    @Override
    public int getFuse() {
        // doesn't have a fuse
        return 0;
    }

    @Override
    public void setFuse(int i) {
        // doesn't have a fuse
    }

    @Override
    public void increaseFuse(int i) {
        // doesn't have a fuse
    }

    @Override
    public void decreaseFuse(int i) {
        // doesn't have a fuse
    }

    @Override
    public void detonate() {
        this.worldObj.createExplosion((Entity) (Object) this, this.getX(), this.getY(), this.getZ(), this.getPower(), this.damageWorld);
    }

    @Override
    public NBTTagCompound writeCanaryNBT(NBTTagCompound tagCompound) {
        super.writeCanaryNBT(tagCompound);
        tagCompound.setBoolean(NbtConstants.DAMAGE_ENTITIES, this.damageEntity);
        tagCompound.setBoolean(NbtConstants.DAMAGE_WORLD, this.damageWorld);
        tagCompound.setFloat(NbtConstants.POWER, this.power);
        return tagCompound;
    }

    @Override
    public void readCanaryNBT(NBTTagCompound tagCompound) {
        super.readCanaryNBT(tagCompound);
        this.damageEntity = tagCompound.hasKey(NbtConstants.DAMAGE_ENTITIES) && tagCompound.getBoolean(NbtConstants.DAMAGE_ENTITIES);
        this.damageWorld = tagCompound.hasKey(NbtConstants.DAMAGE_WORLD) && tagCompound.getBoolean(NbtConstants.DAMAGE_WORLD);
        this.power = tagCompound.hasKey(NbtConstants.POWER) ? tagCompound.getFloat(NbtConstants.POWER) : 4.0f;
    }

    @Override
    public String getFqName() {
        return "WitherSkull";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WITHERSKULL;
    }

}
