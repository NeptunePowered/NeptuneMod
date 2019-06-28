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
package org.neptunepowered.vanilla.mixin.bungee.network;

import com.mojang.authlib.properties.Property;
import net.minecraft.network.NetworkManager;
import org.neptunepowered.vanilla.bridge.bungee.network.BridgeNetworkManager_Bungee;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager_Bungee implements BridgeNetworkManager_Bungee {

    private Property[] spoofedProfile;
    private UUID spoofedUUID;

    @Override
    public Property[] bungeeBridge$getSpoofedProfile() {
        return this.spoofedProfile;
    }

    @Override
    public void bungeeBridge$setSpoofedProfile(final Property[] spoofedProfile) {
        this.spoofedProfile = spoofedProfile;
    }

    @Override
    public UUID bungeeBridge$getSpoofedUUID() {
        return this.spoofedUUID;
    }

    @Override
    public void bungeeBridge$setSpoofedUUID(final UUID uuid) {
        this.spoofedUUID = uuid;
    }

}
