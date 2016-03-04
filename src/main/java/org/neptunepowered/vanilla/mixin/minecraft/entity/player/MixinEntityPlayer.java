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
package org.neptunepowered.vanilla.mixin.minecraft.entity.player;

import com.mojang.authlib.GameProfile;
import net.canarymod.api.entity.EntityItem;
import net.canarymod.api.entity.living.humanoid.Human;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.PlayerInventory;
import net.canarymod.hook.player.BedEnterHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.neptunepowered.vanilla.mixin.minecraft.entity.MixinEntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase implements Human {

    @Shadow public PlayerCapabilities capabilities;

    @Shadow
    public abstract ITextComponent shadow$getDisplayName();

    @Shadow
    protected GameProfile getGameProfile() {
        return null;
    }

    @Inject(method = "trySleep", at = @At(value = "INVOKE"))
    public void onTrySleep(BlockPos bedLocation, CallbackInfoReturnable<EntityPlayer.EnumStatus> callbackInfo) {
        if (this instanceof Player) {
            BedEnterHook bedEnterHook = (BedEnterHook)
                    new BedEnterHook((Player) this, this.getWorld().getBlockAt(bedLocation.getX(),
                            bedLocation.getY(), bedLocation.getZ())).call();
            if (bedEnterHook.isCanceled()) {
                callbackInfo.setReturnValue(EntityPlayer.EnumStatus.OTHER_PROBLEM);
            }
        }
    }

    @Override
    public String getDisplayName() {
        return shadow$getDisplayName().getUnformattedText(); // .getFormattedText(); Doesn't exist on the server!!
    }

    @Override
    public void setDisplayName(String display) {

    }

    @Override
    public void destroyItemHeld() {

    }

    @Override
    public Item getItemHeld() {
        return null;
    }

    @Override
    public void dropItem(Item item) {

    }

    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    @Override
    public EntityItem[] dropInventory() {
        return new EntityItem[0];
    }

    @Override
    public void giveItem(Item item) {

    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void setPrefix(String prefix) {

    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isShooting() {
        return false;
    }

    @Override
    public HumanCapabilities getCapabilities() {
        return (HumanCapabilities) this.capabilities;
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public boolean isDeeplySleeping() {
        return false;
    }

    @Override
    public boolean isSleepingIgnored() {
        return false;
    }

    @Override
    public void setSleepingIgnored(boolean ignored) {

    }

    @Override
    public boolean isUsingItem() {
        return false;
    }

    @Override
    public Item getItemInUse() {
        return null;
    }
}
