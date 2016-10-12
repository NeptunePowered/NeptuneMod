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
package org.neptunepowered.vanilla.mixin.minecraft.util;

import net.canarymod.api.chat.ChatFormatting;
import net.canarymod.api.chat.ChatStyle;
import net.canarymod.api.chat.ClickEvent;
import net.canarymod.api.chat.HoverEvent;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.util.ChatStyle.class)
@Implements(@Interface(iface = ChatStyle.class, prefix = "style$"))
public abstract class MixinChatStyle implements ChatStyle {

    @Shadow private net.minecraft.util.ChatStyle parentStyle;
    @Shadow private EnumChatFormatting color;
    @Shadow private Boolean bold;
    @Shadow private Boolean italic;
    @Shadow private Boolean underlined;
    @Shadow private Boolean strikethrough;
    @Shadow private Boolean obfuscated;
    @Shadow private net.minecraft.event.ClickEvent chatClickEvent;
    @Shadow private net.minecraft.event.HoverEvent chatHoverEvent;

    @Shadow public abstract net.minecraft.util.ChatStyle createShallowCopy();
    @Shadow public abstract boolean isEmpty();

    @Override
    public ChatFormatting getColor() {
        return (ChatFormatting) (Object) this.color;
    }

    @Override
    public boolean isBold() {
        return this.bold;
    }

    @Override
    public boolean isItalic() {
        return this.italic;
    }

    @Override
    public boolean isStrikethrough() {
        return this.strikethrough;
    }

    @Override
    public boolean isUnderlined() {
        return this.underlined;
    }

    @Override
    public boolean isObfuscated() {
        return this.obfuscated;
    }

    @Intrinsic
    public boolean style$isEmpty() {
        return this.isEmpty();
    }

    @Override
    public ClickEvent getChatClickEvent() {
        return (ClickEvent) this.chatClickEvent;
    }

    @Override
    public HoverEvent getChatHoverEvent() {
        return (HoverEvent) this.chatHoverEvent;
    }

    @Override
    public ChatStyle setColor(ChatFormatting color) {
        this.color = (EnumChatFormatting) (Object) color;
        return this;
    }

    @Override
    public ChatStyle setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    @Override
    public ChatStyle setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    @Override
    public ChatStyle setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    @Override
    public ChatStyle setUnderlined(boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    @Override
    public ChatStyle setObfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    @Override
    public ChatStyle setChatClickEvent(ClickEvent clickEvent) {
        this.chatClickEvent = (net.minecraft.event.ClickEvent) clickEvent;
        return this;
    }

    @Override
    public ChatStyle setChatHoverEvent(HoverEvent hoverEvent) {
        this.chatHoverEvent = (net.minecraft.event.HoverEvent) hoverEvent;
        return this;
    }

    @Override
    public ChatStyle setParentStyle(ChatStyle chatStyle) {
        this.parentStyle = (net.minecraft.util.ChatStyle) chatStyle;
        return this;
    }

    @Override
    public ChatStyle getParentStyle() {
        return (ChatStyle) this.parentStyle;
    }

    @Override
    public ChatStyle clone() {
        return (ChatStyle) this.createShallowCopy();
    }

}
