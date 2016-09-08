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
package org.neptunepowered.vanilla.mixin.minecraft.entity.player;

import static net.canarymod.Canary.log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.api.GameMode;
import net.canarymod.api.NetServerHandler;
import net.canarymod.api.PlayerListAction;
import net.canarymod.api.PlayerListData;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.statistics.Achievement;
import net.canarymod.api.statistics.Achievements;
import net.canarymod.api.statistics.Stat;
import net.canarymod.api.statistics.Statistics;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.api.world.position.Direction;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.ChatFormat;
import net.canarymod.config.Configuration;
import net.canarymod.hook.command.PlayerCommandHook;
import net.canarymod.hook.player.ChatHook;
import net.canarymod.hook.player.ReturnFromIdleHook;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.permissionsystem.PermissionProvider;
import net.canarymod.user.Group;
import net.canarymod.user.UserAndGroupsProvider;
import net.canarymod.warp.Warp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.visualillusionsent.utils.DateUtils;
import net.visualillusionsent.utils.StringUtils;
import org.neptunepowered.vanilla.interfaces.minecraft.network.IMixinNetHandlerPlayServer;
import org.neptunepowered.vanilla.interfaces.minecraft.util.IMixinFoodStats;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;
import org.neptunepowered.vanilla.chat.NeptuneChatComponent;
import org.neptunepowered.vanilla.util.converter.PlayerListActionConverter;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(EntityPlayerMP.class)
@Implements(@Interface(iface = Player.class, prefix = "player$"))
public abstract class MixinEntityPlayerMP extends MixinEntityPlayer implements Player {

    private static Pattern BAD_CHAT_PATTERN = Pattern.compile("[\u2302\u00D7\u00AA\u00BA\u00AE\u00AC\u00BD\u00BC\u00A1\u00AB\u00BB]");
    private static String CHAT_FORMAT = Configuration.getServerConfig().getChatFormat().replace("&", ChatFormat.MARKER.toString());

    @Shadow public int ping;
    @Shadow private StatisticsFile statsFile;
    @Shadow public NetHandlerPlayServer playerNetServerHandler;
    @Shadow public ItemInWorldManager theItemInWorldManager;
    @Shadow private long playerLastActiveTime;

    private List<Group> groups;
    private PermissionProvider permissions;
    private boolean muted = false;
    private HashMap<String, String> defaultChatPattern = Maps.newHashMap();
    private long currentSessionStart = ToolBox.getUnixTimestamp();

    @Shadow
    public abstract void openEditSign(TileEntitySign signTile);

    @Shadow
    public abstract void closeScreen();

    @Shadow
    public abstract String getPlayerIP();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstruction(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager,
            CallbackInfo info) {
        this.initPlayerData();
    }

    @Inject(method = "markPlayerActive", at = @At("INVOKE"))
    public void onMarkPlayerActive(CallbackInfo info) {
        final long idleTime = MinecraftServer.getCurrentTimeMillis() - this.playerLastActiveTime;
        if (idleTime > 10000) {
            new ReturnFromIdleHook(this, idleTime).call();
        }
    }

    @Override
    public void initPlayerData() {
        final UserAndGroupsProvider provider = Canary.usersAndGroups();
        final String uuid = this.getUUIDString();

        final boolean isNew = !provider.playerExists(uuid);
        final String[] data = provider.getPlayerData(uuid);
        final Group[] subs = provider.getModuleGroupsForPlayer(uuid);

        this.groups = Lists.newLinkedList();
        this.groups.add(Canary.usersAndGroups().getGroup(data[1]));
        for (Group g : subs) {
            if (g != null) {
                this.groups.add(g);
            }
        }

        this.permissions = Canary.permissionManager().getPlayerProvider(uuid, this.getWorld().getFqName());
        if (data[0] != null && (!data[0].isEmpty() && !data[0].equals(" "))) {
            this.prefix = ToolBox.stringToNull(data[0]);
        }

        if (data[2] != null && !data[2].isEmpty()) {
            this.muted = Boolean.valueOf(data[2]);
        }

        this.defaultChatPattern.put("%prefix", this.getPrefix());

        if (isNew || provider.nameChanged(this)) {
            provider.addOrUpdatePlayerData(this);
        }
    }

