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
package org.neptunepowered.vanilla.mixin.minecraft.potion;

import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.potion.PotionEffect;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.potion.PotionEffect.class)
@Implements(@Interface(iface = PotionEffect.class, prefix = "effect$"))
public abstract class MixinPotionEffect implements PotionEffect {

    @Shadow private boolean isAmbient;

    @Shadow public abstract String getEffectName();
    @Shadow public abstract int getPotionID();
    @Shadow public abstract int getDuration();
    @Shadow public abstract int getAmplifier();
    @Shadow public abstract void performEffect(EntityLivingBase entityIn);

    @Intrinsic
    public int effect$getPotionID() {
        return this.getPotionID();
    }

    @Intrinsic
    public int effect$getDuration() {
        return this.getDuration();
    }

    @Intrinsic
    public int effect$getAmplifier() {
        return this.getAmplifier();
    }

    @Override
    public boolean isAmbient() {
        return this.isAmbient;
    }

    @Override
    public String getName() {
        return this.getEffectName();
    }

    @Override
    public void performEffect(LivingBase entity) {
        this.performEffect((EntityLivingBase) entity);
    }

}
