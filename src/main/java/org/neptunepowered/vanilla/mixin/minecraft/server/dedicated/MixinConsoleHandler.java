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
package org.neptunepowered.vanilla.mixin.minecraft.server.dedicated;

import jline.console.ConsoleReader;
import jline.console.UserInterruptException;
import net.canarymod.Canary;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.EnumChatFormatting;
import org.neptunepowered.vanilla.console.ConsoleCommandCompleter;
import org.neptunepowered.vanilla.console.ConsoleFormatter;
import org.neptunepowered.vanilla.launch.console.TerminalConsoleAppender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServer$2")
public abstract class MixinConsoleHandler extends Thread {

    @Shadow(remap = false, aliases = {"field_72428_a", "this$0"})
    private DedicatedServer server;

    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRun(CallbackInfo ci) {
        final ConsoleReader reader = TerminalConsoleAppender.getReader();

        if (reader != null) {
            TerminalConsoleAppender.setFormatter(ConsoleFormatter::format);
            reader.addCompleter(new ConsoleCommandCompleter());

            reader.setPrompt("> ");
            reader.setHandleUserInterrupt(true);

            try {
                String line;
                while (!this.server.isServerStopped() && this.server.isServerRunning()) {
                    try {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }

                        line = line.trim();
                        final String lineFinal = line;
                        if (!line.isEmpty()) {
                            this.server.addScheduledTask(() -> Canary.getServer().consoleCommand(lineFinal));
                        }
                    } catch (IOException e) {
                        Canary.log.error("Exception handling console input", e);
                    }
                }
            } catch (UserInterruptException exception) {
                reader.shutdown();
                Canary.commands().parseCommand(Canary.getServer(), "stop", new String[]{ "stop", "Ctrl-C", "at", "console" });
            }

            ci.cancel();
        } else {
            TerminalConsoleAppender.setFormatter(EnumChatFormatting::getTextWithoutFormattingCodes);
        }
    }

}
