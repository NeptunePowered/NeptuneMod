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
package org.neptunepowered.vanilla.world;

import net.canarymod.api.world.ChunkProviderCustom;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

public class NeptuneChunkProviderCustom implements IChunkProvider {

    private final ChunkProviderCustom chunkProviderCustom;
    private final IChunkProvider chunkProvider;

    public NeptuneChunkProviderCustom(ChunkProviderCustom chunkProviderCustom, IChunkProvider chunkProvider) {
        this.chunkProviderCustom = chunkProviderCustom;
        this.chunkProvider = chunkProvider;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return this.chunkProvider.chunkExists(x, z);
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        return (Chunk) this.chunkProviderCustom.provideChunk(x, z);
    }

    @Override
    public Chunk provideChunk(BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }

    @Override
    public void populate(IChunkProvider chunkProvider, int x, int z) {
        this.chunkProviderCustom.populate(x, z);
    }

    @Override
    public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
        return this.chunkProvider.populateChunk(chunkProvider, chunkIn, x, z);
    }

    @Override
    public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
        return this.chunkProvider.saveChunks(saveAllChunks, progressCallback);
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return this.chunkProvider.makeString();
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.chunkProvider.getPossibleCreatures(creatureType, pos);
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return this.chunkProvider.getStrongholdGen(worldIn, structureName, position);
    }

    @Override
    public int getLoadedChunkCount() {
        return this.chunkProvider.getLoadedChunkCount();
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
    }

    @Override
    public void saveExtraData() {
        this.chunkProvider.saveExtraData();
    }
}
