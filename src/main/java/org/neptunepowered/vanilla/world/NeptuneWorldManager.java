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

    private final Map<String, World> loadedWorlds = Maps.newHashMap();
    private final List<String> existingWorlds = Lists.newArrayList();

    public NeptuneWorldManager() {
        File worldsDir = new File(Canary.getWorkingDirectory(), "worlds");
        if (!worldsDir.exists()) {
            worldsDir.mkdirs();
            return;
        }

        File[] worlds = worldsDir.listFiles(File::isDirectory);
        if (worlds == null) {
            return;
        }

        for (File world : worlds) {
            File[] dimensions = world.listFiles(pathname -> pathname.isDirectory() && pathname.getName().contains("_"));
            if (dimensions == null) {
                continue;
            }

            for (File dimension : dimensions) {
                this.existingWorlds.add(dimension.getName());
            }
        }
    }

    @Override
    public World getWorld(String s, boolean b) {
        return null;
    }

    @Override
    public World getWorld(String s, DimensionType dimensionType, boolean b) {
        return null;
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
            Canary.log.debug("World configuration already exists for " + name + "_" + dimensionType.getName());
            worldConfiguration = Configuration.getWorldConfig(name + "_" + dimensionType.getName());
        } else {
            Canary.log.debug("Updating world configuration for " + name + "_" + dimensionType.getName());
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

        final AnvilSaveHandler saveHandler =
                new AnvilSaveHandler(new File(Canary.getWorkingDirectory(), "worlds"), worldName, true);

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
        worldServer.getWorldInfo().setDifficulty(
                EnumDifficulty.getDifficultyEnum(worldConfiguration.getDifficulty().getId()));

        ((IMixinMinecraftServer) minecraftServer).initialWorldChunkLoad(worldServer);

        this.existingWorlds.add(worldName + "_" + dimensionType.getName());
        log.debug(String.format(
                "Adding new world to world manager, filed as %s_%s", worldName, dimensionType.getName()));
        this.loadedWorlds.put(worldName + "_" + dimensionType.getName(), (World) worldServer);
        new LoadWorldHook((World) worldServer).call();
        return true;
    }

    @Override
    public boolean destroyWorld(String s) {
        return false;
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
        return false;
    }

    @Override
    public boolean worldIsLoaded(String name, DimensionType type) {
        return this.loadedWorlds.containsKey(name + "_" + type.getName());
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
        List<String> loadedWorlds = this.loadedWorlds.values().stream()
                .filter(w -> w.getType() == dimensionType)
                .map(w -> w.getFqName())
                .collect(Collectors.toList());
        return loadedWorlds.toArray(new String[loadedWorlds.size()]);
    }
}
