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
package org.neptunepowered.vanilla.mixin.minecraft.server.dedicated;

import net.canarymod.Canary;
import net.canarymod.config.Configuration;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import org.neptunepowered.vanilla.Neptune;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(DedicatedServer.class)
public abstract class MixinDedicatedServer extends MinecraftServer {

    @Shadow private boolean guiIsEnabled;

    MixinDedicatedServer() {
        super(null, null, null);
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadAllWorlds(Ljava/lang/String;"
                    + "Ljava/lang/String;JLnet/minecraft/world/WorldType;Ljava/lang/String;)V"))
    public void onStartServer(CallbackInfoReturnable<Boolean> ci) throws IOException {
        Canary.enableEarlyPlugins();
        ((Neptune) Canary.instance()).lateInitialisation();
        Canary.enableLatePlugins();
    }

    @Redirect(method = "startServer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/PropertyManager;"
                    + "getStringProperty(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))
    public String handleStringProperties(PropertyManager propertyManager, String key, String defaultValue) {
        if ("server-ip".equals(key)) {
            return Configuration.getServerConfig().getBindIp();
        } else if ("motd".equals(key)) {
            return Configuration.getServerConfig().getMotd();
        } else if ("level-name".equals(key)) {
            return Configuration.getServerConfig().getDefaultWorldName();
        } else if ("level-seed".equals(key)) {
            return Configuration.getWorldConfig(this.getFolderName() + "_NORMAL").getWorldSeed();
        } else if ("level-type".equals(key)) {
            return Configuration.getWorldConfig(this.getFolderName() + "_NORMAL").getWorldType().toString();
        } else if ("generator-type".equals(key)) {
            return Configuration.getWorldConfig(this.getFolderName() + "_NORMAL").getGeneratorSettings();
        } else {
            return propertyManager.getStringProperty(key, defaultValue);
        }
    }

    /**
     * @author jamierocks - 30th September 2016
     * @reason The GUI has long been not recommended for use with Neptune, and on top that it no longer works
     * with Neptune. It also takes up a considerable amount of resources.
     */
    @Overwrite
    public void setGuiEnabled() {
        //MinecraftServerGui.createServerGui(this);
        this.guiIsEnabled = true;
    }

    /**
     * @author jamierocks - 26th April 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     */
    @Overwrite
    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("whitelist_enabled", Configuration.getServerConfig().isWhitelistEnabled());
        playerSnooper.addClientStat("whitelist_count", Canary.whitelist().getSize());
        super.addServerStatsToSnooper(playerSnooper);
    }

    /**
     * @author jamierocks - 26th April 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     */
    @Overwrite
    public boolean isSnooperEnabled() {
        return Configuration.getServerConfig().isSnooperEnabled();
    }

    /**
     * @author jamierocks - 26th April 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     */
    @Overwrite
    public boolean isCommandBlockEnabled() {
        return Configuration.getServerConfig().isCommandBlockEnabled();
    }

    /**
     * @author jamierocks - 26th April 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     */
    @Overwrite
    public boolean isAnnouncingPlayerAchievements() {
        return Configuration.getServerConfig().getAnnounceAchievements();
    }

    /**
     * @author jamierocks - 26th April 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     */
    @Overwrite
    public int getNetworkCompressionTreshold() {
        return Configuration.getServerConfig().getNetworkCompressionThreshold();
    }

    /**
     * @author jamierocks - 26th April 2016
     * @reason Overwrite to use Canary's {@link Configuration} rather than the original server.properties
     */
    @Overwrite
    public long getMaxTickTime() {
        return Configuration.getServerConfig().getMaxTickTime();
    }

}
