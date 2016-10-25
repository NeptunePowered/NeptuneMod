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
package org.neptunepowered.vanilla.world;

import static net.canarymod.Canary.log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.canarymod.Canary;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.UnknownWorldException;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.api.world.WorldType;
import net.canarymod.config.Configuration;
import net.canarymod.config.WorldConfiguration;
import net.canarymod.hook.system.LoadWorldHook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.neptunepowered.vanilla.interfaces.minecraft.server.IMixinMinecraftServer;
import org.neptunepowered.vanilla.interfaces.minecraft.world.IMixinWorld;
import org.neptunepowered.vanilla.interfaces.minecraft.world.storage.IMixinWorldInfo;
import org.neptunepowered.vanilla.util.converter.GameModeConverter;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NeptuneWorldManager implements WorldManager {

    public static final File WORLDS_DIR = new File(Canary.getWorkingDirectory(), "worlds");
    public static final File WORLDS_BACKUP_DIR = new File(Canary.getWorkingDirectory(), "worldsbackup");
    public static final File PLAYERS_DIR = new File(NeptuneWorldManager.WORLDS_DIR, "players");

    private final Map<String, World> loadedWorlds = Maps.newHashMap();
    private final List<String> existingWorlds = Lists.newArrayList();

    public NeptuneWorldManager() {
        if (!WORLDS_DIR.exists()) {
            WORLDS_DIR.mkdirs();
            return;
        }

        final File[] worlds = WORLDS_DIR.listFiles(File::isDirectory);
        if (worlds == null) {
            return;
        }

        for (File world : worlds) {
            final File[] dimensions = world.listFiles(pathname -> pathname.isDirectory() && pathname.getName().contains("_"));
            if (dimensions == null) {
                continue;
            }

            for (File dimension : dimensions) {
                this.existingWorlds.add(dimension.getName());
            }
        }
    }

    @Override
    public World getWorld(String name, boolean autoload) {
        if (name == null || name.isEmpty()) {
            // assume the world is the default world
            name = Configuration.getServerConfig().getDefaultWorldName() + "_" + DimensionType.fromId(0).getName();
        }
        final String world = name.substring(0, Math.max(0, name.lastIndexOf("_")));
        final DimensionType type = DimensionType.fromName(name.substring(Math.max(0, name.lastIndexOf("_") + 1)));

        if (type != null) {
            return this.getWorld(world, type, autoload);
        }

        if (this.worldIsLoaded(name)) {
            return this.loadedWorlds.get(name);
        } else if (this.worldIsLoaded(name, DimensionType.NORMAL)) {
            return this.loadedWorlds.get(name + "_NORMAL");
        } else {
            if (autoload) {
                if (this.worldExists(name)) {
                    return loadWorld(name, DimensionType.NORMAL);
                } else if (this.worldExists(name + "_NORMAL")) {
                    return loadWorld(name, DimensionType.NORMAL);
                } else {
                    throw new UnknownWorldException("World " + name + " is unknown. Autoload was enabled for this call.");
                }
            } else {
                throw new UnknownWorldException("World " + name + " is not loaded. Autoload was disabled for this call.");
            }
        }
    }

    @Override
    public World getWorld(String world, DimensionType type, boolean autoload) {
        if (world == null || world.isEmpty()) {
            // assume that the world is the default world
            world = Configuration.getServerConfig().getDefaultWorldName();
        }
        final String worldId = world + "_" + type.getName();

        if (this.worldIsLoaded(worldId)) {
            return this.loadedWorlds.get(worldId);
        } else {
            if (this.worldExists(worldId) && autoload) {
                log.debug("World exists but is not loaded. Loading ...");
                return this.loadWorld(world, type);
            } else {
                if (autoload) {
                    log.debug("World does not exist, we can autoload, will load!");
                    this.createWorld(world, type);
                    return this.loadedWorlds.get(worldId);
                } else {
                    throw new UnknownWorldException("Tried to get a none existent world: " + world + " (" + type.toString() +
                            ") either use autoload or have it pre-created!");
                }
            }
        }
    }

    @Override
    public boolean createWorld(String name, DimensionType type) {
        return this.createWorld(name, System.currentTimeMillis(), type, WorldType.DEFAULT);
    }

    @Override
    public boolean createWorld(String name, long seed, DimensionType type) {
        return this.createWorld(name, seed, type, WorldType.DEFAULT);
    }

    @Override
    public boolean createWorld(String name, long seed, DimensionType dimensionType, WorldType worldType) {
        WorldConfiguration worldConfiguration = WorldConfiguration.create(name, dimensionType);

        if (worldConfiguration == null) {
            log.debug("World configuration already exists for " + name + "_" + dimensionType.getName());
            worldConfiguration = Configuration.getWorldConfig(name + "_" + dimensionType.getName());
        } else {
            log.debug("Updating world configuration for " + name + "_" + dimensionType.getName());
            worldConfiguration.getFile().setLong("world-seed", seed);
            worldConfiguration.getFile().setString("world-type", worldType.toString());
        }

        return this.createWorld(worldConfiguration);
    }

    @Override
    public boolean createWorld(WorldConfiguration worldConfiguration) {
        if (worldConfiguration == null) {
            return false;
        }

        final MinecraftServer minecraftServer = MinecraftServer.getServer();

        final String worldFqName = worldConfiguration.getFile().getFileName().replace(".cfg", "");
        final String worldName = worldFqName.replaceAll("_.+", "");
        final DimensionType dimensionType = DimensionType.fromName(worldFqName.replaceAll(".+_", ""));
        final long seed = worldConfiguration.getWorldSeed().matches("\\d+") ?
                Long.valueOf(worldConfiguration.getWorldSeed()) : worldConfiguration.getWorldSeed().hashCode();

        final AnvilSaveHandler saveHandler = new AnvilSaveHandler(WORLDS_DIR, worldName, true);

        final WorldSettings worldSettings = new WorldSettings(
                seed,
                GameModeConverter.of(worldConfiguration.getGameMode()),
                worldConfiguration.generatesStructures(),
                false,
                net.minecraft.world.WorldType.parseWorldType(worldConfiguration.getWorldType().toString()));
        final WorldInfo worldInfo = new WorldInfo(worldSettings, worldName);
        ((IMixinWorldInfo) worldInfo).setDimensionType(dimensionType);

        final WorldServer worldServer;
        if (dimensionType == DimensionType.NORMAL) {
            worldServer = new WorldServer(minecraftServer, saveHandler, worldInfo, dimensionType.getId(),
                    minecraftServer.theProfiler);
        } else {
            worldServer = new WorldServerMulti(minecraftServer, saveHandler, dimensionType.getId(),
                    (WorldServer) this.getWorld(worldName, DimensionType.NORMAL, true), minecraftServer.theProfiler);
            ((IMixinWorld) worldServer).setWorldInfo(worldInfo);
        }

        worldServer.initialize(worldSettings);
        worldServer.addWorldAccess(new net.minecraft.world.WorldManager(minecraftServer, worldServer));
        worldServer.getWorldInfo().setGameType(GameModeConverter.of(worldConfiguration.getGameMode()));
        minecraftServer.getConfigurationManager().setPlayerManager(new WorldServer[] { worldServer });
        worldServer.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(worldConfiguration.getDifficulty().getId()));

        ((IMixinMinecraftServer) minecraftServer).prepareSpawnArea(worldServer);

        this.existingWorlds.add(worldName + "_" + dimensionType.getName());
        log.debug(String.format("Adding new world to world manager, filed as %s_%s", worldName, dimensionType.getName()));
        this.loadedWorlds.put(worldName + "_" + dimensionType.getName(), (World) worldServer);
        new LoadWorldHook((World) worldServer).call();
        return true;
    }

    @Override
    public boolean destroyWorld(String name) {
        final File worldDir = new File(WORLDS_DIR, name.replaceAll("_.+", "") + "/" + name);
        final File backupDir = new File(WORLDS_BACKUP_DIR, name + "(" + System.currentTimeMillis() + ")");
        final boolean success = backupDir.mkdirs() && worldDir.renameTo(new File(backupDir, worldDir.getName()));
        if (success) {
            this.existingWorlds.remove(name);
        }
        return success;
    }

    @Override
    public World loadWorld(String s, DimensionType dimensionType) {
        return null;
    }

    @Override
    public void unloadWorld(String s, DimensionType dimensionType, boolean b) {

    }

    @Override
    public Collection<World> getAllWorlds() {
        return Lists.newArrayList(this.loadedWorlds.values());
    }

    @Override
    public boolean worldIsLoaded(String s) {
        return this.loadedWorlds.containsKey(s);
    }

    @Override
    public boolean worldIsLoaded(String name, DimensionType type) {
        return this.worldIsLoaded(name + "_" + type.getName());
    }

    @Override
    public boolean worldExists(String name) {
        return this.existingWorlds.contains(name);
    }

    @Override
    public List<String> getExistingWorlds() {
        return Lists.newArrayList(this.existingWorlds);
    }

    @Override
    public String[] getExistingWorldsArray() {
        return this.existingWorlds.toArray(new String[this.existingWorlds.size()]);
    }

    @Override
    public String[] getLoadedWorldsNames() {
        return this.loadedWorlds.keySet().toArray(new String[this.loadedWorlds.keySet().size()]);
    }

    @Override
    public String[] getLoadedWorldsNamesOfDimension(DimensionType dimensionType) {
        final List<String> loadedWorlds = this.loadedWorlds.values().stream()
                .filter(w -> w.getType() == dimensionType)
                .map(World::getFqName)
                .collect(Collectors.toList());
        return loadedWorlds.toArray(new String[loadedWorlds.size()]);
    }

}
