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
package org.neptunepowered.vanilla.launch;

import static com.google.common.io.Resources.getResource;
import static org.spongepowered.asm.mixin.MixinEnvironment.Side.SERVER;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NeptuneServerTweaker implements ITweaker {

    public static final Logger log = LogManager.getLogger("Neptune");

    private String[] args;

    @Override
    public void acceptOptions(List<String> args, File file, File file1, String s) {
        this.args = args.toArray(new String[args.size()]);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader loader) {
        log.info("Initialising Neptune...");

        configureLaunchClassLoader(loader);
        configureDeobfuscation(loader);
        registerAccessTransformers(loader);
        configureMixinEnvironment();

        log.info("Initialisation finished. Starting Minecraft server...");
    }


    private static boolean isObfuscated() {
        try {
            // If the dedicated server class exists in the de-obfuscated name, we're likely in dev env
            return Launch.classLoader.getClassBytes("net.minecraft.server.dedicated.DedicatedServer") == null;
        } catch (IOException ignored) {
            return true;
        }
    }

    @Override
    public String getLaunchTarget() {
        return "org.neptunepowered.vanilla.NeptuneMain";
    }

    @Override
    public String[] getLaunchArguments() {
        return this.args;
    }

    private static void configureLaunchClassLoader(final LaunchClassLoader loader) {
        // Logging
        loader.addClassLoaderExclusion("org.slf4j.");
        loader.addClassLoaderExclusion("org.jline.");
        loader.addClassLoaderExclusion("com.sun.");
        loader.addClassLoaderExclusion("com.mojang.util.QueueLogAppender");

        // Neptune Launch
        loader.addClassLoaderExclusion("com.google.common.");
        loader.addClassLoaderExclusion("org.neptunepowered.vanilla.launch.");

        // Don't allow transforming libraries
        loader.addTransformerExclusion("com.google.");
        loader.addTransformerExclusion("org.apache.");
        loader.addTransformerExclusion("joptsimple.");
        loader.addTransformerExclusion("io.netty.");
        loader.addTransformerExclusion("it.unimi.dsi.fastutil.");
        loader.addTransformerExclusion("com.github.benmanes.caffeine.");
        loader.addTransformerExclusion("net.visualillusionsent.utils.");

        // Add a transformer exclusion for translator so we can get the JAR path from it
        loader.addTransformerExclusion("net.canarymod.Translator");
    }

    private static void configureDeobfuscation(final LaunchClassLoader loader) {
        // Check if we're running in de-obfuscated environment already
        log.debug("Applying runtime de-obfuscation...");
        if (isObfuscated()) {
            log.info("De-obfuscation mappings are provided by MCP (http://www.modcoderpack.com)");
            Launch.blackboard.put("vanilla.mappings", getResource("mappings.srg"));
            loader.registerTransformer("org.neptunepowered.vanilla.launch.transformer.DeobfuscationTransformer");
            log.debug("Runtime de-obfuscation is applied.");
        } else {
            log.debug(
                    "Runtime de-obfuscation was not applied. Neptune is being loaded in a de-obfuscated environment.");
        }
    }

    private static void registerAccessTransformers(final LaunchClassLoader loader) {
        log.debug("Applying access transformer...");
        Launch.blackboard.put("vanilla.at", new URL[]{getResource("vanilla_at.cfg")});
        loader.registerTransformer("org.neptunepowered.vanilla.launch.transformer.AccessTransformer");
    }

    private static void configureMixinEnvironment() {
        log.debug("Initialising Mixin environment...");
        MixinBootstrap.init();
        Mixins.addConfigurations(
                "mixins.vanilla.bungee.json",
                "mixins.vanilla.core.json",
                "mixins.vanilla.perf.json");
        MixinEnvironment.getDefaultEnvironment().setSide(SERVER);
    }

}
