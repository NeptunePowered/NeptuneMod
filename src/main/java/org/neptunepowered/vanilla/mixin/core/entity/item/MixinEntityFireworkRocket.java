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

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.FireworkRocket;
import net.canarymod.api.inventory.Item;
import net.canarymod.hook.world.FireworkExplodeHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import org.neptunepowered.vanilla.mixin.core.entity.MixinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityFireworkRocket.class)
public abstract class MixinEntityFireworkRocket extends MixinEntity implements FireworkRocket {

    @Shadow private int fireworkAge;
    @Shadow private int lifetime;

    /**
     * @author jamierocks - 26th February 2017
     * @reason Fire FireworkExplodeHook
     */
    @Overwrite
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        this.motionX *= 1.15D;
        this.motionZ *= 1.15D;
        this.motionY += 0.04D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        // Neptune: convert to while loop
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (this.fireworkAge == 0 && !this.isSilent()) {
            this.worldObj.playSoundAtEntity((Entity) (Object) this, "fireworks.launch", 3.0F, 1.0F);
        }

        ++this.fireworkAge;

        if (this.worldObj.isRemote && this.fireworkAge % 2 < 2) {
            this.worldObj.spawnParticle(
                    EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D,
                    this.rand.nextGaussian() * 0.05D);
        }

        if (!this.worldObj.isRemote && this.fireworkAge > this.lifetime) {
            // Neptune: start
            final FireworkExplodeHook hook = (FireworkExplodeHook) new FireworkExplodeHook(this).call();
            if (!hook.isCanceled()) {
                this.worldObj.setEntityState((Entity) (Object) this, (byte) 17);
                this.setDead();
            }
            // Neptune: end
        }
    }

    @Override
    public Item getItem() {
        return (Item) this.dataWatcher.getWatchableObjectItemStack(8);
    }

    @Override
    public void setItem(Item item) {
        this.dataWatcher.updateObject(8, item);

        // Update flight information
        final NBTTagCompound tagCompound = ((ItemStack) item).getTagCompound().getCompoundTag("Fireworks");
        if (tagCompound != null) {
            this.lifetime = 10 * tagCompound.getByte("Flight") + this.rand.nextInt(6) + this.rand.nextInt(7);
        }
    }

    @Override
    public int getLifeTime() {
        return this.fireworkAge;
    }

    @Override
    public void setLifeTime(int life) {
        this.fireworkAge = life;
    }

    @Override
    public int getLifeTimeMax() {
        return this.lifetime;
    }

    @Override
    public void setLifeTimeMax(int life_time) {
        this.lifetime = life_time;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.FIREWORKROCKET;
    }

    @Override
    public String getFqName() {
        return "FireworkRocket";
    }

}
