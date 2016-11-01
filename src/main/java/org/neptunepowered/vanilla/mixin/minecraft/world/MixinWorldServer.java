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
package org.neptunepowered.vanilla.mixin.minecraft.world;

import co.aikar.timings.Timing;
import net.canarymod.Canary;
import net.canarymod.api.EntityTracker;
import net.canarymod.api.GameMode;
import net.canarymod.api.PlayerManager;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityItem;
import net.canarymod.api.entity.living.EntityLiving;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.entity.vehicle.Boat;
import net.canarymod.api.entity.vehicle.Minecart;
import net.canarymod.api.entity.vehicle.Vehicle;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.Biome;
import net.canarymod.api.world.BiomeType;
import net.canarymod.api.world.Chunk;
import net.canarymod.api.world.ChunkProvider;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.TreeType;
import net.canarymod.api.world.Village;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldType;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.api.world.effects.AuxiliarySoundEffect;
import net.canarymod.api.world.effects.Particle;
import net.canarymod.api.world.effects.SoundEffect;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Position;
import net.canarymod.config.Configuration;
import net.canarymod.config.WorldConfiguration;
import net.canarymod.tasks.TaskOwner;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ReportedException;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.neptunepowered.vanilla.chunk.ChunkGCTask;
import org.neptunepowered.vanilla.interfaces.minecraft.block.IMixinBlock;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorldProvider;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorldServer;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;
import org.neptunepowered.vanilla.util.converter.PositionConverter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Mixin(WorldServer.class)
@Implements(@Interface(iface = World.class, prefix = "world$"))
public abstract class MixinWorldServer extends MixinWorld implements World, IMixinWorldServer, TaskOwner {

