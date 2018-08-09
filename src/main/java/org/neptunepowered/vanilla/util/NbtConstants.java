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
package org.neptunepowered.vanilla.util;

public final class NbtConstants {

    public static final String SPAWN_POTENTIALS = "SpawnPotentials";
    public static final String ENTITY_TYPE = "Type";
    public static final String GAME_TYPE = "playerGameType";
    public static final String INVENTORY = "Inventory";

    // NBT tag ids
    public static final int TAG_END = 0;
    public static final int TAG_BYTE = 1;
    public static final int TAG_SHORT = 2;
    public static final int TAG_INT = 3;
    public static final int TAG_LONG = 4;
    public static final int TAG_FLOAT = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_BYTE_ARRAY = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_LIST = 9;
    public static final int TAG_COMPOUND = 10;
    public static final int TAG_INT_ARRAY = 11;

    // Canary tags
    public static final String CANARY_TAG = "Canary";
    public static final String SLEEPING_IGNORED = "SleepingIgnored";
    public static final String TIME_PLAYED = "TimePlayed";
    public static final String PREVIOUS_IP = "PreviousIP";
    public static final String FIRST_JOINED = "FirstJoin";
    public static final String LAST_JOINED = "LastJoin";
    public static final String DAMAGE_ENTITIES = "DamageEntities";
    public static final String DAMAGE_WORLD = "DamageWorld";
    public static final String POWER = "Power";
    public static final String SPAWN_ROT_X = "SpawnRotX";
    public static final String SPAWN_ROT_Y = "SpawnRotY";

    // Bukkit tags
    public static final String BUKKIT_TAG = "bukkit";
    public static final String BUKKIT_FIRST_JOINED = "firstPlayed";
    public static final String BUKKIT_LAST_JOINED = "lastPlayed";

    private NbtConstants() {
    }

}
