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
package org.neptunepowered.vanilla.mixin.core.network;

import net.minecraft.network.NetworkManager;
import org.neptunepowered.vanilla.bridge.core.network.BridgeNetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetworkManager.class)
public class MixinNetworkManager implements BridgeNetworkManager {

    private int protocolVersion;
    private String hostnamePinged;
    private int portPinged;

    @Override
    public int bridge$getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public void bridge$setProtocolVersion(final int version) {
        this.protocolVersion = version;
    }

    @Override
    public String bridge$getHostnamePinged() {
        return this.hostnamePinged;
    }

    @Override
    public void bridge$setHostnamePinged(final String hostname) {
        this.hostnamePinged = hostname;
    }

    @Override
    public int bridge$getPortPinged() {
        return this.portPinged;
    }

    @Override
    public void bridge$setPortPinged(final int port) {
        this.portPinged = port;
    }

}
