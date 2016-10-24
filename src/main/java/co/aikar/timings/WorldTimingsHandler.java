/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
package co.aikar.timings;

import net.canarymod.config.Configuration;
import net.minecraft.world.World;

/**
 * Set of timers per world, to track world specific Timings.
 */
public class WorldTimingsHandler {

    public final Timing mobSpawn;
    public final Timing doChunkUnload;
    public final Timing doPortalForcer;
    public final Timing scheduledBlocks;
    public final Timing scheduledBlocksCleanup;
    public final Timing scheduledBlocksTicking;
    public final Timing chunkTicks;
    public final Timing chunkTicksBlocks;
    public final Timing doVillages;
    public final Timing doChunkMap;
    public final Timing doChunkGC;
    public final Timing doSounds;
    public final Timing entityRemoval;
    public final Timing entityTick;
    public final Timing tileEntityTick;
    public final Timing tileEntityPending;
    public final Timing tracker1;
    public final Timing tracker2;
    public final Timing doTick;
    public final Timing tickEntities;

    public final Timing syncChunkLoadTimer;
    public final Timing syncChunkLoadDataTimer;
    public final Timing syncChunkLoadStructuresTimer;
    public final Timing syncChunkLoadEntitiesTimer;
    public final Timing syncChunkLoadTileEntitiesTimer;
    public final Timing syncChunkLoadTileTicksTimer;
    public final Timing syncChunkLoadPostTimer;
    public final Timing worldSave;
    public final Timing worldSaveChunks;
    public final Timing worldSaveLevel;
    public final Timing chunkSaveNop;
    public final Timing chunkSaveData;

    public WorldTimingsHandler(World world) {
        final String name = Configuration.getServerConfig().getDefaultWorldName() + " - ";

        this.mobSpawn = NeptuneTimingsFactory.ofSafe(name + "mobSpawn");
        this.doChunkUnload = NeptuneTimingsFactory.ofSafe(name + "doChunkUnload");
        this.scheduledBlocks = NeptuneTimingsFactory.ofSafe(name + "Scheduled Blocks");
        this.scheduledBlocksCleanup = NeptuneTimingsFactory.ofSafe(name + "Scheduled Blocks - Cleanup");
        this.scheduledBlocksTicking = NeptuneTimingsFactory.ofSafe(name + "Scheduled Blocks - Ticking");
        this.chunkTicks = NeptuneTimingsFactory.ofSafe(name + "Chunk Ticks");
        this.chunkTicksBlocks = NeptuneTimingsFactory.ofSafe(name + "Chunk Ticks - Blocks");
        this.doVillages = NeptuneTimingsFactory.ofSafe(name + "doVillages");
        this.doChunkMap = NeptuneTimingsFactory.ofSafe(name + "doChunkMap");
        this.doSounds = NeptuneTimingsFactory.ofSafe(name + "doSounds");
        this.doChunkGC = NeptuneTimingsFactory.ofSafe(name + "doChunkGC"); // TODO
        this.doPortalForcer = NeptuneTimingsFactory.ofSafe(name + "doPortalForcer");
        this.entityTick = NeptuneTimingsFactory.ofSafe(name + "entityTick");
        this.entityRemoval = NeptuneTimingsFactory.ofSafe(name + "entityRemoval");
        this.tileEntityTick = NeptuneTimingsFactory.ofSafe(name + "tileEntityTick");
        this.tileEntityPending = NeptuneTimingsFactory.ofSafe(name + "tileEntityPending");

        this.syncChunkLoadTimer = NeptuneTimingsFactory.ofSafe(name + "syncChunkLoad"); // TODO
        this.syncChunkLoadDataTimer = NeptuneTimingsFactory.ofSafe(name + "syncChunkLoad - Data"); // TODO
        this.syncChunkLoadStructuresTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - Structures"); // TODO
        this.syncChunkLoadEntitiesTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - Entities"); // TODO
        this.syncChunkLoadTileEntitiesTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - TileEntities"); // TODO
        this.syncChunkLoadTileTicksTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - TileTicks"); // TODO
        this.syncChunkLoadPostTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - Post"); // TODO
        this.worldSave = NeptuneTimingsFactory.ofSafe(name + "World Save");
        this.worldSaveLevel = NeptuneTimingsFactory.ofSafe(name + "World Save - Level");
        this.worldSaveChunks = NeptuneTimingsFactory.ofSafe(name + "World Save - Chunks");
        this.chunkSaveNop = NeptuneTimingsFactory.ofSafe(name + "Chunk Save - NOP");
        this.chunkSaveData = NeptuneTimingsFactory.ofSafe(name + "Chunk Save - Data");

        this.tracker1 = NeptuneTimingsFactory.ofSafe(name + "tracker stage 1");
        this.tracker2 = NeptuneTimingsFactory.ofSafe(name + "tracker stage 2");
        this.doTick = NeptuneTimingsFactory.ofSafe(name + "doTick");
        this.tickEntities = NeptuneTimingsFactory.ofSafe(name + "tickEntities");
    }

}
