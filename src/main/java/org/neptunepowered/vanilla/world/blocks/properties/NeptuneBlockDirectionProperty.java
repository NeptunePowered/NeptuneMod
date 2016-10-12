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
package org.neptunepowered.vanilla.world.blocks.properties;

import net.canarymod.api.world.blocks.BlockFace;
import net.canarymod.api.world.blocks.properties.BlockDirectionProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public class NeptuneBlockDirectionProperty extends NeptuneBlockEnumProperty implements BlockDirectionProperty {

    protected NeptuneBlockDirectionProperty(PropertyDirection handle) {
        super(handle);
    }

    @Override
    public boolean canApply(Comparable value) {
        if (value instanceof BlockFace) {
            EnumFacing facing;
            switch ((BlockFace) value) {
                case BOTTOM:
                    facing = EnumFacing.DOWN;
                    break;
                case TOP:
                    facing = EnumFacing.UP;
                    break;
                case NORTH:
                    facing = EnumFacing.NORTH;
                    break;
                case SOUTH:
                    facing = EnumFacing.SOUTH;
                    break;
                case WEST:
                    facing = EnumFacing.WEST;
                    break;
                case EAST:
                    facing = EnumFacing.EAST;
                    break;
                default:
                    return false;
            }
            return this.getAllowedValues().contains(facing);
        }
        return super.canApply(value);
    }

    @Override
    public PropertyDirection getHandle() {
        return (PropertyDirection) super.getHandle();
    }

}
