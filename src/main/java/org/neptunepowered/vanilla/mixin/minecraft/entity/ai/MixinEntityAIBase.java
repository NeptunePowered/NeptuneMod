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
package org.neptunepowered.vanilla.mixin.minecraft.entity.ai;

import net.canarymod.api.ai.AIBase;
import net.minecraft.entity.ai.EntityAIBase;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAIBase.class)
@Implements(@Interface(iface = AIBase.class, prefix = "ai$"))
public abstract class MixinEntityAIBase implements AIBase {

    @Shadow public abstract boolean shouldExecute();
    @Shadow public abstract boolean continueExecuting();
    @Shadow public abstract void startExecuting();
    @Shadow public abstract void resetTask();
    @Shadow public abstract void updateTask();
    @Shadow public abstract boolean isInterruptible();

    @Intrinsic
    public boolean ai$shouldExecute() {
        return this.shouldExecute();
    }

    @Intrinsic
    public boolean ai$continueExecuting() {
        return this.continueExecuting();
    }

    @Override
    public boolean isContinuous() {
        return this.isInterruptible();
    }

    @Intrinsic
    public void ai$startExecuting() {
        this.startExecuting();
    }

    @Intrinsic
    public void ai$resetTask() {
        this.resetTask();
    }

    @Intrinsic
    public void ai$updateTask() {
        this.updateTask();
    }

}
