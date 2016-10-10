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
package org.neptunepowered.vanilla.util.converter;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.canarymod.api.DyeColor;
import net.minecraft.item.EnumDyeColor;

public final class DyeColorConverter {

    private static BiMap<EnumDyeColor, DyeColor> map =
            ImmutableBiMap.<EnumDyeColor, DyeColor>builder()
                    .put(EnumDyeColor.WHITE, DyeColor.WHITE)
                    .put(EnumDyeColor.ORANGE, DyeColor.ORANGE)
                    .put(EnumDyeColor.MAGENTA, DyeColor.MAGENTA)
                    .put(EnumDyeColor.LIGHT_BLUE, DyeColor.LIGHT_BLUE)
                    .put(EnumDyeColor.YELLOW, DyeColor.YELLOW)
                    .put(EnumDyeColor.LIME, DyeColor.LIME)
                    .put(EnumDyeColor.PINK, DyeColor.PINK)
                    .put(EnumDyeColor.GRAY, DyeColor.GRAY)
                    .put(EnumDyeColor.SILVER, DyeColor.LIGHT_GRAY)
                    .put(EnumDyeColor.CYAN, DyeColor.CYAN)
                    .put(EnumDyeColor.PURPLE, DyeColor.PURPLE)
                    .put(EnumDyeColor.BLUE, DyeColor.BLUE)
                    .put(EnumDyeColor.BROWN, DyeColor.BROWN)
                    .put(EnumDyeColor.GREEN, DyeColor.GREEN)
                    .put(EnumDyeColor.RED, DyeColor.RED)
                    .put(EnumDyeColor.BLACK, DyeColor.BLACK)
                    .build();

    private DyeColorConverter() {
    }

    public static DyeColor of(EnumDyeColor artType) {
        return map.get(artType);
    }

    public static EnumDyeColor of(DyeColor art) {
        return map.inverse().get(art);
    }

}
