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
package org.neptunepowered.vanilla.mixin.minecraft.tileentity;

import net.canarymod.api.inventory.InventoryType;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.api.world.blocks.Beacon;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityBeacon.class)
public abstract class MixinTileEntityBeacon extends MixinTileEntityLockable implements Beacon {

    @Shadow @Final public static Potion[][] effectsList;

    @Shadow private int levels;
    @Shadow private int primaryEffect;
    @Shadow private int secondaryEffect;

    @Shadow public abstract void setField(int id, int value);

    @Override
    public boolean isValidEffect(PotionEffectType potionEffectType) {
        return this.isValidEffectAtLevels(potionEffectType, effectsList.length);
    }

    @Override
    public boolean isValidEffectAtLevels(PotionEffectType effect, int levels) {
        final Potion[][] potions = effectsList;

        for (int i = 0; i < levels && i < potions.length; i++) {
            for (int j = 0; j < potions[i].length; j++) {
                if (potions[i][j].getId() == effect.getID()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public PotionEffectType getPrimaryEffect() {
        return PotionEffectType.fromId(this.primaryEffect);
    }

    @Override
    public void setPrimaryEffect(PotionEffectType potionEffectType) {
        this.setField(1, potionEffectType.getID());
    }

    @Override
    public void setPrimaryEffectDirectly(PotionEffectType potionEffectType) {
        this.primaryEffect = potionEffectType.getID();
    }

    @Override
    public PotionEffectType getSecondaryEffect() {
        return PotionEffectType.fromId(this.secondaryEffect);
    }

    @Override
    public void setSecondaryEffect(PotionEffectType potionEffectType) {
        this.setField(2, potionEffectType.getID());
    }

    @Override
    public void setSecondaryEffectDirectly(PotionEffectType potionEffectType) {
        this.secondaryEffect = potionEffectType.getID();
    }

    @Override
    public int getLevels() {
        return this.levels;
    }

    @Override
    public void setLevels(int i) {
        this.levels = i;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.BEACON;
    }

}
