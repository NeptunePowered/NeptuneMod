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
package org.neptunepowered.vanilla.mixin.minecraft.scoreboard;

import net.canarymod.api.scoreboard.ScoreObjective;
import net.canarymod.api.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Score;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Score.class)
public abstract class MixinScore implements net.canarymod.api.scoreboard.Score {

    @Shadow @Final private net.minecraft.scoreboard.Scoreboard theScoreboard;
    @Shadow @Final private net.minecraft.scoreboard.ScoreObjective theScoreObjective;

    @Shadow public abstract void increseScore(int amount);
    @Shadow public abstract void decreaseScore(int amount);
    @Shadow public abstract int getScorePoints();
    @Shadow public abstract void setScorePoints(int points);
    @Shadow public abstract void func_96651_a(List p_96651_1_);
    @Shadow public abstract String getPlayerName();

    @Override
    public String getName() {
        return this.getPlayerName();
    }

    @Override
    public void addToScore(int toAdd) {
        this.increseScore(toAdd);
    }

    @Override
    public void removeFromScore(int toRemove) {
        this.decreaseScore(toRemove);
    }

    @Override
    public int getScore() {
        return this.getScorePoints();
    }

    @Override
    public void setScore(int toSet) {
        this.setScorePoints(toSet);
    }

    @Override
    public ScoreObjective getScoreObjective() {
        return (ScoreObjective) this.theScoreObjective;
    }

    @Override
    public Scoreboard getScoreboard() {
        return (Scoreboard) this.theScoreboard;
    }

    @Override
    public void setReadOnlyScore(List<?> list) {
        this.func_96651_a(list);
    }

    @Override
    public void update() {
        // TODO: look into this
    }

}
