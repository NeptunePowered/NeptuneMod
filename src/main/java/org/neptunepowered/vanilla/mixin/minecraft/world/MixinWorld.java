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
package org.neptunepowered.vanilla.mixin.minecraft.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Mixin(World.class)
public abstract class MixinWorld implements IMixinWorld {

    @Shadow @Final public List<Entity> weatherEffects;
    @Shadow @Final public List<Entity> loadedEntityList;
    @Shadow @Final protected List<Entity> unloadedEntityList;
    @Shadow @Final private List<TileEntity> tileEntitiesToBeRemoved;
    @Shadow @Final public List<TileEntity> loadedTileEntityList;
    @Shadow @Final public List<TileEntity> tickableTileEntities;
    @Shadow @Final private List<TileEntity> addedTileEntityList;
    @Shadow @Final public List<EntityPlayer> playerEntities;
    @Shadow @Final private WorldBorder worldBorder;
    @Shadow @Final public Profiler theProfiler;
    @Shadow @Final public Random rand;
    @Shadow @Final public WorldProvider provider;
    @Shadow protected WorldInfo worldInfo;
    @Shadow private boolean processingLoadedTiles;
    @Shadow protected boolean spawnHostileMobs;
    @Shadow protected boolean spawnPeacefulMobs;
    @Shadow protected IChunkProvider chunkProvider;
    @Shadow protected VillageCollection villageCollectionObj;
    @Shadow protected Set<ChunkCoordIntPair> activeChunkSet;
    @Shadow protected int updateLCG;

    @Shadow public abstract long getSeed();
    @Shadow public abstract void updateEntity(Entity ent);
    @Shadow protected abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty);
    @Shadow public abstract Chunk getChunkFromChunkCoords(int chunkX, int chunkZ);
    @Shadow protected abstract void onEntityRemoved(Entity entityIn);
    @Shadow public abstract boolean isBlockLoaded(BlockPos pos);
    @Shadow public abstract Chunk getChunkFromBlockCoords(BlockPos pos);
    @Shadow public abstract boolean addTileEntity(TileEntity tile);
    @Shadow public abstract void markBlockForUpdate(BlockPos pos);
    @Shadow public abstract boolean isAreaLoaded(BlockPos from, BlockPos to);
    @Shadow public abstract IBlockState getBlockState(BlockPos pos);
    @Shadow public abstract WorldInfo getWorldInfo();
    @Shadow public abstract EnumDifficulty shadow$getDifficulty();
    @Shadow public abstract GameRules getGameRules();
    @Shadow public abstract int calculateSkylightSubtracted(float p_72967_1_);
    @Shadow public abstract int getSkylightSubtracted();
    @Shadow public abstract void setSkylightSubtracted(int newSkylightSubtracted);
    @Shadow public abstract long getTotalWorldTime();
    @Shadow protected abstract void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn);
    @Shadow public abstract boolean isRainingAt(BlockPos strikePosition);
    @Shadow public abstract BlockPos getPrecipitationHeight(BlockPos pos);
    @Shadow public abstract boolean canBlockFreezeNoWater(BlockPos pos);
    @Shadow public abstract boolean setBlockState(BlockPos pos, IBlockState state);
    @Shadow public abstract boolean canSnowAt(BlockPos pos, boolean checkLight);
    @Shadow public abstract BiomeGenBase getBiomeGenForCoords(final BlockPos pos);
    @Shadow public abstract WorldType shadow$getWorldType();
    @Shadow public void tick() {}
    @Shadow protected void updateBlocks() {}

    @Override
    public void setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
    }

}
