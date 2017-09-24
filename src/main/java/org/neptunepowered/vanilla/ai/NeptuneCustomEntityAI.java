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

import net.canarymod.Canary;
import net.canarymod.api.ai.AIBase;
import net.minecraft.entity.ai.EntityAIBase;

public class NeptuneCustomEntityAI extends EntityAIBase {

    private final AIBase handle;

    public NeptuneCustomEntityAI(AIBase handle) {
        this.handle = handle;
    }

    @Override
    public boolean shouldExecute() {
        try {
            return this.handle.shouldExecute();
        } catch (final Exception ex) {
            Canary.log.error("Exception in " + this.handle.getClass().getName() +
                    " while executing #shouldExecute()", ex);
            return false;
        }
    }

    @Override
    public boolean continueExecuting() {
        try {
            return this.handle.continueExecuting();
        } catch (final Exception ex) {
            Canary.log.error("Exception in " + this.handle.getClass().getName() +
                    " while executing #continueExecuting()", ex);
            return false;
        }
    }

    @Override
    public boolean isInterruptible() {
        try {
            return this.handle.isContinuous();
        } catch (final Exception ex) {
            Canary.log.error("Exception in " + this.handle.getClass().getName() +
                    " while executing #isContinuous()", ex);
            return false;
        }
    }

    @Override
    public void startExecuting() {
        try {
            this.handle.startExecuting();
        } catch (final Exception ex) {
            Canary.log.error("Exception in " + this.handle.getClass().getName() +
                    " while executing #startExecuting()", ex);
        }
    }

    @Override
    public void resetTask() {
        try {
            this.handle.resetTask();
        } catch (final Exception ex) {
            Canary.log.error("Exception in " + this.handle.getClass().getName() +
                    " while executing #resetTask()", ex);
        }
    }

    @Override
    public void updateTask() {
        try {
            this.handle.updateTask();
        } catch (final Exception ex) {
            Canary.log.error("Exception in " + this.handle.getClass().getName() +
                    " while executing #updateTask()", ex);
        }
    }

}
