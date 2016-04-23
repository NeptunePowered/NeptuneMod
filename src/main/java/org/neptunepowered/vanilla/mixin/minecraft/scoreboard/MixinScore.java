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
package org.neptunepowered.vanilla.mixin.minecraft.scoreboard;

import net.canarymod.api.scoreboard.ScoreObjective;
import net.canarymod.api.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Score.class)
public abstract class MixinScore implements net.canarymod.api.scoreboard.Score {

    @Shadow private net.minecraft.scoreboard.Scoreboard theScoreboard;
    @Shadow private net.minecraft.scoreboard.ScoreObjective theScoreObjective;

    @Shadow
    public abstract void increaseScore(int amount);

    @Shadow
    public abstract void decreaseScore(int amount);

    @Shadow
    public abstract int getScorePoints();

    @Shadow
    public abstract void setScorePoints(int points);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addToScore(int toAdd) {
        this.increaseScore(toAdd);
    }

    @Override
    public void removeFromScore(int toRemove) {
        decreaseScore(toRemove);
    }

    @Override
    public int getScore() {
        return getScorePoints();
    }

    @Override
    public void setScore(int toSet) {
        setScorePoints(toSet);
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
        // TODO: 1.9
    }

    @Override
    public void update() {
        // Not all that sure about this method, as I'm not sure this type of functionality exists
    }
}
