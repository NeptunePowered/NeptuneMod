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
package org.neptunepowered.vanilla.mixin.minecraft.entity.item;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.TNTPrimed;
import net.canarymod.api.entity.living.LivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import org.neptunepowered.vanilla.mixin.minecraft.entity.MixinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTNTPrimed.class)
public abstract class MixinEntityTNTPrimed extends MixinEntity implements TNTPrimed {

    @Shadow public int fuse;

    private boolean damageWorld = true;
    private boolean damageEntity = true;
    private float power = 4.0f;

    @Shadow public abstract EntityLivingBase getTntPlacedBy();

    @Override
    public LivingBase getActivatedBy() {
        return (LivingBase) this.getTntPlacedBy();
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
        return this.fuse;
    }

    @Override
    public void setFuse(int i) {
        this.fuse = i;
    }

    @Override
    public void increaseFuse(int i) {
        this.fuse += i;
    }

    @Override
    public void decreaseFuse(int i) {
        this.fuse -= i;
    }

    @Override
    public void detonate() {
        this.fuse = 0;
    }

    @Override
    public NBTTagCompound writeCanaryNBT(NBTTagCompound tagCompound) {
        super.writeCanaryNBT(tagCompound);
        tagCompound.setBoolean("DamageEntities", this.damageEntity);
        tagCompound.setBoolean("DamageWorld", this.damageWorld);
        tagCompound.setFloat("Power", this.power);
        return tagCompound;
    }

    @Override
    public void readCanaryNBT(NBTTagCompound tagCompound) {
        super.readCanaryNBT(tagCompound);
        this.damageEntity = !tagCompound.hasKey("DamageEntities") || tagCompound.getBoolean("DamageEntities");
        this.damageWorld = !tagCompound.hasKey("DamageWorld") || tagCompound.getBoolean("DamageWorld");
        this.power = !tagCompound.hasKey("Power") ? tagCompound.getFloat("Power") : 4.0f;
    }

    @Override
    public String getFqName() {
        return "TNTPrimed";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.TNTPRIMED;
    }

}
