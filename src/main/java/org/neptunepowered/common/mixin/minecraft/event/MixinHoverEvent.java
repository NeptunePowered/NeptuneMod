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
package org.neptunepowered.common.mixin.minecraft.event;

import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.chat.HoverEvent;
import net.canarymod.api.chat.HoverEventAction;
import net.minecraft.util.IChatComponent;
import org.neptunepowered.common.wrapper.chat.NeptuneChatComponent;
import org.neptunepowered.common.wrapper.chat.NeptuneHoverEventAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.event.HoverEvent.class)
public class MixinHoverEvent implements HoverEvent {

    @Shadow private net.minecraft.event.HoverEvent.Action action;
    @Shadow private IChatComponent value;

    @Override
    public HoverEventAction getAction() {
        return new NeptuneHoverEventAction(action);
    }

    @Override
    public ChatComponent getValue() {
        return new NeptuneChatComponent(value);
    }
}
