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
package org.neptunepowered.vanilla.mixin.minecraft.pathfinding;

import net.canarymod.api.PathFinder;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.position.Location;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathNavigate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PathNavigate.class)
public abstract class MixinPathNavigate implements PathFinder {

    @Shadow public IAttributeInstance pathSearchRange;
    @Shadow protected double speed;

    @Shadow public abstract void setSpeed(double p_setSpeed_1_);
    @Shadow public abstract boolean tryMoveToXYZ(double x, double y, double z, double speed);

    @Override
    public boolean setPathToXYZ(double x, double y, double z, World world) {
        return this.tryMoveToXYZ(x, y, z, this.speed);
    }

    @Override
    public boolean setPathToLocation(Location location) {
        return this.setPathToXYZ(location.getX(), location.getY(), location.getZ(), location.getWorld());
    }

    @Override
    public boolean setPathToEntity(Entity entity) {
        return this.setPathToLocation(entity.getLocation());
    }

    @Override
    public boolean setPathToBlock(Block block) {
        return this.setPathToLocation(block.getLocation());
    }

    @Override
    public void setSpeed(float speed) {
        this.setSpeed((double) speed);
    }

    @Override
    public void setPathSearchRange(float range) {
        this.pathSearchRange.setBaseValue(range);
    }

}
