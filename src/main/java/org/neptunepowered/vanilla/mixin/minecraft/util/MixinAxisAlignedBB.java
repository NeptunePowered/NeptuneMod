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
package org.neptunepowered.vanilla.mixin.minecraft.util;

import net.canarymod.api.BoundingBox;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AxisAlignedBB.class)
@Implements(@Interface(iface = BoundingBox.class, prefix = "box$"))
public abstract class MixinAxisAlignedBB implements BoundingBox {

    @Shadow @Final public double minX;
    @Shadow @Final public double minY;
    @Shadow @Final public double minZ;
    @Shadow @Final public double maxX;
    @Shadow @Final public double maxY;
    @Shadow @Final public double maxZ;

    @Shadow public abstract AxisAlignedBB shadow$contract(double x, double y, double z);
    @Shadow public abstract AxisAlignedBB addCoord(double x, double y, double z);
    @Shadow public abstract AxisAlignedBB shadow$offset(double x, double y, double z);
    @Shadow public abstract AxisAlignedBB shadow$expand(double x, double y, double z);
    @Shadow public abstract AxisAlignedBB union(AxisAlignedBB other);
    @Shadow public abstract double calculateXOffset(AxisAlignedBB other, double offsetX);
    @Shadow public abstract double calculateYOffset(AxisAlignedBB other, double offsetY);
    @Shadow public abstract double calculateZOffset(AxisAlignedBB other, double offsetZ);
    @Shadow public abstract boolean intersectsWith(AxisAlignedBB other);
    @Shadow public abstract double getAverageEdgeLength();

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public double getMinZ() {
        return minZ;
    }

    @Override
    public double getMaxX() {
        return maxX;
    }

    @Override
    public double getMaxY() {
        return maxY;
    }

    @Override
    public double getMaxZ() {
        return maxZ;
    }

    @Override
    public BoundingBox addCoordinates(double x, double y, double z) {
        return (BoundingBox) addCoord(x, y, z);
    }

    @Override
    public BoundingBox expand(double x, double y, double z) {
        return (BoundingBox) shadow$expand(x, y, z);
    }

    @Override
    public BoundingBox contract(double x, double y, double z) {
        return (BoundingBox) shadow$contract(x, y, z);
    }

    @Override
    public BoundingBox union(BoundingBox boundingBox) {
        return (BoundingBox) union((AxisAlignedBB) boundingBox);
    }

    @Override
    public BoundingBox offset(double x, double y, double z) {
        return (BoundingBox) shadow$offset(x, y, z);
    }

    @Override
    public double calculateXOffset(BoundingBox other, double xOffset) {
        return calculateXOffset((AxisAlignedBB) other, xOffset);
    }

    @Override
    public double calculateYOffset(BoundingBox other, double yOffset) {
        return calculateYOffset((AxisAlignedBB) other, yOffset);
    }

    @Override
    public double calculateZOffset(BoundingBox other, double zOffset) {
        return calculateZOffset((AxisAlignedBB) other, zOffset);
    }

    @Override
    public boolean intersectsWith(BoundingBox other) {
        return intersectsWith((AxisAlignedBB) other);
    }

    @Intrinsic
    public double box$getAverageEdgeLength() {
        return this.getAverageEdgeLength();
    }

}
