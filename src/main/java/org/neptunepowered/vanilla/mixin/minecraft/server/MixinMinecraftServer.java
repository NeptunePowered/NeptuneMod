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
package org.neptunepowered.vanilla.mixin.minecraft.server;

import com.mojang.authlib.GameProfile;
import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.api.CommandBlockLogic;
import net.canarymod.api.ConfigurationManager;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.PlayerListData;
import net.canarymod.api.PlayerReference;
import net.canarymod.api.Server;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.gui.GUIControl;
import net.canarymod.api.inventory.recipes.CraftingRecipe;
import net.canarymod.api.inventory.recipes.Recipe;
import net.canarymod.api.inventory.recipes.SmeltRecipe;
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.config.Configuration;
import net.canarymod.hook.command.ConsoleCommandHook;
import net.canarymod.tasks.ServerTask;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import org.neptunepowered.vanilla.interfaces.minecraft.world.storage.IMixinSaveHandler;
import org.neptunepowered.vanilla.world.NeptuneWorldManager;
import org.neptunepowered.vanilla.wrapper.NeptuneOfflinePlayer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.UUID;

@Mixin(MinecraftServer.class)
@Implements(@Interface(iface = Server.class, prefix = "server$"))
public abstract class MixinMinecraftServer implements Server {

    @Shadow public long[] tickTimeArray;
    @Shadow private int tickCounter;
    @Shadow private boolean serverRunning;
    @Shadow private ServerConfigurationManager serverConfigManager;
    @Shadow private ServerStatusResponse statusResponse;

    private WorldManager worldManager = new NeptuneWorldManager();

    @Shadow
    public abstract void initiateShutdown();

    @Shadow
    public abstract String getMinecraftVersion();

    @Shadow
    public abstract String[] getAllUsernames();

    @Shadow
    public abstract PlayerProfileCache getPlayerProfileCache();

    @Shadow
    public abstract ICommandManager getCommandManager();

    @Shadow
    public abstract String getHostname();

    /**
     * Use the Canary configuration.
     *
     * @author Jamie Mansfield
     */
    @Overwrite
    public int getMaxPlayers() {
        return Configuration.getServerConfig().getMaxPlayers();
    }

    @Intrinsic
    public String server$getHostname() {
        return this.getHostname();
    }

    @Override
    public int getNumPlayersOnline() {
        return serverConfigManager.getCurrentPlayerCount();
    }

    @Intrinsic
    public int server$getMaxPlayers() {
        return this.getMaxPlayers();
    }

    @Override
    public String[] getPlayerNameList() {
        return serverConfigManager.getAllUsernames();
    }

    @Override
    public String[] getKnownPlayerNames() {
        return this.getAllUsernames();
    }

    @Override
    public List<Player> getPlayerList() {
        return (List) this.serverConfigManager.getPlayerList();
    }

    @Override
    public String getDefaultWorldName() {
        return Configuration.getServerConfig().getDefaultWorldName();
    }

    @Override
    public WorldManager getWorldManager() {
        return this.worldManager;
    }

    @Override
    public boolean consoleCommand(String command) {
        ConsoleCommandHook commandHook = (ConsoleCommandHook) new ConsoleCommandHook(this, command).call();
        if (commandHook.isCanceled()) {
            return true;
        }

        final String[] args = command.split(" ");

        String commandName = args[0];
        if (commandName.startsWith("/")) {
            commandName = commandName.substring(1);
        }

        if (!Canary.commands().parseCommand(this, commandName, args)) {
            return this.getCommandManager().executeCommand((ICommandSender) this, command) > 0;
        }

        return true;
    }

    @Override
    public boolean consoleCommand(String command, Player player) {
        ConsoleCommandHook commandHook = (ConsoleCommandHook) new ConsoleCommandHook(player, command).call();
        if (commandHook.isCanceled()) {
            return true;
        }

        final String[] args = command.split(" ");

        String commandName = args[0];
        if (commandName.startsWith("/")) {
            commandName = commandName.substring(1);
        }

        if (!Canary.commands().parseCommand(player, commandName, args)) {
            if (Canary.ops().isOpped(player) || player.hasPermission("canary.vanilla." + commandName)) {
                return this.getCommandManager().executeCommand((ICommandSender) player, command) > 0;
            }
            return false;
        }

        return true;
    }

    @Override
    public boolean consoleCommand(String command, CommandBlockLogic cmdBlockLogic) {
        ConsoleCommandHook commandHook = (ConsoleCommandHook) new ConsoleCommandHook(cmdBlockLogic, command).call();
        if (commandHook.isCanceled()) {
            return true;
        }

        final String[] args = command.split(" ");

        String commandName = args[0];
        if (commandName.startsWith("/")) {
            commandName = commandName.substring(1);
        }

        // Don't pass to vanilla as that is already handled by CommandBlockLogic
        // This is only called if Minecraft found no command
        return Canary.commands().parseCommand(cmdBlockLogic, commandName, args);
    }

    @Override
    public void executeVanillaCommand(MessageReceiver caller, String command) {
        this.getCommandManager().executeCommand((ICommandSender) caller, command);
    }

    @Override
    public void setTimer(String uniqueName, int time) {

    }

    @Override
    public boolean isTimerExpired(String uniqueName) {
        return false;
    }

