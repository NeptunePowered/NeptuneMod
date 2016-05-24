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
package org.neptunepowered.vanilla.mixin.minecraft.entity.monster;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.IronGolem;
import net.canarymod.api.world.Village;
import net.minecraft.entity.monster.EntityIronGolem;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityIronGolem.class)
@Implements(@Interface(iface = IronGolem.class, prefix = "golem$"))
public abstract class MixinEntityIronGolem extends MixinEntityGolem implements IronGolem {

    @Shadow private net.minecraft.village.Village villageObj;

    @Shadow
    public abstract boolean isPlayerCreated();

    @Shadow
    public abstract void setPlayerCreated(boolean b);

    @Shadow
    public abstract void setHoldingRose(boolean b);

    @Override
    public Village getVillage() {
        return (Village) this.villageObj;
    }

    @Override
    public void setVillage(Village village) {
        this.villageObj = (net.minecraft.village.Village) village;
    }

    @Intrinsic
    public boolean golem$isPlayerCreated() {
        return this.isPlayerCreated();
    }

    @Intrinsic
    public void golem$setPlayerCreated(boolean b) {
        this.setPlayerCreated(b);
    }

    @Override
    public boolean isHoldingRose() {
        return false;
    }

    @Intrinsic
    public void golem$setHoldingRose(boolean b) {
        this.setHoldingRose(b);
    }

    @Override
    public int getHoldRoseTicks() {
        return 0;
    }

    @Override
    public void setHoldRoseTicks(int i) {

    }

    @Override
    public String getFqName() {
        return "IronGolem";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.IRONGOLEM;
    }
}
