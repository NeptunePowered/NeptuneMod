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
import net.minecraft.profiler.Profiler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(WorldServer.class)
@Implements(@Interface(iface = World.class, prefix = "world$"))
public abstract class MixinWorldServer extends net.minecraft.world.World implements IThreadListener {

    @Shadow private net.minecraft.entity.EntityTracker theEntityTracker;

    MixinWorldServer(ISaveHandler saveHandlerIn,
            WorldInfo info,
            WorldProvider providerIn, Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    public void world$setNanoTick(int i, long l) {

    }

    public long world$getNanoTick(int i) {
        return 0;
    }

    public EntityTracker world$getEntityTracker() {
        return (EntityTracker) this.theEntityTracker;
    }

    public DimensionType world$getType() {
        return null;
    }

    public EntityItem world$dropItem(int i, int i1, int i2, int i3, int i4, int i5) {
        return null;
    }

    public EntityItem world$dropItem(int i, int i1, int i2, Item item) {
        return null;
    }

    public EntityItem world$dropItem(Position position, Item item) {
        return null;
    }

    public List<EntityAnimal> world$getAnimalList() {
        return null;
    }

    public List<EntityMob> world$getMobList() {
        return null;
    }

    public List<EntityLiving> world$getEntityLivingList() {
        return null;
    }

    public List<Entity> world$getTrackedEntities() {
        return null;
    }

    public List<Player> world$getPlayerList() {
        return null;
    }

    public List<Boat> world$getBoatList() {
        return null;
    }

    public List<Minecart> world$getMinecartList() {
        return null;
    }

    public List<Vehicle> world$getVehicleList() {
        return null;
    }

    public List<EntityItem> world$getItemList() {
        return null;
    }

    public Block world$getBlockAt(int i, int i1, int i2) {
        return null;
    }

    public Block world$getBlockAt(Position position) {
        return null;
    }

    public short world$getDataAt(int i, int i1, int i2) {
        return 0;
    }

    public short world$getDataAt(Position position) {
        return 0;
    }

    public Location world$getSpawnLocation() {
        return null;
    }

    public void world$setSpawnLocation(Location location) {

    }

    public int world$getLightLevelAt(int i, int i1, int i2) {
        return 0;
    }

    public void world$setLightLevelOnBlockMap(int i, int i1, int i2, int i3) {

    }

    public void world$setLightLevelOnSkyMap(int i, int i1, int i2, int i3) {

    }

    public void world$setBlock(Block block) {

    }

    public void world$setBlockAt(int i, int i1, int i2, short i3) {

    }

    public void world$setBlockAt(Position position, Block block) {

    }

    public void world$setBlockAt(Position position, short i) {

    }

    public void world$setBlockAt(Position position, short i, short i1) {

    }

    public void world$setBlockAt(Position position, BlockType blockType) {

    }

    public void world$setBlockAt(int i, int i1, int i2, short i3, short i4) {

    }

    public void world$setBlockAt(int i, int i1, int i2, BlockType blockType) {

    }

    public void world$setDataAt(int i, int i1, int i2, short i3) {

    }

    public void world$markBlockNeedsUpdate(int i, int i1, int i2) {

    }

    public Player world$getClosestPlayer(double v, double v1, double v2, double v3) {
        return null;
    }

    public Player world$getClosestPlayer(Entity entity, int i) {
        return null;
    }

    public ChunkProvider world$getChunkProvider() {
        return null;
    }

    public boolean world$isChunkLoaded(Block block) {
        return false;
    }

    public boolean world$isChunkLoaded(int i, int i1, int i2) {
        return false;
    }

    public boolean world$isChunkLoaded(int i, int i1) {
        return false;
    }

    public Chunk world$loadChunk(int i, int i1) {
        return null;
    }

    public Chunk world$loadChunk(Location location) {
        return null;
    }

    public Chunk world$loadChunk(Position position) {
        return null;
    }

    public Chunk world$getChunk(int i, int i1) {
        return null;
    }

    public List<Chunk> world$getLoadedChunks() {
        return null;
    }

    public BiomeType world$getBiomeType(int i, int i1) {
        return null;
    }

    public Biome world$getBiome(int i, int i1) {
        return null;
    }

    public void world$setBiome(int i, int i1, BiomeType biomeType) {

    }

    public int world$getHeight() {
        return 0;
    }

    public int world$getHighestBlockAt(int i, int i1) {
        return 0;
    }

    public void world$playNoteAt(int i, int i1, int i2, int i3, byte b) {

    }

    public void world$setTime(long l) {

    }

    public long world$getRelativeTime() {
        return 0;
    }

    public long world$getRawTime() {
        return 0;
    }

    public long world$getTotalTime() {
        return 0;
    }

    public World.Difficulty world$getDifficulty() {
        return null;
    }

    public void world$setDifficulty(World.Difficulty difficulty) {

    }

    public WorldType world$getWorldType() {
        return null;
    }

    public void world$spawnParticle(Particle particle) {

    }

    public void world$playSound(SoundEffect soundEffect) {

    }

    public void world$playAUXEffect(AuxiliarySoundEffect auxiliarySoundEffect) {

    }

    public void world$playAUXEffectAt(Player player, AuxiliarySoundEffect auxiliarySoundEffect) {

    }

    public String world$getName() {
        return null;
    }

    public String world$getFqName() {
        return null;
    }

    public PlayerManager world$getPlayerManager() {
        return null;
    }

    public int world$getBlockPower(Block block) {
        return 0;
    }

    public int world$getBlockPower(Position position) {
        return 0;
    }

    public int world$getBlockPower(int i, int i1, int i2) {
        return 0;
    }

    public boolean world$isBlockPowered(Block block) {
        return false;
    }

    public boolean world$isBlockPowered(Position position) {
        return false;
    }

    public boolean world$isBlockPowered(int i, int i1, int i2) {
        return false;
    }

    public boolean world$isBlockIndirectlyPowered(Block block) {
        return false;
    }

    public boolean world$isBlockIndirectlyPowered(Position position) {
        return false;
    }

    public boolean world$isBlockIndirectlyPowered(int i, int i1, int i2) {
        return false;
    }

    public void world$setThundering(boolean b) {

    }

    public void world$setThunderStrength(float v) {

    }

    public float world$getThunderStrength() {
        return 0;
    }

    public void world$setThunderTime(int i) {

    }

    public void world$setRaining(boolean b) {

    }

    public void world$setRainStrength(float v) {

    }

    public float world$getRainStrength() {
        return 0;
    }

    public void world$setRainTime(int i) {

    }

    public boolean world$isRaining() {
        return false;
    }

    public boolean world$isThundering() {
        return false;
    }

    public void world$makeLightningBolt(int i, int i1, int i2) {

    }

    public void world$makeLightningBolt(Position position) {

    }

    public void world$makeExplosion(Entity entity, double v, double v1, double v2, float v3, boolean b) {

    }

    public void world$makeExplosion(Entity entity, Position position, float v, boolean b) {

    }

    public int world$getRainTicks() {
        return 0;
    }

    public int world$getThunderTicks() {
        return 0;
    }

    public long world$getWorldSeed() {
        return 0;
    }

    public void world$removePlayerFromWorld(Player player) {

    }

    public void world$addPlayerToWorld(Player player) {

    }

    public TileEntity world$getTileEntity(Block block) {
        return null;
    }

    public TileEntity world$getTileEntityAt(int i, int i1, int i2) {
        return null;
    }

    public TileEntity world$getOnlyTileEntity(Block block) {
        return null;
    }

    public TileEntity world$getOnlyTileEntityAt(int i, int i1, int i2) {
        return null;
    }

    public GameMode world$getGameMode() {
        return null;
    }

    public void world$setGameMode(GameMode gameMode) {

    }

    public void world$save() {

    }

    public void world$broadcastMessage(String s) {

    }

    public List<Village> world$getVillages() {
        return null;
    }

    public Village world$getNearestVillage(Position position, int i) {
        return null;
    }

    public Village world$getNearestVillage(Location location, int i) {
        return null;
    }

    public Village world$getNearestVillage(int i, int i1, int i2, int i3) {
        return null;
    }

    public boolean world$generateTree(Position position, TreeType treeType) {
        return false;
    }

    public void world$showTitle(ChatComponent chatComponent) {

    }

    public void world$showTitle(ChatComponent chatComponent, ChatComponent chatComponent1) {

    }
}
