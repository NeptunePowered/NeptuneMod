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
package org.neptunepowered.vanilla.mixin.minecraft.tileentity;

import com.google.common.collect.Lists;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Sign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.neptunepowered.vanilla.chat.NeptuneChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TileEntitySign.class)
public abstract class MixinTileEntitySign extends MixinTileEntity implements Sign {

    @Shadow public IChatComponent[] signText;
    @Shadow private boolean isEditable;
    @Shadow private EntityPlayer player;

    @Override
    public String[] getText() {
        List<String> lines = Lists.newArrayList();

        for (IChatComponent chatComponent : this.signText) {
            lines.add(chatComponent.getUnformattedText());
        }

        return lines.toArray(new String[lines.size()]);
    }

    @Override
    public ChatComponent[] getLines() {
        List<ChatComponent> chatComponents = Lists.newArrayList();

        for (IChatComponent chatComponent : this.signText) {
            chatComponents.add(new NeptuneChatComponent(chatComponent));
        }

        return chatComponents.toArray(new ChatComponent[chatComponents.size()]);
    }

    @Override
    public String getTextOnLine(int i) {
        return this.getText()[i];
    }

    @Override
    public ChatComponent getComponentOnLine(int i) {
        return this.getLines()[i];
    }

    @Override
    public void setText(String[] strings) {
        int i = 0;
        for (String line : strings) {
            this.signText[i] = new ChatComponentText(line);
            i++;
        }
    }

    @Override
    public void setComponents(ChatComponent[] chatComponents) {
        int i = 0;
        for (ChatComponent line : chatComponents) {
            this.signText[i] = ((NeptuneChatComponent) line).getHandle();
            i++;
        }
    }

    @Override
    public void setTextOnLine(String s, int i) {
        this.signText[i] = new ChatComponentText(s);
    }

    @Override
    public void setComponentOnLine(ChatComponent chatComponent, int i) {
        this.signText[i] = ((NeptuneChatComponent) chatComponent).getHandle();
    }

    @Override
    public boolean isWallSign() {
        return getBlock().getType() == BlockType.WallSign;
    }

    @Override
    public boolean isSignPost() {
        return getBlock().getType() == BlockType.StandingSign;
    }

    @Override
    public Block getBlockAttached() {
        return null;
    }

    @Override
    public boolean isEditable() {
        return this.isEditable;
    }

    @Override
    public void setEditable(boolean b) {
        this.isEditable = b;
    }

    @Override
    public String getOwnerName() {
        return this.player.getName();
    }

    @Override
    public Player getOwner() {
        return (Player) this.player;
    }

    @Override
    public void setOwner(Player player) {
        this.player = (EntityPlayer) player;
    }
}
