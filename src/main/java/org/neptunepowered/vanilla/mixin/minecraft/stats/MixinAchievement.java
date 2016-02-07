/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
package org.neptunepowered.vanilla.mixin.minecraft.stats;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Achievement.class)
public abstract class MixinAchievement extends StatBase implements net.canarymod.api.statistics.Achievement {

    @Shadow public Achievement parentAchievement;
    @Shadow private String achievementDescription;
    @Shadow private boolean isSpecial;

    public MixinAchievement(String p_i45307_1_, IChatComponent p_i45307_2_, IStatType p_i45307_3_) {
        super(p_i45307_1_, p_i45307_2_, p_i45307_3_);
    }

    @Override
    public String getDescription() {
        return achievementDescription;
    }

    @Override
    public net.canarymod.api.statistics.Achievement getParent() {
        return (net.canarymod.api.statistics.Achievement) parentAchievement;
    }

    @Override
    public boolean isSpecial() {
        return isSpecial;
    }
}
