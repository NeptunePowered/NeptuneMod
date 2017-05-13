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
package org.neptunepowered.vanilla.mixin.api;

import net.canarymod.Canary;
import net.canarymod.Translator;
import net.visualillusionsent.utils.JarUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Canary.class, remap = false)
public abstract class MixinCanary {

    @Shadow private static String jarPath;

    /**
     * @author jamierocks - 27th October 2015
     * @reason Overwrite to get the implementation title.
     */
    @Overwrite
    public static String getImplementationTitle() {
        return "NeptuneVanilla";
    }

    /**
     * @author jamierocks - 27th October 2015
     * @reason Overwrite to get the implementation version.
     */
    @Overwrite
    public static String getImplementationVersion() {
        return "1.8.9-1.2.1-SNAPSHOT";
    }

    /**
     * @author jamierocks - 27th October 2015
     * @reason Overwrite to get the jar path of Canary properly.
     */
    @Overwrite
    public static String getCanaryJarPath() {
        if (jarPath == null) {
            jarPath = JarUtils.getJarPath(Translator.class);
        }
        return jarPath;
    }

}
