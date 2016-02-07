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

import net.canarymod.api.attributes.RangedAttribute;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.entity.ai.attributes.RangedAttribute.class)
@Implements(@Interface(iface = RangedAttribute.class, prefix = "atr$"))
public abstract class MixinRangedAttribute extends BaseAttribute {

    @Shadow private double minimumValue;
    @Shadow private double maximumValue;

    protected MixinRangedAttribute(IAttribute p_i45892_1_, String unlocalizedNameIn,
            double defaultValueIn) {
        super(p_i45892_1_, unlocalizedNameIn, defaultValueIn);
    }

    @Shadow
    public abstract net.minecraft.entity.ai.attributes.RangedAttribute setDescription(String descriptionIn);

    @Override
    @Shadow
    public abstract double clampValue(double p_111109_1_);

    public RangedAttribute atr$setDescription(String description) {
        return (RangedAttribute) setDescription(description);
    }

    public double atr$setValue(double value) {
        return clampValue(value);
    }

    public double atr$getMaxValue() {
        return maximumValue;
    }

    public double atr$getMinValue() {
        return minimumValue;
    }
}
