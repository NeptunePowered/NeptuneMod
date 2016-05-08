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
package org.neptunepowered.vanilla.mixin.minecraft.entity;

import net.canarymod.api.EntityTracker;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(net.minecraft.entity.EntityTracker.class)
public abstract class MixinEntityTracker implements EntityTracker {

    @Shadow private WorldServer theWorld;

    @Shadow
    public abstract void trackEntity(net.minecraft.entity.Entity p_72786_1_);

    @Shadow
    public abstract void untrackEntity(net.minecraft.entity.Entity entityIn);

    @Shadow
    public abstract void sendToAllTrackingEntity(net.minecraft.entity.Entity entityIn,
            net.minecraft.network.Packet p_151247_2_);

    @Override
    public void trackEntity(Entity entity) {
        this.trackEntity((net.minecraft.entity.Entity) entity);
    }

    @Override
    public void untrackEntity(Entity entity) {
        this.untrackEntity((net.minecraft.entity.Entity) entity);
    }

    @Override
    public void hidePlayer(Player player, Player player1) {

    }

    @Override
    public void hidePlayerGlobal(Player player) {

    }

    @Override
    public void showPlayer(Player player, Player player1) {

    }

    @Override
    public void showPlayerGlobal(Player player) {

    }

    @Override
    public void untrackPlayerSymmetrics(Player player) {

    }

    @Override
    public void updateTrackedEntities() {

    }

    @Override
    public World getAttachedDimension() {
        return (World) this.theWorld;
    }

    @Override
    public void sendPacketToTrackedPlayer(Player player, Packet packet) {
        this.sendToAllTrackingEntity((net.minecraft.entity.Entity) player, (net.minecraft.network.Packet) packet);
    }

    @Override
    public List<Entity> getTrackedEntities() {
        return null;
    }

    @Override
    public boolean isPlayerHidden(Player player, Player player1) {
        return false;
    }
}
