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
package org.neptunepowered.vanilla.mixin.minecraft.util;

import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.chat.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IChatComponent.class)
@Implements(@Interface(iface = ChatComponent.class, prefix = "component$"))
public interface MixinIChatComponent extends IChatComponent {

    default ChatComponent component$setChatStyle(ChatStyle chatStyle) {
        return (ChatComponent) this.setChatStyle((net.minecraft.util.ChatStyle) chatStyle);
    }

    default ChatStyle component$getChatStyle() {
        return (ChatStyle) this.getChatStyle();
    }

    default ChatComponent component$setText(String s) {
        return (ChatComponent) this;
    }

    default ChatComponent component$appendText(String var1) {
        return (ChatComponent) this.appendText(var1);
    }

    default ChatComponent component$appendSibling(ChatComponent chatComponent) {
        return (ChatComponent) this.appendSibling((IChatComponent) chatComponent);
    }

    default String component$getText() {
        return this.getUnformattedText();
    }

    default String component$getFullText() {
        return this.getUnformattedTextForChat();
    }

    default String component$serialize() {
        return IChatComponent.Serializer.componentToJson(this);
    }

    default ChatComponent component$clone() {
        return (ChatComponent) this.createCopy();
    }

}
