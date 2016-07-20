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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import org.apache.logging.log4j.Logger;
import org.neptunepowered.vanilla.interfaces.minecraft.world.storage.IMixinSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(SaveHandler.class)
public class MixinSaveHandler implements IMixinSaveHandler {

    @Shadow private static Logger logger;

    @Shadow private File playersDirectory;

    @Override
    public NBTTagCompound readPlayerData(UUID id) {
        NBTTagCompound nbttagcompound = null;

        try {
            File file1 = new File(IMixinSaveHandler.PLAYERS_DIR, id.toString() + ".dat");

            if (file1.exists() && file1.isFile()) {
                nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            } else {
                logger.warn("Player data not found for " + id.toString());
            }
        } catch (Exception ex) {
            logger.warn("Failed to load player data for " + id.toString(), ex);
        }

        return nbttagcompound;
    }

    /**
     * Use the correct player directory
     *
     * @author jamierocks
     */
    @Overwrite
    public void writePlayerData(EntityPlayer player) {
        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            player.writeToNBT(nbttagcompound);
            File file1 = new File(IMixinSaveHandler.PLAYERS_DIR, player.getUniqueID().toString() + ".dat.tmp");
            File file2 = new File(IMixinSaveHandler.PLAYERS_DIR, player.getUniqueID().toString() + ".dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));

            if (file2.exists()) {
                file2.delete();
            }

            file1.renameTo(file2);
        } catch (Exception var5) {
            logger.warn("Failed to save player data for " + player.getName());
        }
    }

    /**
     * Use the correct player directory
     *
     * @author jamierocks
     */
    @Overwrite
    public NBTTagCompound readPlayerData(EntityPlayer player) {
        NBTTagCompound nbttagcompound = this.readPlayerData(player.getUniqueID());

        if (nbttagcompound != null) {
            player.readFromNBT(nbttagcompound);
        }

        return nbttagcompound;
    }

    /**
     * Use the correct player directory
     *
     * @author jamierocks
     */
    @Overwrite
    public String[] getAvailablePlayerDat() {
        List<String> availablePlayerData = Arrays.asList(IMixinSaveHandler.PLAYERS_DIR.listFiles((dir, name) -> {
            return name.endsWith(".dat");
        })).stream().map(f -> f.getName().replace(".dat", "")).collect(Collectors.toList());
        return availablePlayerData.toArray(new String[availablePlayerData.size()]);
    }
}
