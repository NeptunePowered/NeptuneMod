package org.neptunepowered.vanilla.interfaces.minecraft.server;

import net.minecraft.world.WorldServer;

public interface IMixinMinecraftServer {

    void initialWorldChunkLoad(WorldServer world);
}
