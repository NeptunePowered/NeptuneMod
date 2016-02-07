/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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

import net.canarymod.Canary;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.EntityLiving;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.animal.Horse;
import net.canarymod.api.entity.living.humanoid.NonPlayableCharacter;
import net.canarymod.api.entity.living.humanoid.Villager;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.entity.living.monster.Skeleton;
import net.canarymod.api.entity.throwable.EntityThrowable;
import net.canarymod.api.entity.vehicle.Vehicle;
import net.canarymod.api.factory.EntityFactory;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;

public class NeptuneEntityFactory implements EntityFactory {

    @Override
    public Entity newEntity(String name) {
        return null;
    }

    @Override
    public Entity newEntity(String name, World world) {
        return null;
    }

    @Override
    public Entity newEntity(String name, Location location) {
        return null;
    }

    @Override
    public Entity newEntity(EntityType type) {
        return this.newEntity(type, Canary.getServer().getDefaultWorld());
    }

    @Override
    public Entity newEntity(EntityType type, World world) {
        switch (type) {
            case ARMORSTAND:
                return (Entity) new EntityArmorStand((net.minecraft.world.World) world);
            case ARROW:
                return (Entity) new EntityArrow((net.minecraft.world.World) world);
            case BAT:
                return (Entity) new EntityBat((net.minecraft.world.World) world);
            case BLACKSMITH:
                Villager blacksmith = (Villager) new EntityVillager((net.minecraft.world.World) world);
                blacksmith.setProfession(Villager.Profession.BLACKSMITH);
                return blacksmith;
            case BLAZE:
                return (Entity) new EntityBlaze((net.minecraft.world.World) world);
            case BUTCHER:
                Villager butcher = (Villager) new EntityVillager((net.minecraft.world.World) world);
                butcher.setProfession(Villager.Profession.BUTCHER);
                return butcher;
            case BOAT:
                return (Entity) new EntityBoat((net.minecraft.world.World) world);
            case CAVESPIDER:
                return (Entity) new EntityCaveSpider((net.minecraft.world.World) world);
            case CHESTMINECART:
                return (Entity) new EntityMinecartChest((net.minecraft.world.World) world);
            case CHICKEN:
                return (Entity) new EntityChicken((net.minecraft.world.World) world);
            case CHICKENEGG:
                return (Entity) new EntityEgg((net.minecraft.world.World) world);
            case COMMANDBLOCKMINECART:
                return (Entity) new EntityMinecartCommandBlock((net.minecraft.world.World) world);
            case COW:
                return (Entity) new EntityCow((net.minecraft.world.World) world);
            case CREEPER:
                return (Entity) new EntityCreeper((net.minecraft.world.World) world);
            case DONKEY:
                Horse donkey = (Horse) new EntityHorse((net.minecraft.world.World) world);
                donkey.setType(Horse.HorseType.DONKEY);
                return donkey;
            case EMPTYMINECART:
                return (Entity) new EntityMinecartEmpty((net.minecraft.world.World) world);
            case ENDERCRYSTAL:
                return (Entity) new EntityEnderCrystal((net.minecraft.world.World) world);
            case ENDERDRAGON:
                return (Entity) new EntityDragon((net.minecraft.world.World) world);
            case ENDEREYE:
                return (Entity) new EntityEnderEye((net.minecraft.world.World) world);
            case ENDERMAN:
                return (Entity) new EntityEnderman((net.minecraft.world.World) world);
            case ENDERMITE:
                return (Entity) new EntityEndermite((net.minecraft.world.World) world);
            case ENDERPEARL:
                // TODO:
                break;
            case ENTITYITEM:
                return (Entity) new EntityItem((net.minecraft.world.World) world);
            case ENTITYPOTION:
                return (Entity) new EntityPotion((net.minecraft.world.World) world);
            case FALLINGBLOCK:
                return (Entity) new EntityFallingBlock((net.minecraft.world.World) world);
            case FARMER:
                Villager farmer = (Villager) new EntityVillager((net.minecraft.world.World) world);
                farmer.setProfession(Villager.Profession.FARMER);
                return farmer;
            case FIREWORKROCKET:
                return (Entity) new EntityFireworkRocket((net.minecraft.world.World) world);
            case FISHHOOK:
                return (Entity) new EntityFishHook((net.minecraft.world.World) world);
            case FURNACEMINECART:
                return (Entity) new EntityMinecartFurnace((net.minecraft.world.World) world);
            case GUARDIAN:
                return (Entity) new EntityGuardian((net.minecraft.world.World) world);
            case GHAST:
                return (Entity) new EntityGhast((net.minecraft.world.World) world);
            case GIANTZOMBIE:
                return (Entity) new EntityGiantZombie((net.minecraft.world.World) world);
            case HOPPERMINECART:
                return (Entity) new EntityMinecartHopper((net.minecraft.world.World) world);
            case HORSE:
                return (Entity) new EntityHorse((net.minecraft.world.World) world);
            case IRONGOLEM:
                return (Entity) new EntityIronGolem((net.minecraft.world.World) world);
            case ITEMFRAME:
                return (Entity) new EntityItemFrame((net.minecraft.world.World) world);
            case LARGEFIREBALL:
                return (Entity) new EntityLargeFireball((net.minecraft.world.World) world);
            case LEASHKNOT:
                return (Entity) new EntityLeashKnot((net.minecraft.world.World) world);
            case LIBRARIAN:
                Villager librarian = (Villager) new EntityVillager((net.minecraft.world.World) world);
                librarian.setProfession(Villager.Profession.LIBRARIAN);
                return librarian;
            case LIGHTNINGBOLT:
                return (Entity) new EntityLightningBolt((net.minecraft.world.World) world, 0, 0, 0);
            case MAGMACUBE:
                return (Entity) new EntityMagmaCube((net.minecraft.world.World) world);
            case MOBSPAWNERMINECART:
                return (Entity) new EntityMinecartMobSpawner((net.minecraft.world.World) world);
            case MOOSHROOM:
                return (Entity) new EntityMooshroom((net.minecraft.world.World) world);
            case MULE:
                Horse mule = (Horse) new EntityHorse((net.minecraft.world.World) world);
                mule.setType(Horse.HorseType.MULE);
                return mule;
            case OCELOT:
                return (Entity) new EntityOcelot((net.minecraft.world.World) world);
            case PAINTING:
                return (Entity) new EntityPainting((net.minecraft.world.World) world);
            case PIG:
                return (Entity) new EntityPig((net.minecraft.world.World) world);
            case PIGZOMBIE:
                return (Entity) new EntityPigZombie((net.minecraft.world.World) world);
            case POTION:
               return (Entity) new EntityPotion((net.minecraft.world.World) world);
            case PRIEST:
                Villager priest = (Villager) new EntityVillager((net.minecraft.world.World) world);
                priest.setProfession(Villager.Profession.PRIEST);
                return priest;
            case SHEEP:
                return (Entity) new EntitySheep((net.minecraft.world.World) world);
            case SILVERFISH:
                return (Entity) new EntitySilverfish((net.minecraft.world.World) world);
            case SKELETON:
                return (Entity) new EntitySkeleton((net.minecraft.world.World) world);
            case SKELETONHORSE:
                Horse skeletonHorse = (Horse) new EntityHorse((net.minecraft.world.World) world);
                skeletonHorse.setType(Horse.HorseType.SKELETON);
                return skeletonHorse;
            case SLIME:
                return (Entity) new EntitySlime((net.minecraft.world.World) world);
            case SMALLFIREBALL:
                return (Entity) new EntitySmallFireball((net.minecraft.world.World) world);
            case SNOWBALL:
                return (Entity) new EntitySnowball((net.minecraft.world.World) world);
            case SNOWMAN:
                return (Entity) new EntitySnowman((net.minecraft.world.World) world);
            case SPIDER:
                return (Entity) new EntitySpider((net.minecraft.world.World) world);
            case SQUID:
                return (Entity) new EntitySquid((net.minecraft.world.World) world);
            case RABBIT:
                return (Entity) new EntityRabbit((net.minecraft.world.World) world);
            case TNTMINECART:
                return (Entity) new EntityMinecartTNT((net.minecraft.world.World) world);
            case TNTPRIMED:
                return (Entity) new EntityTNTPrimed((net.minecraft.world.World) world);
            case VILLAGER:
                return (Entity) new EntityVillager((net.minecraft.world.World) world);
            case WITCH:
                return (Entity) new EntityWitch((net.minecraft.world.World) world);
            case WITHER:
                return (Entity) new EntityWither((net.minecraft.world.World) world);
            case WITHERSKELETON:
                Skeleton witherSkeleton = (Skeleton) new EntitySkeleton((net.minecraft.world.World) world);
                witherSkeleton.setIsWitherSkeleton(true);
                return witherSkeleton;
            case WITHERSKULL:
                return (Entity) new EntityWitherSkull((net.minecraft.world.World) world);
            case WOLF:
                return (Entity) new EntityWolf((net.minecraft.world.World) world);
            case XPBOTTLE:
                return (Entity) new EntityExpBottle((net.minecraft.world.World) world);
            case XPORB:
                return (Entity) new EntityXPOrb((net.minecraft.world.World) world);
            case ZOMBIE:
                return (Entity) new EntityZombie((net.minecraft.world.World) world);
            case ZOMBIEHORSE:
                return (Entity) new EntityZombie((net.minecraft.world.World) world);
        }
        return null;
    }

