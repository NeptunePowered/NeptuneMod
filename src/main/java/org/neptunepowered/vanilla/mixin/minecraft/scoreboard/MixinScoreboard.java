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

import com.google.common.collect.Lists;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.scoreboard.Score;
import net.canarymod.api.scoreboard.ScoreObjective;
import net.canarymod.api.scoreboard.ScoreObjectiveCriteria;
import net.canarymod.api.scoreboard.ScorePosition;
import net.canarymod.api.scoreboard.Team;
import net.canarymod.api.world.World;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(Scoreboard.class)
public abstract class MixinScoreboard implements net.canarymod.api.scoreboard.Scoreboard {

    @Shadow @Final private Map<String, ScorePlayerTeam> teams;

    @Shadow public abstract net.minecraft.scoreboard.ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria);
    @Shadow public abstract net.minecraft.scoreboard.ScoreObjective getObjective(String name);
    @Shadow public abstract void removeObjective(net.minecraft.scoreboard.ScoreObjective p_96519_1_);
    @Shadow public abstract Collection<net.minecraft.scoreboard.ScoreObjective> shadow$getScoreObjectives();
    @Shadow public abstract ScorePlayerTeam shadow$getTeam(String p_96508_1_);

    @Override
    public List<ScoreObjective> getScoreObjectives() {
        final List<ScoreObjective> scoreObjectives = Lists.newArrayList();

        for (net.minecraft.scoreboard.ScoreObjective objective : this.shadow$getScoreObjectives()) {
            scoreObjectives.add((ScoreObjective) objective);
        }

        return scoreObjectives;
    }

    @Override
    public ScoreObjective addScoreObjective(String name) {
        return null;
    }

    @Override
    public ScoreObjective addScoreObjective(String name, ScoreObjectiveCriteria criteria) {
        return (ScoreObjective) this.addScoreObjective(name, (IScoreObjectiveCriteria) criteria);
    }

    @Override
    public void removeScoreObjective(ScoreObjective objective) {
        this.removeObjective((net.minecraft.scoreboard.ScoreObjective) objective);
    }

    @Override
    public void removeScoreObjective(String name) {

    }

    @Override
    public ScoreObjective getScoreObjective(String name) {
        return (ScoreObjective) this.getObjective(name);
    }

    @Override
    public List<Team> getTeams() {
        return (List) Lists.newArrayList(this.teams.values());
    }

    @Override
    public void addTeam(Team team) {

    }

    @Override
    public void removeTeam(Team team) {

    }

    @Override
    public void removeTeam(String name) {

    }

    @Override
    public Score getScore(Player player, ScoreObjective scoreObjective) {
        return null;
    }

    @Override
    public Score getScore(String name, ScoreObjective scoreObjective) {
        return null;
    }

    @Override
    public List<Score> getScores(ScoreObjective scoreObjective) {
        return null;
    }

    @Override
    public List<Score> getAllScores() {
        return null;
    }

    @Override
    public void setScoreboardPosition(ScorePosition type, ScoreObjective objective) {

    }

    @Override
    public void setScoreboardPosition(ScorePosition type, ScoreObjective objective, Player player) {

    }

    @Override
    public void setScoreboardPosition(ScorePosition type, ScoreObjective objective, World world) {

    }

    @Override
    public void clearScoreboardPosition(ScorePosition type) {

    }

    @Override
    public void clearScoreboardPosition(ScorePosition type, Player player) {

    }

    @Override
    public String getSaveName() {
        return null;
    }

    @Override
    public void removeScore(String name) {

    }

    @Override
    public void removeScore(String name, ScoreObjective objective) {

    }

    @Override
    public Team getTeam(String name) {
        return (Team) this.shadow$getTeam(name);
    }

    @Override
    public Team addTeam(String name) throws IllegalArgumentException {
        return null;
    }

}
