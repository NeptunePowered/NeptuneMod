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
package org.neptunepowered.vanilla.mixin.perf.world;

import net.canarymod.Canary;
import net.canarymod.api.world.World;
import net.canarymod.config.Configuration;
import net.canarymod.config.WorldConfiguration;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.neptunepowered.vanilla.chunk.ChunkGCTask;
import org.neptunepowered.vanilla.interfaces.perf.world.IMixinWorldServer_Performance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer_Performance extends MixinWorld_Performance implements World, IMixinWorldServer_Performance {

    private WorldConfiguration worldConfig;

    @Shadow protected abstract void saveLevel() throws MinecraftException;

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onConstruction(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn,
            CallbackInfo ci) {
        // Get the world configuration
        this.worldConfig = Configuration.getWorldConfig(this.getFqName());

        // Register ths ChunkGC task
        Canary.getServer().addSynchronousTask(new ChunkGCTask((WorldServer) (Object) this));
    }

    /**
     * @author jamierocks - 25th October 2016
     * @reason Disable vanilla ChunkGC
     */
    @Overwrite
    public void saveAllChunks(boolean p_73044_1_, IProgressUpdate progressCallback) throws MinecraftException {
        if (this.chunkProvider.canSave()) {
            if (progressCallback != null) {
                progressCallback.displaySavingString("Saving level");
            }

            this.saveLevel();

            if (progressCallback != null) {
                progressCallback.displayLoadingString("Saving chunks");
            }

            this.chunkProvider.saveChunks(p_73044_1_, progressCallback);

            // Neptune - Disable vanilla ChunkGC
            // for (net.minecraft.world.chunk.Chunk chunk : Lists.newArrayList(this.theChunkProviderServer.func_152380_a())) {
            //     if (chunk != null && !this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
            //         this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
            //     }
            // }
            // Neptune - end
        }
    }

    @Override
    public WorldConfiguration getWorldConfig() {
        return this.worldConfig;
    }

}
