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
package org.neptunepowered.vanilla.mixin.minecraft.world;

import net.canarymod.api.world.ChunkProviderCustom;
import net.canarymod.api.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorldProvider;
import org.neptunepowered.vanilla.world.NeptuneChunkProviderCustom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider implements IMixinWorldProvider {

    @Shadow protected World worldObj;
    @Shadow private WorldType terrainType;
    @Shadow private String generatorSettings;
    @Shadow protected int dimensionId;

    private DimensionType dimensionType;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstruction(CallbackInfo ci) {
        this.dimensionType = DimensionType.fromId(this.dimensionId);
    }

    /**
     * @author jamierocks - 24th October 2016
     * @reason To use custom ChunkProviders where necessary
     */
    @Overwrite
    public IChunkProvider createChunkGenerator() {
        final IChunkProvider chunkProvider = this.terrainType == WorldType.FLAT ?
                new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(),
                        this.generatorSettings) :
                (this.terrainType == WorldType.DEBUG_WORLD ?
                        new ChunkProviderDebug(this.worldObj) :
                        (this.terrainType == WorldType.CUSTOMIZED ?
                                new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(),
                                        this.generatorSettings) :
                                new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(),
                                        this.generatorSettings)));

        if (this.dimensionType.hasChunkProvider()) {
            final ChunkProviderCustom chunkProviderCustom = this.dimensionType.getChunkProvider();
            chunkProviderCustom.setWorld((net.canarymod.api.world.World) this.worldObj);
            return new NeptuneChunkProviderCustom(chunkProviderCustom, chunkProvider);
        } else {
            return chunkProvider;
        }
    }

    @Override
    public DimensionType getDimensionType() {
        return this.dimensionType;
    }

}
