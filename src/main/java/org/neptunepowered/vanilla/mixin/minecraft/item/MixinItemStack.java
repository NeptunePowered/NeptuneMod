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

import com.google.common.collect.Multimap;
import net.canarymod.api.attributes.AttributeModifier;
import net.canarymod.api.inventory.BaseItem;
import net.canarymod.api.inventory.Enchantment;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.nbt.CompoundTag;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = Item.class, prefix = "item$"))
public abstract class MixinItemStack implements Item {

    @Shadow public int stackSize;
    @Shadow private net.minecraft.item.Item item;
    @Shadow private int itemDamage;

    @Shadow public abstract boolean isItemEnchantable();
    @Shadow public abstract boolean isItemEnchanted();
    @Shadow public abstract ItemStack copy();
    @Shadow public abstract void clearCustomName();
    @Shadow public abstract ItemStack setStackDisplayName(String displayName);
    @Shadow public abstract boolean hasDisplayName();
    @Shadow public abstract String getDisplayName();
    @Shadow public abstract int getRepairCost();
    @Shadow public abstract void setRepairCost(int cost);

    @Override
    public int getId() {
        return net.minecraft.item.Item.getIdFromItem(item);
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public int getDamage() {
        return itemDamage;
    }

    @Override
    public void setDamage(int damage) {
        itemDamage = damage;
    }

    @Override
    public int getAmount() {
        return stackSize;
    }

    @Override
    public void setAmount(int amount) {
        stackSize = amount;
    }

    @Override
    public int getMaxAmount() {
        return item.getItemStackLimit();
    }

    @Override
    public void setMaxAmount(int amount) {
        item.setMaxStackSize(amount);
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public void setSlot(int slot) {

    }

    @Override
    public ItemType getType() {
        return ItemType.fromId(getId());
    }

    @Override
    public BaseItem getBaseItem() {
        return (BaseItem) item;
    }

    @Override
    public boolean isEnchanted() {
        return isItemEnchanted();
    }

    @Override
    public boolean isEnchantable() {
        return isItemEnchantable();
    }

    @Override
    public Enchantment getEnchantment() {
        return null;
    }

    @Override
    public Enchantment getEnchantment(int index) {
        return null;
    }

    @Override
    public Enchantment[] getEnchantments() {
        return new Enchantment[0];
    }

    @Override
    public void setEnchantments(Enchantment... enchantments) {

    }

    @Override
    public void addEnchantments(Enchantment... enchantments) {

    }

    @Override
    public void removeEnchantment(Enchantment enchantment) {

    }

    @Override
    public void removeAllEnchantments() {

    }

    @Intrinsic
    public boolean item$hasDisplayName() {
        return this.hasDisplayName();
    }

    @Intrinsic
    public String item$getDisplayName() {
        return this.getDisplayName();
    }

    @Override
    public void setDisplayName(String name) {
        setStackDisplayName(name);
    }

    @Override
    public void removeDisplayName() {
        clearCustomName();
    }

    @Intrinsic
    public int item$getRepairCost() {
        return this.getRepairCost();
    }

    @Intrinsic
    public void item$setRepairCost(int cost) {
        this.setRepairCost(cost);
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }

    @Override
    public void setLore(String... lore) {

    }

    @Override
    public boolean hasLore() {
        return false;
    }

    @Override
    public boolean hasMetaTag() {
        return false;
    }

    @Override
    public CompoundTag getMetaTag() {
        return null;
    }

    @Override
    public boolean hasDataTag() {
        return false;
    }

    @Override
    public CompoundTag getDataTag() {
        return null;
    }

    @Override
    public void setDataTag(CompoundTag tag) {

    }

    @Override
    public CompoundTag writeToTag(CompoundTag tag) {
        return null;
    }

    @Override
    public void readFromTag(CompoundTag tag) {

    }

    @Override
    public Item clone() {
        return (Item) copy();
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributes() {
        return null;
    }

    @Override
    public void updateAttributes(Multimap<String, AttributeModifier> attributeMap) {

    }

    @Override
    public boolean equalsIgnoreSize(Item item) {
        return false;
    }

}
