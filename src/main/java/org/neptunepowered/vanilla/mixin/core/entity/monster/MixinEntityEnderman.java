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
package org.neptunepowered.vanilla.mixin.core.entity.monster;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.monster.Enderman;
import net.canarymod.api.world.blocks.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityEnderman.class)
@Implements(@Interface(iface = Enderman.class, prefix = "enderman$"))
public abstract class MixinEntityEnderman extends MixinEntityMob implements Enderman {

    @Shadow public abstract IBlockState getHeldBlockState();
    @Shadow public abstract void setHeldBlockState(IBlockState state);
    @Shadow protected abstract boolean teleportRandomly();
    @Shadow public abstract boolean isScreaming();
    @Shadow public abstract void setScreaming(boolean screaming);

    @Override
    public Block getCarriedBlock() {
        return (Block) this.getHeldBlockState();
    }

    @Override
    public short getCarriedBlockID() {
        return this.getCarriedBlock().getTypeId();
    }

    @Override
    public void setCarriedBlock(Block block) {
        this.setHeldBlockState((IBlockState) block);
    }

    @Override
    public void setCarriedBlockID(short blockId) {
        this.getCarriedBlock().setTypeId(blockId);
    }

    @Override
    public short getCarriedBlockMetaData() {
        return 0;
    }

    @Override
    public void setCarriedBlockMetaData(short metadata) {

    }

    @Override
    public boolean randomTeleport() {
        return this.teleportRandomly();
    }

    @Intrinsic
    public boolean enderman$isScreaming() {
        return this.isScreaming();
    }

    @Intrinsic
    public void enderman$setScreaming(boolean screaming) {
        this.setScreaming(screaming);
    }

    @Override
    public String getFqName() {
        return "Enderman";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ENDERMAN;
    }

}