    @Override
    public void chat(final String message) {
        if (message.length() > 100) {
            this.kick("Message too long!");
        }

        String out = message.trim();
        final Matcher matcher = BAD_CHAT_PATTERN.matcher(out);

        if (matcher.find() && !this.canIgnoreRestrictions()) {
            out = out.replaceAll(matcher.group(), "");
        }

        if (out.startsWith("/")) {
            this.executeCommand(out.split(" "));
        } else if (this.isMuted()) {
            this.notice("You are currently muted!");
        } else {
            final String displayName = this.getDisplayName();

            this.defaultChatPattern.put("%name", displayName != null ? displayName : getName());
            this.defaultChatPattern.put("%message", out);
            this.defaultChatPattern.put("%group", this.getGroup().getName());

            ChatHook hook = (ChatHook) new ChatHook(this, CHAT_FORMAT, Canary.getServer().getPlayerList(), this.defaultChatPattern).call();
            if (hook.isCanceled()) {
                return;
            }

            final String formattedMessage = hook.buildSendMessage();
            for (Player player : hook.getReceiverList()) {
                player.message(formattedMessage);
            }
            log.info(ChatFormat.consoleFormat(formattedMessage));
        }
    }

    @Override
    public Location getSpawnPosition() {
        return null;
    }

    @Override
    public void setSpawnPosition(Location spawn) {

    }

