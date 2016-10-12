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
package org.neptunepowered.vanilla.factory;

import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.inventory.Enchantment;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.inventory.MapData;

public class NeptuneItemFactory implements ItemFactory {

    protected NeptuneItemFactory() {}

    @Override
    public Item newItem(int id) {
        return (Item) net.minecraft.item.Item.getItemById(id);
    }

    @Override
    public Item newItem(int id, int damage) {
        final net.minecraft.item.Item item = net.minecraft.item.Item.getItemById(id);
        item.setMaxDamage(damage);
        return (Item) item;
    }

    @Override
    public Item newItem(int id, int damage, int stackSize) {
        final net.minecraft.item.Item item = net.minecraft.item.Item.getItemById(id);
        item.setMaxDamage(damage);
        item.setMaxStackSize(stackSize);
        return (Item) item;
    }

    @Override
    public Item newItem(ItemType type) {
        final net.minecraft.item.Item item = net.minecraft.item.Item.getItemById(type.getId());
        item.setMaxDamage(type.getData());
        return (Item) item;
    }

    @Override
    public Item newItem(ItemType type, int damage) {
        final net.minecraft.item.Item item = net.minecraft.item.Item.getItemById(type.getId());
        item.setMaxDamage(damage);
        return (Item) item;
    }

    @Override
    public Item newItem(ItemType type, int damage, int stackSize) {
        final net.minecraft.item.Item item = net.minecraft.item.Item.getItemById(type.getId());
        item.setMaxDamage(damage);
        item.setMaxStackSize(stackSize);
        return (Item) item;
    }

    @Override
    public Item newItem(Item item) {
        return null;
    }

    @Override
    public Item newItem(int id, int damage, Enchantment[] enchantments) {
        return null;
    }

    @Override
    public Item newItem(String commandInput) {
        return null;
    }

    @Override
    public Enchantment newEnchantment(short id, short level) {
        return null;
    }

    @Override
    public Enchantment newEnchantment(Enchantment.Type type, short level) {
        return null;
    }

    @Override
    public MapData getMapData(Item item) {
        return null;
    }

}
