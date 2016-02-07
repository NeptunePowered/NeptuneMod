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
package org.neptunepowered.common.mixin.minecraft.entity;

import net.canarymod.api.BoundingBox;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityItem;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.nbt.BaseTag;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Position;
import net.canarymod.api.world.position.Vector3D;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;

@Mixin(net.minecraft.entity.Entity.class)
public abstract class MixinEntity implements Entity {

    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;
    @Shadow public double motionX;
    @Shadow public double motionY;
    @Shadow public double motionZ;
    @Shadow public float rotationPitch;
    @Shadow public float rotationYaw;
    @Shadow public boolean preventEntitySpawning;
    @Shadow public net.minecraft.world.World worldObj;
    @Shadow public net.minecraft.entity.Entity riddenByEntity;
    @Shadow public net.minecraft.entity.Entity ridingEntity;
    @Shadow public boolean onGround;
    @Shadow public boolean isDead;
    @Shadow protected boolean isInWeb;
    @Shadow protected UUID entityUniqueID;
    @Shadow private int entityId;
    @Shadow private AxisAlignedBB boundingBox;

    @Shadow
    public abstract void setDead();

    @Shadow
    protected abstract boolean getFlag(int flag);

    @Override
    public double getX() {
        return posX;
    }

    @Override
    public void setX(int x) {
        this.posX = x;
    }

    @Override
    public void setX(double x) {
        this.posX = x;
    }

    @Override
    public double getY() {
        return posY;
    }

    @Override
    public void setY(int y) {
        this.posY = y;
    }

    @Override
    public void setY(double y) {
        this.posY = y;
    }

    @Override
    public double getZ() {
        return posZ;
    }

    @Override
    public void setZ(int z) {
        this.posZ = z;
    }

    @Override
    public void setZ(double z) {
        this.posZ = z;
    }

    @Override
    public double getMotionX() {
        return motionX;
    }

    @Override
    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    @Override
    public double getMotionY() {
        return motionY;
    }

    @Override
    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    @Override
    public double getMotionZ() {
        return motionZ;
    }

    @Override
    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }

    @Override
    public float getPitch() {
        return rotationPitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.rotationPitch = pitch;
    }

    @Override
    public float getRotation() {
        return rotationYaw;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotationYaw = rotation;
    }

    @Override
    public Position getPosition() {
        return new Position(getX(), getY(), getZ());
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), getX(), getY(), getZ(), getPitch(), getRotation());
    }

    @Override
    @Shadow
    public abstract float getEyeHeight();

    @Override
    public int getID() {
        return entityId;
    }

    @Override
    public UUID getUUID() {
        return entityUniqueID;
    }

    @Override
    public Vector3D getMotion() {
        return null;
    }

    @Override
    public Vector3D getForwardVector() {
        return null;
    }

    @Override
    public void translate(Vector3D factor) {

    }

    @Override
    public void moveEntity(double motionX, double motionY, double motionZ) {

    }

    @Override
    public void teleportTo(double x, double y, double z) {

    }

    @Override
    public void teleportTo(double x, double y, double z, World world) {

    }

    @Override
    public void teleportTo(double x, double y, double z, float pitch, float rotation) {

    }

    @Override
    public void teleportTo(double x, double y, double z, float pitch, float rotation, World dim) {

    }

    @Override
    public void teleportTo(Location location) {

    }

    @Override
    public void teleportTo(Position position) {

    }

    @Override
    public World getWorld() {
        return (World) worldObj;
    }

    @Override
    @Shadow
    public abstract boolean isSprinting();

    @Override
    @Shadow
    public abstract void setSprinting(boolean sprinting);

    @Override
    @Shadow
    public abstract boolean isSneaking();

    @Override
    @Shadow
    public abstract void setSneaking(boolean sneaking);

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {

    }

    @Override
    public boolean isLiving() {
        return false;
    }

    @Override
    public boolean isItem() {
        return false;
    }

    @Override
    public boolean isMob() {
        return false;
    }

    @Override
    public boolean isAnimal() {
        return false;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isGolem() {
        return false;
    }

    @Override
    public boolean isNPC() {
        return false;
    }

    @Override
    public EntityItem dropLoot(int itemId, int amount) {
        return null;
    }

    @Override
    public EntityItem dropLoot(Item item) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean canSpawn() {
        return !preventEntitySpawning;
    }

    @Override
    public boolean spawn() {
        return false;
    }

    @Override
    public boolean spawn(Entity rider) {
        return false;
    }

    @Override
    public boolean isRiding() {
        return false;
    }

    @Override
    public boolean isRidden() {
        return false;
    }

    @Override
    public Entity getRiding() {
        return (Entity) riddenByEntity;
    }

    @Override
    public Entity getRider() {
        return (Entity) ridingEntity;
    }

    @Override
    public void setRider(Entity rider) {
        this.ridingEntity = (net.minecraft.entity.Entity) rider;
    }

    @Override
    public void mount(Entity entity) {
        this.riddenByEntity = (net.minecraft.entity.Entity) entity;
    }

    @Override
    public void dismount() {

    }

    @Override
    public void destroy() {
        setDead();
    }

    @Override
    public boolean isDead() {
        return this.isDead;
    }

    @Override
    public CompoundTag getNBT() {
        return null;
    }

    @Override
    public void setNBT(BaseTag tag) {

    }

    @Override
    @Shadow
    public abstract boolean isInvisible();

    @Override
    @Shadow
    public abstract void setInvisible(boolean invisible);

    @Override
    public CompoundTag getMetaData() {
        return null;
    }

    @Override
    public boolean isAmbient() {
        return false;
    }

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public boolean isInWeb() {
        return isInWeb;
    }

    @Override
    @Shadow
    public abstract boolean isInWater();

    @Override
    @Shadow
    public abstract boolean isInLava();

    @Override
    public boolean hasDisplayName() {
        return false;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String display) {

    }

    @Override
    public boolean showingDisplayName() {
        return false;
    }

    @Override
    public void setShowDisplayName(boolean show) {

    }

    @Override
    public boolean isEating() {
        return getFlag(4);
    }

    @Override
    public List<Entity> getNearbyEntities(double radius) {
        return null;
    }

    @Override
    public List<Entity> getNearbyEntities(Vector3D vector) {
        return null;
    }

    @Override
    public List<Entity> getNearbyEntities(double radius, EntityType... filter) {
        return null;
    }

    @Override
    public List<Entity> getNearbyEntities(Vector3D vector, EntityType... filter) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return (BoundingBox) boundingBox;
    }
}
