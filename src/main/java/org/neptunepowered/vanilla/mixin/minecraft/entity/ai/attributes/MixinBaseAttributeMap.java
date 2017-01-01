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

import com.google.common.collect.Multimap;
import net.canarymod.api.attributes.Attribute;
import net.canarymod.api.attributes.AttributeMap;
import net.canarymod.api.attributes.AttributeModifier;
import net.canarymod.api.attributes.ModifiedAttribute;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

@Mixin(BaseAttributeMap.class)
@Implements(@Interface(iface = AttributeMap.class, prefix = "map$"))
public abstract class MixinBaseAttributeMap implements AttributeMap {

    @Shadow public abstract void applyAttributeModifiers(Multimap p_111147_1_);
    @Shadow public abstract void removeAttributeModifiers(Multimap p_111148_1_);
    @Shadow public abstract Collection<IAttributeInstance> shadow$getAllAttributes();

    @Override
    public ModifiedAttribute getModifiedAttribute(Attribute attribute) {
        return null;
    }

    @Override
    public ModifiedAttribute getModifiedAttributeByName(String name) {
        return null;
    }

    @Override
    public ModifiedAttribute registerAttribute(Attribute attribute) {
        return null;
    }

    @Intrinsic
    public Collection<ModifiedAttribute> map$getAllAttributes() {
        return (Collection) this.shadow$getAllAttributes();
    }

    @Override
    public void addModifier(ModifiedAttribute attribute) {

    }

    @Override
    public void removeModifiers(Multimap<String, AttributeModifier> map) {
        this.removeAttributeModifiers(map);
    }

    @Override
    public void applyModifiers(Multimap<String, AttributeModifier> map) {
        this.applyAttributeModifiers(map);
    }

}
