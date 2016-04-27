package org.neptunepowered.vanilla.interfaces.minecraft.world.storage;

import net.canarymod.api.world.DimensionType;

public interface IMixinWorldInfo {

    void setDimensionType(DimensionType dimensionType);
}
