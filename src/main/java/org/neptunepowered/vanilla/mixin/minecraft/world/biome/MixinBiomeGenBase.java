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
package org.neptunepowered.vanilla.mixin.minecraft.world.biome;

import net.canarymod.api.world.Biome;
import net.canarymod.api.world.BiomeType;
import net.canarymod.api.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(BiomeGenBase.class)
public abstract class MixinBiomeGenBase implements Biome {

    @Shadow public float temperature;
    @Shadow public int biomeID;
    @Shadow protected boolean enableSnow;
    @Shadow protected boolean enableRain;

    @Shadow public abstract void decorate(net.minecraft.world.World worldIn, Random p_180624_2_, BlockPos p_180624_3_);
    @Shadow public abstract float getSpawningChance();
    @Shadow public abstract int getIntRainfall();
    @Shadow public abstract BiomeGenBase setTemperatureRainfall(float p_76732_1_, float p_76732_2_);
    @Shadow public abstract BiomeGenBase setColor(int p_76739_1_);

    @Override
    public boolean canSpawnLightning() {
        return false; // TODO:
    }

    @Override
    public boolean isTropic() {
        return false;
    }

    @Override
    public float getSpawnChance() {
        return getSpawningChance();
    }

    @Override
    public int getRainfall() {
        return getIntRainfall();
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public void decorate(World world, Random rnd, int x, int z) {
        decorate((net.minecraft.world.WorldServer) world, rnd, new BlockPos(x, 0, z));
    }

    @Override
    public BiomeType getBiomeType() {
        return BiomeType.fromId((byte) biomeID);
    }

    @Override
    public void setTemperatureAndPrecipitation(float temp, float precipitation) {
        setTemperatureRainfall(temp, precipitation);
    }

    @Override
    public void setCanSnow(boolean canSnow) {
        enableSnow = canSnow;
    }

    @Override
    public void setCanRain(boolean canRain) {
        enableRain = canRain;
    }

    @Override
    public boolean canSnow() {
        return enableSnow;
    }

    @Override
    public boolean canRain() {
        return enableRain;
    }

    @Override
    public void setColor(String hexColor) {
        setColor(Integer.parseInt(hexColor.replaceFirst("#", ""), 16));
    }
}