    @Override
    public boolean executeCommand(String[] command) {
        try {
            PlayerCommandHook commandHook = (PlayerCommandHook) new PlayerCommandHook(this, command).call();
            if (commandHook.isCanceled()) {
                return true;
            }

            String commandName = command[0];
            if (commandName.startsWith("/")) {
                commandName = commandName.substring(1);
            }

            if (Canary.commands().parseCommand(this, commandName, command)) {
                log.info("Command used by " + getName() + ": " + StringUtils.joinString(command, " ", 0));
                return true;
            } else {
                log.debug("Vanilla Command Execution...");
                return Canary.getServer().consoleCommand(StringUtils.joinString(command, " ", 0), this);
            }
        } catch (Throwable t) {
            log.error("Exception in command handler!", t);
            if (this.isAdmin()) {
                this.message(ChatFormat.RED + "Exception occurred: " + t.getMessage());
            }
            return false;
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        this.getNetServerHandler().sendPacket(packet);
    }

    @Override
    public NetServerHandler getNetServerHandler() {
        return (NetServerHandler) this.playerNetServerHandler;
    }

    @Override
    public void teleportTo(Location location, TeleportHook.TeleportCause cause) {

    }

    @Override
    public void kick(String reason) {
        this.playerNetServerHandler.kickPlayerFromServer(reason);
    }

    @Override
    public void kickNoHook(String reason) {
        ((IMixinNetHandlerPlayServer) this.playerNetServerHandler).kickPlayerFromServerWithoutHook(reason);
    }

    @Override
    public Direction getCardinalDirection() {
        return null;
    }

    @Override
    public int getPing() {
        return this.ping;
    }

    @Override
    public PlayerListData getPlayerListData(PlayerListAction action) {
        return new PlayerListData(action, this.getGameProfile(), this.getPing(), this.getMode(), this.getDisplayNameComponent());
    }

    @Override
    public void sendPlayerListData(PlayerListData data) {
        final S38PacketPlayerListItem packet = new S38PacketPlayerListItem(PlayerListActionConverter.of(data.getAction()));
        packet.players.add(packet.new AddPlayerData(
                data.getProfile(),                                          // gameProfile
                data.getPing(),                                             // ping
                GameModeConverter.of(data.getMode()),                       // gameType
                ((NeptuneChatComponent) data.getDisplayName()).getHandle()) // displayName
        );
        this.playerNetServerHandler.sendPacket(packet);
    }

    @Override
    public void refreshCreativeMode() {

    }

    @Override
    public void updateCapabilities() {
        this.playerNetServerHandler.sendPacket(new S39PacketPlayerAbilities((PlayerCapabilities) this.getCapabilities()));
    }

    @Override
    public void openInventory(Inventory inventory) {

    }

    @Override
    public void createAndOpenWorkbench() {

    }

    @Override
    public void createAndOpenAnvil() {

    }

    @Override
    public void createAndOpenEnchantmentTable(int bookshelves) {

    }

    @Override
    public void openSignEditWindow(Sign sign) {
        this.openEditSign((TileEntitySign) sign);
    }

    @Override
    public void openBook(Item writtenbook) {

    }

    @Override
    public void closeWindow() {
        this.closeScreen();
    }

    @Override
    public void sendChatComponent(ChatComponent chatComponent) {
        this.playerNetServerHandler.sendPacket(new S02PacketChat((IChatComponent) chatComponent));
    }

    @Override
    public String getPreviousIP() {
        if (this.metadata.hasKey("PreviousIP")) {
            return this.metadata.getString("PreviousIP");
        }
        return "UNKNOWN";
    }

    @Override
    public void hidePlayer(Player player) {

    }

    @Override
    public void hideFrom(Player player) {

    }

    @Override
    public void hidePlayerGlobal() {

    }

    @Override
    public void hideFromAll() {

    }

    @Override
    public void showPlayer(Player player) {

    }

    @Override
    public void showTo(Player player) {

    }

    @Override
    public void showPlayerGlobal() {

    }

    @Override
    public void showToAll() {

    }

    @Override
    public boolean isPlayerHidden(Player player, Player isHidden) {
        return false;
    }

    @Override
    public boolean isHiddenFrom(Player player) {
        return false;
    }

    @Override
    public boolean isHiddenFromAll() {
        return false;
    }

    @Override
    public void setCompassTarget(int x, int y, int z) {

    }

    @Override
    public GameProfile getGameProfile() {
        return super.getGameProfile();
    }

    @Override
    public ChatComponent getDisplayNameComponent() {
        return null;
    }

    @Override
    public void setDisplayNameComponent(ChatComponent component) {

    }

    @Override
    public Inventory getSecondInventory() {
        return null;
    }

    @Override
    public void showTitle(ChatComponent title) {
        this.showTitle(title, null);
    }

    @Override
    public void showTitle(ChatComponent title, ChatComponent subtitle) {
        if (title != null) {
            this.playerNetServerHandler.sendPacket(new S45PacketTitle(S45PacketTitle.Type.TITLE, (IChatComponent) title));

            if (subtitle != null) {
                this.playerNetServerHandler.sendPacket(new S45PacketTitle(S45PacketTitle.Type.SUBTITLE, (IChatComponent) subtitle));
            }
        }
    }

    @Override
    public PermissionProvider getPermissionProvider() {
        return this.permissions;
    }

    @Override
    public Group getGroup() {
        return this.groups.get(0);
    }

    @Override
    public void setGroup(Group group) {
        this.groups.set(0, group);
        Canary.usersAndGroups().addOrUpdatePlayerData(this);
        this.defaultChatPattern.put("%prefix", this.getPrefix()); // Update Prefix
    }

    @Override
    public String getPrefix() {
        if (this.prefix != null) {
            return this.prefix;
        } else if (this.groups.get(0).getPrefix() != null) {
            return this.groups.get(0).getPrefix();
        } else {
            return ChatFormat.WHITE.toString();
        }
    }

    @Override
    public void setPrefix(String prefix) {
        super.setPrefix(prefix);
        Canary.usersAndGroups().addOrUpdatePlayerData(this);
        this.defaultChatPattern.put("%prefix", this.getPrefix());
    }

    @Override
    public boolean isOnline() {
        return Canary.getServer().getPlayer(this.getName()) != null;
    }

    @Override
    public void addGroup(Group group) {
        if (!this.groups.contains(group)) {
            this.groups.add(group);
            Canary.usersAndGroups().addOrUpdatePlayerData(this);
        }
    }

    @Override
    public boolean removeGroup(Group group) {
        boolean success = this.groups.remove(group);
        if (success) {
            Canary.usersAndGroups().addOrUpdatePlayerData(this);
        }
        return success;
    }

    @Override
    public boolean removeGroup(String group) {
        Group g = Canary.usersAndGroups().getGroup(group);
        if (g == null) {
            return false;
        }
        return this.removeGroup(g);
    }

    @Override
    public boolean isInGroup(Group group, boolean parents) {
        for (Group g : this.groups) {
            if (g.getName().equals(group.getName())) {
                return true;
            }
            if (parents) {
                for (Group parent : g.parentsToList()) {
                    if (parent.getName().equals(group.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInGroup(String group, boolean parents) {
        for (Group g : this.groups) {
            if (g.getName().equals(group)) {
                return true;
            }
            if (parents) {
                for (Group parent : g.parentsToList()) {
                    if (parent.getName().equals(group)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public UUID getUUID() {
        return EntityPlayer.getUUID(this.getGameProfile());
    }

    @Override
    public String getUUIDString() {
        return this.getUUID().toString();
    }

    @Override
    public boolean isMuted() {
        return this.muted;
    }

    @Override
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    @Override
    public Group[] getPlayerGroups() {
        return this.groups.toArray(new Group[this.groups.size()]);
    }

    @Override
    public String getFirstJoined() {
        return this.metadata.getString("FirstJoin");
    }

    @Override
    public long getTimePlayed() {
        return this.metadata.getLong("TimePlayed") + (ToolBox.getUnixTimestamp() - this.currentSessionStart);
    }

    @Override
    public GameMode getMode() {
        return GameModeConverter.of(this.theItemInWorldManager.getGameType());
    }

    @Override
    public void setMode(GameMode mode) {
        this.theItemInWorldManager.setGameType(GameModeConverter.of(mode));
    }

    @Override
    public int getModeId() {
        return this.theItemInWorldManager.getGameType().getID();
    }

    @Override
    public void setModeId(int mode) {
        this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(mode));
    }

    @Override
    public boolean isOperator() {
        return Canary.ops().isOpped(this);
    }

    @Override
    public boolean isAdmin() {
        return this.isOperator() || this.hasPermission("canary.super.administrator");
    }

    @Override
    public boolean canBuild() {
        return this.isAdmin() || this.hasPermission("canary.world.build");
    }

    @Override
    public void setCanBuild(boolean canModify) {
        this.permissions.addPermission("canary.world.build", canModify);
    }

    @Override
    public boolean canIgnoreRestrictions() {
        return this.isAdmin() || this.hasPermission("canary.super.ignoreRestrictions");
    }

    @Override
    public void setCanIgnoreRestrictions(boolean canIgnore) {
        this.permissions.addPermission("canary.super.ignoreRestrictions", canIgnore, -1);
    }

    @Override
    public void addExhaustion(float exhaustion) {
        ((IMixinFoodStats) this.foodStats).setExhaustionLevel(this.getExhaustionLevel() + exhaustion);
    }

    @Override
    public void setExhaustion(float exhaustion) {
        ((IMixinFoodStats) this.foodStats).setExhaustionLevel(exhaustion);
    }

    @Override
    public float getExhaustionLevel() {
        return ((IMixinFoodStats) this.foodStats).getExhaustionLevel();
    }

    @Override
    public void addSaturation(float saturation) {
        ((IMixinFoodStats) this.foodStats).setSaturationLevel(this.getSaturationLevel() + saturation);
    }

    @Override
    public void setSaturation(float saturation) {
        ((IMixinFoodStats) this.foodStats).setSaturationLevel(saturation);
    }

    @Override
    public float getSaturationLevel() {
        return this.foodStats.getSaturationLevel();
    }

    @Override
    public int getHunger() {
        return this.foodStats.getFoodLevel();
    }

    @Override
    public void setHunger(int hunger) {
        this.foodStats.setFoodLevel(hunger);
    }

    @Override
    public void addExperience(int experience) {

    }

    @Override
    public void removeExperience(int experience) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperience(int xp) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void addLevel(int level) {

    }

    @Override
    public void removeLevel(int level) {

    }

    @Override
    public Location getHome() {
        Warp home = Canary.warps().getHome(this);

        if (home != null) {
            return home.getLocation();
        }
        return this.getSpawnPosition();
    }

    @Override
    public void setHome(Location home) {
        Canary.warps().setHome(this, home);
    }

    @Override
    public boolean hasHome() {
        return Canary.warps().getHome(this) != null;
    }

    @Override
    public String[] getAllowedIPs() {
        return new String[0];
    }

    @Override
    public String getIP() {
        return this.getPlayerIP();
    }

    @Override
    public String getLastJoined() {
        return this.metadata.getString("LastJoin");
    }

    @Override
    public void setStat(Stat stat, int value) {

    }

    @Override
    public void setStat(Statistics stat, int value) {

    }

    @Override
    public void increaseStat(Stat stat, int value) {

    }

    @Override
    public void increaseStat(Statistics stat, int value) {

    }

    @Override
    public void decreaseStat(Stat stat, int value) {

    }

    @Override
    public void decreaseStat(Statistics stat, int value) {

    }

    @Override
    public int getStat(Stat stat) {
        return this.statsFile.readStat((StatBase) stat);
    }

    @Override
    public int getStat(Statistics stat) {
        return getStat(stat.getInstance());
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return this.statsFile.hasAchievementUnlocked((net.minecraft.stats.Achievement) achievement);
    }

    @Override
    public boolean hasAchievement(Achievements achievement) {
        return this.hasAchievement(achievement.getInstance());
    }

    @Override
    public void removeAchievement(Achievement achievement) {

    }

    @Override
    public void removeAchievement(Achievements achievement) {

    }

    @Override
    public void awardAchievement(Achievement achievement) {

    }

    @Override
    public void awardAchievement(Achievements achievement) {

    }

    @Override
    public Inventory getEnderChestInventory() {
        return null;
    }

    @Override
    public String getFqName() {
        return "Player";
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public NBTTagCompound writeCanaryNBT(NBTTagCompound tagCompound) {
        this.metadata.setLong("TimePlayed", this.metadata.getLong("TimePlayed") + (ToolBox.getUnixTimestamp() - this.currentSessionStart));
        this.currentSessionStart = ToolBox.getUnixTimestamp(); // Reset time
        this.metadata.setString("PreviousIP", this.getIP());

        return super.writeCanaryNBT(tagCompound);
    }

    @Override
    public void initializeMetaData() {
        this.metadata.setString("FirstJoin", DateUtils.longToDateTime(System.currentTimeMillis()));
        this.metadata.setString("LastJoin", DateUtils.longToDateTime(System.currentTimeMillis()));
        this.metadata.setLong("TimePlayed", 1L); // Initialise to 1
    }
}
