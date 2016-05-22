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
package org.neptunepowered.vanilla.mixin.minecraft.world.chunk;

import com.google.common.collect.Maps;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.Biome;
import net.canarymod.api.world.BiomeType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.api.world.position.Position;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import org.neptunepowered.vanilla.util.converter.PositionConverter;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(Chunk.class)
@Implements(@Interface(iface = net.canarymod.api.world.Chunk.class, prefix = "chunk$"))
public abstract class MixinChunk implements net.canarymod.api.world.Chunk {

    @Shadow private int xPosition;
    @Shadow private int zPosition;
    @Shadow private boolean isModified;
    @Shadow private boolean isTerrainPopulated;
    @Shadow private boolean hasEntities;
    @Shadow private long lastSaveTime;
    @Shadow private int[] precipitationHeightMap;
    @Shadow private byte[] blockBiomeArray;
    @Shadow private net.minecraft.world.World worldObj;

    @Shadow
    public abstract BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager);

    @Shadow
    public abstract void generateSkylightMap();

    @Shadow
    public abstract boolean isLoaded();

    @Shadow
    public abstract Map<BlockPos, net.minecraft.tileentity.TileEntity> shadow$getTileEntityMap();

    @Shadow
    public abstract int[] getHeightMap();

    @Shadow
    public abstract void relightBlock(int x, int y, int z);

    @Override
    public int getX() {
        return xPosition;
    }

    @Override
    public int getZ() {
        return zPosition;
    }

    @Override
    public int getBlockTypeAt(int x, int y, int z) {
        return 0;
    }

    @Override
    public void setBlockTypeAt(int x, int y, int z, int type) {

    }

    @Override
    public int getBlockDataAt(int x, int y, int z) {
        return 0;
    }

    @Override
    public void setBlockDataAt(int x, int y, int z, int data) {

    }

    @Override
    public int getMaxHeigth() {
        return 256;
    }

    @Override
    public int getMaxHeight() {
        return 256;
    }

    @Intrinsic
    public boolean chunk$isLoaded() {
        return this.isLoaded();
    }

    @Override
    public World getDimension() {
        return (World) worldObj;
    }

    @Override
    public BiomeType[] getBiomeData() {
        return new BiomeType[0];
    }

    @Override
    public void setBiomeData(byte[] data) {
        blockBiomeArray = data;
    }

    @Override
    public void setBiomeData(BiomeType[] data) {

    }

    @Override
    public byte[] getBiomeByteData() {
        return blockBiomeArray;
    }

    @Override
    public Biome getBiome(int x, int z) {
        return (Biome) getBiome(new BlockPos(x, 0, z), worldObj.getWorldChunkManager());
    }

    /**
     * This is a hack as so that it's not included on the dev env as it will cause a StackOverflowException :(
     *
     * @author jamierocks
     */
    @Intrinsic
    public Map<Position, TileEntity> chunk$getTileEntityMap() {
        final Map<Position, TileEntity> tileEntityMap = Maps.newHashMap();

        for (BlockPos pos : this.shadow$getTileEntityMap().keySet()) {
            net.minecraft.tileentity.TileEntity tileEntity = this.shadow$getTileEntityMap().get(pos);
            tileEntityMap.put(PositionConverter.of(pos), (TileEntity) tileEntity);
        }

        return tileEntityMap;
    }

    @Override
    public boolean hasEntities() {
        return hasEntities;
    }

    @Override
    public List<Entity>[] getEntityLists() {
        return null;
    }

    @Intrinsic
    public int[] chunk$getHeightMap() {
        return this.getHeightMap();
    }

    @Override
    public int[] getPrecipitationHeightMap() {
        return precipitationHeightMap;
    }

    @Override
    public long getLastSaveTime() {
        return lastSaveTime;
    }

    @Override
    public boolean isTerrainPopulated() {
        return isTerrainPopulated;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void generateSkyLightMap() {
        generateSkylightMap();
    }

    @Override
    public void updateSkyLightMap(boolean force) {

    }

    @Intrinsic
    public void chunk$relightBlock(int x, int y, int z) {
        this.relightBlock(x, y, z);
    }
}
