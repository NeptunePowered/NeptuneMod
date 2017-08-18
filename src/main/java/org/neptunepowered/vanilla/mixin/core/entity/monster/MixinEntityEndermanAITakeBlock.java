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

import net.canarymod.api.entity.living.monster.Enderman;
import net.canarymod.config.Configuration;
import net.canarymod.hook.entity.EndermanPickupBlockHook;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.Random;

@Mixin(EntityEnderman.AITakeBlock.class)
public abstract class MixinEntityEndermanAITakeBlock {

    @Shadow private EntityEnderman enderman;

    /**
     * @author jamierocks - 18th August 2017
     * @reason Fire hook + check blocks from config
     */
    @Overwrite
    public void updateTask() {
        final Random random = this.enderman.getRNG();
        final World world = this.enderman.worldObj;
        final int x = MathHelper.floor_double(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
        final int y = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 3.0D);
        final int z = MathHelper.floor_double(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
        final BlockPos blockpos = new BlockPos(x, y, z);
        final IBlockState iblockstate = world.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();

        //if (EntityEnderman.carriableBlocks.contains(block)) {
        //    this.enderman.setHeldBlockState(iblockstate);
        //    world.setBlockState(blockpos, Blocks.air.getDefaultState());
        //}

        // Neptune: Use world config to get carriable blocks
        if (Arrays.stream(Configuration.getWorldConfig(((net.canarymod.api.world.World) world).getFqName()).getEnderBlocks())
                .anyMatch(id -> id == Block.getIdFromBlock(block))) {
            // Neptune: Fire hook
            final EndermanPickupBlockHook hook = new EndermanPickupBlockHook((Enderman) this.enderman, (net.canarymod.api.world.blocks.Block) iblockstate);
            if (!hook.call().isCanceled()) {
                this.enderman.setHeldBlockState(iblockstate);
                world.setBlockState(blockpos, Blocks.air.getDefaultState());
            }
        }
    }

}
