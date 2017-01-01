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
package org.neptunepowered.vanilla.mixin.minecraft.entity.player;

import net.canarymod.api.inventory.InventoryType;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.PlayerInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryPlayer.class)
public abstract class MixinInventoryPlayer implements PlayerInventory {

    @Shadow public int currentItem;

    @Shadow public abstract ItemStack getCurrentItem();

    @Override
    public Item getHelmetSlot() {
        return null;
    }

    @Override
    public void setHelmetSlot(Item item) {

    }

    @Override
    public Item getChestplateSlot() {
        return null;
    }

    @Override
    public void setChestPlateSlot(Item item) {

    }

    @Override
    public Item getLeggingsSlot() {
        return null;
    }

    @Override
    public void setLeggingsSlot(Item item) {

    }

    @Override
    public Item getBootsSlot() {
        return null;
    }

    @Override
    public void setBootsSlot(Item item) {

    }

    @Override
    public int getSelectedHotbarSlotId() {
        return this.currentItem;
    }

    @Override
    public Item getItemInHand() {
        return (Item) this.getCurrentItem();
    }

    @Override
    public Item getItemOnCursor() {
        return null;
    }

    @Override
    public void setItemOnCursor(Item item) {

    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.PLAYER;
    }

}
