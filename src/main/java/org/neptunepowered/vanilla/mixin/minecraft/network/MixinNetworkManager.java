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
package org.neptunepowered.vanilla.mixin.minecraft.network;

import com.mojang.authlib.properties.Property;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.NetworkManager;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;
import java.util.UUID;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager extends SimpleChannelInboundHandler implements IMixinNetworkManager {

    @Shadow private SocketAddress socketAddress;

    /*
     * For ServerListPingHook
     */
    private int protocolVersion;
    private String hostnamePinged;
    private int portPinged;

    /*
     * For BungeeCord support
     */
    private Property[] spoofedProfile;
    private UUID spoofedUUID;

    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public void setProtocolVersion(int version) {
        this.protocolVersion = version;
    }

    @Override
    public String getHostnamePinged() {
        return this.hostnamePinged;
    }

    @Override
    public void setHostnamePinged(String hostname) {
        this.hostnamePinged = hostname;
    }

    @Override
    public int getPortPinged() {
        return this.portPinged;
    }

    @Override
    public void setPortPinged(int port) {
        this.portPinged = port;
    }

    @Override
    public void setRemoteAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public Property[] getSpoofedProfile() {
        return this.spoofedProfile;
    }

    @Override
    public void setSpoofedProfile(Property[] spoofedProfile) {
        this.spoofedProfile = spoofedProfile;
    }

    @Override
    public UUID getSpoofedUUID() {
        return this.spoofedUUID;
    }

    @Override
    public void setSpoofedUUID(UUID uuid) {
        this.spoofedUUID = uuid;
    }
}
