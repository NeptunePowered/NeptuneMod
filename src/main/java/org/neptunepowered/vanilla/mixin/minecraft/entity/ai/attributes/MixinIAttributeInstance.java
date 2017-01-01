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
package org.neptunepowered.vanilla.mixin.minecraft.entity.ai.attributes;

import net.canarymod.api.attributes.Attribute;
import net.canarymod.api.attributes.AttributeModifier;
import net.canarymod.api.attributes.ModifiedAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(IAttributeInstance.class)
@Implements(@Interface(iface = ModifiedAttribute.class, prefix = "modifier$"))
public interface MixinIAttributeInstance extends IAttributeInstance {

    @Intrinsic
    default Attribute modifier$getAttribute() {
        return (Attribute) this.getAttribute();
    }

    @Intrinsic
    default double modifier$getBaseValue() {
        return this.getBaseValue();
    }

    @Intrinsic
    default void modifier$setBaseValue(double var1) {
        this.setBaseValue(var1);
    }

    @Intrinsic
    default AttributeModifier modifier$getModifier(UUID var1) {
        return (AttributeModifier) this.getModifier(var1);
    }

    default void modifier$apply(AttributeModifier var1) {
        this.applyModifier((net.minecraft.entity.ai.attributes.AttributeModifier) var1);
    }

    default void modifier$remove(AttributeModifier var1) {
        this.removeModifier((net.minecraft.entity.ai.attributes.AttributeModifier) var1);
    }

    default double modifier$getValue() {
        return this.getAttributeValue();
    }

}
