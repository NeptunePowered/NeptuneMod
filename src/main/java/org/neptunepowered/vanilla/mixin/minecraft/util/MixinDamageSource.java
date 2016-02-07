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
package org.neptunepowered.vanilla.mixin.minecraft.util;

import net.canarymod.api.DamageSource;
import net.canarymod.api.DamageType;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.util.DamageSource.class)
public abstract class MixinDamageSource implements DamageSource {

    @Shadow public String damageType;
    @Shadow private boolean isDamageAllowedInCreativeMode;
    @Shadow private boolean fireDamage;
    @Shadow private float hungerDamage;
    @Shadow private boolean damageIsAbsolute;

    @Shadow
    public abstract IChatComponent getDeathMessage(EntityLivingBase p_151519_1_);

    @Shadow
    public abstract net.minecraft.entity.Entity getEntity();

    @Override
    public boolean validInCreativeMode() {
        return isDamageAllowedInCreativeMode;
    }

    @Override
    @Shadow
    public abstract boolean isFireDamage();

    @Override
    @Shadow
    public abstract boolean isProjectile();

    @Override
    public DamageType getDamagetype() {
        return DamageType.fromDamageSource(this);
    }

    @Override
    public void setCustomDeathMessage(String deathmessage) {
        // TODO: Might involve some complicated stuff
    }

    @Override
    public String getDeathMessage(Player player) {
        return getDeathMessage((EntityLivingBase) player).getUnformattedText();
    }

    @Override
    @Shadow
    public abstract float getHungerDamage();

    @Override
    public void setHungerDamage(float hunger) {
        hungerDamage = hunger;
    }

    @Override
    public Entity getDamageDealer() {
        return (Entity) getEntity();
    }

    @Override
    public boolean isUnblockable() {
        return damageIsAbsolute;
    }

    @Override
    public void setUnblockable(boolean blockable) {
        damageIsAbsolute = blockable;
    }

    @Override
    public String getNativeName() {
        return damageType;
    }

    @Override
    public boolean isCritical() {
        return false;
    }
}
