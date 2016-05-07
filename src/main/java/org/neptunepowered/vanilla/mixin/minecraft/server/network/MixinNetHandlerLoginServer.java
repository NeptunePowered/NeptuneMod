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
package org.neptunepowered.vanilla.mixin.minecraft.server.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.network.NetHandlerLoginServer;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(NetHandlerLoginServer.class)
public class MixinNetHandlerLoginServer {

    @Shadow public NetworkManager networkManager;

    /**
     * Overwrite to enable Bungeecord support.
     *
     * @author jamierocks
     */
    @Overwrite
    protected GameProfile getOfflineProfile(GameProfile original) {
        UUID uuid;
        if (((IMixinNetworkManager) this.networkManager).getSpoofedUUID() != null) {
            uuid = ((IMixinNetworkManager) this.networkManager).getSpoofedUUID();
        } else {
            uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
        }

        original = new GameProfile(uuid, original.getName());

        if (((IMixinNetworkManager) this.networkManager).getSpoofedProfile() != null) {
            for (Property property : ((IMixinNetworkManager) this.networkManager).getSpoofedProfile()) {
                original.getProperties().put(property.getName(), property);
            }
        }

        return original;
    }
}
