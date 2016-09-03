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
import net.canarymod.api.PlayerListAction;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

public final class PlayerListActionConverter {

    private static BiMap<S38PacketPlayerListItem.Action, PlayerListAction> map =
            ImmutableBiMap.<S38PacketPlayerListItem.Action, PlayerListAction>builder()
                    .put(S38PacketPlayerListItem.Action.ADD_PLAYER, PlayerListAction.ADD_PLAYER)
                    .put(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, PlayerListAction.UPDATE_GAME_MODE)
                    .put(S38PacketPlayerListItem.Action.UPDATE_LATENCY, PlayerListAction.UPDATE_LATENCY)
                    .put(S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME, PlayerListAction.UPDATE_DISPLAY_NAME)
                    .put(S38PacketPlayerListItem.Action.REMOVE_PLAYER, PlayerListAction.REMOVE_PLAYER)
                    .build();

    public static PlayerListAction of(S38PacketPlayerListItem.Action action) {
        return map.get(action);
    }

    public static S38PacketPlayerListItem.Action of(PlayerListAction action) {
        return map.inverse().get(action);
    }

}
