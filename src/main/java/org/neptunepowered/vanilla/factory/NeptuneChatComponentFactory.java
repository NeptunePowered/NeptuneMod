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
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatFormatting;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneClickEventAction;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneHoverEventAction;

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
        return null;
    }

    @Override
    public ChatFormatting getStyleByChar(char charcode) {
        return null;
    }

    @Override
    public ChatFormatting colorBlack() {
        return new NeptuneChatFormatting(EnumChatFormatting.BLACK);
    }

    @Override
    public ChatFormatting colorDarkBlue() {
        return new NeptuneChatFormatting(EnumChatFormatting.DARK_BLUE);
    }

    @Override
    public ChatFormatting colorDarkGreen() {
        return new NeptuneChatFormatting(EnumChatFormatting.DARK_GREEN);
    }

    @Override
    public ChatFormatting colorDarkAqua() {
        return new NeptuneChatFormatting(EnumChatFormatting.DARK_AQUA);
    }

    @Override
    public ChatFormatting colorDarkRed() {
        return new NeptuneChatFormatting(EnumChatFormatting.DARK_RED);
    }

    @Override
    public ChatFormatting colorDarkPurple() {
        return new NeptuneChatFormatting(EnumChatFormatting.DARK_PURPLE);
    }

    @Override
    public ChatFormatting colorGold() {
        return new NeptuneChatFormatting(EnumChatFormatting.GOLD);
    }

    @Override
    public ChatFormatting colorGray() {
        return new NeptuneChatFormatting(EnumChatFormatting.GRAY);
    }

    @Override
    public ChatFormatting colorDarkGray() {
        return new NeptuneChatFormatting(EnumChatFormatting.DARK_GRAY);
    }

    @Override
    public ChatFormatting colorBlue() {
        return new NeptuneChatFormatting(EnumChatFormatting.BLUE);
    }

    @Override
    public ChatFormatting colorGreen() {
        return new NeptuneChatFormatting(EnumChatFormatting.GREEN);
    }

    @Override
    public ChatFormatting colorAqua() {
        return new NeptuneChatFormatting(EnumChatFormatting.AQUA);
    }

    @Override
    public ChatFormatting colorRed() {
        return new NeptuneChatFormatting(EnumChatFormatting.RED);
    }

    @Override
    public ChatFormatting colorLightPurple() {
        return new NeptuneChatFormatting(EnumChatFormatting.LIGHT_PURPLE);
    }

    @Override
    public ChatFormatting colorYellow() {
        return new NeptuneChatFormatting(EnumChatFormatting.YELLOW);
    }

    @Override
    public ChatFormatting colorWhite() {
        return new NeptuneChatFormatting(EnumChatFormatting.WHITE);
    }

    @Override
    public ChatFormatting styleObfuscated() {
        return new NeptuneChatFormatting(EnumChatFormatting.OBFUSCATED);
    }

    @Override
    public ChatFormatting styleBold() {
        return new NeptuneChatFormatting(EnumChatFormatting.BOLD);
    }

    @Override
    public ChatFormatting styleStrikethrough() {
        return new NeptuneChatFormatting(EnumChatFormatting.STRIKETHROUGH);
    }

    @Override
    public ChatFormatting styleUnderline() {
        return new NeptuneChatFormatting(EnumChatFormatting.UNDERLINE);
    }

    @Override
    public ChatFormatting styleItalic() {
        return new NeptuneChatFormatting(EnumChatFormatting.ITALIC);
    }

    @Override
    public ChatFormatting styleReset() {
        return new NeptuneChatFormatting(EnumChatFormatting.RESET);
    }

    @Override
    public ClickEvent newClickEvent(ClickEventAction action, String value) {
        return (ClickEvent) new net.minecraft.event.ClickEvent(((NeptuneClickEventAction) action).getHandle(), value);
    }

    @Override
    public ClickEventAction getClickEventActionByName(String name) {
        return new NeptuneClickEventAction(net.minecraft.event.ClickEvent.Action.getValueByCanonicalName(name));
    }

    @Override
    public ClickEventAction getOpenURL() {
        return new NeptuneClickEventAction(net.minecraft.event.ClickEvent.Action.OPEN_URL);
    }

    @Override
    public ClickEventAction getOpenFile() {
        return new NeptuneClickEventAction(net.minecraft.event.ClickEvent.Action.OPEN_FILE);
    }

    @Override
    public ClickEventAction getRunCommand() {
        return new NeptuneClickEventAction(net.minecraft.event.ClickEvent.Action.RUN_COMMAND);
    }

    @Override
    public ClickEventAction getSuggestCommand() {
        return new NeptuneClickEventAction(net.minecraft.event.ClickEvent.Action.SUGGEST_COMMAND);
    }

    @Override
    public HoverEvent newHoverEvent(HoverEventAction action, ChatComponent value) {
        return (HoverEvent) new net.minecraft.event.HoverEvent(((NeptuneHoverEventAction) action).getHandle(),
                ((NeptuneChatComponent) value).getHandle());
    }

    @Override
    public HoverEventAction getHoverEventActionByName(String name) {
        return new NeptuneHoverEventAction(net.minecraft.event.HoverEvent.Action.getValueByCanonicalName(name));
    }

    @Override
    public HoverEventAction getShowText() {
        return new NeptuneHoverEventAction(net.minecraft.event.HoverEvent.Action.SHOW_TEXT);
    }

    @Override
    public HoverEventAction getShowAchievement() {
        return new NeptuneHoverEventAction(net.minecraft.event.HoverEvent.Action.SHOW_ACHIEVEMENT);
    }

    @Override
    public HoverEventAction getShowItem() {
        return new NeptuneHoverEventAction(net.minecraft.event.HoverEvent.Action.SHOW_ITEM);
    }
}