    @Override
    public Player matchPlayer(String player) {
        player = player.toLowerCase();

        Player lastPlayer = null;
        for (Player cPlayer : getConfigurationManager().getAllPlayers()) {
            if (cPlayer.getName().toLowerCase().equals(player)) {
                // Perfect match found
                lastPlayer = cPlayer;
                break;
            }
            if (cPlayer.getName().toLowerCase().contains(player)) {
                // Partial match
                if (lastPlayer != null) {
                    // Found multiple
                    return null;
                }
                lastPlayer = cPlayer;
            }
        }

        return lastPlayer;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String player) {
        UUID uuid = ToolBox.uuidFromUsername(player);
        if (uuid == null) {
            return null;
        }
        return this.getOfflinePlayer(uuid);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        ISaveHandler saveHandler = MinecraftServer.getServer().getEntityWorld().getSaveHandler();

        if (saveHandler instanceof SaveHandler) {
            NBTTagCompound tagCompound = ((IMixinSaveHandler) saveHandler).readPlayerData(uuid);

            if (tagCompound != null) {
                GameProfile profile = getPlayerProfileCache().getProfileByUUID(uuid);

                if (profile != null) {
                    return new NeptuneOfflinePlayer(profile.getName(), uuid, (CompoundTag) tagCompound);
                } else {
                    return new NeptuneOfflinePlayer("PLAYER_NAME_UNKNOWN", uuid, (CompoundTag) tagCompound);
                }
            }

            return null;
        } else {
            throw new RuntimeException("ISaveHandler is not of type SaveHandler! Failing to load playerdata");
        }
    }

    @Override
    public PlayerReference matchKnownPlayer(String player) {
        PlayerReference reference = matchPlayer(player);
        if (reference == null) {
            reference = getOfflinePlayer(player);
        }
        return reference;
    }

    @Override
    public PlayerReference matchKnownPlayer(UUID uuid) {
        PlayerReference reference = getPlayerFromUUID(uuid);
        if (reference == null) {
            reference = getOfflinePlayer(uuid);
        }
        return reference;
    }

    @Override
    public Player getPlayer(String player) {
        return getConfigurationManager().getPlayerByName(player);
    }

    @Override
    public Player getPlayerFromUUID(String uuid) {
        Player player = null;

        for (Player p : getConfigurationManager().getAllPlayers()) {
            if (p.getUUIDString().equals(uuid)) {
                player = p;
                break;
            }
        }

        return player;
    }

    @Override
    public Player getPlayerFromUUID(UUID uuid) {
        Player player = null;

        for (Player p : getConfigurationManager().getAllPlayers()) {
            if (p.getUUID().equals(uuid)) {
                player = p;
                break;
            }
        }

        return player;
    }

    @Override
    public void broadcastMessage(String message) {
        for (Player player : this.getPlayerList()) {
            player.message(message);
        }
        Canary.log.info(message);
    }

    @Override
    public void broadcastMessageToOps(String message) {
        for (Player player : this.getPlayerList()) {
            if (player.isOperator()) {
                player.message(message);
            }
        }
        Canary.log.info(message);
    }

    @Override
    public void broadcastMessageToAdmins(String message) {
        for (Player player : this.getPlayerList()) {
            if (player.isAdmin()) {
                player.message(message);
            }
        }
        Canary.log.info(message);
    }

    @Override
    public boolean loadWorld(String name, long seed) {
        return false;
    }

    @Override
    public World getWorld(String name) {
        return null;
    }

    @Override
    public World getDefaultWorld() {
        return null;
    }

    @Override
    public ConfigurationManager getConfigurationManager() {
        return (ConfigurationManager) this.serverConfigManager;
    }

    @Override
    public void initiateShutdown(String message) {
        initiateShutdown();
    }

    @Override
    public void restart(boolean reloadCanary) {

    }

    @Override
    public boolean isRunning() {
        return this.serverRunning;
    }

    @Override
    public Recipe addRecipe(CraftingRecipe recipe) {
        return null;
    }

    @Override
    public List<Recipe> getServerRecipes() {
        return (List) CraftingManager.getInstance().getRecipeList();
    }

    @Override
    public boolean removeRecipe(Recipe recipe) {
        return false;
    }

    @Override
    public void addSmeltingRecipe(SmeltRecipe recipe) {

    }

    @Override
    public List<SmeltRecipe> getServerSmeltRecipes() {
        return null;
    }

    @Override
    public boolean removeSmeltRecipe(SmeltRecipe recipe) {
        return false;
    }

    @Override
    public void addGUI(GUIControl gui) {

    }

    @Override
    public long[] getTickTimeArray() {
        return tickTimeArray;
    }

    @Override
    public String getCanaryModVersion() {
        return Canary.getImplementationVersion();
    }

    @Override
    public String getServerVersion() {
        return getMinecraftVersion();
    }

    @Override
    public int getProtocolVersion() {
        return this.statusResponse.getProtocolVersionInfo().getProtocol();
    }

    @Override
    public String getServerGUILog() {
        return null;
    }

    @Override
    public GUIControl getCurrentGUI() {
        return null;
    }

    @Override
    public boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

    @Override
    public boolean addSynchronousTask(ServerTask task) {
        return false;
    }

    @Override
    public boolean removeSynchronousTask(ServerTask task) {
        return false;
    }

    @Override
    public void sendPlayerListData(PlayerListData data) {

    }

    @Override
    public int getCurrentTick() {
        return tickCounter;
    }

    @Override
    public float getTicksPerSecond() {
        return 0;
    }

    @Override
    public void showTitle(ChatComponent title) {

    }

    @Override
    public void showTitle(ChatComponent title, ChatComponent subtitle) {

    }

    @Override
    public String getLocale() {
        return Configuration.getServerConfig().getServerLocale();
    }

    @Overwrite
    public String getServerModName() {
        return "NeptuneVanilla";
    }
}