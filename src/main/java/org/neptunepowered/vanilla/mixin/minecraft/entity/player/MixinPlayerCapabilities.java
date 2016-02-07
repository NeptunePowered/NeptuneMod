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
package org.neptunepowered.vanilla.mixin.minecraft.entity.player;

import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.minecraft.entity.player.PlayerCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerCapabilities.class)
public abstract class MixinPlayerCapabilities implements HumanCapabilities {

    @Shadow public boolean disableDamage;
    @Shadow public boolean isFlying;
    @Shadow public boolean allowFlying;
    @Shadow public boolean isCreativeMode;
    @Shadow private float flySpeed;
    @Shadow private float walkSpeed;

    @Override
    public boolean isInvulnerable() {
        return this.disableDamage;
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        this.disableDamage = invulnerable;
    }

    @Override
    public boolean isFlying() {
        return this.isFlying;
    }

    @Override
    public void setFlying(boolean flying) {
        this.isFlying = flying;
    }

    @Override
    public boolean mayFly() {
        return this.allowFlying;
    }

    @Override
    public void setMayFly(boolean mayfly) {
        this.allowFlying = mayfly;
    }

    @Override
    public boolean instantBuild() {
        return this.isCreativeMode;
    }

    @Override
    public void setInstantBuild(boolean instant) {
        this.isCreativeMode = instant;
    }

    @Override
    @Shadow
    public abstract float getFlySpeed();

    @Override
    public void setFlySpeed(float speed) {
        this.flySpeed = speed;
    }

    @Override
    @Shadow
    public abstract float getWalkSpeed();

    @Override
    public void setWalkSpeed(float speed) {
        this.walkSpeed = speed;
    }
}
