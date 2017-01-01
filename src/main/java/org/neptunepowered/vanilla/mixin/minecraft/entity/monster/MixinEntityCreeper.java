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
package org.neptunepowered.vanilla.mixin.minecraft.entity.monster;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.monster.Creeper;
import net.minecraft.entity.monster.EntityCreeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityCreeper.class)
public abstract class MixinEntityCreeper extends MixinEntityMob implements Creeper {

    @Shadow public abstract boolean getPowered();
    @Shadow public abstract int getCreeperState();
    @Shadow public abstract void setCreeperState(int state);

    @Override
    public boolean isCharged() {
        return this.getPowered();
    }

    @Override
    public void setCharged(boolean b) {
        this.dataWatcher.updateObject(17, b ? (byte) 1 : (byte) 0);
    }

    @Override
    public boolean isAgro() {
        return this.getCreeperState() == 1;
    }

    @Override
    public void setAgro(boolean b) {
        this.setCreeperState(b ? 1 : -1);
    }

    @Override
    public void setCanDamageWorld(boolean b) {

    }

    @Override
    public boolean canDamageWorld() {
        return false;
    }

    @Override
    public void setCanDamageEntities(boolean b) {

    }

    @Override
    public boolean canDamageEntities() {
        return false;
    }

    @Override
    public float getPower() {
        return 0;
    }

    @Override
    public void setPower(float v) {

    }

    @Override
    public int getFuse() {
        return 0;
    }

    @Override
    public void setFuse(int i) {

    }

    @Override
    public void increaseFuse(int i) {

    }

    @Override
    public void decreaseFuse(int i) {

    }

    @Override
    public void detonate() {

    }

    @Override
    public String getFqName() {
        return "Creeper";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.CREEPER;
    }

}
