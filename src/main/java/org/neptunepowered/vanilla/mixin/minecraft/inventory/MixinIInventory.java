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
package org.neptunepowered.vanilla.mixin.minecraft.inventory;

import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IInventory.class)
@Implements(@Interface(iface = Inventory.class, prefix = "inventory$"))
public interface MixinIInventory extends IInventory, Inventory {

    @Override
    default void addItem(Item var1){

    }

    @Override
    default void addItem(ItemType var1) {

    }

    @Override
    default void addItem(int var1) {

    }

    @Override
    default void addItem(String var1) {

    }

    @Override
    default void addItem(int var1, short var2) {

    }

    @Override
    default void addItem(int var1, int var2) {

    }

    @Override
    default void addItem(ItemType var1, int var2) {

    }

    @Override
    default void addItem(String var1, int var2) {

    }

    @Override
    default void addItem(int var1, int var2, short var3) {

    }

    @Override
    default void clearContents() {
        this.clear();
    }

    @Override
    default Item[] clearInventory() {
        return null;
    }

    @Override
    default void decreaseItemStackSize(int var1, int var2) {

    }

    @Override
    default void decreaseItemStackSize(int var1, int var2, short var3) {

    }

    @Override
    default void decreaseItemStackSize(Item var1) {

    }

    @Override
    default Item[] getContents() {
        return null;
    }

    @Override
    default int getEmptySlot() {
        return 0;
    }

    @Override
    default String getInventoryName() {
        return this.getName();
    }

    @Intrinsic
    default int inventory$getInventoryStackLimit() {
        return this.getInventoryStackLimit();
    }

    @Override
    default Item getItem(ItemType var1) {
        return null;
    }

    @Override
    default Item getItem(int var1) {
        return null;
    }

    @Override
    default Item getItem(String var1) {
        return null;
    }

    @Override
    default Item getItem(ItemType var1, int var2) {
        return null;
    }

    @Override
    default Item getItem(int var1, int var2) {
        return null;
    }

    @Override
    default Item getItem(String var1, int var2) {
        return null;
    }

    @Override
    default Item getItem(int var1, int var2, short var3) {
        return null;
    }

    @Override
    default int getSize() {
        return this.getSizeInventory();
    }

    @Override
    default Item getSlot(int var1) {
        return (Item) this.getStackInSlot(var1);
    }

    @Override
    default boolean hasItem(int var1) {
        return false;
    }

    @Override
    default boolean hasItem(String var1) {
        return false;
    }

    @Override
    default boolean hasItem(ItemType var1) {
        return false;
    }

    @Override
    default boolean hasItem(int var1, short var2) {
        return false;
    }

    @Override
    default boolean hasItemStack(ItemType var1, int var2) {
        return false;
    }

    @Override
    default boolean hasItemStack(int var1, int var2) {
        return false;
    }

    @Override
    default boolean hasItemStack(String var1, int var2) {
        return false;
    }

    default boolean hasItemStack(int var1, int var2, int var3) {
        return false;
    }

    @Override
    default boolean hasItemStack(String var1, int var2, int var3) {
        return false;
    }

    @Override
    default boolean hasItemStack(ItemType var1, int var2, int var3) {
        return false;
    }

    @Override
    default boolean hasItemStack(int var1, int var2, int var3, int var4) {
        return false;
    }

    @Override
    default boolean insertItem(Item var1) {
        return false;
    }

    @Override
    default void setSlot(Item var1) {

    }

    @Override
    default void setSlot(int var1, int var2, int var3) {

    }

    @Override
    default void setSlot(int var1, int var2, int var3, int var4) {

    }

    @Override
    default void setSlot(String var1, int var2, int var3) {

    }

    @Override
    default void setSlot(ItemType var1, int var2, int var3) {

    }

    @Override
    default Item removeItem(Item var1) {
        return null;
    }

    @Override
    default Item removeItem(int var1) {
        return null;
    }

    @Override
    default Item removeItem(int var1, int var2) {
        return null;
    }

    @Override
    default Item removeItem(String var1) {
        return null;
    }

    @Override
    default Item removeItem(ItemType var1) {
        return null;
    }

    @Override
    default void setContents(Item[] var1) {

    }

    @Override
    default void setInventoryName(String var1) {

    }

    @Override
    default void setSlot(int var1, Item var2) {

    }

    @Override
    default boolean canOpenRemote() {
        return false;
    }

    @Override
    default void setCanOpenRemote(boolean var1) {

    }

    @Override
    default void update() {

    }

    @Override
    default boolean canInsertItems(Item var1) {
        return false;
    }
}
