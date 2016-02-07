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
package org.neptunepowered.common.mixin.minecraft.entity.ai.attributes;

import net.canarymod.api.attributes.Attribute;
import net.canarymod.api.attributes.AttributeModifier;
import net.canarymod.api.attributes.ModifiedAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ModifiableAttributeInstance.class)
public abstract class MixinModifiableAttributeInstance implements ModifiedAttribute {

    @Shadow private IAttribute genericAttribute;

    @Shadow
    public abstract net.minecraft.entity.ai.attributes.AttributeModifier shadow$getModifier(UUID uuid);

    @Shadow
    public abstract void applyModifier(net.minecraft.entity.ai.attributes.AttributeModifier modifier);

    @Shadow
    public abstract void removeModifier(net.minecraft.entity.ai.attributes.AttributeModifier modifier);

    @Shadow
    public abstract double getAttributeValue();

    @Override
    public Attribute getAttribute() {
        return (Attribute) genericAttribute;
    }

    @Override
    @Shadow
    public abstract double getBaseValue();

    @Override
    @Shadow
    public abstract void setBaseValue(double value);

    @Override
    public AttributeModifier getModifier(UUID uuid) {
        return (AttributeModifier) shadow$getModifier(uuid);
    }

    @Override
    public void apply(AttributeModifier attributeModifier) {
        applyModifier((net.minecraft.entity.ai.attributes.AttributeModifier) attributeModifier);
    }

    @Override
    public void remove(AttributeModifier attributeModifier) {
        removeModifier((net.minecraft.entity.ai.attributes.AttributeModifier) attributeModifier);
    }

    @Override
    public double getValue() {
        return getAttributeValue();
    }
}
