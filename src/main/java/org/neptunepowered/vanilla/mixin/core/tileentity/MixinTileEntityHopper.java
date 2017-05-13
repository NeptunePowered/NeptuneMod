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
package org.neptunepowered.vanilla.mixin.core.tileentity;

import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.world.blocks.HopperBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityHopper.class)
public abstract class MixinTileEntityHopper extends MixinTileEntityLockable implements HopperBlock {

    @Shadow private int transferCooldown;

    @Shadow private IInventory getInventoryForHopperTransfer() {
        throw new RuntimeException("noop");
    }

    @Override
    public Inventory getInputInventory() {
        return (Inventory) TileEntityHopper.getHopperInventory((IHopper) this);
    }

    @Override
    public Inventory getOutputInventory() {
        return (Inventory) this.getInventoryForHopperTransfer();
    }

    @Override
    public boolean isConnected() {
        return this.isInputConnected() && this.isOutputConnected();
    }

    @Override
    public boolean isInputConnected() {
        return this.getInputInventory() != null;
    }

    @Override
    public boolean isOutputConnected() {
        return this.getOutputInventory() != null;
    }

    @Override
    public int getTranferCooldown() {
        return this.transferCooldown;
    }

    @Override
    public void setTransferCooldown(int i) {
        this.transferCooldown = i;
    }

}
