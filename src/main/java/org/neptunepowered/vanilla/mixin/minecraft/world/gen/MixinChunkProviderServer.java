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
package org.neptunepowered.vanilla.mixin.minecraft.world.gen;

import com.google.common.collect.Lists;
import net.canarymod.api.world.ChunkProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChunkProviderServer.class)
@Implements(@Interface(iface = ChunkProvider.class, prefix = "provider$"))
public abstract class MixinChunkProviderServer implements IChunkProvider {

    @Shadow public abstract List<Chunk> func_152380_a();
    @Shadow public abstract Chunk loadChunk(int chunkX, int chunkZ);
    @Shadow public abstract void dropChunk(int x, int z);


    @Intrinsic
    public boolean provider$canSave() {
        return this.canSave();
    }

    @Intrinsic
    public boolean provider$chunkExists(int i, int i1) {
        return this.chunkExists(i, i1);
    }

    public net.canarymod.api.world.Chunk provider$loadChunk(int i, int i1) {
        return (net.canarymod.api.world.Chunk) this.loadChunk(i, i1);
    }

    public void provider$populate(ChunkProvider chunkProvider, int i, int i1) {
        this.populate((IChunkProvider) chunkProvider, i, i1);
    }

    public String provider$getStatistics() {
        return this.makeString();
    }

    public void provider$reloadChunk(int i, int i1) {
        this.dropChunk(i, i1);
        this.loadChunk(i, i1);
    }

    @Intrinsic
    public void provider$dropChunk(int i, int i1) {
        this.dropChunk(i, i1);
    }

    public net.canarymod.api.world.Chunk provider$provideChunk(int i, int i1) {
        return (net.canarymod.api.world.Chunk) this.provideChunk(i, i1);
    }

    public boolean provider$saveChunk(boolean b) {
        return this.saveChunks(b, null);
    }

    public net.canarymod.api.world.Chunk provider$regenerateChunk(int i, int i1) {
        // TODO: implement
        return null;
    }

    public boolean provider$isChunkLoaded(int i, int i1) {
        return this.chunkExists(i, i1);
    }

    public List<net.canarymod.api.world.Chunk> provider$getLoadedChunks() {
        return (List) Lists.newArrayList(this.func_152380_a());
    }

}
