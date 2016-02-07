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
package org.neptunepowered.vanilla.mixin.minecraft.tileentity;

import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.tileentity.TileEntity.class)
public abstract class MixinTileEntity implements TileEntity {

    @Shadow protected net.minecraft.block.Block blockType;
    @Shadow protected net.minecraft.world.World worldObj;
    @Shadow protected BlockPos pos;

    @Shadow
    public abstract void updateContainingBlockInfo();

    @Shadow
    public abstract void readFromNBT(NBTTagCompound compound);

    @Shadow
    public abstract void writeToNBT(NBTTagCompound compound);

    @Override
    public Block getBlock() {
        return (Block) blockType;
    }

    @Override
    public int getX() {
        return pos.getX();
    }

    @Override
    public int getY() {
        return pos.getY();
    }

    @Override
    public int getZ() {
        return pos.getZ();
    }

    @Override
    public World getWorld() {
        return (World) worldObj;
    }

    @Override
    public void update() {
        updateContainingBlockInfo();
    }

    @Override
    public CompoundTag getDataTag() {
        return null;
    }

    @Override
    public CompoundTag getMetaTag() {
        return null;
    }

    @Override
    public CompoundTag writeToTag(CompoundTag tag) {
        NBTTagCompound mcTag = (NBTTagCompound) tag;
        writeToNBT(mcTag);
        return (CompoundTag) mcTag;
    }

    @Override
    public void readFromTag(CompoundTag tag) {
        readFromNBT((NBTTagCompound) tag);
    }
}
