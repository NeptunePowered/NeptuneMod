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

import net.canarymod.api.DamageType;
import net.canarymod.api.attributes.AttributeMap;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.potion.Potion;
import net.canarymod.api.potion.PotionEffect;
import net.canarymod.api.potion.PotionEffectType;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.entity.EntityDeathHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityLivingBase.class)
@Implements(@Interface(iface = LivingBase.class, prefix = "livingbase$"))
public abstract class MixinEntityLivingBase extends MixinEntity implements LivingBase {

    @Shadow public int deathTime;
    @Shadow protected int entityAge;
    @Shadow protected int scoreValue;

    @Shadow
    public abstract void heal(float healAmount);

    @Shadow
    public abstract float shadow$getMaxHealth();

    @Shadow
    public abstract IAttributeInstance getEntityAttribute(IAttribute attribute);

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract int getAge();

    @Shadow
    protected abstract void shadow$kill();

    @Shadow
    public abstract CombatTracker getCombatTracker();

    @Shadow
    public abstract EntityLivingBase getAttackingEntity();

    @Intrinsic
    public float livingbase$getHealth() {
        return this.getHealth();
    }

    @Intrinsic
    public void livingbase$setHealth(float health) {
        this.setHealth(health);
    }

    @Intrinsic
    public int livingbase$getAge() {
        return this.getAge();
    }

    @Intrinsic
    public void livingbase$kill() {
        this.shadow$kill();
    }

    @Inject(method = "onDeath", at = @At("INVOKE"))
    public void onEntityDeath(DamageSource source, CallbackInfo ci) {
        new EntityDeathHook(this, (net.canarymod.api.DamageSource) source);
    }

    @Override
    public void increaseHealth(float health) {
        this.heal(health);
    }

    @Override
    public double getMaxHealth() {
        return this.shadow$getMaxHealth();
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
    }

    @Override
    public boolean canSee(LivingBase entity) {
        return false;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTime;
    }

    @Override
    public void setDeathTicks(int ticks) {
        this.deathTime = ticks;
    }

    @Override
    public int getInvulnerabilityTicks() {
        return 0;
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {

    }

    @Override
    public void setAge(int age) {
        this.entityAge = age;
    }

    @Override
    public void dealDamage(DamageType type, float damage) {

    }

    @Override
    public void knockBack(double xForce, double zForce) {

    }

    @Override
    public void addPotionEffect(PotionEffect effect) {

    }

    @Override
    public void addPotionEffect(PotionEffectType type, int duration, int amplifier) {

    }

    @Override
    public void removePotionEffect(PotionEffectType type) {

    }

    @Override
    public void removeAllPotionEffects() {

    }

    @Override
    public boolean isPotionActive(Potion potion) {
        return false;
    }

    @Override
    public PotionEffect getActivePotionEffect(Potion potion) {
        return null;
    }

    @Override
    public List<PotionEffect> getAllActivePotionEffects() {
        return null;
    }

    @Override
    public LivingBase getRevengeTarget() {
        return null;
    }

    @Override
    public void setRevengeTarget(LivingBase target) {

    }

    @Override
    public LivingBase getLastAssailant() {
        return null;
    }

    @Override
    public void setLastAssailant(LivingBase entity) {

    }

    @Override
    public void lookAt(double x, double y, double z) {

    }

    @Override
    public void lookAt(Location location) {

    }

    @Override
    public void lookAt(Entity entity) {

    }

    @Override
    public int getArrowCountInEntity() {
        return 0;
    }

    @Override
    public void setArrowCountInEntity(int arrows) {

    }

    @Override
    public void swingArm() {

    }

    @Override
    public void attackEntity(LivingBase target, float damage) {

    }

    @Override
    public float getHeadRotation() {
        return 0;
    }

    @Override
    public void setHeadRotation(float rot) {

    }

    @Override
    public AttributeMap getAttributeMap() {
        return null;
    }

    @Override
    public Entity getTargetLookingAt() {
        return null;
    }

    @Override
    public Entity getTargetLookingAt(int searchRadius) {
        return null;
    }

    @Override
    public boolean lootDrop() {
        return false;
    }

    @Override
    public void setLootDrop(boolean lootDrop) {

    }

    @Override
    public boolean xpDrop() {
        return false;
    }

    @Override
    public void setXPDrop(boolean xpDrop) {

    }

}
