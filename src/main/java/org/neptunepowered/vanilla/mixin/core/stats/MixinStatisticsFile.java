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
package org.neptunepowered.vanilla.mixin.core.stats;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentTranslation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(StatisticsFile.class)
public abstract class MixinStatisticsFile extends StatFileWriter {

    @Shadow @Final private MinecraftServer mcServer;
    @Shadow @Final private Set<StatBase> field_150888_e;
    @Shadow private boolean field_150886_g;

    /**
     * @author jamierocks - 28th October 2016
     * @reason Add appropriate null checks
     */
    @Overwrite
    public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
        int i = statIn.isAchievement() ? this.readStat(statIn) : 0;
        super.unlockAchievement(playerIn, statIn, p_150873_3_);
        this.field_150888_e.add(statIn);

        if (statIn.isAchievement() && i == 0 && p_150873_3_ > 0) {
            this.field_150886_g = true;

            if (playerIn != null && this.mcServer.isAnnouncingPlayerAchievements()) { // Neptune - Null check
                this.mcServer.getConfigurationManager().sendChatMsg(
                        new ChatComponentTranslation("chat.type.achievement", playerIn.getDisplayName(), statIn.createChatComponent()));
            }
        }

        if (statIn.isAchievement() && i > 0 && p_150873_3_ == 0) {
            this.field_150886_g = true;

            if (playerIn != null && this.mcServer.isAnnouncingPlayerAchievements()) { // Neptune - Null check
                this.mcServer.getConfigurationManager().sendChatMsg(
                        new ChatComponentTranslation("chat.type.achievement.taken", playerIn.getDisplayName(), statIn.createChatComponent()));
            }
        }
    }

}
