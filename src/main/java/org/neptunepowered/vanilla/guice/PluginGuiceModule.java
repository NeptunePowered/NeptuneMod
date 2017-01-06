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
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import net.canarymod.Canary;
import net.canarymod.api.Server;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.PluginDescriptor;
import net.visualillusionsent.utils.PropertiesFile;
import org.neptunepowered.lib.config.ConfigurationProvider;
import org.neptunepowered.lib.plugin.ConfigFile;

public class PluginGuiceModule extends AbstractModule {

    private final PluginDescriptor descriptor;
    private final Class<?> pluginClass;

    public PluginGuiceModule(PluginDescriptor descriptor, Class<?> pluginClass) {
        this.descriptor = descriptor;
        this.pluginClass = pluginClass;
    }

    @Override
    protected void configure() {
        final ConfigFileAnnotation canaryInf = new ConfigFileAnnotation(ConfigFile.Type.CANARY_INF);
        final ConfigFileAnnotation config = new ConfigFileAnnotation(ConfigFile.Type.CONFIG);

        // Canary globals
        this.bind(Server.class).toInstance(Canary.getServer());

        // Plugin related
        this.bind(this.pluginClass).in(Scopes.SINGLETON);
        this.bind(PluginDescriptor.class).toInstance(this.descriptor);
        this.bind(Logman.class).toInstance(Logman.getLogman(this.descriptor.getName()));

        // Config related
        this.bind(ConfigurationProvider.class).toInstance(ConfigurationProvider.getConfigurationProvider(this.descriptor.getName()));
        this.bind(PropertiesFile.class).annotatedWith(canaryInf).toProvider(CanaryInfConfigProvider.class);
        this.bind(PropertiesFile.class).annotatedWith(config).toProvider(ConfigConfigProvider.class);
    }

    private static class CanaryInfConfigProvider implements Provider<PropertiesFile> {

        private final PluginDescriptor descriptor;

        @Inject
        public CanaryInfConfigProvider(PluginDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        @Override
        public PropertiesFile get() {
            return this.descriptor.getCanaryInf();
        }

    }

    private static class ConfigConfigProvider implements Provider<PropertiesFile> {

        private final PluginDescriptor descriptor;

        @Inject
        public ConfigConfigProvider(PluginDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        @Override
        public PropertiesFile get() {
            return ConfigurationProvider.getConfigurationProvider(this.descriptor.getName()).getPluginConfig();
        }

    }

}
