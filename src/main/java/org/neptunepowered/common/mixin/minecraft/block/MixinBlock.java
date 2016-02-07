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
package org.neptunepowered.common.mixin.minecraft.block;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockBase;
import net.canarymod.api.world.blocks.BlockMaterial;
import net.canarymod.api.world.blocks.MapColor;
import net.canarymod.api.world.position.Position;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(Block.class)
public abstract class MixinBlock implements BlockBase {

    @Shadow protected Material blockMaterial;
    @Shadow protected boolean needsRandomTick;
    @Shadow protected double minX;
    @Shadow protected double minY;
    @Shadow protected double minZ;
    @Shadow protected double maxX;
    @Shadow protected double maxY;
    @Shadow protected double maxZ;

    @Override
    @Shadow
    public abstract boolean isFullBlock();

    @Override
    @Shadow
    public abstract int getLightOpacity();

    @Override
    @Shadow
    public abstract int getLightValue();

    @Override
    @Shadow
    public abstract boolean getUseNeighborBrightness();

    @Override
    public BlockMaterial getMaterial() {
        return (BlockMaterial) this.blockMaterial;
    }

    @Override
    public MapColor getMapColor(net.canarymod.api.world.blocks.Block block) {
        return null;
    }

    @Override
    public boolean isSolidFullCube() {
        return false;
    }

    @Override
    @Shadow
    public abstract boolean isNormalCube();

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Shadow
    @Override
    public abstract boolean isFullCube();

    @Override
    public boolean isPassable(net.canarymod.api.world.blocks.Block block, Position pos) {
        return false;
    }

    @Override
    public int getRenderType() {
        return 0;
    }

    @Override
    public boolean isReplaceable(World worldIn, Position pos) {
        return false;
    }

    @Override
    public float getBlockHardness(World worldIn, Position pos) {
        return 0;
    }

    @Override
    public boolean ticksRandomly() {
        return this.needsRandomTick;
    }

    @Override
    @Shadow
    public abstract boolean hasTileEntity();

    @Override
    @Shadow
    public abstract boolean isOpaqueCube();

    @Override
    @Shadow
    public abstract boolean isCollidable();

    @Override
    public int tickRate(World worldIn) {
        return 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int damageDropped(net.canarymod.api.world.blocks.Block block) {
        return 0;
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return 0;
    }

    @Override
    public double getBlockBoundsMinX() {
        return this.minX;
    }

    @Override
    public double getBlockBoundsMaxX() {
        return this.maxX;
    }

    @Override
    public double getBlockBoundsMinY() {
        return this.minY;
    }

    @Override
    public double getBlockBoundsMaxY() {
        return this.maxY;
    }

    @Override
    public double getBlockBoundsMinZ() {
        return this.minZ;
    }

    @Override
    public double getBlockBoundsMaxZ() {
        return this.maxZ;
    }

    @Override
    public boolean canProvidePower() {
        return false;
    }

    @Override
    public String getLocalizedName() {
        return null;
    }

    @Override
    public boolean getEnableStats() {
        return false;
    }

    @Override
    @Shadow
    public abstract int getMobilityFlag();

    @Override
    public boolean requiresUpdates() {
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return false;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, Position pos) {
        return 0;
    }
}
