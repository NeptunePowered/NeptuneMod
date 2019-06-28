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
package org.neptunepowered.vanilla.mixin.core.world.storage;

import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.position.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import org.neptunepowered.vanilla.bridge.core.world.storage.BridgeWorldInfo;
import org.neptunepowered.vanilla.util.NbtConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldInfo.class)
public abstract class MixinWorldInfo implements BridgeWorldInfo {

    @Shadow private int dimension;
    @Shadow private int spawnX;
    @Shadow private int spawnY;
    @Shadow private int spawnZ;
    @Shadow private String levelName;

    // Neptune: Save the spawn rotation
    private float rotX = 0.0F;
    private float rotY = 0.0F;

    @Inject(method = "<init>(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"))
    private void onNbtConstruction(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey(NbtConstants.SPAWN_ROT_X)) {
            this.rotX = tag.getFloat(NbtConstants.SPAWN_ROT_X);
        }
        if (tag.hasKey(NbtConstants.SPAWN_ROT_Y)) {
            this.rotY = tag.getFloat(NbtConstants.SPAWN_ROT_Y);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/storage/WorldInfo;)V", at = @At("RETURN"))
    private void onInfoConstruction(WorldInfo info, CallbackInfo ci) {
        this.rotX = ((BridgeWorldInfo) info).bridge$getRotX();
        this.rotY = ((BridgeWorldInfo) info).bridge$getRotY();
    }

    @Inject(method = "updateTagCompound", at = @At("RETURN"))
    private void onUpdateTagCompound(NBTTagCompound tag, NBTTagCompound playerTag, CallbackInfo ci) {
        tag.setFloat(NbtConstants.SPAWN_ROT_X, this.rotX);
        tag.setFloat(NbtConstants.SPAWN_ROT_Y, this.rotY);
    }

    @Override
    public void bridge$setDimensionType(DimensionType dimensionType) {
        this.dimension = dimensionType.getId();
    }

    @Override
    public float bridge$getRotX() {
        return this.rotX;
    }

    @Override
    public float bridge$getRotY() {
        return this.rotY;
    }

    @Override
    public Location bridge$getSpawn() {
        final Location spawn = new Location(this.spawnX, this.spawnY, this.spawnZ);
        spawn.setPitch(this.rotX);
        spawn.setRotation(this.rotY);
        spawn.setType(DimensionType.fromId(this.dimension));
        spawn.setWorldName(this.levelName);
        return spawn;
    }

    @Override
    public void bridge$setSpawn(Location spawn) {
        this.spawnX = spawn.getBlockX();
        this.spawnY = spawn.getBlockY();
        this.spawnZ = spawn.getBlockZ();
        this.rotX = spawn.getPitch();
        this.rotY = spawn.getRotation();
        this.dimension = spawn.getType().getId();
        this.levelName = spawn.getWorldName();
    }

}
