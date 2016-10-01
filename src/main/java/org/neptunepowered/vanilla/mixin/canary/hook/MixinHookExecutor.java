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
package org.neptunepowered.vanilla.mixin.canary.hook;

import co.aikar.timings.TimingsManager;
import com.google.common.collect.ArrayListMultimap;
import net.canarymod.Canary;
import net.canarymod.hook.Hook;
import net.canarymod.hook.HookExecutionException;
import net.canarymod.hook.HookExecutor;
import net.canarymod.plugin.RegisteredPluginListener;
import org.neptunepowered.vanilla.interfaces.canary.plugin.IMixinRegisteredPluginListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(HookExecutor.class)
public class MixinHookExecutor {

    @Shadow @Final public ArrayListMultimap<Class<? extends Hook>, RegisteredPluginListener> listeners;

    @Overwrite
    public void callHook(Hook hook) {
        if(!hook.executed()) {
            hook.hasExecuted();
            if(this.listeners.containsKey(hook.getClass())) {
                Iterator iter = this.listeners.get(hook.getClass()).iterator();

                TimingsManager.PLUGIN_EVENT_HANDLER.startTimingIfSync();
                while(iter.hasNext()) {
                    RegisteredPluginListener listener = (RegisteredPluginListener)iter.next();

                    try {
                        ((IMixinRegisteredPluginListener) listener).getTimingsHandler().startTimingIfSync();
                        listener.execute(hook);
                        ((IMixinRegisteredPluginListener) listener).getTimingsHandler().stopTimingIfSync();
                    } catch (HookExecutionException var5) {
                        ((IMixinRegisteredPluginListener) listener).getTimingsHandler().stopTimingIfSync();
                        Canary.log.error("Exception while executing Hook: " + hook.getHookName() + " in PluginListener: " + listener.getListener().getClass().getSimpleName() + " (Plugin: " + listener.getPlugin().getName() + ")", var5.getCause());
                    }
                }
                TimingsManager.PLUGIN_EVENT_HANDLER.stopTimingIfSync();
            }
        }
    }

}
