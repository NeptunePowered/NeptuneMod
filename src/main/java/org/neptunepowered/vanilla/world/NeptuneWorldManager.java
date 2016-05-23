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

import com.google.common.collect.Lists;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.api.world.WorldType;
import net.canarymod.config.WorldConfiguration;

import java.util.Collection;
import java.util.List;

public class NeptuneWorldManager implements WorldManager {

    public NeptuneWorldManager() {

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
    public boolean createWorld(String s, DimensionType dimensionType) {
        return false;
    }

    @Override
    public boolean createWorld(String s, long l, DimensionType dimensionType) {
        return false;
    }

    @Override
    public boolean createWorld(String s, long l, DimensionType dimensionType, WorldType worldType) {
        return false;
    }

    @Override
    public boolean createWorld(WorldConfiguration worldConfiguration) {
        return false;
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
        return Lists.newArrayList();
    }

    @Override
    public boolean worldIsLoaded(String s) {
        return false;
    }

    @Override
    public boolean worldIsLoaded(String s, DimensionType dimensionType) {
        return false;
    }

    @Override
    public boolean worldExists(String s) {
        return false;
    }

    @Override
    public List<String> getExistingWorlds() {
        return Lists.newArrayList();
    }

    @Override
    public String[] getExistingWorldsArray() {
        return new String[0];
    }

    @Override
    public String[] getLoadedWorldsNames() {
        return new String[0];
    }

    @Override
    public String[] getLoadedWorldsNamesOfDimension(DimensionType dimensionType) {
        return new String[0];
    }
}
