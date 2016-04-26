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
package org.neptunepowered.vanilla.factory;

import com.google.common.base.Predicate;
import net.canarymod.api.ai.AIArrowAttack;
import net.canarymod.api.ai.AIAttackOnCollide;
import net.canarymod.api.ai.AIAvoidEntity;
import net.canarymod.api.ai.AIBeg;
import net.canarymod.api.ai.AIBreakDoor;
import net.canarymod.api.ai.AIControlledByPlayer;
import net.canarymod.api.ai.AICreeperSwell;
import net.canarymod.api.ai.AIDefendVillage;
import net.canarymod.api.ai.AIEatGrass;
import net.canarymod.api.ai.AIFindEntityNearest;
import net.canarymod.api.ai.AIFindEntityNearestPlayer;
import net.canarymod.api.ai.AIFleeSun;
import net.canarymod.api.ai.AIFollowGolem;
import net.canarymod.api.ai.AIFollowOwner;
import net.canarymod.api.ai.AIFollowParent;
import net.canarymod.api.ai.AIHarvestFarmland;
import net.canarymod.api.ai.AIHurtByTarget;
import net.canarymod.api.ai.AILeapAtTarget;
import net.canarymod.api.ai.AILookAtTradePlayer;
import net.canarymod.api.ai.AILookAtVillager;
import net.canarymod.api.ai.AILookIdle;
import net.canarymod.api.ai.AIMate;
import net.canarymod.api.ai.AIMoveIndoors;
import net.canarymod.api.ai.AIMoveThroughVillage;
import net.canarymod.api.ai.AIMoveTowardsRestriction;
import net.canarymod.api.ai.AIMoveTowardsTarget;
import net.canarymod.api.ai.AINearestAttackableTarget;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.EntityLiving;
import net.canarymod.api.entity.living.IronGolem;
import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.animal.Tameable;
import net.canarymod.api.entity.living.animal.Wolf;
import net.canarymod.api.entity.living.humanoid.Villager;
import net.canarymod.api.entity.living.monster.Creeper;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.entity.living.monster.RangedAttackMob;
import net.canarymod.api.factory.AIFactory;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityWolf;

public class NeptuneAIFactory implements AIFactory {

    @Override
    public AIArrowAttack newAIArrowAttack(RangedAttackMob mob, double moveSpeed, int attackTimeModifier,
            int maxRangedAttackTime, int maxAttackDistance) {
        return (AIArrowAttack) new EntityAIArrowAttack(null, moveSpeed, attackTimeModifier, maxRangedAttackTime,
                maxAttackDistance);
    }

    @Override
    public AIAttackOnCollide newAIAttackOnCollide(EntityMob creature, Class<? extends LivingBase> targetClass,
            double moveSpeed, boolean persistant) {
        return (AIAttackOnCollide) new EntityAIAttackOnCollide(
                (EntityCreature) creature, (Class <? extends net.minecraft.entity.Entity>) targetClass,
                moveSpeed, persistant);
    }

    @Override
    public AIAvoidEntity newAIAvoidEntity(EntityMob mob, Predicate predicate, float radius, double farSpeed,
            double nearSpeed) {
        return null; // TODO: ?
    }

    @Override
    public AIBeg newAIBeg(Wolf wolf, float minBegDistance) {
        return (AIBeg) new EntityAIBeg((EntityWolf) wolf, minBegDistance);
    }

    @Override
    public AIBreakDoor newAIBreakDoor(EntityLiving entity) {
        return (AIBreakDoor) new EntityAIBreakDoor((net.minecraft.entity.EntityLiving) entity);
    }

    @Override
    public AIControlledByPlayer newAIControlledByPlayer(EntityLiving entity, float speed) {
        return (AIControlledByPlayer) new EntityAIControlledByPlayer((net.minecraft.entity.EntityLiving) entity, speed);
    }

    @Override
    public AICreeperSwell newAICreeperSwell(Creeper creeper) {
        return (AICreeperSwell) new EntityAICreeperSwell((EntityCreeper) creeper);
    }

    @Override
    public AIDefendVillage newAIDefendVillage(IronGolem ironGolem) {
        return null;
    }

    @Override
    public AIEatGrass newAIEatGrass(EntityLiving entity) {
        return null;
    }

    @Override
    public AIFindEntityNearest newAIFindEntityNearest(EntityLiving entityLiving, Class<? extends Entity> entityClass) {
        return null;
    }

    @Override
    public AIFindEntityNearestPlayer newAIFindEntityNearestPlayer(EntityLiving entityLiving) {
        return null;
    }

    @Override
    public AIFleeSun newAIFleeSun(EntityMob mob, double speed) {
        return null;
    }

    @Override
    public AIFollowGolem newAIFollowGolem(Villager villager) {
        return null;
    }

    @Override
    public AIFollowOwner newAIFollowOwner(Tameable entity, double speed, float minDistance, float maxDistance) {
        return null;
    }

    @Override
    public AIFollowParent newAIFollowParent(EntityAnimal animal, double speed) {
        return null;
    }

    @Override
    public AIHarvestFarmland newAIHarvestFarmland(Villager villager, double speed) {
        return null;
    }

    @Override
    public AIHurtByTarget newAIHurtByTarget(EntityMob entity, boolean callForHelp, Class<? extends Entity>... targets) {
        return null;
    }

    @Override
    public AILeapAtTarget newAILeapAtTarget(EntityLiving entity, float leapMotionY) {
        return null;
    }

    @Override
    public AILookAtTradePlayer newAILookAtTradePlayer(Villager villager) {
        return null;
    }

    @Override
    public AILookAtVillager newAILookAtVillager(IronGolem golem) {
        return null;
    }

    @Override
    public AILookIdle newAILookIdle(EntityLiving entity) {
        return null;
    }

    @Override
    public AIMate newAIMate(EntityAnimal animal, double speed) {
        return null;
    }

    @Override
    public AIMoveIndoors newAIMoveIndoors(EntityMob entity) {
        return null;
    }

    @Override
    public AIMoveThroughVillage newAIMoveThroughVillage(EntityMob entity, double speed, boolean isNoctournal) {
        return null;
    }

    @Override
    public AIMoveTowardsRestriction newAIMoveTowardsRestriction(EntityMob entity, double speed) {
        return null;
    }

    @Override
    public AIMoveTowardsTarget newAIMoveTowardsTarget(EntityMob entity, double speed, float maxDistance) {
        return null;
    }

    @Override
    public AINearestAttackableTarget newAINearestAttackableTarget(EntityMob entity, Class<? extends Entity> target,
            int targetChanve, boolean shouldCheckSight, boolean nearbyOnly) {
        return null;
    }
}
