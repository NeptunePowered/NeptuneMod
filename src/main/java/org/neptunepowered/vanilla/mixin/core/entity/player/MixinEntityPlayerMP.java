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
package org.neptunepowered.vanilla.mixin.core.entity.player;

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
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.hook.player.ReturnFromIdleHook;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.hook.system.PermissionCheckHook;
import net.canarymod.permissionsystem.PermissionProvider;
import net.canarymod.user.Group;
import net.canarymod.user.UserAndGroupsProvider;
import net.canarymod.warp.Warp;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.visualillusionsent.utils.DateUtils;
import net.visualillusionsent.utils.StringUtils;
import org.neptunepowered.vanilla.interfaces.core.network.IMixinNetHandlerPlayServer;
import org.neptunepowered.vanilla.interfaces.core.util.IMixinFoodStats;
import org.neptunepowered.vanilla.util.NbtConstants;
import org.neptunepowered.vanilla.util.PermissionConstants;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;
import org.neptunepowered.vanilla.util.converter.PlayerListActionConverter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
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

    @Shadow @Final public MinecraftServer mcServer;
    @Shadow @Final public ItemInWorldManager theItemInWorldManager;
    @Shadow @Final private StatisticsFile statsFile;
    @Shadow private String translator;
    @Shadow public int ping;
    @Shadow public NetHandlerPlayServer playerNetServerHandler;
    @Shadow private long playerLastActiveTime;

    private List<Group> groups;
    private PermissionProvider permissions;
    private boolean muted = false;
    private HashMap<String, String> defaultChatPattern = Maps.newHashMap();
    private long currentSessionStart = ToolBox.getUnixTimestamp();

    @Shadow public abstract void openEditSign(TileEntitySign signTile);
    @Shadow public abstract void closeScreen();
    @Shadow public abstract String getPlayerIP();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruction(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager,
            CallbackInfo info) {
        this.initPlayerData();
    }

    @Inject(method = "markPlayerActive", at = @At("INVOKE"))
    private void onMarkPlayerActive(CallbackInfo info) {
        final long idleTime = MinecraftServer.getCurrentTimeMillis() - this.playerLastActiveTime;
        if (idleTime > 10000) {
            new ReturnFromIdleHook(this, idleTime).call();
        }
    }

    @Redirect(method = "travelToDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/NetHandlerPlayServer;setPlayerLocation(DDDFF)V"
            ))
    private void handleSpawnRotation(NetHandlerPlayServer netHandlerPlayServer, double x, double y, double z, float yaw, float pitch) {
        final Location location = this.getWorld().getSpawnLocation();
        netHandlerPlayServer.setPlayerLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getRotation());
    }

    /**
     * @author jamierocks - 2nd October 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     *         and fire the {@link PlayerDeathHook}.
     */
    @Overwrite
    public void onDeath(DamageSource cause) {
        // Neptune - PlayerDeathHook start
        PlayerDeathHook hook = (PlayerDeathHook) new PlayerDeathHook(
                this,
                (net.canarymod.api.DamageSource) cause,
                (ChatComponent) this.getCombatTracker().getDeathMessage()
        ).call();
        // Neptune - PlayerDeathHook end

        //if (this.worldObj.getGameRules().getBoolean("showDeathMessages")) {
        if (Configuration.getServerConfig().isDeathMessageEnabled()) { // Neptune - Use Canary configuration
            Team team = this.getTeam();

            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                    this.mcServer.getConfigurationManager()
                            .sendMessageToAllTeamMembers((EntityPlayerMP) (Object) this, (IChatComponent) hook.getDeathMessage1());
                } else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                    this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer(
                            (EntityPlayerMP) (Object) this, (IChatComponent) hook.getDeathMessage1());
                }
            } else {
                this.mcServer.getConfigurationManager().sendChatMsg((IChatComponent) hook.getDeathMessage1());
            }
        }

        if (!this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.dropAllItems();
        }

        for (ScoreObjective scoreobjective : this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount)) {
            Score score = this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective);
            score.func_96648_a();
        }

        EntityLivingBase entitylivingbase = this.getAttackingEntity();

        if (entitylivingbase != null) {
            EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.entityEggs.get(EntityList.getEntityID(entitylivingbase));

            if (entitylist$entityegginfo != null) {
                this.triggerAchievement(entitylist$entityegginfo.field_151513_e);
            }

            entitylivingbase.addToPlayerScore((EntityPlayerMP) (Object) this, this.scoreValue);
        }

        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
        this.getCombatTracker().reset();
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
        for (Group group : subs) {
            if (group != null) {
                this.groups.add(group);
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
        double degrees = (getRotation() - 180) % 360;

        if (degrees < 0) {
            degrees += 360.0;
        }

        if (0 <= degrees && degrees < 22.5) {
            return Direction.NORTH;
        } else if (22.5 <= degrees && degrees < 67.5) {
            return Direction.NORTHEAST;
        } else if (67.5 <= degrees && degrees < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= degrees && degrees < 157.5) {
            return Direction.SOUTHEAST;
        } else if (157.5 <= degrees && degrees < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= degrees && degrees < 247.5) {
            return Direction.SOUTHWEST;
        } else if (247.5 <= degrees && degrees < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= degrees && degrees < 337.5) {
            return Direction.NORTHWEST;
        } else if (337.5 <= degrees && degrees < 360.0) {
            return Direction.NORTH;
        } else {
            return Direction.ERROR;
        }
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
                data.getProfile(),                      // gameProfile
                data.getPing(),                         // ping
                GameModeConverter.of(data.getMode()),   // gameType
                (IChatComponent) data.getDisplayName()) // displayName
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
        this.getWorld().getEntityTracker().hidePlayer(player, this);
    }

    @Override
    public void hideFrom(Player player) {
        this.getWorld().getEntityTracker().hidePlayer(this, player);
    }

    @Override
    public void hidePlayerGlobal() {
        this.getWorld().getEntityTracker().hidePlayerGlobal(this);
    }

    @Override
    public void hideFromAll() {
        this.getWorld().getEntityTracker().hidePlayerGlobal(this);
    }

    @Override
    public void showPlayer(Player player) {
        this.getWorld().getEntityTracker().showPlayer(player, this);
    }

    @Override
    public void showTo(Player player) {
        this.getWorld().getEntityTracker().showPlayer(this, player);
    }

    @Override
    public void showPlayerGlobal() {
        this.getWorld().getEntityTracker().showPlayerGlobal(this);
    }

    @Override
    public void showToAll() {
        this.getWorld().getEntityTracker().showPlayerGlobal(this);
    }

    @Override
    public boolean isPlayerHidden(Player player, Player isHidden) {
        return this.isHiddenFrom(player);
    }

    @Override
    public boolean isHiddenFrom(Player player) {
        return this.getWorld().getEntityTracker().isPlayerHidden(player, this);
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
        final Group g = Canary.usersAndGroups().getGroup(group);
        return g != null && this.removeGroup(g);
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
        return this.metadata.getString(NbtConstants.FIRST_JOINED);
    }

    @Override
    public long getTimePlayed() {
        return this.metadata.getLong(NbtConstants.TIME_PLAYED) + (ToolBox.getUnixTimestamp() - this.currentSessionStart);
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
        return this.isOperator() || this.hasPermission(PermissionConstants.Super.ADMINISTRATOR);
    }

    @Override
    public boolean canBuild() {
        return this.isAdmin() || this.hasPermission(PermissionConstants.World.BUILD);
    }

    @Override
    public void setCanBuild(boolean canModify) {
        this.permissions.addPermission(PermissionConstants.World.BUILD, canModify);
    }

    @Override
    public boolean canIgnoreRestrictions() {
        return this.isAdmin() || this.hasPermission(PermissionConstants.Super.IGNORE_RESTRICTIONS);
    }

    @Override
    public void setCanIgnoreRestrictions(boolean canIgnore) {
        this.permissions.addPermission(PermissionConstants.Super.IGNORE_RESTRICTIONS, canIgnore, -1);
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
        final Warp home = Canary.warps().getHome(this);

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
        return this.metadata.getString(NbtConstants.LAST_JOINED);
    }

    @Override
    public void setStat(Stat stat, int value) {
        this.statsFile.unlockAchievement((EntityPlayerMP) (Object) this, (StatBase) stat, value);
    }

    @Override
    public void setStat(Statistics stat, int value) {
        this.setStat(stat.getInstance(), value);
    }

    @Override
    public void increaseStat(Stat stat, int value) {
        if (value < 0) return;
        this.statsFile.increaseStat((EntityPlayerMP) (Object) this, (StatBase) stat, value);
    }

    @Override
    public void increaseStat(Statistics stat, int value) {
        this.increaseStat(stat.getInstance(), value);
    }

    @Override
    public void decreaseStat(Stat stat, int value) {
        if (value < 0) return;
        this.setStat(stat, this.getStat(stat) - value);
    }

    @Override
    public void decreaseStat(Statistics stat, int value) {
        this.decreaseStat(stat.getInstance(), value);
    }

    @Override
    public int getStat(Stat stat) {
        return this.statsFile.readStat((StatBase) stat);
    }

    @Override
    public int getStat(Statistics stat) {
        return this.getStat(stat.getInstance());
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
        // Ensure all children achievements are removed
        Arrays.stream(Achievements.values())
                .map(Achievements::getInstance).map(Achievement::getParent)
                .filter(achievement::equals).filter(this::hasAchievement)
                .forEach(this::removeAchievement);

        this.statsFile.unlockAchievement((EntityPlayerMP) (Object) this, (StatBase) achievement, 0);
    }

    @Override
    public void removeAchievement(Achievements achievement) {
        this.removeAchievement(achievement.getInstance());
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        // Ensure all parent achievements are awarded
        if (!this.hasAchievement(achievement.getParent())) {
            this.awardAchievement(achievement.getParent());
        }

        this.statsFile.unlockAchievement((EntityPlayerMP) (Object) this, (StatBase) achievement, 1);
        this.statsFile.sendAchievements((EntityPlayerMP) (Object) this);
    }

    @Override
    public void awardAchievement(Achievements achievement) {
        this.awardAchievement(achievement.getInstance());
    }

    @Override
    public Inventory getEnderChestInventory() {
        return (Inventory) this.getEnderChest();
    }

    @Override
    public boolean hasPermission(String permission) {
        if (this.isOperator()) {
            return true;
        }

        final PermissionCheckHook hook = new PermissionCheckHook(permission, this, false);
        // If player has the permission set, use its personal permissions
        if (this.permissions.pathExists(permission)) {
            hook.setResult(this.permissions.queryPermission(permission));
            Canary.hooks().callHook(hook);
            return hook.getResult();
        } else if (this.groups.size() == 1) { // Only main group is set
            hook.setResult(this.groups.get(0).hasPermission(permission));
            Canary.hooks().callHook(hook);
            return hook.getResult();
        }

        // Check sub groups
        for (int i = 1; i < this.groups.size(); i++) {
            // First group that
            if (this.groups.get(i).getPermissionProvider().pathExists(permission)) {
                hook.setResult(this.groups.get(i).hasPermission(permission));
                Canary.hooks().callHook(hook);
                return hook.getResult();
            }
        }

        // No subgroup has permission defined, use what base group has to say
        hook.setResult(this.groups.get(0).hasPermission(permission));
        Canary.hooks().callHook(hook);
        return hook.getResult();
    }

    @Override
    public boolean safeHasPermission(String permission) {
        if (this.isOperator()) {
            return true;
        }

        // If player has the permission set, use its personal permissions
        if (this.permissions.pathExists(permission)) {
            return this.permissions.queryPermission(permission);
        } else if (this.groups.size() == 1) { // Only main group is set
            return this.groups.get(0).hasPermission(permission);
        }

        // Check sub groups
        for (int i = 1; i < this.groups.size(); i++) {
            // First group that
            if (this.groups.get(i).getPermissionProvider().pathExists(permission)) {
                return this.groups.get(i).hasPermission(permission);
            }
        }

        // No subgroup has permission defined, use what base group has to say
        return this.groups.get(0).hasPermission(permission);
    }

    @Override
    public String getLocale() {
        return this.translator;
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
        this.metadata.setLong(NbtConstants.TIME_PLAYED, this.metadata.getLong(NbtConstants.TIME_PLAYED) + (ToolBox.getUnixTimestamp() - this.currentSessionStart));
        this.currentSessionStart = ToolBox.getUnixTimestamp(); // Reset time
        this.metadata.setString(NbtConstants.PREVIOUS_IP, this.getIP());

        return super.writeCanaryNBT(tagCompound);
    }

    @Override
    public void initializeMetaData() {
        if (!this.bukkitData.hasKey(NbtConstants.BUKKIT_FIRST_JOINED)) {
            this.metadata.setString(NbtConstants.FIRST_JOINED, DateUtils.longToDateTime(System.currentTimeMillis()));
        } else {
            this.metadata.setString(NbtConstants.FIRST_JOINED, DateUtils.longToDateTime(this.bukkitData.getLong(NbtConstants.BUKKIT_FIRST_JOINED)));
        }
        if (!this.bukkitData.hasKey(NbtConstants.BUKKIT_LAST_JOINED)) {
            this.metadata.setString(NbtConstants.LAST_JOINED, DateUtils.longToDateTime(System.currentTimeMillis()));
        } else {
            this.metadata.setString(NbtConstants.LAST_JOINED, DateUtils.longToDateTime(this.bukkitData.getLong(NbtConstants.BUKKIT_LAST_JOINED)));
        }
        this.metadata.setLong(NbtConstants.TIME_PLAYED, 1L); // Initialise to 1
    }

}
