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
package org.neptunepowered.vanilla.mixin.minecraft.world.storage;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import org.apache.logging.log4j.Logger;
import org.neptunepowered.vanilla.interfaces.minecraft.world.storage.IMixinSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Mixin(SaveHandler.class)
public class MixinSaveHandler implements IMixinSaveHandler {

    @Shadow private static Logger logger;

    @Shadow private File playersDirectory;

    @Override
    public NBTTagCompound readPlayerData(UUID id) {
        NBTTagCompound nbttagcompound = null;

        try {
            File file1 = new File(this.playersDirectory, id.toString() + ".dat");

            if (file1.exists() && file1.isFile()) {
                nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        } catch (Exception var4) {
            logger.warn("Failed to load player data for " + id.toString());
        }

        return nbttagcompound;
    }
}
