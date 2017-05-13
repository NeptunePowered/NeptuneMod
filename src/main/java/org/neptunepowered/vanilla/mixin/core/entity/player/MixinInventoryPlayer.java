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
package org.neptunepowered.vanilla.mixin.core.entity.player;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.InventoryType;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.PlayerInventory;
import net.canarymod.hook.player.ArmorBrokenHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryPlayer.class)
public abstract class MixinInventoryPlayer implements PlayerInventory {

    @Shadow public int currentItem;
    @Shadow public ItemStack[] armorInventory;
    @Shadow public EntityPlayer player;

    @Shadow public abstract ItemStack getCurrentItem();

    /**
     * @author jamierocks - 12th January 2017
     * @reason Fire ArmorBrokenHook
     */
    @Overwrite
    public void damageArmor(float damage) {
        damage = damage / 4.0F;

        if (damage < 1.0F) {
            damage = 1.0F;
        }

        for (int i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
                this.armorInventory[i].damageItem((int)damage, this.player);

                if (this.armorInventory[i].stackSize == 0) {
                    // Neptune - ArmorBrokenHook start
                    final ArmorBrokenHook hook = (ArmorBrokenHook) new ArmorBrokenHook((Player) this.player, (Item) this.armorInventory[i]).call();
                    if (hook.getArmor().getAmount() <= 0) {
                        this.armorInventory[i] = null;
                    }
                    // Neptune - ArmorBrokenHook end
                }
            }
        }
    }

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
