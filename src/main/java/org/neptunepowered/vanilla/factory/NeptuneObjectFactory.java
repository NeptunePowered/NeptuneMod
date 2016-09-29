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

import net.canarymod.api.MobSpawnerEntry;
import net.canarymod.api.VillagerTrade;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.factory.ObjectFactory;
import net.canarymod.api.inventory.CustomStorageInventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.Chunk;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.properties.BlockProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

public class NeptuneObjectFactory implements ObjectFactory {

    public NeptuneObjectFactory() {}

    @Override
    public VillagerTrade newVillagerTrade(Item buying, Item selling) {
        return (VillagerTrade) new MerchantRecipe((ItemStack) buying, (ItemStack) selling);
    }

    @Override
    public VillagerTrade newVillagerTrade(Item buyingOne, Item buyingTwo, Item selling) {
        return (VillagerTrade) new MerchantRecipe((ItemStack) buyingOne, (ItemStack) buyingTwo, (ItemStack) selling);
    }

    @Override
    public MobSpawnerEntry newMobSpawnerEntry(String entity) {
        return null;
    }

    @Override
    public MobSpawnerEntry newMobSpawnerEntry(Entity entity) {
        return null;
    }

    @Override
    public MobSpawnerEntry newMobSpawnerEntry(Item item) {
        return null;
    }

    @Override
    public CustomStorageInventory newCustomStorageInventory(int rows) {
        return null;
    }

    @Override
    public CustomStorageInventory newCustomStorageInventory(String name, int rows) {
        return null;
    }

    @Override
    public Chunk newChunk(World world, int x, int z) {
        return null;
    }

    @Override
    public <T extends BlockProperty> T getPropertyInstance(BlockType blockType, String propertyName) {
        return null;
    }

}
