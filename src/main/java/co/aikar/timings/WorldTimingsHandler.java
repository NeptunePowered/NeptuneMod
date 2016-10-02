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
    public final Timing updateBlocks;
    public final Timing updateBlocksCheckNextLight;
    public final Timing updateBlocksChunkTick;
    public final Timing updateBlocksIceAndSnow;
    public final Timing updateBlocksRandomTick;
    public final Timing updateBlocksThunder;
    public final Timing doVillages;
    public final Timing doChunkMap;
    public final Timing doChunkGC;
    public final Timing doSounds;
    public final Timing entityRemoval;
    public final Timing entityTick;
    public final Timing tileEntityTick;
    public final Timing tileEntityPending;
    public final Timing tileEntityRemoval;
    public final Timing tracker1;
    public final Timing tracker2;
    public final Timing doTick;
    public final Timing tickEntities;

    // Chunk Load
    public final Timing syncChunkLoadTimer;
    public final Timing syncChunkLoadDataTimer;
    public final Timing syncChunkLoadStructuresTimer;
    public final Timing syncChunkLoadEntitiesTimer;
    public final Timing syncChunkLoadTileEntitiesTimer;
    public final Timing syncChunkLoadTileTicksTimer;
    public final Timing syncChunkLoadPostTimer;

    // Chunk population
    public final Timing chunkPopulate;

    public WorldTimingsHandler(World world) {
        String name = Configuration.getServerConfig().getDefaultWorldName() + " - ";

        this.mobSpawn = NeptuneTimingsFactory.ofSafe(name + "mobSpawn");
        this.doChunkUnload = NeptuneTimingsFactory.ofSafe(name + "doChunkUnload");
        this.scheduledBlocks = NeptuneTimingsFactory.ofSafe(name + "Scheduled Blocks");
        this.scheduledBlocksCleanup = NeptuneTimingsFactory.ofSafe(name + "Scheduled Blocks - Cleanup");
        this.scheduledBlocksTicking = NeptuneTimingsFactory.ofSafe(name + "Scheduled Blocks - Ticking");
        this.chunkTicks = NeptuneTimingsFactory.ofSafe(name + "Chunk Ticks");
        this.chunkTicksBlocks = NeptuneTimingsFactory.ofSafe(name + "Chunk Ticks - Blocks");
        this.updateBlocks = NeptuneTimingsFactory.ofSafe(name + "Update Blocks");
        this.updateBlocksCheckNextLight = NeptuneTimingsFactory.ofSafe(name + "Update Blocks - CheckNextLight");
        this.updateBlocksChunkTick = NeptuneTimingsFactory.ofSafe(name + "Update Blocks - ChunkTick");
        this.updateBlocksIceAndSnow = NeptuneTimingsFactory.ofSafe(name + "Update Blocks - IceAndSnow");
        this.updateBlocksRandomTick = NeptuneTimingsFactory.ofSafe(name + "Update Blocks - RandomTick");
        this.updateBlocksThunder = NeptuneTimingsFactory.ofSafe(name + "Update Blocks - Thunder");
        this.doVillages = NeptuneTimingsFactory.ofSafe(name + "doVillages");
        this.doChunkMap = NeptuneTimingsFactory.ofSafe(name + "doChunkMap");
        this.doSounds = NeptuneTimingsFactory.ofSafe(name + "doSounds");
        this.doChunkGC = NeptuneTimingsFactory.ofSafe(name + "doChunkGC");
        this.doPortalForcer = NeptuneTimingsFactory.ofSafe(name + "doPortalForcer");
        this.entityTick = NeptuneTimingsFactory.ofSafe(name + "entityTick");
        this.entityRemoval = NeptuneTimingsFactory.ofSafe(name + "entityRemoval");
        this.tileEntityTick = NeptuneTimingsFactory.ofSafe(name + "tileEntityTick");
        this.tileEntityPending = NeptuneTimingsFactory.ofSafe(name + "tileEntityPending");
        this.tileEntityRemoval = NeptuneTimingsFactory.ofSafe(name + "tileEntityRemoval");

        this.syncChunkLoadTimer = NeptuneTimingsFactory.ofSafe(name + "syncChunkLoad");
        this.syncChunkLoadDataTimer = NeptuneTimingsFactory.ofSafe(name + "syncChunkLoad - Data");
        this.syncChunkLoadStructuresTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - Structures");
        this.syncChunkLoadEntitiesTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - Entities");
        this.syncChunkLoadTileEntitiesTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - TileEntities");
        this.syncChunkLoadTileTicksTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - TileTicks");
        this.syncChunkLoadPostTimer = NeptuneTimingsFactory.ofSafe(name + "chunkLoad - Post");

        this.tracker1 = NeptuneTimingsFactory.ofSafe(name + "tracker stage 1");
        this.tracker2 = NeptuneTimingsFactory.ofSafe(name + "tracker stage 2");
        this.doTick = NeptuneTimingsFactory.ofSafe(name + "doTick");
        this.tickEntities = NeptuneTimingsFactory.ofSafe(name + "tickEntities");

        this.chunkPopulate = NeptuneTimingsFactory.ofSafe(name + "chunkPopulate");
    }
}