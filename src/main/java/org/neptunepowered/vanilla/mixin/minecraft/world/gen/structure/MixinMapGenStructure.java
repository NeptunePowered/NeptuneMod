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
package org.neptunepowered.vanilla.mixin.minecraft.world.gen.structure;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Random;

@Mixin(MapGenStructure.class)
public abstract class MixinMapGenStructure extends MapGenBase {

    @Shadow protected Map<Long, StructureStart> structureMap;

    @Shadow private void initializeStructureData(World worldIn) {}
    @Shadow private void setStructureStart(int chunkX, int chunkZ, StructureStart start) {}

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstruction(CallbackInfo info) {
        // Performance!
        this.structureMap = new Long2ObjectOpenHashMap<>(1024);
    }

    /**
     * @author jamierocks - 25th October 2016
     * @reason Prevent CME
     */
    @Overwrite
    public boolean generateStructure(World worldIn, Random randomIn, ChunkCoordIntPair chunkCoord) {
        this.initializeStructureData(worldIn);
        int i = (chunkCoord.chunkXPos << 4) + 8;
        int j = (chunkCoord.chunkZPos << 4) + 8;
        boolean flag = false;

        synchronized (this.structureMap) { // Neptune - prevent CME
            for (StructureStart structurestart : this.structureMap.values()) {
                if (structurestart.isSizeableStructure() && structurestart.func_175788_a(chunkCoord) && structurestart.getBoundingBox()
                        .intersectsWith(i, j, i + 15, j + 15)) {
                    structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
                    structurestart.func_175787_b(chunkCoord);
                    flag = true;
                    this.setStructureStart(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
                }
            }
        }

        return flag;
    }

}
