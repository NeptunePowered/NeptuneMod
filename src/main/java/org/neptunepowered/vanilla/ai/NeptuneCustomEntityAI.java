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
package org.neptunepowered.vanilla.ai;

import net.canarymod.api.ai.AIBase;
import net.minecraft.entity.ai.EntityAIBase;

public class NeptuneCustomEntityAI extends EntityAIBase {

    private final AIBase handle;

    public NeptuneCustomEntityAI(AIBase handle) {
        this.handle = handle;
    }

    @Override
    public boolean shouldExecute() {
        return this.handle.shouldExecute();
    }

    @Override
    public boolean continueExecuting() {
        return this.handle.continueExecuting();
    }

    @Override
    public boolean isInterruptible() {
        return this.handle.isContinuous();
    }

    @Override
    public void startExecuting() {
        this.handle.startExecuting();
    }

    @Override
    public void resetTask() {
        this.handle.resetTask();
    }

    @Override
    public void updateTask() {
        this.handle.updateTask();
    }

}
