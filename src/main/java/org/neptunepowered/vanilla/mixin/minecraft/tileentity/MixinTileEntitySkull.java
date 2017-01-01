/*
 * This file is part of NeptuneVanilla, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015-2017, Jamie Mansfield <https://github.com/jamierocks>
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
package org.neptunepowered.vanilla.mixin.minecraft.tileentity;

import com.mojang.authlib.GameProfile;
import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.api.PlayerReference;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Skull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntitySkull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntitySkull.class)
@Implements(@Interface(iface = Skull.class, prefix = "skull$"))
public abstract class MixinTileEntitySkull extends MixinTileEntity implements Skull {

    @Shadow private int skullType;
    @Shadow private int skullRotation;
    @Shadow private GameProfile playerProfile;

    @Shadow public abstract int getSkullType();
    @Shadow public abstract void setSkullRotation(int rotation);
    @Shadow public abstract void setPlayerProfile(GameProfile playerProfile);

    @Intrinsic
    public int skull$getSkullType() {
        return this.getSkullType();
    }

    @Override
    public void setSkullType(int i) {
        this.skullType = i;
    }

    @Override
    public String getExtraType() {
        if (this.playerProfile == null) {
            return null;
        }
        if (this.playerProfile.getId() != null) {
            return this.playerProfile.getId().toString();
        } else {
            return this.playerProfile.getName();
        }
    }

    @Override
    public void setExtraType(String s) {
        GameProfile profile = null;
        if (ToolBox.isUUID(s)) {
            Player player = Canary.getServer().getPlayerFromUUID(s);
            if (player != null) {
                profile = player.getGameProfile();
            }
        } else {
            PlayerReference player = Canary.getServer().matchKnownPlayer(s);
            if (player != null && player.isOnline()) {
                profile = ((EntityPlayerMP) player).getGameProfile();
            } else {
                profile = new GameProfile(null, s);
            }
        }

        if (profile != null) {
            this.setPlayerProfile(profile);
        }
    }

    @Override
    public void setSkullAndExtraType(int i, String s) {
        this.setSkullType(i);
        this.setExtraType(s);
    }

    @Override
    public int getRotation() {
        return this.skullRotation;
    }

    @Override
    public void setRotation(int i) {
        this.setSkullRotation(i);
    }

}
