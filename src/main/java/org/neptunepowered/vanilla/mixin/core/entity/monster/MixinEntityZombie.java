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
package org.neptunepowered.vanilla.mixin.core.entity.monster;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.monster.Zombie;
import net.minecraft.entity.monster.EntityZombie;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityZombie.class)
@Implements(@Interface(iface = Zombie.class, prefix = "zombie$"))
public abstract class MixinEntityZombie extends MixinEntityMob implements Zombie {

    @Shadow private int conversionTime;

    @Shadow public abstract boolean isVillager();
    @Shadow public abstract void setVillager(boolean villager);
    @Shadow public abstract boolean isChild();
    @Shadow public abstract void setChild(boolean childZombie);
    @Shadow public abstract boolean isConverting();
    @Shadow public abstract void convertToVillager();

    @Intrinsic
    public boolean zombie$isVillager() {
        return this.isVillager();
    }

    @Intrinsic
    public void zombie$setVillager(boolean b) {
        this.setVillager(b);
    }

    @Intrinsic
    public boolean zombie$isChild() {
        return this.isChild();
    }

    @Intrinsic
    public void zombie$setChild(boolean b) {
        this.setChild(b);
    }

    @Override
    public int getConversionTime() {
        return this.conversionTime;
    }

    @Override
    public void setConversionTime(int i) {
        this.conversionTime = i;
    }

    @Intrinsic
    public boolean zombie$isConverting() {
        return this.isConverting();
    }

    @Override
    public void stopConverting() {
        // TODO: implement
    }

    @Intrinsic
    public void zombie$convertToVillager() {
        this.convertToVillager();
    }

    @Override
    public String getFqName() {
        return "Zombie";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ZOMBIE;
    }

}