    private static final IBlockState JUNGLE_LOG = Blocks.log.getDefaultState()
            .withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    private static final IBlockState JUNGLE_LEAF = Blocks.leaves.getDefaultState()
            .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false);
    private static final IBlockState OAK_LEAF = Blocks.leaves.getDefaultState()
            .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);

    @Shadow @Final private Set<NextTickListEntry> pendingTickListEntriesHashSet;
    @Shadow @Final private TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet;
    @Shadow @Final private SpawnerAnimals mobSpawner;
    @Shadow @Final private net.minecraft.server.management.PlayerManager thePlayerManager;
    @Shadow @Final protected VillageSiege villageSiege;
    @Shadow @Final private Teleporter worldTeleporter;
    @Shadow @Final private net.minecraft.entity.EntityTracker theEntityTracker;
    @Shadow private List<NextTickListEntry> pendingTickListEntriesThisTick;
    @Shadow public ChunkProviderServer theChunkProviderServer;

    private WorldConfiguration worldConfig;

    @Shadow public abstract void scheduleUpdate(BlockPos pos, net.minecraft.block.Block blockIn, int delay);
    @Shadow public abstract boolean areAllPlayersAsleep();
    @Shadow protected abstract void wakeAllPlayers();
    @Shadow protected abstract BlockPos adjustPosToNearbyEntity(BlockPos pos);
    @Shadow public abstract boolean addWeatherEffect(net.minecraft.entity.Entity entityIn);
    @Shadow protected abstract void saveLevel() throws MinecraftException;
    @Shadow private void sendQueuedBlockEvents() {}

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onConstruction(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn,
            CallbackInfo ci) {
        // Get the world configuration
        this.worldConfig = Configuration.getWorldConfig(this.getFqName());

        // Register ths ChunkGC task
        Canary.getServer().addSynchronousTask(new ChunkGCTask((WorldServer) (Object) this));
    }

    /**
     * @author jamierocks - 2nd October 2016
     * @reason Add timings calls
     */
    @Overwrite
    public void tick() {
        super.tick();

        if (this.getWorldInfo().isHardcoreModeEnabled() && this.shadow$getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }

        this.provider.getWorldChunkManager().cleanupCache();

        if (this.areAllPlayersAsleep()) {
            if (this.getGameRules().getBoolean("doDaylightCycle")) {
                long i = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(i - i % 24000L);
            }

            this.wakeAllPlayers();
        }

        this.theProfiler.startSection("mobSpawner");

        if (this.getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != net.minecraft.world.WorldType.DEBUG_WORLD) {
            this.timings.mobSpawn.startTiming(); // Neptune - timings
            this.mobSpawner.findChunksForSpawning(
                    (WorldServer) (Object) this,
                    this.spawnHostileMobs,
                    this.spawnPeacefulMobs,
                    this.worldInfo.getWorldTotalTime() % 400L == 0L);
            this.timings.mobSpawn.stopTiming(); // Neptune - timings
        }

        this.timings.doChunkUnload.startTiming(); // Neptune - timings
        this.theProfiler.endStartSection("chunkSource");
        this.chunkProvider.unloadQueuedChunks();
        int j = this.calculateSkylightSubtracted(1.0F);

        if (j != this.getSkylightSubtracted()) {
            this.setSkylightSubtracted(j);
        }

        this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);

        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }
        this.timings.doChunkUnload.stopTiming(); // Neptune - timings

        this.theProfiler.endStartSection("tickPending");
        this.timings.scheduledBlocks.startTiming(); // Neptune - timings
        this.tickUpdates(false);
        this.timings.scheduledBlocks.stopTiming(); // Neptune - timings
        this.theProfiler.endStartSection("tickBlocks");
        this.timings.chunkTicks.startTiming(); // Neptune - timings
        this.updateBlocks();
        this.timings.chunkTicks.stopTiming(); // Neptune - timings
        this.theProfiler.endStartSection("chunkMap");
        this.timings.doChunkMap.startTiming(); // Neptune - timings
        this.thePlayerManager.updatePlayerInstances();
        this.timings.doChunkMap.stopTiming(); // Neptune - timings
        this.theProfiler.endStartSection("village");
        this.timings.doVillages.startTiming(); // Neptune - timings
        this.villageCollectionObj.tick();
        this.villageSiege.tick();
        this.timings.doVillages.stopTiming(); // Neptune - timings
        this.theProfiler.endStartSection("portalForcer");
        this.timings.doPortalForcer.startTiming(); // Neptune - timings
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        this.timings.doPortalForcer.stopTiming(); // Neptune - timings
        this.theProfiler.endSection();
        this.timings.doSounds.startTiming(); // Neptune - timings
        this.sendQueuedBlockEvents();
        this.timings.doSounds.stopTiming(); // Neptune - timings
    }

    /**
     * @author jamierocks - 2nd October 2016
     * @reason Add timings calls
     */
    @Overwrite
    public boolean tickUpdates(boolean p_72955_1_) {
        if (this.worldInfo.getTerrainType() == net.minecraft.world.WorldType.DEBUG_WORLD) {
            return false;
        } else {
            int i = this.pendingTickListEntriesTreeSet.size();

            if (i != this.pendingTickListEntriesHashSet.size()) {
                throw new IllegalStateException("TickNextTick list out of synch");
            } else {
                if (i > 1000) {
                    i = 1000;
                }

                this.theProfiler.startSection("cleaning");
                this.timings.scheduledBlocksCleanup.startTiming(); // Neptune - timings

                for (int j = 0; j < i; ++j) {
                    NextTickListEntry nextticklistentry = this.pendingTickListEntriesTreeSet.first();

                    if (!p_72955_1_ && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                        break;
                    }

                    this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
                    this.pendingTickListEntriesHashSet.remove(nextticklistentry);
                    this.pendingTickListEntriesThisTick.add(nextticklistentry);
                }
                this.timings.scheduledBlocksCleanup.stopTiming(); // Neptune - timings

                this.theProfiler.endSection();
                this.theProfiler.startSection("ticking");
                this.timings.scheduledBlocksTicking.startTiming(); // Neptune - timings
                Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();

                while (iterator.hasNext()) {
                    NextTickListEntry nextticklistentry1 = iterator.next();
                    iterator.remove();
                    int k = 0;

                    if (this.isAreaLoaded(nextticklistentry1.position.add(-k, -k, -k), nextticklistentry1.position.add(k, k, k))) {
                        IBlockState iblockstate = this.getBlockState(nextticklistentry1.position);
                        final Timing timing = ((IMixinBlock) iblockstate.getBlock()).getTimingsHandler(); // Neptune - timings
                        timing.startTiming(); // Neptune - timings

                        if (iblockstate.getBlock().getMaterial() != Material.air && net.minecraft.block.Block
                                .isEqualTo(iblockstate.getBlock(), nextticklistentry1.getBlock())) {
                            try {
                                iblockstate.getBlock().updateTick((WorldServer) (Object) this, nextticklistentry1.position, iblockstate, this.rand);
                            } catch (Throwable throwable) {
                                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                                CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position, iblockstate);
                                throw new ReportedException(crashreport);
                            }
                        }

                        timing.stopTiming(); // Neptune - timings
                    } else {
                        this.scheduleUpdate(nextticklistentry1.position, nextticklistentry1.getBlock(), 0);
                    }
                }
                this.timings.scheduledBlocksTicking.stopTiming(); // Neptune - timings

                this.theProfiler.endSection();
                this.pendingTickListEntriesThisTick.clear();
                return !this.pendingTickListEntriesTreeSet.isEmpty();
            }
        }
    }

    /**
     * @author jamierocks - 7th October 2016
     * @reason Add timings calls
     */
    @Overwrite
    protected void updateBlocks() {
        super.updateBlocks();

        if (this.worldInfo.getTerrainType() == net.minecraft.world.WorldType.DEBUG_WORLD) {
            for (ChunkCoordIntPair chunkcoordintpair1 : this.activeChunkSet) {
                this.getChunkFromChunkCoords(chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos).func_150804_b(false);
            }
        } else {
            int i = 0;
            int j = 0;

            for (ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet) {
                int k = chunkcoordintpair.chunkXPos * 16;
                int l = chunkcoordintpair.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                net.minecraft.world.chunk.Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
                this.playMoodSoundAndCheckLight(k, l, chunk);
                this.theProfiler.endStartSection("tickChunk");
                chunk.func_150804_b(false);
                this.theProfiler.endStartSection("thunder");

                if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    int i1 = this.updateLCG >> 2;
                    BlockPos blockpos = this.adjustPosToNearbyEntity(new BlockPos(k + (i1 & 15), 0, l + (i1 >> 8 & 15)));

                    if (this.isRainingAt(blockpos)) {
                        this.addWeatherEffect(new EntityLightningBolt(
                                (net.minecraft.world.World) (Object) this,
                                (double) blockpos.getX(),
                                (double) blockpos.getY(),
                                (double) blockpos.getZ()
                        ));
                    }
                }

                this.theProfiler.endStartSection("iceandsnow");

                if (this.rand.nextInt(16) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    int k2 = this.updateLCG >> 2;
                    BlockPos blockpos2 = this.getPrecipitationHeight(new BlockPos(k + (k2 & 15), 0, l + (k2 >> 8 & 15)));
                    BlockPos blockpos1 = blockpos2.down();

                    if (this.canBlockFreezeNoWater(blockpos1)) {
                        this.setBlockState(blockpos1, Blocks.ice.getDefaultState());
                    }

                    if (this.isRaining() && this.canSnowAt(blockpos2, true)) {
                        this.setBlockState(blockpos2, Blocks.snow_layer.getDefaultState());
                    }

                    if (this.isRaining() && this.getBiomeGenForCoords(blockpos1).canRain()) {
                        this.getBlockState(blockpos1).getBlock().fillWithRain((net.minecraft.world.World) (Object) this, blockpos1);
                    }
                }

                this.theProfiler.endStartSection("tickBlocks");
                int l2 = this.getGameRules().getInt("randomTickSpeed");

                this.timings.chunkTicksBlocks.startTiming(); // Neptune - timings
                if (l2 > 0) {
                    for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
                        if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick()) {
                            for (int j1 = 0; j1 < l2; ++j1) {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                int k1 = this.updateLCG >> 2;
                                int l1 = k1 & 15;
                                int i2 = k1 >> 8 & 15;
                                int j2 = k1 >> 16 & 15;
                                ++j;
                                IBlockState iblockstate = extendedblockstorage.get(l1, j2, i2);
                                net.minecraft.block.Block block = iblockstate.getBlock();

                                if (block.getTickRandomly()) {
                                    ++i;
                                    block.randomTick(
                                            (net.minecraft.world.World) (Object) this,
                                            new BlockPos(l1 + k, j2 + extendedblockstorage.getYLocation(), i2 + l),
                                            iblockstate,
                                            this.rand
                                    );
                                }
                            }
                        }
                    }
                }
                this.timings.chunkTicksBlocks.stopTiming(); // Neptune - timings

                this.theProfiler.endSection();
            }
        }
    }

    /**
     * @author jamierocks - 25th October 2016
     * @reason Add timings calls
     */
    @Overwrite
    public void saveAllChunks(boolean p_73044_1_, IProgressUpdate progressCallback) throws MinecraftException {
        if (this.chunkProvider.canSave()) {
            this.getTimings().worldSave.startTiming(); // Neptune - timings

            if (progressCallback != null) {
                progressCallback.displaySavingString("Saving level");
            }

            this.saveLevel();

            if (progressCallback != null) {
                progressCallback.displayLoadingString("Saving chunks");
            }

            this.getTimings().worldSaveChunks.startTiming(); // Neptune - timings
            this.chunkProvider.saveChunks(p_73044_1_, progressCallback);
            this.getTimings().worldSaveChunks.startTiming(); // Neptune - timings

            // Neptune - Disable vanilla ChunkGC
            // for (net.minecraft.world.chunk.Chunk chunk : Lists.newArrayList(this.theChunkProviderServer.func_152380_a())) {
            //     if (chunk != null && !this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
            //         this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
            //     }
            // }
            // Neptune - end

            this.getTimings().worldSave.stopTiming(); // Neptune - timings
        }
    }

    @Inject(method = "saveLevel", at = @At("HEAD"))
    public void onSaveLevel(CallbackInfo ci) {
        this.getTimings().worldSaveLevel.startTiming();
    }

    @Inject(method = "saveLevel", at = @At("RETURN"))
    public void afterSaveLevel(CallbackInfo ci) {
        this.getTimings().worldSaveLevel.stopTiming();
    }

    @Override
    public void setNanoTick(int i, long l) {

    }

    @Override
    public long getNanoTick(int i) {
        return 0;
    }

    @Override
    public EntityTracker getEntityTracker() {
        return (EntityTracker) this.theEntityTracker;
    }

    @Override
    public DimensionType getType() {
        return ((IMixinWorldProvider) this.provider).getDimensionType();
    }

    @Override
    public EntityItem dropItem(int i, int i1, int i2, int i3, int i4, int i5) {
        return null;
    }

    @Override
    public EntityItem dropItem(int i, int i1, int i2, Item item) {
        return null;
    }

    @Override
    public EntityItem dropItem(Position position, Item item) {
        return null;
    }

    @Override
    public List<EntityAnimal> getAnimalList() {
        return null;
    }

    @Override
    public List<EntityMob> getMobList() {
        return null;
    }

    @Override
    public List<EntityLiving> getEntityLivingList() {
        return null;
    }

    @Override
    public List<Entity> getTrackedEntities() {
        return null;
    }

    @Override
    public List<Player> getPlayerList() {
        return (List) this.playerEntities;
    }

    @Override
    public List<Boat> getBoatList() {
        return null;
    }

    @Override
    public List<Minecart> getMinecartList() {
        return null;
    }

    @Override
    public List<Vehicle> getVehicleList() {
        return null;
    }

    @Override
    public List<EntityItem> getItemList() {
        return null;
    }

    @Override
    public Block getBlockAt(int i, int i1, int i2) {
        return null;
    }

    @Override
    public Block getBlockAt(Position position) {
        return null;
    }

    @Override
    public short getDataAt(int i, int i1, int i2) {
        return 0;
    }

    @Override
    public short getDataAt(Position position) {
        return 0;
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.getWorldInfo().getSpawnX(), this.getWorldInfo().getSpawnY(), this.getWorldInfo().getSpawnZ());
    }

    @Override
    public void setSpawnLocation(Location location) {
        this.getWorldInfo().setSpawn(PositionConverter.of(location));
    }

    @Override
    public int getLightLevelAt(int i, int i1, int i2) {
        return 0;
    }

    @Override
    public void setLightLevelOnBlockMap(int i, int i1, int i2, int i3) {

    }

    @Override
    public void setLightLevelOnSkyMap(int i, int i1, int i2, int i3) {

    }

    @Override
    public void setBlock(Block block) {

    }

    @Override
    public void setBlockAt(int i, int i1, int i2, short i3) {

    }

    @Override
    public void setBlockAt(Position position, Block block) {

    }

    @Override
    public void setBlockAt(Position position, short i) {

    }

    @Override
    public void setBlockAt(Position position, short i, short i1) {

    }

    @Override
    public void setBlockAt(Position position, BlockType blockType) {

    }

    @Override
    public void setBlockAt(int i, int i1, int i2, short i3, short i4) {

    }

    @Override
    public void setBlockAt(int i, int i1, int i2, BlockType blockType) {

    }

    @Override
    public void setDataAt(int i, int i1, int i2, short i3) {

    }

    @Override
    public void markBlockNeedsUpdate(int i, int i1, int i2) {

    }

    @Override
    public Player getClosestPlayer(double v, double v1, double v2, double v3) {
        return null;
    }

    @Override
    public Player getClosestPlayer(Entity entity, int i) {
        return null;
    }

    @Override
    public ChunkProvider getChunkProvider() {
        return (ChunkProvider) this.chunkProvider;
    }

    @Override
    public boolean isChunkLoaded(Block block) {
        return this.getChunkProvider().isChunkLoaded(block.getX(), block.getZ());
    }

    @Override
    public boolean isChunkLoaded(int i, int i1, int i2) {
        return this.getChunkProvider().isChunkLoaded(i, i2);
    }

    @Override
    public boolean isChunkLoaded(int i, int i1) {
        return this.getChunkProvider().isChunkLoaded(i, i1);
    }

    @Override
    public Chunk loadChunk(int i, int i1) {
        return this.getChunkProvider().loadChunk(i, i1);
    }

    @Override
    public Chunk loadChunk(Location location) {
        return this.getChunkProvider().loadChunk((int) location.getX(), (int) location.getZ());
    }

    @Override
    public Chunk loadChunk(Position position) {
        return this.getChunkProvider().loadChunk((int) position.getX(), (int) position.getZ());
    }

    @Override
    public Chunk getChunk(int i, int i1) {
        return this.getChunkProvider().provideChunk(i, i1);
    }

    @Override
    public List<Chunk> getLoadedChunks() {
        return this.getChunkProvider().getLoadedChunks();
    }

    @Override
    public BiomeType getBiomeType(int i, int i1) {
        return null;
    }

    @Override
    public Biome getBiome(int i, int i1) {
        return null;
    }

    @Override
    public void setBiome(int i, int i1, BiomeType biomeType) {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getHighestBlockAt(int i, int i1) {
        return 0;
    }

    @Override
    public void playNoteAt(int i, int i1, int i2, int i3, byte b) {

    }

    @Override
    public void setTime(long l) {

    }

    @Override
    public long getRelativeTime() {
        return 0;
    }

    @Override
    public long getRawTime() {
        return 0;
    }

    @Override
    public long getTotalTime() {
        return 0;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.fromId(this.shadow$getDifficulty().getDifficultyId());
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(difficulty.getId()));
    }

    @Override
    public WorldType getWorldType() {
        return WorldType.fromString(this.shadow$getWorldType().getWorldTypeName());
    }

    @Override
    public void spawnParticle(Particle particle) {

    }

    @Override
    public void playSound(SoundEffect soundEffect) {

    }

    @Override
    public void playAUXEffect(AuxiliarySoundEffect auxiliarySoundEffect) {

    }

    @Override
    public void playAUXEffectAt(Player player, AuxiliarySoundEffect auxiliarySoundEffect) {

    }

    @Override
    public String getName() {
        return this.worldInfo.getWorldName();
    }

    @Override
    public String getFqName() {
        return this.getName() + "_" + this.getType().toString();
    }

    @Override
    public PlayerManager getPlayerManager() {
        return (PlayerManager) this.thePlayerManager;
    }

    @Override
    public int getBlockPower(Block block) {
        return 0;
    }

    @Override
    public int getBlockPower(Position position) {
        return 0;
    }

    @Override
    public int getBlockPower(int i, int i1, int i2) {
        return 0;
    }

    @Override
    public boolean isBlockPowered(Block block) {
        return false;
    }

    @Override
    public boolean isBlockPowered(Position position) {
        return false;
    }

    @Override
    public boolean isBlockPowered(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered(Block block) {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered(Position position) {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered(int i, int i1, int i2) {
        return false;
    }

    @Override
    public float getThunderStrength() {
        return 0;
    }

    @Override
    public void setThunderStrength(float v) {

    }

    @Override
    public void setThunderTime(int i) {
        this.worldInfo.setThunderTime(i);
    }

    @Override
    public float getRainStrength() {
        return 0;
    }

    @Override
    public void setRainStrength(float v) {

    }

    @Override
    public void setRainTime(int i) {
        this.worldInfo.setRainTime(i);
    }

    @Override
    public boolean isRaining() {
        return this.worldInfo.isRaining();
    }

    @Override
    public void setRaining(boolean b) {
        this.worldInfo.setRaining(b);
    }

    @Override
    public boolean isThundering() {
        return this.worldInfo.isThundering();
    }

    @Override
    public void setThundering(boolean b) {
        this.worldInfo.setThundering(b);
    }

    @Override
    public void makeLightningBolt(int i, int i1, int i2) {

    }

    @Override
    public void makeLightningBolt(Position position) {

    }

    @Override
    public void makeExplosion(Entity entity, double v, double v1, double v2, float v3, boolean b) {

    }

    @Override
    public void makeExplosion(Entity entity, Position position, float v, boolean b) {

    }

    @Override
    public int getRainTicks() {
        return 0;
    }

    @Override
    public int getThunderTicks() {
        return 0;
    }

    @Override
    public long getWorldSeed() {
        return this.getSeed();
    }

    @Override
    public void removePlayerFromWorld(Player player) {

    }

    @Override
    public void addPlayerToWorld(Player player) {

    }

    @Override
    public TileEntity getTileEntity(Block block) {
        return null;
    }

    @Override
    public TileEntity getTileEntityAt(int i, int i1, int i2) {
        return null;
    }

    @Override
    public TileEntity getOnlyTileEntity(Block block) {
        return null;
    }

    @Override
    public TileEntity getOnlyTileEntityAt(int i, int i1, int i2) {
        return null;
    }

    @Override
    public GameMode getGameMode() {
        return GameModeConverter.of(this.worldInfo.getGameType());
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.worldInfo.setGameType(GameModeConverter.of(gameMode));
    }

    @Override
    public void save() {
        this.theChunkProviderServer.saveChunks(true, null);
    }

    @Override
    public void broadcastMessage(String s) {
        for (Player player : this.getPlayerList()) {
            player.message(s);
        }
    }

    @Override
    public List<Village> getVillages() {
        return null;
    }

    @Override
    public Village getNearestVillage(Position position, int i) {
        return null;
    }

    @Override
    public Village getNearestVillage(Location location, int i) {
        return null;
    }

    @Override
    public Village getNearestVillage(int i, int i1, int i2, int i3) {
        return null;
    }

    @Override
    public boolean generateTree(Position position, TreeType treeType) {
        final WorldGenerator treeGen;

        switch (treeType) {
            case BIGOAK:
                treeGen = new WorldGenBigTree(true);
                break;
            case SWAMPOAK:
                treeGen = new WorldGenSwamp();
                break;
            case DARKOAK:
                treeGen = new WorldGenCanopyTree(true);
                break;
            case BIRCH:
                treeGen = new WorldGenForest(true, false);
                break;
            case TALLBIRCH:
                treeGen = new WorldGenForest(true, true);
                break;
            case SPRUCE:
                treeGen = new WorldGenTaiga2(true);
                break;
            case TALLSPRUCE:
                treeGen = new WorldGenTaiga1();
                break;
            case MEGASPRUCE:
                treeGen = new WorldGenMegaPineTree(false, this.rand.nextBoolean());
                break;
            case JUNGLE:
                treeGen = new WorldGenTrees(true, 4 + this.rand.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, false);
                break;
            case TALLJUNGLE:
                treeGen = new WorldGenTrees(true, 4 + this.rand.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, true);
                break;
            case JUNGLEBUSH:
                treeGen = new WorldGenShrub(JUNGLE_LOG, OAK_LEAF);
                break;
            case MEGAJUNGLE:
                treeGen = new WorldGenMegaJungle(true, 10, 20, JUNGLE_LOG, JUNGLE_LEAF);
                break;
            case ACACIA:
                treeGen = new WorldGenSavannaTree(true);
                break;
            case REDMUSHROOM:
                treeGen = new WorldGenBigMushroom(Blocks.red_mushroom_block);
                break;
            case BROWNMUSHROOM:
                treeGen = new WorldGenBigMushroom(Blocks.brown_mushroom_block);
                break;
            default:
                treeGen = new WorldGenTrees(true);
                break;
        }

        return treeGen.generate((net.minecraft.world.World) (Object) this, this.rand, PositionConverter.of(position));
    }

    @Override
    public void showTitle(ChatComponent title) {
        this.showTitle(title, null);
    }

    @Override
    public void showTitle(ChatComponent title, ChatComponent subtitle) {
        for (Player player : this.getPlayerList()) {
            player.showTitle(title, subtitle);
        }
    }

    @Override
    public WorldConfiguration getWorldConfig() {
        return this.worldConfig;
    }
}
