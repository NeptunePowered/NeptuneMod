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
package org.neptunepowered.vanilla.mixin.minecraft.command.server;

import net.canarymod.Canary;
import net.canarymod.api.world.World;
import net.canarymod.config.Configuration;
import net.canarymod.hook.command.CommandBlockCommandHook;
import net.canarymod.user.Group;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandBlockLogic.class)
@Implements(@Interface(iface = net.canarymod.api.CommandBlockLogic.class, prefix = "logic$"))
public abstract class MixinCommandBlockLogic implements net.canarymod.api.CommandBlockLogic {

    @Shadow private int successCount;
    @Shadow private IChatComponent lastOutput;
    @Shadow private String commandStored;

    @Shadow public abstract String getCommand();
    @Shadow public abstract void setCommand(String command);
    @Shadow public abstract void setName(String p_145754_1_);

    /**
     * @author jamierocks - 3rd September 2016
     * @reason Support Canary commands
     */
    @Overwrite
    public void trigger(net.minecraft.world.World worldIn) {
        if (worldIn.isRemote) {
            this.successCount = 0;
        }

        MinecraftServer minecraftserver = MinecraftServer.getServer();

        if (minecraftserver != null && minecraftserver.isAnvilFileSet() && minecraftserver.isCommandBlockEnabled()) {
            ICommandManager icommandmanager = minecraftserver.getCommandManager();

            try {
                this.lastOutput = null;
                // Neptune - Canary commands
                this.successCount = this.handleCommandExecution(icommandmanager, (CommandBlockLogic) (Object) this, this.commandStored);
                // Neptune - end
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
                crashreportcategory.addCrashSectionCallable("Command", this::getCommand);
                crashreportcategory.addCrashSectionCallable("Name", this::getName);
                throw new ReportedException(crashreport);
            }
        } else {
            this.successCount = 0;
        }
    }

    public int handleCommandExecution(ICommandManager commandManager, CommandBlockLogic commandBlockLogic, String commandStored) {
        final String[] args = commandStored.split(" ");
        String commandName = args[0];
        if (commandName.startsWith("/")) {
            commandName = commandName.substring(1);
        }

        CommandBlockCommandHook commandHook = (CommandBlockCommandHook) new CommandBlockCommandHook(
                (net.canarymod.api.CommandBlockLogic) commandBlockLogic, args).call();

        if (!commandHook.isCanceled()
                && (Configuration.getServerConfig().isCommandBlockOpped()
                || ((net.canarymod.api.CommandBlockLogic) commandBlockLogic).hasPermission("canary.command." + commandName))) {
            int result = commandManager.executeCommand(commandBlockLogic, commandStored);
            if (result == 0) {
                // Minecraft found no command, now its our turn
                Canary.getServer().consoleCommand(commandStored, (net.canarymod.api.CommandBlockLogic) commandBlockLogic);
            }
            return result;
        }

        return 0;
    }

    @Intrinsic
    public void logic$setCommand(String s) {
        this.setCommand(s);
    }

    @Intrinsic
    public String logic$getCommand() {
        return this.getCommand();
    }

    @Override
    public void activate() {

    }

    @Override
    public World getWorld() {
        return null;
    }

    @Intrinsic
    public void logic$setName(String s) {
        this.setName(s);
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public void setGroup(Group group) {

    }

}
