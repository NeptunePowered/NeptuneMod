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
package org.neptunepowered.vanilla.mixin.minecraft.block;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockBase;
import net.canarymod.api.world.blocks.BlockMaterial;
import net.canarymod.api.world.blocks.MapColor;
import net.canarymod.api.world.position.Position;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(Block.class)
@Implements(@Interface(iface = BlockBase.class, prefix = "block$"))
public abstract class MixinBlock implements BlockBase {

    @Shadow protected Material blockMaterial;
    @Shadow protected boolean needsRandomTick;
    @Shadow protected double minX;
    @Shadow protected double minY;
    @Shadow protected double minZ;
    @Shadow protected double maxX;
    @Shadow protected double maxY;
    @Shadow protected double maxZ;

    @Shadow public abstract boolean isFullBlock();
    @Shadow public abstract int getLightOpacity();
    @Shadow public abstract int getLightValue();
    @Shadow public abstract boolean getUseNeighborBrightness();
    @Shadow public abstract boolean isNormalCube();
    @Shadow public abstract boolean isFullCube();
    @Shadow public abstract boolean hasTileEntity();
    @Shadow public abstract boolean isOpaqueCube();
    @Shadow public abstract boolean isCollidable();
    @Shadow public abstract int getMobilityFlag();

    @Intrinsic
    public boolean block$isFullBlock() {
        return this.isFullBlock();
    }

    @Intrinsic
    public int block$getLightOpacity() {
        return this.getLightOpacity();
    }

    @Intrinsic
    public int block$getLightValue() {
        return this.getLightValue();
    }

    @Intrinsic
    public boolean block$getUseNeighborBrightness() {
        return this.getUseNeighborBrightness();
    }

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

    @Intrinsic
    public boolean block$isNormalCube() {
        return this.isNormalCube();
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Intrinsic
    public boolean block$isFullCube() {
        return this.isFullCube();
    }

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

    @Intrinsic
    public boolean block$hasTileEntity() {
        return this.hasTileEntity();
    }

    @Intrinsic
    public boolean block$isOpaqueCube() {
        return this.isOpaqueCube();
    }

    @Intrinsic
    public boolean block$isCollidable() {
        return this.isCollidable();
    }

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

    @Intrinsic
    public int block$getMobilityFlag() {
        return this.getMobilityFlag();
    }

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
