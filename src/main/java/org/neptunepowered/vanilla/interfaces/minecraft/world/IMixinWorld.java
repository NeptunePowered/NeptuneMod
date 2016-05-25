package org.neptunepowered.vanilla.interfaces.minecraft.world;

import net.minecraft.world.storage.WorldInfo;

public interface IMixinWorld {

    void setWorldInfo(WorldInfo worldInfo);
}
