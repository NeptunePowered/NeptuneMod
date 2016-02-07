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
import net.canarymod.api.entity.XPOrb;
import net.canarymod.api.entity.living.humanoid.Player;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import org.neptunepowered.vanilla.mixin.minecraft.entity.MixinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityXPOrb.class)
public abstract class MixinEntityXPOrb extends MixinEntity implements XPOrb {

    @Shadow public int xpOrbAge;
    @Shadow public int delayBeforeCanPickup;
    @Shadow private int xpOrbHealth;
    @Shadow private int xpValue;
    @Shadow private EntityPlayer closestPlayer;

    @Override
    public short getOrbAge() {
        return (short) this.xpOrbAge;
    }

    @Override
    public void setOrbAge(short age) {
        this.xpOrbAge = age;
    }

    @Override
    public int getPickUpDelay() {
        return this.delayBeforeCanPickup;
    }

    @Override
    public void setPickUpDelay(int delay) {
        this.delayBeforeCanPickup = delay;
    }

    @Override
    public short getHealth() {
        return (short) this.xpOrbHealth;
    }

    @Override
    public void setHealth(short health) {
        this.xpOrbHealth = health;
    }

    @Override
    public short getXPValue() {
        return (short) this.xpValue;
    }

    @Override
    public void setXPValue(short value) {
        this.xpValue = value;
    }

    @Override
    public Player getClosestPlayer() {
        return (Player) this.closestPlayer;
    }

    @Override
    public String getFqName() {
        return "XPOrb";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.XPORB;
    }
}
