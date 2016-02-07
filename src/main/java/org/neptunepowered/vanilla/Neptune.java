/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
package org.neptunepowered.vanilla;

import net.canarymod.Canary;
import net.canarymod.bansystem.BanManager;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.commandsys.CommandList;
import net.canarymod.commandsys.CommandManager;
import net.canarymod.commandsys.CommandOwner;
import net.canarymod.commandsys.DuplicateCommandException;
import net.canarymod.database.DatabaseLoader;
import net.canarymod.help.HelpManager;
import net.canarymod.hook.HookExecutor;
import net.canarymod.motd.CanaryMessageOfTheDayListener;
import net.canarymod.motd.MessageOfTheDay;
import net.canarymod.plugin.DefaultPluginManager;
import net.canarymod.plugin.PluginLangLoader;
import net.canarymod.user.OperatorsProvider;
import net.canarymod.user.WhitelistProvider;
import net.minecraft.server.MinecraftServer;
import org.neptunepowered.vanilla.interfaces.minecraft.command.IMixinServerCommandManager;
import org.neptunepowered.vanilla.wrapper.NeptuneTranslator;
import org.neptunepowered.vanilla.wrapper.commandsys.NeptunePlayerSelector;
import org.neptunepowered.vanilla.wrapper.factory.NeptuneFactory;
import org.neptunepowered.vanilla.wrapper.util.NeptuneJsonNBTUtility;

import java.io.File;

public class Neptune extends Canary {

    public static final CommandOwner minecraftCommandOwner = new CommandOwner() {
        @Override
        public String getName() {
            return "Minecraft";
        }
    };

    private boolean isInitialised = false;

    public Neptune() {
        Canary.setCanary(this);

        DatabaseLoader.load();
        PluginLangLoader.load();
        NeptuneTranslator.load();

        this.jsonNBT = new NeptuneJsonNBTUtility(); // JSON utility
        this.motd = new MessageOfTheDay(); // MessageOfTheDay
        this.commandManager = new CommandManager(); // Manage all the commands :D
        this.hookExecutor = new HookExecutor(); // Execute the hooks
        this.helpManager = new HelpManager(); // /help
        this.banManager = new BanManager(); // bans
        this.whitelist = new WhitelistProvider(); // whitelist
        this.ops = new OperatorsProvider(); // op
        this.factory = new NeptuneFactory(); // Factories
        this.playerSelector = new NeptunePlayerSelector(); // player selector
        this.pluginManager = new DefaultPluginManager();

        pluginManager.scanForPlugins(); // Scan for plugins
    }

    public void registerCanaryCommands() {
        ((IMixinServerCommandManager) ((MinecraftServer) server).getCommandManager()).registerEarlyCommands();
        try {
            this.commandManager.registerCommands(new CommandList(), getServer(), true);
        } catch (CommandDependencyException e) {
            log.error("Failed to set up system commands! Dependency resolution failed!", e);
        } catch (DuplicateCommandException f) {
            log.error("Failed to set up system commands! The command already exists!", f);
        }
        try {
            this.commandManager.registerCommands(new org.neptunepowered.vanilla.NeptuneCommands(), getServer(), true);
        } catch (CommandDependencyException e) {
            log.error("Failed to set up system commands! Dependency resolution failed!", e);
        } catch (DuplicateCommandException f) {
            log.error("Failed to set up system commands! The command already exists!", f);
        }
    }

    public void initMOTDListener() {
        motd().registerMOTDListener(new CanaryMessageOfTheDayListener(), getServer(), false);
    }

    public void lateInitialisation() {
        if (isInitialised) {
            return;
        }

        this.registerCanaryCommands();
        this.initMOTDListener();
        isInitialised = true;
    }
}
