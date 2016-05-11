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
import net.canarymod.api.ai.AIManager;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAITasks.class)
public abstract class MixinEntityAITasks implements AIManager {

    @Shadow public abstract void addTask(int p_75776_1_, EntityAIBase p_75776_2_);
    @Shadow public abstract void removeTask(EntityAIBase p_85156_1_);

    @Override
    public boolean addTask(int priority, AIBase ai) {
        this.addTask(priority, (EntityAIBase) ai);
        return this.hasTask(ai.getClass());
    }

    @Override
    public boolean removeTask(Class<? extends AIBase> ai) {
        this.removeTask((EntityAIBase) this.getTask(ai));
        return !this.hasTask(ai);
    }

    @Override
    public boolean hasTask(Class<? extends AIBase> ai) {
        return false;
    }

    @Override
    public AIBase getTask(Class<? extends AIBase> ai) {
        return null;
    }
}