    @Override
    public Entity newEntity(EntityType type, Location location) {
        Entity entity = this.newEntity(type, location.getWorld());
        entity.setX(location.getX());
        entity.setY(location.getY());
        entity.setZ(location.getZ());
        return entity;
    }

    @Override
    public EntityThrowable newThrowable(String name) {
        return null;
    }

    @Override
    public EntityThrowable newThrowable(String name, World world) {
        return null;
    }

    @Override
    public EntityThrowable newThrowable(String name, Location location) {
        return null;
    }

    @Override
    public EntityThrowable newThrowable(EntityType type) {
        return null;
    }

    @Override
    public EntityThrowable newThrowable(EntityType type, World world) {
        return null;
    }

    @Override
    public EntityThrowable newThrowable(EntityType type, Location location) {
        return null;
    }

    @Override
    public Vehicle newVehicle(String name) {
        return null;
    }

    @Override
    public Vehicle newVehicle(String name, World world) {
        return null;
    }

    @Override
    public Vehicle newVehicle(String name, Location location) {
        return null;
    }

    @Override
    public Vehicle newVehicle(EntityType type) {
        return null;
    }

    @Override
    public Vehicle newVehicle(EntityType type, World world) {
        return null;
    }

    @Override
    public Vehicle newVehicle(EntityType type, Location location) {
        return null;
    }

