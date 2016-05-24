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
import net.canarymod.api.GameMode;
import net.minecraft.world.WorldSettings;

public final class GameModeConverter {

    private static BiMap<WorldSettings.GameType, GameMode> map =
            ImmutableBiMap.<WorldSettings.GameType, GameMode>builder()
                    .put(WorldSettings.GameType.SURVIVAL, GameMode.SURVIVAL)
                    .put(WorldSettings.GameType.CREATIVE, GameMode.CREATIVE)
                    .put(WorldSettings.GameType.ADVENTURE, GameMode.ADVENTURE)
                    .put(WorldSettings.GameType.SPECTATOR, GameMode.SPECTATOR)
                    .build();

    public static GameMode of(WorldSettings.GameType artType) {
        return map.get(artType);
    }

    public static WorldSettings.GameType of(GameMode art) {
        return map.inverse().get(art);
    }
}
