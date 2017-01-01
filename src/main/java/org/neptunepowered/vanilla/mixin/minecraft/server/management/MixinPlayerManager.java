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
package org.neptunepowered.vanilla.mixin.minecraft.server.management;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager implements net.canarymod.api.PlayerManager {

    @Shadow @Final private List<EntityPlayerMP> players;
    @Shadow private int playerViewRadius;

    @Shadow public abstract WorldServer getWorldServer();
    @Shadow public abstract void markBlockForUpdate(BlockPos pos);
    @Shadow public abstract void addPlayer(EntityPlayerMP player);
    @Shadow public abstract void removePlayer(EntityPlayerMP player);
    @Shadow public abstract void updateMountedMovingPlayer(EntityPlayerMP player);

    @Override
    public void updateMountedMovingPlayer(Player player) {
        this.updateMountedMovingPlayer((EntityPlayerMP) player);
    }

    @Override
    public void addPlayer(Player player) {
        this.addPlayer((EntityPlayerMP) player);
    }

    @Override
    public void removePlayer(Player player) {
        this.removePlayer((EntityPlayerMP) player);
    }

    @Override
    public List<Player> getManagedPlayers() {
        return (List) this.players;
    }

    @Override
    public void markBlockNeedsUpdate(int x, int y, int z) {
        this.markBlockForUpdate(new BlockPos(x, y, z));
    }

    @Override
    public int getMaxTrackingDistance() {
        return PlayerManager.getFurthestViewableBlock(this.playerViewRadius);
    }

    @Override
    public World getAttachedDimension() {
        return (World) this.getWorldServer();
    }

}
