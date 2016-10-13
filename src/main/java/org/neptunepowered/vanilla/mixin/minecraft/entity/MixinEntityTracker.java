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

import com.google.common.collect.Lists;
import net.canarymod.api.EntityTracker;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.world.World;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(net.minecraft.entity.EntityTracker.class)
@Implements(@Interface(iface = EntityTracker.class, prefix = "entitytracker$"))
public abstract class MixinEntityTracker implements EntityTracker {

    @Shadow @Final private WorldServer theWorld;
    @Shadow private Set<EntityTrackerEntry> trackedEntities;

    @Shadow public abstract void trackEntity(net.minecraft.entity.Entity p_72786_1_);
    @Shadow public abstract void untrackEntity(net.minecraft.entity.Entity entityIn);
    @Shadow public abstract void sendToAllTrackingEntity(net.minecraft.entity.Entity entityIn, net.minecraft.network.Packet p_151247_2_);

    /**
     * @author jamierocks - 2nd October 2016
     * @reason Add timings calls
     */
    @Overwrite
    public void updateTrackedEntities() {
        final List<EntityPlayerMP> list = Lists.newArrayList();

        ((IMixinWorld) this.theWorld).getTimings().tracker1.startTiming(); // Neptune - timings
        for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
            entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);

            if (entitytrackerentry.playerEntitiesUpdated && entitytrackerentry.trackedEntity instanceof EntityPlayerMP) {
                list.add((EntityPlayerMP) entitytrackerentry.trackedEntity);
            }
        }
        ((IMixinWorld) this.theWorld).getTimings().tracker1.stopTiming(); // Neptune - timings

        ((IMixinWorld) this.theWorld).getTimings().tracker2.startTiming(); // Neptune - timings
        for (int i = 0; i < ((List) list).size(); ++i) {
            EntityPlayerMP entityplayermp = list.get(i);

            for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities) {
                if (entitytrackerentry1.trackedEntity != entityplayermp) {
                    entitytrackerentry1.updatePlayerEntity(entityplayermp);
                }
            }
        }
        ((IMixinWorld) this.theWorld).getTimings().tracker2.stopTiming(); // Neptune - timings
    }

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

    @Intrinsic
    public void entitytracker$updateTrackedEntities() {
        this.updateTrackedEntities();
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
        return this.trackedEntities.stream().map(entry -> (Entity) entry.trackedEntity).collect(Collectors.toList());
    }

    @Override
    public boolean isPlayerHidden(Player player, Player player1) {
        return false;
    }

}
