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
package org.neptunepowered.vanilla.mixin.core.world;

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
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
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
import org.neptunepowered.vanilla.bridge.core.world.BridgeWorldProvider;
import org.neptunepowered.vanilla.bridge.core.world.BridgeWorldServer;
import org.neptunepowered.vanilla.bridge.core.world.storage.BridgeWorldInfo;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;
import org.neptunepowered.vanilla.util.converter.PositionConverter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldServer.class)
@Implements(@Interface(iface = World.class, prefix = "world$"))
public abstract class MixinWorldServer extends MixinWorld implements World, TaskOwner, BridgeWorldServer {

    private WorldConfiguration worldConfig;

    private static final IBlockState JUNGLE_LOG = Blocks.log.getDefaultState()
            .withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    private static final IBlockState JUNGLE_LEAF = Blocks.leaves.getDefaultState()
            .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false);
    private static final IBlockState OAK_LEAF = Blocks.leaves.getDefaultState()
            .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);

    @Shadow @Final private net.minecraft.server.management.PlayerManager thePlayerManager;
    @Shadow @Final private net.minecraft.entity.EntityTracker theEntityTracker;
    @Shadow public ChunkProviderServer theChunkProviderServer;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstruction(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn,
                                CallbackInfo ci) {
        // Get the world configuration
        this.worldConfig = Configuration.getWorldConfig(this.getFqName());
    }

    @Override
    public WorldConfiguration bridge$getWorldConfig() {
        return this.worldConfig;
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
        return ((BridgeWorldProvider) this.provider).bridge$getDimensionType();
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
        return ((BridgeWorldInfo) this.worldInfo).bridge$getSpawn();
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

}
