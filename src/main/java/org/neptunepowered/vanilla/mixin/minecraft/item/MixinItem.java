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
package org.neptunepowered.vanilla.mixin.minecraft.item;

import net.canarymod.api.inventory.BaseItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
@Implements(@Interface(iface = BaseItem.class, prefix = "item$"))
public abstract class MixinItem implements BaseItem {

    @Shadow protected int maxStackSize;

    @Shadow public abstract Item shadow$setMaxDamage(int maxDamageIn);
    @Shadow public abstract int getMaxDamage();
    @Shadow public abstract boolean isDamageable();

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Intrinsic
    public int item$getMaxDamage() {
        return this.getMaxDamage();
    }

    @Override
    public void setMaxDamage(int damage) {
        shadow$setMaxDamage(damage);
    }

    @Intrinsic
    public boolean item$isDamageable() {
        return this.isDamageable();
    }

}
