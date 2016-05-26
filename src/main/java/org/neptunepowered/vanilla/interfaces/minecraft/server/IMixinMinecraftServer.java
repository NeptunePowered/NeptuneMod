package org.neptunepowered.vanilla.interfaces.minecraft.server;

import net.canarymod.api.world.DimensionType;
import net.minecraft.world.WorldServer;

public interface IMixinMinecraftServer {

    void prepareSpawnArea(WorldServer world);

    void loadWorld(String name, DimensionType dimensionType);
}
