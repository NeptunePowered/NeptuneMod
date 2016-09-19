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
import net.canarymod.Canary;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.EntityItem;
import net.canarymod.api.entity.living.humanoid.Human;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.PlayerInventory;
import net.canarymod.hook.player.BedEnterHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import org.neptunepowered.vanilla.chat.NeptuneChatComponent;
import org.neptunepowered.vanilla.mixin.minecraft.entity.MixinEntityLivingBase;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
@Implements(@Interface(iface = Human.class, prefix = "human$"))
public abstract class MixinEntityPlayer extends MixinEntityLivingBase implements Human {

    @Shadow public PlayerCapabilities capabilities;
    @Shadow protected FoodStats foodStats;
    @Shadow private ItemStack itemInUse;

    protected String prefix = null;
    private boolean sleepIgnored;
    private ChatComponent displayName;

    @Shadow
    public GameProfile getGameProfile() {
        return null;
    }

    @Shadow
    public abstract boolean isBlocking();

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract boolean isPlayerSleeping();

    @Shadow
    public abstract boolean isPlayerFullyAsleep();

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
        return this.displayName != null ? this.displayName.getFullText() : null;
    }

    @Override
    public void setDisplayName(String name) {
        this.setDisplayNameComponent(name != null && !name.isEmpty() ? Canary.factory().getChatComponentFactory().newChatComponent(name) : null);
    }

    public void setDisplayNameComponent(ChatComponent displayName) {
        this.displayName = displayName;

        String serialised = "";
        if (this.displayName != null) {
            serialised = IChatComponent.Serializer.componentToJson(((NeptuneChatComponent) displayName).getHandle());
        }

        this.metadata.setString("displayName", serialised);
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
        return this.prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Intrinsic
    public boolean human$isBlocking() {
        return this.isBlocking();
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
        return this.isPlayerSleeping();
    }

    @Override
    public boolean isDeeplySleeping() {
        return this.isPlayerFullyAsleep();
    }

    @Override
    public boolean isSleepingIgnored() {
        return this.sleepIgnored;
    }

    @Override
    public void setSleepingIgnored(boolean ignored) {
        this.sleepIgnored = ignored;
    }

    @Intrinsic
    public boolean human$isUsingItem() {
        return this.isUsingItem();
    }

    @Override
    public Item getItemInUse() {
        return (Item) this.itemInUse;
    }

    @Override
    public NBTTagCompound writeCanaryNBT(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("SleepingIgnored", this.sleepIgnored);

        return super.writeCanaryNBT(tagCompound);
    }

    @Override
    public void readCanaryNBT(NBTTagCompound tagCompound) {
        if (tagCompound.hasKey("SleepingIgnored")) {
            this.sleepIgnored = tagCompound.getBoolean("SleepingIgnored");
        }

        super.readCanaryNBT(tagCompound);
    }

}
