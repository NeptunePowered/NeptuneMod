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
package org.neptunepowered.vanilla.factory;

import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.chat.ChatFormatting;
import net.canarymod.api.chat.ClickEvent;
import net.canarymod.api.chat.ClickEventAction;
import net.canarymod.api.chat.HoverEvent;
import net.canarymod.api.chat.HoverEventAction;
import net.canarymod.api.factory.ChatComponentFactory;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatComponent;

public class NeptuneChatComponentFactory implements ChatComponentFactory {

    @Override
    public ChatComponent newChatComponent(String text) {
        return new NeptuneChatComponent(new ChatComponentText(text));
    }

    @Override
    public ChatComponent compileChatComponent(String text) {
        return this.newChatComponent(text);
    }

    @Override
    public String decompileChatComponent(ChatComponent chatComponent) {
        return null;
    }

    @Override
    public ChatComponent deserialize(String json) {
        return new NeptuneChatComponent(IChatComponent.Serializer.jsonToComponent(json));
    }

    @Override
    public ChatFormatting getFormattingByName(String name) {
        return (ChatFormatting) (Object) EnumChatFormatting.getValueByName(name);
    }

    @Override
    public ChatFormatting getStyleByChar(char charcode) {
        for (EnumChatFormatting chatFormatting : EnumChatFormatting.values()) {
            if (chatFormatting.formattingCode == charcode) {
                return (ChatFormatting) (Object) chatFormatting;
            }
        }
        return null;
    }

    @Override
    public ChatFormatting colorBlack() {
        return (ChatFormatting) (Object) EnumChatFormatting.BLACK;
    }

    @Override
    public ChatFormatting colorDarkBlue() {
        return (ChatFormatting) (Object) EnumChatFormatting.DARK_BLUE;
    }

    @Override
    public ChatFormatting colorDarkGreen() {
        return (ChatFormatting) (Object) EnumChatFormatting.DARK_GREEN;
    }

    @Override
    public ChatFormatting colorDarkAqua() {
        return (ChatFormatting) (Object) EnumChatFormatting.DARK_AQUA;
    }

    @Override
    public ChatFormatting colorDarkRed() {
        return (ChatFormatting) (Object) EnumChatFormatting.DARK_RED;
    }

    @Override
    public ChatFormatting colorDarkPurple() {
        return (ChatFormatting) (Object) EnumChatFormatting.DARK_PURPLE;
    }

    @Override
    public ChatFormatting colorGold() {
        return (ChatFormatting) (Object) EnumChatFormatting.GOLD;
    }

    @Override
    public ChatFormatting colorGray() {
        return (ChatFormatting) (Object) EnumChatFormatting.GRAY;
    }

    @Override
    public ChatFormatting colorDarkGray() {
        return (ChatFormatting) (Object) EnumChatFormatting.DARK_GRAY;
    }

    @Override
    public ChatFormatting colorBlue() {
        return (ChatFormatting) (Object) EnumChatFormatting.BLUE;
    }

    @Override
    public ChatFormatting colorGreen() {
        return (ChatFormatting) (Object) EnumChatFormatting.GREEN;
    }

    @Override
    public ChatFormatting colorAqua() {
        return (ChatFormatting) (Object) EnumChatFormatting.AQUA;
    }

    @Override
    public ChatFormatting colorRed() {
        return (ChatFormatting) (Object) EnumChatFormatting.RED;
    }

    @Override
    public ChatFormatting colorLightPurple() {
        return (ChatFormatting) (Object) EnumChatFormatting.LIGHT_PURPLE;
    }

    @Override
    public ChatFormatting colorYellow() {
        return (ChatFormatting) (Object) EnumChatFormatting.YELLOW;
    }

    @Override
    public ChatFormatting colorWhite() {
        return (ChatFormatting) (Object) EnumChatFormatting.WHITE;
    }

    @Override
    public ChatFormatting styleObfuscated() {
        return (ChatFormatting) (Object) EnumChatFormatting.OBFUSCATED;
    }

    @Override
    public ChatFormatting styleBold() {
        return (ChatFormatting) (Object) EnumChatFormatting.BOLD;
    }

    @Override
    public ChatFormatting styleStrikethrough() {
        return (ChatFormatting) (Object) EnumChatFormatting.STRIKETHROUGH;
    }

    @Override
    public ChatFormatting styleUnderline() {
        return (ChatFormatting) (Object) EnumChatFormatting.UNDERLINE;
    }

    @Override
    public ChatFormatting styleItalic() {
        return (ChatFormatting) (Object) EnumChatFormatting.ITALIC;
    }

    @Override
    public ChatFormatting styleReset() {
        return (ChatFormatting) (Object) EnumChatFormatting.RESET;
    }

    @Override
    public ClickEvent newClickEvent(ClickEventAction action, String value) {
        return (ClickEvent) new net.minecraft.event.ClickEvent(
                (net.minecraft.event.ClickEvent.Action) (Object) action, value);
    }

    @Override
    public ClickEventAction getClickEventActionByName(String name) {
        return (ClickEventAction) (Object) net.minecraft.event.ClickEvent.Action.getValueByCanonicalName(name);
    }

    @Override
    public ClickEventAction getOpenURL() {
        return (ClickEventAction) (Object) net.minecraft.event.ClickEvent.Action.OPEN_URL;
    }

    @Override
    public ClickEventAction getOpenFile() {
        return (ClickEventAction) (Object) net.minecraft.event.ClickEvent.Action.OPEN_FILE;
    }

    @Override
    public ClickEventAction getRunCommand() {
        return (ClickEventAction) (Object) net.minecraft.event.ClickEvent.Action.RUN_COMMAND;
    }

    @Override
    public ClickEventAction getSuggestCommand() {
        return (ClickEventAction) (Object) net.minecraft.event.ClickEvent.Action.SUGGEST_COMMAND;
    }

    @Override
    public HoverEvent newHoverEvent(HoverEventAction action, ChatComponent value) {
        return (HoverEvent) new net.minecraft.event.HoverEvent((net.minecraft.event.HoverEvent.Action) (Object) action,
                ((NeptuneChatComponent) value).getHandle());
    }

    @Override
    public HoverEventAction getHoverEventActionByName(String name) {
        return (HoverEventAction) (Object) net.minecraft.event.HoverEvent.Action.getValueByCanonicalName(name);
    }

    @Override
    public HoverEventAction getShowText() {
        return (HoverEventAction) (Object) net.minecraft.event.HoverEvent.Action.SHOW_TEXT;
    }

    @Override
    public HoverEventAction getShowAchievement() {
        return (HoverEventAction) (Object) net.minecraft.event.HoverEvent.Action.SHOW_ACHIEVEMENT;
    }

    @Override
    public HoverEventAction getShowItem() {
        return (HoverEventAction) (Object) net.minecraft.event.HoverEvent.Action.SHOW_ITEM;
    }
}
