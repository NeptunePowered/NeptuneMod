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
package org.neptunepowered.vanilla.mixin.core.network.play.server;

import net.canarymod.api.packet.BlockChangePacket;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Position;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(S23PacketBlockChange.class)
public abstract class MixinS23PacketBlockChange implements BlockChangePacket {

    @Shadow public BlockPos blockPosition;
    @Shadow public IBlockState blockState;

    @Override
    public int getX() {
        return this.blockPosition.getX();
    }

    @Override
    public void setX(int x) {
        this.setPosition(new Position(x, this.getY(), this.getZ()));
    }

    @Override
    public int getY() {
        return this.blockPosition.getY();
    }

    @Override
    public void setY(int y) {
        this.setPosition(new Position(this.getX(), y, this.getZ()));
    }

    @Override
    public int getZ() {
        return this.blockPosition.getZ();
    }

    @Override
    public void setZ(int z) {
        this.setPosition(new Position(this.getX(), this.getY(), z));
    }

    @Override
    public Position getPosition() {
        return new Position(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public void setPosition(Position position) {
        this.blockPosition = new BlockPos(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public BlockType getType() {
        return null;
    }

    @Override
    public void setType(BlockType blockType) {

    }

    @Override
    public int getTypeId() {
        return net.minecraft.block.Block.getIdFromBlock(this.blockState.getBlock());
    }

    @Override
    public void setTypeId(int i) {

    }

    @Override
    public int getData() {
        return 0;
    }

    @Override
    public void setData(int i) {

    }

    @Override
    public Block getBlock() {
        return (Block) this.blockState.getBlock();
    }

    @Override
    public void setBlock(Block block) {

    }

}
