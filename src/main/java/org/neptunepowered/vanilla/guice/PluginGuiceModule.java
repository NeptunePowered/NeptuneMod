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
package org.neptunepowered.vanilla.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.PluginDescriptor;

public class PluginGuiceModule extends AbstractModule {

    private final PluginDescriptor descriptor;
    private final Class<?> pluginClass;

    public PluginGuiceModule(PluginDescriptor descriptor, Class<?> pluginClass) {
        this.descriptor = descriptor;
        this.pluginClass = pluginClass;
    }

    @Override
    protected void configure() {
        this.bind(this.pluginClass).in(Scopes.SINGLETON);
        this.bind(PluginDescriptor.class).toInstance(this.descriptor);
        this.bind(Logman.class).toInstance(Logman.getLogman(this.descriptor.getName()));
    }

}
