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
package org.neptunepowered.vanilla.mixin.minecraft.potion;

import net.canarymod.api.potion.PotionEffectType;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Potion.class)
@Implements(@Interface(iface = net.canarymod.api.potion.Potion.class, prefix = "potion$"))
public abstract class MixinPotion implements net.canarymod.api.potion.Potion {

    @Shadow @Final public int id;
    @Shadow @Final private boolean isBadEffect;

    @Shadow public abstract String getName();
    @Shadow public abstract double getEffectiveness();
    @Shadow public abstract boolean isUsable();
    @Shadow public abstract boolean isInstant();

    @Override
    public int getID() {
        return this.id;
    }

    @Intrinsic
    public String potion$getName() {
        return this.getName();
    }

    @Override
    public PotionEffectType getEffectType() {
        return PotionEffectType.fromName(this.getName());
    }

    @Override
    public boolean isBad() {
        return this.isBadEffect;
    }

    @Intrinsic
    public double potion$getEffectiveness() {
        return this.getEffectiveness();
    }

    @Intrinsic
    public boolean potion$isUsable() {
        return this.isUsable();
    }

    @Intrinsic
    public boolean potion$isInstant() {
        return this.isInstant();
    }

}
