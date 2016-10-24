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
package org.neptunepowered.vanilla.mixin.minecraft.world.gen;

import co.aikar.timings.Timing;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import org.apache.logging.log4j.Logger;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer {

    @Shadow @Final private static Logger logger;
    @Shadow private IChunkLoader chunkLoader;
    @Shadow private WorldServer worldObj;

    /**
     * @author jamierocks - 25th October 2016
     * @reason Add timings calls
     */
    @Overwrite
    private void saveChunkExtraData(Chunk chunkIn) {
        if (this.chunkLoader != null) {
            try (Timing ignored = ((IMixinWorld) this.worldObj).getTimings().chunkSaveNop.startTiming()) { // Neptune - timings
                this.chunkLoader.saveExtraChunkData(this.worldObj, chunkIn);
            } catch (Exception exception) {
                logger.error("Couldn't save entities", exception);
            }
        }
    }

    /**
     * @author jamierocks - 25th October 2016
     * @reason Add timings calls
     */
    @Overwrite
    private void saveChunkData(Chunk chunkIn) {
        if (this.chunkLoader != null) {
            try (Timing ignored = ((IMixinWorld) this.worldObj).getTimings().chunkSaveData.startTiming()) { // Neptune - timings
                chunkIn.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkLoader.saveChunk(this.worldObj, chunkIn);
            } catch (IOException ioexception) {
                logger.error("Couldn't save chunk", ioexception);
            } catch (MinecraftException minecraftexception) {
                logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", minecraftexception);
            }
        }
    }

}
