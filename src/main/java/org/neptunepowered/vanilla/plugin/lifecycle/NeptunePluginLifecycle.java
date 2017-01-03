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
package org.neptunepowered.vanilla.plugin.lifecycle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.canarymod.exceptions.PluginLoadFailedException;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginDescriptor;
import net.canarymod.plugin.lifecycle.InvalidPluginLifecycleException;
import net.canarymod.plugin.lifecycle.PluginLifecycleBase;
import net.canarymod.plugin.lifecycle.PluginLifecycleFactory;
import net.minecraft.launchwrapper.Launch;
import org.neptunepowered.vanilla.guice.PluginGuiceModule;
import org.neptunepowered.vanilla.plugin.NeptunePluginWrapper;

import java.io.File;

/**
 * Lifecycle manager for a Neptune plugin
 *
 * @author Jamie Mansfield (jamierocks)
 */
public class NeptunePluginLifecycle extends PluginLifecycleBase {

    public static void init() {
        try {
            PluginLifecycleFactory.registerLifecycle("neptune", NeptunePluginLifecycle.class);
        } catch (InvalidPluginLifecycleException e) {
            e.printStackTrace();
        }
    }

    public NeptunePluginLifecycle(PluginDescriptor desc) {
        super(desc);
    }

    @Override
    protected void _load() throws PluginLoadFailedException {
        try {
            Launch.classLoader.addURL(new File(this.desc.getPath()).toURI().toURL());
            final Class<?> pluginClass = Launch.classLoader.loadClass(this.desc.getCanaryInf().getString("main-class"));

            // mad haks bro
            Plugin.threadLocalName.set(this.desc.getName());
            final Injector injector = Guice.createInjector(new PluginGuiceModule(this.desc, pluginClass));
            final Plugin plugin = new NeptunePluginWrapper(injector.getInstance(pluginClass));

            // gotta be certain
            plugin.setName(this.desc.getName());
            plugin.setPriority(this.desc.getPriority());
            this.desc.setPlugin(plugin);
        } catch (Exception ex) {
            throw new PluginLoadFailedException("Failed to load plugin", ex);
        }
    }

    @Override
    protected void _unload() {
    }

}
