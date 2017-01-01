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
package org.neptunepowered.vanilla.chunk;

import com.google.common.collect.Lists;
import net.canarymod.tasks.ServerTask;
import net.canarymod.tasks.TaskOwner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorld;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorldServer;

/**
 * A {@link ServerTask} for performing garbage collection on a {@link WorldServer}'s chunks.
 */
public final class ChunkGCTask extends ServerTask {

    private final WorldServer world;

    public ChunkGCTask(WorldServer world) {
        super((TaskOwner) world, ((IMixinWorldServer) world).getWorldConfig().getTickInterval(), true);
        this.world = world;
    }

    @Override
    public void run() {
        ((IMixinWorld) this.world).getTimings().doChunkGC.startTiming();
        for (Chunk chunk : Lists.newArrayList(this.world.theChunkProviderServer.func_152380_a())) {
            if (chunk != null && !this.world.getPlayerManager().hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
                this.world.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
            }
        }
        ((IMixinWorld) this.world).getTimings().doChunkGC.stopTiming();
    }

}