    @Override
    public EntityLiving newEntityLiving(String name) {
        return null;
    }

    @Override
    public EntityLiving newEntityLiving(String name, World world) {
        return null;
    }

    @Override
    public EntityLiving newEntityLiving(String name, Location location) {
        return null;
    }

    @Override
    public EntityLiving newEntityLiving(EntityType type) {
        return null;
    }

    @Override
    public EntityLiving newEntityLiving(EntityType type, World world) {
        return null;
    }

    @Override
    public EntityLiving newEntityLiving(EntityType type, Location location) {
        return null;
    }

    @Override
    public EntityAnimal newEntityAnimal(String name) {
        return null;
    }

    @Override
    public EntityAnimal newEntityAnimal(String name, World world) {
        return null;
    }

    @Override
    public EntityAnimal newEntityAnimal(String name, Location location) {
        return null;
    }

    @Override
    public EntityAnimal newEntityAnimal(EntityType type) {
        return null;
    }

    @Override
    public EntityAnimal newEntityAnimal(EntityType type, World world) {
        return null;
    }

    @Override
    public EntityAnimal newEntityAnimal(EntityType type, Location location) {
        return null;
    }

    @Override
    public EntityMob newEntityMob(String name) {
        return null;
    }

    @Override
    public EntityMob newEntityMob(String name, World world) {
        return null;
    }

    @Override
    public EntityMob newEntityMob(String name, Location location) {
        return null;
    }

    @Override
    public EntityMob newEntityMob(EntityType type) {
        return null;
    }

    @Override
    public EntityMob newEntityMob(EntityType type, World world) {
        return null;
    }

    @Override
    public EntityMob newEntityMob(EntityType type, Location location) {
        return null;
    }

    @Override
    public NonPlayableCharacter newNPC(String name, Location location) {
        return null;
    }
}
