/*
 * This file is part of NeptuneCommon, licensed under the MIT License (MIT).
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
package org.neptunepowered.vanilla.wrapper.chat;

import com.google.common.collect.Lists;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.chat.ChatStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.neptunepowered.vanilla.interfaces.minecraft.util.IMixinChatComponentText;
import org.neptunepowered.vanilla.util.Wrapper;

import java.util.List;

public class NeptuneChatComponent extends Wrapper<IChatComponent> implements ChatComponent {

    public NeptuneChatComponent(IChatComponent chatComponent) {
        super(chatComponent);
    }

    @Override
    public ChatComponent setChatStyle(ChatStyle style) {
        return (ChatComponent) getHandle().setChatStyle((net.minecraft.util.ChatStyle) style);
    }

    @Override
    public ChatStyle getChatStyle() {
        return (ChatStyle) getHandle().getChatStyle();
    }

    @Override
    public ChatComponent setText(String text) {
        if (getHandle() instanceof ChatComponentText) {
            ((IMixinChatComponentText) getHandle()).setText(text);
        }
        return this;
    }

    @Override
    public ChatComponent appendText(String text) {
        return (ChatComponent) getHandle().appendText(text);
    }

    @Override
    public ChatComponent appendSibling(ChatComponent sibling) {
        return (ChatComponent) getHandle().appendSibling(((NeptuneChatComponent) sibling).getHandle());
    }

    @Override
    public String getText() {
        return getHandle().getUnformattedText();
    }

    @Override
    public String getFullText() {
        return getHandle().getUnformattedTextForChat();
    }

    @Override
    public List<ChatComponent> getSiblings() {
        List<ChatComponent> components = Lists.newArrayList();
        for (IChatComponent chatComponent : (List<IChatComponent>) getHandle().getSiblings()) {
            components.add(new NeptuneChatComponent(chatComponent));
        }
        return components;
    }

    @Override
    public String serialize() {
        return IChatComponent.Serializer.componentToJson(getHandle());
    }

    @Override
    public ChatComponent clone() {
        return new NeptuneChatComponent(getHandle().createCopy());
    }
}
