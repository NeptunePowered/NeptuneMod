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
package org.neptunepowered.vanilla.mixin.minecraft.server.gui;

import net.canarymod.api.gui.GUIControl;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.gui.MinecraftServerGui;
import org.neptunepowered.vanilla.util.helper.MinecraftServerGuiHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.swing.JFrame;

@Mixin(MinecraftServerGui.class)
public class MixinMinecraftServerGui implements GUIControl {

    private JFrame jframe;

    @Shadow private DedicatedServer server;

    @Override
    public void closeWindow() {
        if (this.jframe != null) {
            this.jframe.dispose();
        }
        this.jframe = null;
    }

    @Override
    public void start() {
        this.jframe = MinecraftServerGuiHelper.createServerGui((MinecraftServerGui) (Object) this, this.server);
    }

    @Override
    public String getName() {
        return this.jframe.getTitle();
    }

    /**
     * Replaced by {@link GUIControl#start()}
     *
     * @param serverIn The incoming {@link DedicatedServer}
     * @author jamierocks
     */
    @Overwrite
    public static void createServerGui(final DedicatedServer serverIn) {
    }
}
