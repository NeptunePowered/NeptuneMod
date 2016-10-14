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
package org.neptunepowered.vanilla.mixin.minecraft.entity.passive;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.animal.Horse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.AnimalChest;
import org.neptunepowered.vanilla.interfaces.minecraft.inventory.IMixinAnimalChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHorse.class)
public abstract class MixinEntityHorse extends MixinEntityAnimal {

    @Shadow private AnimalChest horseChest;

    @Shadow public abstract int getHorseType();

    @Inject(method = "initHorseChest", at = @At("RETURN"))
    public void onInitHorseChest(CallbackInfo ci) {
        ((IMixinAnimalChest) this.horseChest).setOwner((EntityHorse) (Object) this);
    }

    @Override
    public String getFqName() {
        return "Horse";
    }

    @Override
    public EntityType getEntityType() {
        switch (Horse.HorseType.values()[this.getHorseType()]) {
            case DONKEY:
                return EntityType.DONKEY;
            case MULE:
                return EntityType.MULE;
            case ZOMBIE:
                return EntityType.ZOMBIEHORSE;
            case SKELETON:
                return EntityType.SKELETONHORSE;
            case HORSE:
            default:
                return EntityType.HORSE;
        }
    }

}
