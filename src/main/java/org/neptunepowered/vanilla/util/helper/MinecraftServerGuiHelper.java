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
package org.neptunepowered.vanilla.util.helper;

import net.canarymod.config.Configuration;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.gui.MinecraftServerGui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public final class MinecraftServerGuiHelper {

    public static JFrame createServerGui(final MinecraftServerGui serverGui, final DedicatedServer serverIn) {
        JFrame jframe = new JFrame("NeptuneVanilla - Lightweight CanaryLib implementation");
        jframe.add(serverGui);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent p_windowClosing_1_) {
                serverIn.initiateShutdown();

                while (!serverIn.isServerStopped()) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException interruptedException) {
                        // Neptune - start
                        if (Configuration.getServerConfig().isDebugMode()) {
                            interruptedException.printStackTrace();
                        }
                        // interruptedException.printStackTrace();
                        // Neptune - end
                    }
                }

                System.exit(0);
            }
        });
        return jframe;
    }
}
