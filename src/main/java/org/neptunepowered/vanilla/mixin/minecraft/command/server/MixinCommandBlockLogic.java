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
package org.neptunepowered.vanilla.mixin.minecraft.command.server;

import net.canarymod.api.CommandBlockLogic;
import net.canarymod.api.world.World;
import net.canarymod.user.Group;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.command.server.CommandBlockLogic.class)
@Implements(@Interface(iface = CommandBlockLogic.class, prefix = "logic$"))
public abstract class MixinCommandBlockLogic implements CommandBlockLogic {

    @Shadow
    public abstract void setCommand(String command);

    @Shadow
    public abstract String getCommand();

    @Shadow
    public abstract void setName(String p_145754_1_);

    @Intrinsic
    public void logic$setCommand(String s) {
        this.setCommand(s);
    }

    @Intrinsic
    public String logic$getCommand() {
        return this.getCommand();
    }

    @Override
    public void activate() {

    }

    @Override
    public World getWorld() {
        return null;
    }

    @Intrinsic
    public void logic$setName(String s) {
        this.setName(s);
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public void setGroup(Group group) {

    }
}
