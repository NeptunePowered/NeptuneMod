/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
package org.neptunepowered.common.mixin.minecraft.entity.item;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.hanging.ItemFrame;
import net.canarymod.api.inventory.Item;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import org.neptunepowered.common.mixin.minecraft.entity.MixinEntityHanging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityItemFrame.class)
public abstract class MixinEntityItemFrame extends MixinEntityHanging implements ItemFrame {

    @Shadow private float itemDropChance;

    @Shadow
    public abstract ItemStack getDisplayedItem();

    @Shadow
    public abstract void setDisplayedItem(ItemStack p_82334_1_);

    @Shadow
    public abstract int shadow$getRotation();

    @Override
    public Item getItemInFrame() {
        return (Item) this.getDisplayedItem();
    }

    @Override
    public void setItemInFrame(Item item) {
        this.setDisplayedItem((ItemStack) item);
    }

    @Override
    public int getItemRotation() {
        return this.shadow$getRotation();
    }

    @Override
    @Shadow
    public abstract void setItemRotation(int rot);

    @Override
    public float getItemDropChance() {
        return this.itemDropChance;
    }

    @Override
    public void setItemDropChance(float chance) {
        this.itemDropChance = chance;
    }

    @Override
    public String getFqName() {
        return "ItemFrame";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ITEMFRAME;
    }
}
