package org.neptunepowered.vanilla.mixin.minecraft.world.storage;

import net.canarymod.api.world.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import org.neptunepowered.vanilla.interfaces.minecraft.world.storage.IMixinWorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldInfo.class)
public class MixinWorldInfo implements IMixinWorldInfo {

    @Shadow private int dimension;

    @Override
    public void setDimensionType(DimensionType dimensionType) {
        this.dimension = dimensionType.getId();
    }
}
