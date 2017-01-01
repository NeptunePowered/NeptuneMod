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

import net.canarymod.api.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.entity.ai.attributes.RangedAttribute.class)
public abstract class MixinRangedAttribute extends MixinBaseAttribute implements RangedAttribute {

    @Shadow @Final private double minimumValue;
    @Shadow @Final private double maximumValue;

    @Shadow public abstract net.minecraft.entity.ai.attributes.RangedAttribute shadow$setDescription(String desc);
    @Shadow public abstract double clampValue(double p_111109_1_);

    @Override
    public RangedAttribute setDescription(String description) {
        return (RangedAttribute) shadow$setDescription(description);
    }

    @Override
    public double setValue(double value) {
        return this.clampValue(value);
    }

    @Override
    public double getMaxValue() {
        return this.maximumValue;
    }

    @Override
    public double getMinValue() {
        return this.minimumValue;
    }

}
