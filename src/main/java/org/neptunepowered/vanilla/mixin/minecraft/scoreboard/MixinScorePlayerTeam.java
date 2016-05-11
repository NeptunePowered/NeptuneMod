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

import com.google.common.collect.Lists;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.scoreboard.Scoreboard;
import net.canarymod.api.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Set;

@Mixin(ScorePlayerTeam.class)
@Implements(@Interface(iface = Team.class, prefix = "team$"))
public abstract class MixinScorePlayerTeam extends net.minecraft.scoreboard.Team implements Team {

    @Shadow private net.minecraft.scoreboard.Scoreboard theScoreboard;
    @Shadow private Set<String> membershipSet;
    @Shadow private String registeredName;
    @Shadow private boolean canSeeFriendlyInvisibles;
    @Shadow private String teamNameSPT;

    @Shadow public abstract String getColorPrefix();
    @Shadow public abstract void setNamePrefix(String prefix);
    @Shadow public abstract String getColorSuffix();
    @Shadow public abstract void setNameSuffix(String suffix);
    @Shadow public abstract void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles);
    @Shadow public abstract void setTeamName(String name);
    @Shadow public abstract boolean getAllowFriendlyFire();
    @Shadow public abstract void setAllowFriendlyFire(boolean bool);

    @Override
    public String getProtocolName() {
        return this.registeredName;
    }

    @Override
    public String getDisplayName() {
        return this.teamNameSPT;
    }

    @Override
    public void setDisplayName(String name) {
        this.setTeamName(name);
    }

    @Override
    public String getPrefix() {
        return this.getColorPrefix();
    }

    @Override
    public void setPrefix(String prefix) {
        this.setNamePrefix(prefix);
    }

    @Override
    public String getSuffix() {
        return this.getColorSuffix();
    }

    @Override
    public void setSuffix(String suffix) {
        this.setNameSuffix(suffix);
    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public List<String> getPlayerNames() {
        return Lists.newArrayList(this.membershipSet);
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public boolean hasPlayer(Player player) {
        return false;
    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return (Scoreboard) this.theScoreboard;
    }

    @Intrinsic
    public boolean team$getAllowFriendlyFire() {
        return this.getAllowFriendlyFire();
    }

    @Intrinsic
    public void team$setAllowFriendlyFire(boolean bool) {
        this.setAllowFriendlyFire(bool);
    }

    @Override
    public boolean getSeeFriendlyInvisibles() {
        return this.canSeeFriendlyInvisibles;
    }

    @Override
    public void setSeeFriendlyInvisibles(boolean bool) {
        this.setSeeFriendlyInvisiblesEnabled(bool);
    }
}
