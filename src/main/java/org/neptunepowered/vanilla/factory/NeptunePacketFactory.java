/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
package org.neptunepowered.vanilla.factory;

import net.canarymod.api.DataWatcher;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.XPOrb;
import net.canarymod.api.entity.hanging.Painting;
import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.entity.living.humanoid.Human;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.factory.PacketFactory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.packet.InvalidPacketConstructionException;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.potion.PotionEffect;
import net.canarymod.api.statistics.Stat;
import net.canarymod.api.world.Chunk;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Position;
import net.canarymod.api.world.position.Vector3D;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.util.BlockPos;
import org.neptunepowered.vanilla.wrapper.packet.NeptunePacket;

import java.util.List;
import java.util.Map;

public class NeptunePacketFactory implements PacketFactory {

    @Override
    public Packet createPacket(int id, Object... args) throws InvalidPacketConstructionException {
        switch (id) {
            case 0x00: // 0
                throw new InvalidPacketConstructionException(id, "KeepAlive", "Keep Alive packets should only be "
                        + "handled by the Server");
            case 0x01: // 1
                throw new InvalidPacketConstructionException(id, "JoinGame", "Join Game packets should only be "
                        + "handled by the Server");
            case 0x03: // 3
                return updateTime((Long) args[0], (Long) args[1]);
            case 0x06: // 6
                return updateHealth((Float) args[0], (Integer) args[1], (Float) args[2]);
            case 0x2E: // 46
                return closeWindow((Integer) args[0]);
            case 0x36: // 54
                return signEditorOpen((Integer) args[0], (Integer) args[1], (Integer) args[2]);
            case 0x3A:
                throw new InvalidPacketConstructionException(id, "TabComplete",
                        "No function unless requested and client waiting.");
            case 0x3B:
                throw new InvalidPacketConstructionException(id, "ScoreboardObjective",
                        "Use the Scoreboard API instead.");
            case 0x3C:
                throw new InvalidPacketConstructionException(id, "UpdateScore", "Use the Scoreboard API instead.");
            case 0x3D:
                throw new InvalidPacketConstructionException(id, "DisplayScoreboard",
                        "Use the Scoreboard API instead.");
            case 0x3E:
                throw new InvalidPacketConstructionException(id, "Teams", "Use the Scoreboard API instead.");
            case 0x3F:
                throw new InvalidPacketConstructionException(id, "CustomPayload", "Use ChannelManager instead.");
            case 0x40:
                throw new InvalidPacketConstructionException(id, "Disconnect", "Use kick methods instead.");
            default:
                throw new InvalidPacketConstructionException(id, "UNKNOWN", "Unknown Packet ID");
        }
    }

    @Override
    public Packet chat(ChatComponent chatComponent) {
        return null;
    }

    @Override
    public Packet updateTime(long worldAge, long time) {
        return new NeptunePacket(new S03PacketTimeUpdate(worldAge, time, false));
    }

    @Override
    public Packet entityEquipment(int entityID, int slot, Item item) {
        return null;
    }

    @Override
    public Packet spawnPosition(int x, int y, int z) {
        return null;
    }

    @Override
    public Packet updateHealth(float health, int foodLevel, float saturation) {
        return new NeptunePacket(new S06PacketUpdateHealth(health, foodLevel, saturation));
    }

