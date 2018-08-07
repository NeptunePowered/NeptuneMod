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
package org.neptunepowered.vanilla.factory;

import com.google.common.collect.Maps;
import net.canarymod.api.attributes.Attribute;
import net.canarymod.api.attributes.AttributeModifier;
import net.canarymod.api.factory.AttributeFactory;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;

import java.util.Map;
import java.util.UUID;

public class NeptuneAttributeFactory implements AttributeFactory {

    private static Map<String, IAttribute> map = Maps.newHashMap();

    static {
        registerAttribute(SharedMonsterAttributes.maxHealth);
        registerAttribute(SharedMonsterAttributes.followRange);
        registerAttribute(SharedMonsterAttributes.knockbackResistance);
        registerAttribute(SharedMonsterAttributes.movementSpeed);
        registerAttribute(SharedMonsterAttributes.attackDamage);

        registerAttribute(EntityZombie.reinforcementChance);

        registerAttribute(EntityHorse.horseJumpStrength);
    }

    private static void registerAttribute(IAttribute attribute) {
        map.put(attribute.getAttributeUnlocalizedName(), attribute);
    }

    protected NeptuneAttributeFactory() {}

    @Override
    public Attribute getGenericAttribute(String nativeName) {
        return (Attribute) map.get(nativeName);
    }

    @Override
    public AttributeModifier createModifier(String name, double value, int operation) {
        return (AttributeModifier) new net.minecraft.entity.ai.attributes.AttributeModifier(name, value, operation);
    }

    @Override
    public AttributeModifier createModifier(UUID id, String name, double value, int operation) {
        return (AttributeModifier) new net.minecraft.entity.ai.attributes.AttributeModifier(id, name, value, operation);
    }

}
