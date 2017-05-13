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
package org.neptunepowered.vanilla.mixin.core.tileentity;

import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import org.neptunepowered.vanilla.util.NbtConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.tileentity.TileEntity.class)
public abstract class MixinTileEntity implements TileEntity {

    @Shadow protected net.minecraft.block.Block blockType;
    @Shadow protected net.minecraft.world.World worldObj;
    @Shadow protected BlockPos pos;

    private NBTTagCompound canaryMeta = new NBTTagCompound();

    @Shadow public abstract void writeToNBT(NBTTagCompound compound);
    @Shadow public abstract void readFromNBT(NBTTagCompound compound);

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    public void onReadFromNBT(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey(NbtConstants.CANARY_TAG)) {
            this.canaryMeta = tag.getCompoundTag(NbtConstants.CANARY_TAG);
        }
    }

    @Override
    public Block getBlock() {
        return (Block) this.blockType;
    }

    @Override
    public int getX() {
        return this.pos.getX();
    }

    @Override
    public int getY() {
        return this.pos.getY();
    }

    @Override
    public int getZ() {
        return this.pos.getZ();
    }

    @Override
    public World getWorld() {
        return (World) this.worldObj;
    }

    @Override
    public void update() {
        this.worldObj.markBlockForUpdate(this.pos);
    }

    @Override
    public CompoundTag getDataTag() {
        final NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return (CompoundTag) tag;
    }

    @Override
    public CompoundTag getMetaTag() {
        return (CompoundTag) this.canaryMeta;
    }

    @Override
    public CompoundTag writeToTag(CompoundTag tag) {
        this.writeToNBT((NBTTagCompound) tag);
        return tag;
    }

    @Override
    public void readFromTag(CompoundTag tag) {
        this.readFromNBT((NBTTagCompound) tag);
    }

}