    @Override
    public Packet playerPositionLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        return null;
    }

    @Override
    public Packet heldItemChange(int slot) {
        return null;
    }

    @Override
    public Packet useBed(Player player, int x, int y, int z) {
        return null;
    }

    @Override
    public Packet animation(Player player, int animation) {
        return null;
    }

    @Override
    public Packet spawnPlayer(Human human) {
        return null;
    }

    @Override
    public Packet collectItem(int entityItemID, int collectorID) {
        return null;
    }

    @Override
    public Packet spawnObject(Entity entity, int objectID) {
        return null;
    }

    @Override
    public Packet spawnObject(Entity entity, int objectID, int throwerID) {
        return null;
    }

    @Override
    public Packet spawnMob(LivingBase livingbase) {
        return null;
    }

    @Override
    public Packet spawnPainting(Painting painting) {
        return null;
    }

    @Override
    public Packet spawnXPOrb(XPOrb xporb) {
        return null;
    }

    @Override
    public Packet entityVelocity(int entityID, double motX, double motY, double motZ) {
        return null;
    }

    @Override
    public Packet destroyEntities(int... ids) {
        return null;
    }

    @Override
    public Packet entityRelativeMove(int entityID, byte x, byte y, byte z) {
        return null;
    }

    @Override
    public Packet entityLook(int entityID, byte yaw, byte pitch) {
        return null;
    }

    @Override
    public Packet entityLookRelativeMove(int entityID, byte x, byte y, byte z, byte yaw, byte pitch) {
        return null;
    }

    @Override
    public Packet entityTeleport(Entity entity) {
        return null;
    }

    @Override
    public Packet entityTeleport(int entityID, int x, int y, int z, byte yaw, byte pitch) {
        return null;
    }

    @Override
    public Packet entityStatus(int entityID, byte status) {
        return null;
    }

    @Override
    public Packet attachEntity(int leashId, Entity attaching, Entity vehicle) {
        return null;
    }

    @Override
    public Packet entityMetaData(int entityID, DataWatcher watcher) {
        return null;
    }

    @Override
    public Packet entityEffect(int entityID, PotionEffect effect) {
        return null;
    }

    @Override
    public Packet removeEntityEffect(int entityID, PotionEffect effect) {
        return null;
    }

    @Override
    public Packet setExperience(float bar, int level, int totalXp) {
        return null;
    }

    @Override
    public Packet chunkData(Chunk chunk, boolean initialize, int bitflag) {
        return null;
    }

    @Override
    public Packet multiBlockChange(int size, short[] blocks, Chunk chunk) {
        return null;
    }

    @Override
    public Packet blockChange(int x, int y, int z, int typeId, int data) {
        return null;
    }

    @Override
    public Packet blockChange(int x, int y, int z, BlockType type) {
        return null;
    }

    @Override
    public Packet blockAction(int x, int y, int z, int targetId, int stat1, int stat2) {
        return null;
    }

    @Override
    public Packet blockBreakAnimation(int entityId, int x, int y, int z, int state) {
        return null;
    }

    @Override
    public Packet mapChunkBulk(List<Chunk> chunks) {
        return null;
    }

    @Override
    public Packet explosion(double explodeX, double explodeY, double explodeZ, float power,
            List<Position> affectedPositions, Vector3D playerVelocity) {
        return null;
    }

    @Override
    public Packet effect(int sfxID, int x, int y, int z, int aux, boolean disableRelVol) {
        return null;
    }

    @Override
    public Packet soundEffect(String name, double x, double y, double z, float volume, float pitch) {
        return null;
    }

    @Override
    public Packet particles(String name, float x, float y, float z, float velocityX, float velocityY, float velocityZ,
            float speed, int quantity) {
        return null;
    }

    @Override
    public Packet changeGameState(int state, int mode) {
        return null;
    }

    @Override
    public Packet spawnGlobalEntity(Entity entity) {
        return null;
    }

    @Override
    public Packet openWindow(int windowId, int type, String title, int slots, boolean useTitle) {
        return null;
    }

    @Override
    public Packet closeWindow(int windowId) {
        return new NeptunePacket(new S2EPacketCloseWindow(windowId));
    }

    @Override
    public Packet setSlot(int windowId, int slotId, Item item) {
        return null;
    }

    @Override
    public Packet setWindowItems(int windowId, List<Item> items) {
        return null;
    }

    @Override
    public Packet updateWindowProperty(int windowId, int bar, int value) {
        return null;
    }

    @Override
    public Packet updateSign(int x, int y, int z, String[] text) {
        return null;
    }

    @Override
    public Packet maps(short mapID, byte[] data) {
        return null;
    }

    @Override
    public Packet updateTileEntity(int x, int y, int z, int action, CompoundTag compoundTag) {
        return null;
    }

    @Override
    public Packet signEditorOpen(int x, int y, int z) {
        return new NeptunePacket(new S36PacketSignEditorOpen(new BlockPos(x, y, z)));
    }

    @Override
    public Packet statistics(Map<Stat, Integer> stats) {
        return null;
    }

    @Override
    public Packet playerListItem(String name, boolean connected, int ping) {
        return null;
    }
}
