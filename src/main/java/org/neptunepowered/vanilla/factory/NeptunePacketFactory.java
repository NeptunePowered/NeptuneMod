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
package org.neptunepowered.vanilla.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.NotImplementedException;
import org.neptunepowered.vanilla.chat.NeptuneChatComponent;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class NeptunePacketFactory implements PacketFactory {

    private final static String TOO_FEW_ARGUMENTS = "Not enough arguments (Expected: %d Got: %d)",
                                INVALID_ARGUMENT = "Argument at index: '%d' does not match a valid type. (Expected: '%s' Got: '%s')";

    protected NeptunePacketFactory() {}

    @Override
    public Packet createPacket(int id, Object... args) throws InvalidPacketConstructionException {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Arguments cannot be null or empty!");
        }
        switch (id) {
            case 0:
                throw new InvalidPacketConstructionException(id, "KeepAlive", "Keep Alive packets should only be handled by the server!");
            case 1:
                throw new InvalidPacketConstructionException(id, "JoinGame", "Join Game packets should only be handled by the server!");
            case 2:
                this.check(2, "Chat", 1, args, this.test(ChatComponent.class));
                return this.chat((ChatComponent) args[0]);
            case 3:
                this.check(3, "UpdateTime", 2, args, this.test(Long.class), this.test(Long.class));
                return this.updateTime((Long) args[0], (Long) args[1]);
        }
        return null;
    }

    @Override
    public Packet chat(ChatComponent chatComponent) {
        return (Packet) new S02PacketChat(((NeptuneChatComponent) chatComponent).getHandle());
    }

    @Override
    public Packet updateTime(long worldAge, long time) {
        return (Packet) new S03PacketTimeUpdate(worldAge, time, false);
    }

    @Override
    public Packet entityEquipment(int entityID, int slot, Item item) {
        return (Packet) new S04PacketEntityEquipment(entityID, slot, (ItemStack) item);
    }

    @Override
    public Packet spawnPosition(int x, int y, int z) {
        return (Packet) new S05PacketSpawnPosition(new BlockPos(x, y, z));
    }

    @Override
    public Packet updateHealth(float health, int foodLevel, float saturation) {
        return (Packet) new S06PacketUpdateHealth(health, foodLevel, saturation);
    }

    @Override
    public Packet playerPositionLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        return (Packet) new S08PacketPlayerPosLook(x, y, z, yaw, pitch, Sets.newHashSet());
    }

    @Override
    public Packet heldItemChange(int slot) {
        return (Packet) new S09PacketHeldItemChange(slot);
    }

    @Override
    public Packet useBed(Player player, int x, int y, int z) {
        return (Packet) new S0APacketUseBed((EntityPlayer) player, new BlockPos(x, y, z));
    }

    @Override
    public Packet animation(Player player, int animation) {
        return (Packet) new S0BPacketAnimation((net.minecraft.entity.Entity) player, animation);
    }

    @Override
    public Packet spawnPlayer(Human human) {
        return null;
    }

    @Override
    public Packet collectItem(int entityItemID, int collectorID) {
        return (Packet) new S0DPacketCollectItem(entityItemID, collectorID);
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
        return (Packet) new S12PacketEntityVelocity(entityID, motX, motY, motZ);
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
        return (Packet) new S1FPacketSetExperience(bar, level, totalXp);
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
        throw new NotImplementedException("A Minecraft Update has broken this construction!");
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
        return (Packet) new S2BPacketChangeGameState(state, mode);
    }

    @Override
    public Packet spawnGlobalEntity(Entity entity) {
        return (Packet) new S2CPacketSpawnGlobalEntity((net.minecraft.entity.Entity) entity);
    }

    @Override
    public Packet openWindow(int windowId, int type, String title, int slots, boolean useTitle) {
        throw new NotImplementedException("A Minecraft Update has broken this construction!");
    }

    @Override
    public Packet closeWindow(int windowId) {
        return (Packet) new S2EPacketCloseWindow(windowId);
    }

    @Override
    public Packet setSlot(int windowId, int slotId, Item item) {
        return (Packet) new S2FPacketSetSlot(windowId, slotId, (ItemStack) item);
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
        List<ChatComponentText> texts = Lists.newArrayList();

        for (String t : text) {
            texts.add(new ChatComponentText(t));
        }

        return (Packet)
                new S33PacketUpdateSign(null, new BlockPos(x, y, z), texts.toArray(new ChatComponentText[texts.size()]));
    }

    @Override
    public Packet maps(short mapID, byte[] data) {
        return null;
    }

    @Override
    public Packet updateTileEntity(int x, int y, int z, int action, CompoundTag compoundTag) {
        return (Packet) new S35PacketUpdateTileEntity(new BlockPos(x, y, z), action, (NBTTagCompound) compoundTag);
    }

    @Override
    public Packet signEditorOpen(int x, int y, int z) {
        return (Packet) new S36PacketSignEditorOpen(new BlockPos(x, y, z));
    }

    @Override
    public Packet statistics(Map<Stat, Integer> stats) {
        return null;
    }

    @Override
    public Packet playerListItem(String name, boolean connected, int ping) {
        throw new NotImplementedException("A Minecraft Update has broken this construction!");
    }

    private void check(int packetId, String packetName, int minParams, Object[] args, ArgumentPredicate... tests) throws
            InvalidPacketConstructionException {
        if (args.length < minParams) {
            throw new InvalidPacketConstructionException(packetId, packetName, String.format(TOO_FEW_ARGUMENTS, minParams, args.length));
        }

        for (int test = 0; test < tests.length; test++) {
            if (!tests[test].test(args[test].getClass())) {
                throw new InvalidPacketConstructionException(packetId, packetName,
                        String.format(INVALID_ARGUMENT, test, tests[test].getType(), args[test].getClass().getSimpleName()));
            }
        }
    }

    private <T> ArgumentPredicate<T> test(Class<T> type) {
        return new ArgumentPredicate<T>() {
            @Override
            public Class<T> getType() {
                return type;
            }

            @Override
            public boolean test(T t) {
                return type.isAssignableFrom(t.getClass());
            }
        };
    }

    private interface ArgumentPredicate<T> extends Predicate<T> {

        Class<T> getType();

    }

}
