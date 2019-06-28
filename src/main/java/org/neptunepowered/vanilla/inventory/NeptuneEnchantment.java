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
package org.neptunepowered.vanilla.inventory;

import net.canarymod.api.DamageSource;
import net.canarymod.api.entity.living.EntityLiving;
import net.canarymod.api.inventory.Enchantment;
import net.canarymod.api.inventory.Item;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemStack;
import org.neptunepowered.vanilla.util.Wrapper;

/**
 * Wrapper implementation of {@link Enchantment}.
 */
public class NeptuneEnchantment
        extends Wrapper<net.minecraft.enchantment.Enchantment>
        implements Enchantment {

    private short level;

    public NeptuneEnchantment(final net.minecraft.enchantment.Enchantment handle) {
        super(handle);
    }

    public NeptuneEnchantment(final EnchantmentData data) {
        super(data.enchantmentobj);
        this.level = (short) data.enchantmentLevel;
    }

    public NeptuneEnchantment(final Type type, final short level) {
        super(net.minecraft.enchantment.Enchantment.getEnchantmentById(type.getId()));
        this.level = level;
    }

    @Override
    public int getWeight() {
        return this.getHandle().getWeight();
    }

    @Override
    public int getMinEnchantmentLevel() {
        return this.getHandle().getMinLevel();
    }

    @Override
    public int getMaxEnchantmentLevel() {
        return this.getHandle().getMaxLevel();
    }

    @Override
    public int getMinEnchantability() {
        return this.getHandle().getMinEnchantability(this.level);
    }

    @Override
    public int getMaxEnchantability() {
        return this.getHandle().getMaxEnchantability(this.level);
    }

    @Override
    public int getDamageModifier(final DamageSource source) {
        return this.getHandle().calcModifierDamage(this.level, (net.minecraft.util.DamageSource) source);
    }

    @Override
    public float getDamageModifier(final EntityLiving entity) {
        // Note: This is matching the implementation in CanaryMod
        //       It was assumed that this method was removed in 1.8
        return 0;
    }

    @Override
    public boolean canStack(final Enchantment other) {
        return this.getHandle().canApplyTogether(((NeptuneEnchantment) other).getHandle());
    }

    @Override
    public boolean canEnchantItem(final Item item) {
        return this.getHandle().canApply((ItemStack) item);
    }

    @Override
    public Type getType() {
        return Type.fromId(this.getHandle().effectId);
    }

    @Override
    public short getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final short level) {
        this.level = level;
    }

    @Override
    public boolean isValid() {
        return this.level >= this.getMinEnchantmentLevel() && this.level <= this.getMaxEnchantmentLevel();
    }

    public EnchantmentData createEnchantmentData() {
        return new EnchantmentData(this.getHandle(), this.level);
    }

}
