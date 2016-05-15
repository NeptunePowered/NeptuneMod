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
package org.neptunepowered.vanilla.mixin.minecraft.village;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.Village;
import net.canarymod.api.world.position.Location;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.village.Village.class)
@Implements(@Interface(iface = Village.class, prefix = "village$"))
public abstract class MixinVillage implements Village {

    @Shadow private BlockPos center;
    @Shadow private int noBreedTicks;
    @Shadow private int villageRadius;
    @Shadow private int numVillagers;
    @Shadow private int numIronGolems;

    @Shadow
    public abstract int getReputationForPlayer(String p_82684_1_);

    @Shadow
    public abstract int setReputationForPlayer(String p_82688_1_, int p_82688_2_);

    @Shadow
    public abstract boolean isPlayerReputationTooLow(String p_82687_1_);

    @Shadow
    public abstract boolean isMatingSeason();

    @Shadow
    public abstract void endMatingSeason();

    @Shadow
    public abstract boolean isAnnihilated();

    @Override
    public void setReputationForPlayer(Player player, int rep) {
        this.setReputationForPlayer(player.getName(), rep);
    }

    @Override
    public int getReputationForPlayer(Player player) {
        return this.getReputationForPlayer(player.getName());
    }

    @Override
    public boolean isPlayerReputationTooLow(Player player) {
        return this.isPlayerReputationTooLow(player.getName());
    }

    @Intrinsic
    public boolean village$isMatingSeason() {
        return this.isMatingSeason();
    }

    @Override
    public void startMatingSeason() {
        this.noBreedTicks -= 3600;
    }

    @Intrinsic
    public void village$endMatingSeason() {
        this.endMatingSeason();
    }

    @Override
    public Location getCenter() {
        return new Location(this.center.getX(), this.center.getY(), this.center.getZ());
    }

    @Override
    public int getRadius() {
        return this.villageRadius;
    }

    @Override
    public int getVillagerCount() {
        return this.numVillagers;
    }

    @Override
    public int getIronGolemCount() {
        return this.numIronGolems;
    }

    @Intrinsic
    public boolean village$isAnnihilated() {
        return this.isAnnihilated();
    }
}
