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
package org.neptunepowered.vanilla;

import net.canarymod.Canary;
import net.canarymod.api.inventory.Enchantment;
import net.canarymod.api.inventory.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.neptunepowered.vanilla.inventory.NeptuneEnchantment;
import org.neptunepowered.vanilla.serialize.EnchantmentSerializer;
import org.neptunepowered.vanilla.serialize.ItemSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NeptuneMain {

    public static void main(String[] args) throws Exception {
        // Some handy messages that CanaryMod has
        Canary.log.info("Starting: " + Canary.getImplementationTitle() + " " + Canary.getImplementationVersion());
        Canary.log.info("Neptune Path: " + Canary.getCanaryJarPath() + " & Working From: " + Canary.getWorkingPath());

        // Initialise the sqlite jdbc driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (final ClassNotFoundException ignored) {
        }

        // Ensure config directory exists
        final Path config = Paths.get("config");
        if (Files.notExists(config)) Files.createDirectory(config);

        // Now lets initialise Neptune / Canary
        initNeptune();

        // Now we're ready to start the Minecraft server
        MinecraftServer.main(args);
    }

    private static void initNeptune() {
        new Neptune();
        // Serialisers
        Canary.addSerializer(EnchantmentSerializer.INSTANCE, Enchantment.class);
        Canary.addSerializer(EnchantmentSerializer.INSTANCE, NeptuneEnchantment.class);
        Canary.addSerializer(ItemSerializer.INSTANCE, Item.class);
        Canary.addSerializer(ItemSerializer.INSTANCE, ItemStack.class);
    }

}
