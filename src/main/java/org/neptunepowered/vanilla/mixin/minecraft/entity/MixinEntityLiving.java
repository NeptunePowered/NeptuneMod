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
package org.neptunepowered.vanilla.mixin.minecraft.entity;

import net.canarymod.api.PathFinder;
import net.canarymod.api.ai.AIManager;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.inventory.Item;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase implements net.canarymod.api.entity.living.EntityLiving {

    @Shadow protected float[] equipmentDropChances;
    @Shadow private boolean persistenceRequired;
    @Shadow private ItemStack[] equipment;
    @Shadow protected EntityAITasks tasks;
    @Shadow protected EntityAITasks targetTasks;

    @Shadow
    public abstract EntityLivingBase shadow$getAttackTarget();

    @Shadow
    public abstract void setAttackTarget(EntityLivingBase entitylivingbaseIn);

    @Shadow
    public abstract EntityMoveHelper getMoveHelper();

    @Shadow
    public abstract ItemStack getHeldItem();

    @Shadow
    public abstract ItemStack[] getInventory();

    @Shadow
    public abstract ItemStack shadow$getEquipmentInSlot(int slotIn);

    @Shadow
    public abstract PathNavigate getNavigator();

    @Override
    public void moveEntityToXYZ(double x, double y, double z, float speed) {
        this.lookAt(x, y, z);
        this.getMoveHelper().setMoveTo(x, y, z, speed);
    }

    @Override
    @Shadow
    public abstract void playLivingSound();

    @Override
    public boolean spawn(net.canarymod.api.entity.living.EntityLiving... riders) {
        return false;
    }

    @Override
    public LivingBase getAttackTarget() {
        return (LivingBase) this.shadow$getAttackTarget();
    }

    @Override
    public void setAttackTarget(LivingBase livingbase) {
        this.setAttackTarget((EntityLivingBase) livingbase);
    }

    @Override
    public Item getItemInHand() {
        return (Item) this.getHeldItem();
    }

    @Override
    public Item[] getEquipment() {
        return (Item[]) this.getInventory();
    }

    @Override
    public Item getEquipmentInSlot(int slot) {
        return (Item) this.shadow$getEquipmentInSlot(slot);
    }

    @Override
    public void setEquipment(Item[] items) {
        this.equipment = (ItemStack[]) items;
    }

    @Override
    public void setEquipment(Item item, int slot) {
        this.equipment[slot] = (ItemStack) item;
    }

    @Override
    public float getDropChance(int slot) {
        return this.equipmentDropChances[slot];
    }

    @Override
    public void setDropChance(int slot, float chance) {
        this.equipmentDropChances[slot] = chance;
    }

    @Override
    @Shadow
    public abstract boolean canPickUpLoot();

    @Override
    @Shadow
    public abstract void setCanPickUpLoot(boolean loot);

    @Override
    public boolean isPersistenceRequired() {
        return this.persistenceRequired;
    }

    @Override
    public PathFinder getPathFinder() {
        return (PathFinder) this.getNavigator();
    }

    @Override
    public AIManager getAITaskManager() {
        return (AIManager) this.tasks;
    }

    @Override
    public AIManager getAITargetTaskManager() {
        return (AIManager) this.targetTasks;
    }

    @Override
    public boolean canAttackEntity(EntityType type) {
        return false;
    }
}
