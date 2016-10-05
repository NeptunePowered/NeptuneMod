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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import org.neptunepowered.vanilla.interfaces.minecraft.block.IMixinBlock;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Mixin(WorldServer.class)
@Implements(@Interface(iface = World.class, prefix = "world$"))
public abstract class MixinWorldServer extends MixinWorld implements World {

    @Shadow @Final private Set<NextTickListEntry> pendingTickListEntriesHashSet;
    @Shadow @Final private TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet;
    @Shadow @Final private SpawnerAnimals mobSpawner;
    @Shadow @Final private net.minecraft.server.management.PlayerManager thePlayerManager;
    @Shadow @Final protected VillageSiege villageSiege;
    @Shadow @Final private Teleporter worldTeleporter;
    @Shadow @Final private net.minecraft.entity.EntityTracker theEntityTracker;
    @Shadow private List<NextTickListEntry> pendingTickListEntriesThisTick;
    @Shadow public ChunkProviderServer theChunkProviderServer;

    @Shadow public abstract void scheduleUpdate(BlockPos pos, net.minecraft.block.Block blockIn, int delay);
    @Shadow public abstract boolean areAllPlayersAsleep();
    @Shadow protected abstract void wakeAllPlayers();
    @Shadow protected abstract void updateBlocks();

    @Shadow
    private void sendQueuedBlockEvents() {
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
        return null;
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
        return null;
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
        return null;
    }

    @Override
    public void setSpawnLocation(Location location) {

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
        return null;
    }

    @Override
    public boolean isChunkLoaded(Block block) {
        return false;
    }

    @Override
    public boolean isChunkLoaded(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean isChunkLoaded(int i, int i1) {
        return false;
    }

    @Override
    public Chunk loadChunk(int i, int i1) {
        return null;
    }

    @Override
    public Chunk loadChunk(Location location) {
        return null;
    }

    @Override
    public Chunk loadChunk(Position position) {
        return null;
    }

    @Override
    public Chunk getChunk(int i, int i1) {
        return null;
    }

    @Override
    public List<Chunk> getLoadedChunks() {
        return null;
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
        return null;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {

    }

    @Override
    public WorldType getWorldType() {
        return null;
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
        return null;
    }

    @Override
    public String getFqName() {
        return null;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return null;
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
        return false;
    }

    @Override
    public void showTitle(ChatComponent chatComponent) {

    }

    @Override
    public void showTitle(ChatComponent chatComponent, ChatComponent chatComponent1) {

    }

}
