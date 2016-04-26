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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneChatComponent;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneClickEventAction;
import org.neptunepowered.vanilla.wrapper.chat.NeptuneHoverEventAction;

public class NeptuneChatComponentFactory implements ChatComponentFactory {

    @Override
    public ChatComponent newChatComponent(String text) {
        return new NeptuneChatComponent(new TextComponentString(text));
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
        return new NeptuneChatComponent(ITextComponent.Serializer.jsonToComponent(json));
    }

    @Override
    public ChatFormatting getFormattingByName(String name) {
        return (ChatFormatting) (Object) TextFormatting.getValueByName(name);
    }

    @Override
    public ChatFormatting getStyleByChar(char charcode) {
        for (TextFormatting chatFormatting : TextFormatting.values()) {
            if (chatFormatting.formattingCode == charcode) {
                return (ChatFormatting) (Object) chatFormatting;
            }
        }
        return null;
    }

    @Override
    public ChatFormatting colorBlack() {
        return (ChatFormatting) (Object) TextFormatting.BLACK;
    }

    @Override
    public ChatFormatting colorDarkBlue() {
        return (ChatFormatting) (Object) TextFormatting.DARK_BLUE;
    }

    @Override
    public ChatFormatting colorDarkGreen() {
        return (ChatFormatting) (Object) TextFormatting.DARK_GREEN;
    }

    @Override
    public ChatFormatting colorDarkAqua() {
        return (ChatFormatting) (Object) TextFormatting.DARK_AQUA;
    }

    @Override
    public ChatFormatting colorDarkRed() {
        return (ChatFormatting) (Object) TextFormatting.DARK_RED;
    }

    @Override
    public ChatFormatting colorDarkPurple() {
        return (ChatFormatting) (Object) TextFormatting.DARK_PURPLE;
    }

    @Override
    public ChatFormatting colorGold() {
        return (ChatFormatting) (Object) TextFormatting.GOLD;
    }

    @Override
    public ChatFormatting colorGray() {
        return (ChatFormatting) (Object) TextFormatting.GRAY;
    }

    @Override
    public ChatFormatting colorDarkGray() {
        return (ChatFormatting) (Object) TextFormatting.DARK_GRAY;
    }

    @Override
    public ChatFormatting colorBlue() {
        return (ChatFormatting) (Object) TextFormatting.BLUE;
    }

    @Override
    public ChatFormatting colorGreen() {
        return (ChatFormatting) (Object) TextFormatting.GREEN;
    }

    @Override
    public ChatFormatting colorAqua() {
        return (ChatFormatting) (Object) TextFormatting.AQUA;
    }

    @Override
    public ChatFormatting colorRed() {
        return (ChatFormatting) (Object) TextFormatting.RED;
    }

    @Override
    public ChatFormatting colorLightPurple() {
        return (ChatFormatting) (Object) TextFormatting.LIGHT_PURPLE;
    }

    @Override
    public ChatFormatting colorYellow() {
        return (ChatFormatting) (Object) TextFormatting.YELLOW;
    }

    @Override
    public ChatFormatting colorWhite() {
        return (ChatFormatting) (Object) TextFormatting.WHITE;
    }

    @Override
    public ChatFormatting styleObfuscated() {
        return (ChatFormatting) (Object) TextFormatting.OBFUSCATED;
    }

    @Override
    public ChatFormatting styleBold() {
        return (ChatFormatting) (Object) TextFormatting.BOLD;
    }

    @Override
    public ChatFormatting styleStrikethrough() {
        return (ChatFormatting) (Object) TextFormatting.STRIKETHROUGH;
    }

    @Override
    public ChatFormatting styleUnderline() {
        return (ChatFormatting) (Object) TextFormatting.UNDERLINE;
    }

    @Override
    public ChatFormatting styleItalic() {
        return (ChatFormatting) (Object) TextFormatting.ITALIC;
    }

    @Override
    public ChatFormatting styleReset() {
        return (ChatFormatting) (Object) TextFormatting.RESET;
    }

    @Override
    public ClickEvent newClickEvent(ClickEventAction action, String value) {
        return (ClickEvent) new net.minecraft.util.text.event.ClickEvent(((NeptuneClickEventAction) action).getHandle(), value);
    }

    @Override
    public ClickEventAction getClickEventActionByName(String name) {
        return new NeptuneClickEventAction(net.minecraft.util.text.event.ClickEvent.Action.getValueByCanonicalName(name));
    }

    @Override
    public ClickEventAction getOpenURL() {
        return new NeptuneClickEventAction(net.minecraft.util.text.event.ClickEvent.Action.OPEN_URL);
    }

    @Override
    public ClickEventAction getOpenFile() {
        return new NeptuneClickEventAction(net.minecraft.util.text.event.ClickEvent.Action.OPEN_FILE);
    }

    @Override
    public ClickEventAction getRunCommand() {
        return new NeptuneClickEventAction(net.minecraft.util.text.event.ClickEvent.Action.RUN_COMMAND);
    }

    @Override
    public ClickEventAction getSuggestCommand() {
        return new NeptuneClickEventAction(net.minecraft.util.text.event.ClickEvent.Action.SUGGEST_COMMAND);
    }

    @Override
    public HoverEvent newHoverEvent(HoverEventAction action, ChatComponent value) {
        return (HoverEvent) new net.minecraft.util.text.event.HoverEvent(((NeptuneHoverEventAction) action).getHandle(),
                ((NeptuneChatComponent) value).getHandle());
    }

    @Override
    public HoverEventAction getHoverEventActionByName(String name) {
        return new NeptuneHoverEventAction(net.minecraft.util.text.event.HoverEvent.Action.getValueByCanonicalName(name));
    }

    @Override
    public HoverEventAction getShowText() {
        return new NeptuneHoverEventAction(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT);
    }

    @Override
    public HoverEventAction getShowAchievement() {
        return new NeptuneHoverEventAction(net.minecraft.util.text.event.HoverEvent.Action.SHOW_ACHIEVEMENT);
    }

    @Override
    public HoverEventAction getShowItem() {
        return new NeptuneHoverEventAction(net.minecraft.util.text.event.HoverEvent.Action.SHOW_ITEM);
    }
}
