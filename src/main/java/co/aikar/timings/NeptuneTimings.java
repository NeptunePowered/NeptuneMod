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

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.plugin.Plugin;
import net.minecraft.block.Block;
import net.minecraft.world.gen.structure.MapGenStructure;

public final class NeptuneTimings {

    public static final Timing playerListTimer = NeptuneTimingsFactory.ofSafe("Player List");
    public static final Timing connectionTimer = NeptuneTimingsFactory.ofSafe("Connection Handler");
    public static final Timing tickablesTimer = NeptuneTimingsFactory.ofSafe("Tickables");
    public static final Timing minecraftSchedulerTimer = NeptuneTimingsFactory.ofSafe("Minecraft Scheduler");
    public static final Timing canaryTaskManagerTimer = NeptuneTimingsFactory.ofSafe("Canary TaskManager");
    public static final Timing chunkIOTickTimer = NeptuneTimingsFactory.ofSafe("ChunkIOTick"); // TODO
    public static final Timing timeUpdateTimer = NeptuneTimingsFactory.ofSafe("Time Update");
    public static final Timing serverCommandTimer = NeptuneTimingsFactory.ofSafe("Server Command");
    public static final Timing savePlayersTimer = NeptuneTimingsFactory.ofSafe("Save Players");

    public static final Timing tickEntityTimer = NeptuneTimingsFactory.ofSafe("## tickEntity");
    public static final Timing tickTileEntityTimer = NeptuneTimingsFactory.ofSafe("## tickTileEntity");

    public static final Timing processQueueTimer = NeptuneTimingsFactory.ofSafe("processQueue"); // TODO

    public static final Timing playerCommandTimer = NeptuneTimingsFactory.ofSafe("playerCommand");

    private NeptuneTimings() {
    }

    public static Timing getPluginTimings(Plugin plugin, String context) {
        return NeptuneTimingsFactory.ofSafe(plugin.getName(), context, TimingsManager.PLUGIN_HOOK_HANDLER);
    }

    public static Timing getEntityTiming(Entity entity) {
        return NeptuneTimingsFactory.ofSafe("Minecraft", "## tickEntity - " + entity.getFqName(), tickEntityTimer);
    }

    public static Timing getTileEntityTiming(TileEntity entity) {
        return NeptuneTimingsFactory.ofSafe("Minecraft", "## tickTileEntity - " + entity.getClass().getName(), tickTileEntityTimer);
    }

    public static Timing getBlockTiming(Block block) {
        return NeptuneTimingsFactory.ofSafe("## Scheduled Block: " + block.getUnlocalizedName());
    }

    public static Timing getStructureTiming(MapGenStructure structureGenerator) {
        return NeptuneTimingsFactory.ofSafe("Structure Generator - " + structureGenerator.getStructureName());
    }

    public static void stopServer() {
        TimingsManager.stopServer();
    }

}
