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
package org.neptunepowered.vanilla.mixin.minecraft.world.chunk.storage;

import net.canarymod.api.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.neptunepowered.vanilla.mixin.minecraft.world.storage.MixinSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;

// TODO: Work everything out
@Mixin(AnvilSaveHandler.class)
public abstract class MixinAnvilSaveHandler extends MixinSaveHandler {

    /**
     * @author jamierocks - 30th October 2016
     * @reason Use the Canary directory structure
     */
    @Overwrite
    public IChunkLoader getChunkLoader(WorldProvider provider) {
        if (provider instanceof WorldProviderHell) {
            return new AnvilChunkLoader(new File(this.getWorldDirectory(), this.getWorldDirectoryName() + "_" + DimensionType.NETHER.getName()));
        } else if (provider instanceof WorldProviderEnd) {
            return new AnvilChunkLoader(new File(this.getWorldDirectory(), this.getWorldDirectoryName() + "_" + DimensionType.END.getName()));
        } else {
            return new AnvilChunkLoader(new File(this.getWorldDirectory(), this.getWorldDirectoryName() + "_" + DimensionType.NORMAL.getName()));
        }
    }

}
