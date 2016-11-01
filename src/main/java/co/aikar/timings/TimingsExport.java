/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
package co.aikar.timings;

import static co.aikar.timings.TimingsManager.HISTORY;

import co.aikar.util.JSONUtil;
import co.aikar.util.JSONUtil.JsonObjectBuilder;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.canarymod.Canary;
import net.canarymod.api.Server;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.chat.ChatFormat;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.config.Configuration;
import net.canarymod.config.ConfigurationContainer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

class TimingsExport extends Thread {

    private static final Joiner RUNTIME_FLAG_JOINER = Joiner.on(" ");

    private final MessageReceiver sender;
    private final JsonObject out;
    private final TimingHistory[] history;

    TimingsExport(MessageReceiver sender, JsonObject out, TimingHistory[] history) {
        super("Timings paste thread");
        this.sender = sender;
        this.out = out;
        this.history = history;
    }

    private static String getServerName() {
        return Canary.getImplementationTitle() + " " + Canary.getImplementationVersion();
    }

    /**
     * Builds an JSON report of the timings to be uploaded for parsing.
     *
     * @param sender Who to report to
     */
    static void reportTimings(MessageReceiver sender) {
        JsonObjectBuilder builder = JSONUtil.objectBuilder()
                // Get some basic system details about the server
                .add("version", Canary.getImplementationVersion())
                .add("maxplayers", Configuration.getServerConfig().getMaxPlayers())
                .add("start", TimingsManager.timingStart / 1000)
                .add("end", System.currentTimeMillis() / 1000)
                .add("sampletime", (System.currentTimeMillis() - TimingsManager.timingStart) / 1000);
        if (!TimingsManager.privacy) {
            builder.add("server", getServerName())
                    .add("motd", Configuration.getServerConfig().getMotd())
                    .add("online-mode", Configuration.getServerConfig().isOnlineMode())
                    .add("icon", MinecraftServer.getServer().getServerStatusResponse().getFavicon());
        }

        final Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        builder.add("system", JSONUtil.objectBuilder()
                .add("timingcost", getCost())
                .add("name", System.getProperty("os.name"))
                .add("version", System.getProperty("os.version"))
                .add("jvmversion", System.getProperty("java.version"))
                .add("arch", System.getProperty("os.arch"))
                .add("maxmem", runtime.maxMemory())
                .add("cpu", runtime.availableProcessors())
                .add("runtime", ManagementFactory.getRuntimeMXBean().getUptime())
                .add("flags", RUNTIME_FLAG_JOINER.join(runtimeBean.getInputArguments()))
                .add("gc", JSONUtil.mapArrayToObject(ManagementFactory.getGarbageCollectorMXBeans(), (input) -> {
                    return JSONUtil.singleObjectPair(input.getName(), JSONUtil.arrayOf(input.getCollectionCount(), input.getCollectionTime()));
                })));

        Set<BlockType> blockTypeSet = Sets.newHashSet();
        Set<EntityType> entityTypeSet = Sets.newHashSet();

        int size = HISTORY.size();
        TimingHistory[] history = new TimingHistory[size + 1];
        int i = 0;
        for (TimingHistory timingHistory : HISTORY) {
            blockTypeSet.addAll(timingHistory.blockTypeSet);
            entityTypeSet.addAll(timingHistory.entityTypeSet);
            history[i++] = timingHistory;
        }

        history[i] = new TimingHistory(); // Current snapshot
        blockTypeSet.addAll(history[i].blockTypeSet);
        entityTypeSet.addAll(history[i].entityTypeSet);

        JsonObjectBuilder handlersBuilder = JSONUtil.objectBuilder();
        for (TimingIdentifier.TimingGroup group : TimingIdentifier.GROUP_MAP.values()) {
            for (TimingHandler id : group.handlers) {
                if (!id.timed && !id.isSpecial()) {
                    continue;
                }
                handlersBuilder.add(id.id, JSONUtil.arrayOf(
                        group.id,
                        id.name));
            }
        }

        builder.add("idmap", JSONUtil.objectBuilder()
                .add("groups", JSONUtil.mapArrayToObject(TimingIdentifier.GROUP_MAP.values(), (group) -> {
                    return JSONUtil.singleObjectPair(group.id, group.name);
                }))
                .add("handlers", handlersBuilder)
                .add("worlds", JSONUtil.mapArrayToObject(TimingHistory.worldMap.entrySet(), (entry) -> {
                    return JSONUtil.singleObjectPair(entry.getValue(), entry.getKey());
                }))
                .add("tileentity", JSONUtil.mapArrayToObject(blockTypeSet, (blockType) -> {
                    return JSONUtil.singleObjectPair(blockType.getId(), blockType.getId());
                }))
                .add("entity", JSONUtil.mapArrayToObject(entityTypeSet, (entityType) -> {
                    if (entityType == EntityType.GENERIC_ENTITY) {
                        return null;
                    }
                    return JSONUtil.singleObjectPair(entityType.getEntityID(), entityType.getEntityID());
                })));

        // Information about loaded plugins

        builder.add("plugins", JSONUtil.mapArrayToObject(Canary.pluginManager().getPlugins(), (plugin) -> {
            return JSONUtil.objectBuilder().add(plugin.getName().toLowerCase(), JSONUtil.objectBuilder()
                    .add("version", plugin.getVersion())
                    .add("description", "")
                    .add("website", "")
                    .add("authors", plugin.getAuthor())
            ).build();
        }));

        // Information on the users Config

        builder.add("config", JSONUtil.objectBuilder()
                .add("timings", serializeConfig(Configuration.getTimingsConfig()))
                .add("worlds", JSONUtil.mapArrayToObject(Canary.getServer().getWorldManager().getAllWorlds(), (world) -> {
                    return JSONUtil.singleObjectPair(world.getFqName(), serializeConfig(Configuration.getWorldConfig(world.getFqName())));
                })));

        new TimingsExport(sender, builder.build(), history).start();
    }

    static long getCost() {
        // Benchmark the users System.nanotime() for cost basis
        int passes = 200;
        TimingHandler SAMPLER1 = NeptuneTimingsFactory.ofSafe("Timings Sampler 1");
        TimingHandler SAMPLER2 = NeptuneTimingsFactory.ofSafe("Timings Sampler 2");
        TimingHandler SAMPLER3 = NeptuneTimingsFactory.ofSafe("Timings Sampler 3");
        TimingHandler SAMPLER4 = NeptuneTimingsFactory.ofSafe("Timings Sampler 4");
        TimingHandler SAMPLER5 = NeptuneTimingsFactory.ofSafe("Timings Sampler 5");
        TimingHandler SAMPLER6 = NeptuneTimingsFactory.ofSafe("Timings Sampler 6");

        long start = System.nanoTime();
        for (int i = 0; i < passes; i++) {
            SAMPLER1.startTiming();
            SAMPLER2.startTiming();
            SAMPLER3.startTiming();
            SAMPLER3.stopTiming();
            SAMPLER4.startTiming();
            SAMPLER5.startTiming();
            SAMPLER6.startTiming();
            SAMPLER6.stopTiming();
            SAMPLER5.stopTiming();
            SAMPLER4.stopTiming();
            SAMPLER2.stopTiming();
            SAMPLER1.stopTiming();
        }
        long timingsCost = (System.nanoTime() - start) / passes / 6;
        SAMPLER1.reset(true);
        SAMPLER2.reset(true);
        SAMPLER3.reset(true);
        SAMPLER4.reset(true);
        SAMPLER5.reset(true);
        SAMPLER6.reset(true);
        return timingsCost;
    }

    private static List<String> RESTRICTED_CONFIG_VALUES = ImmutableList.<String>builder()
            .add("world-seed")
            .build();

    private static JsonElement serializeConfig(ConfigurationContainer configuration) {
        return JSONUtil.mapArrayToObject(configuration.getFile().getPropertiesMap().entrySet(), (value) -> {
            if (!RESTRICTED_CONFIG_VALUES.contains(value.getKey())) {
                return JSONUtil.singleObjectPair(value.getKey(), value.getValue());
            }
            return null;
        });
    }

    @Override
    public synchronized void start() {
        if (this.sender instanceof RConConsoleSource) {
            this.sender.message(ChatFormat.RED + "Warning: Timings report done over RCON will cause lag spikes.");
            this.sender.message(ChatFormat.RED + "You should use " + ChatFormat.YELLOW,
                    "/timings report" + ChatFormat.RED + " in game or console.");
            run();
        } else {
            super.start();
        }
    }

    @Override
    public void run() {
        this.sender.message(ChatFormat.GREEN + "Preparing Timings Report...");

        this.out.add("data", JSONUtil.mapArray(this.history, TimingHistory::export));

        String response = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://timings.aikar.co/post").openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "Neptune/" + getServerName() + "/" + InetAddress.getLocalHost().getHostName());
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(false);

            OutputStream request = new GZIPOutputStream(con.getOutputStream()) {
                {
                    this.def.setLevel(7);
                }
            };

            request.write(JSONUtil.toString(this.out).getBytes("UTF-8"));
            request.close();

            response = getResponse(con);

            if (con.getResponseCode() != 302) {
                this.sender.message(ChatFormat.RED + "Upload Error: " + con.getResponseCode() + ": " + con.getResponseMessage());
                this.sender.message(ChatFormat.RED + "Check your logs for more information");
                if (response != null) {
                    Canary.log.fatal(response);
                }
                return;
            }

            String location = con.getHeaderField("Location");
            this.sender.message(ChatFormat.GREEN + "View Timings Report: " + location);
            if (!(this.sender instanceof Server)) {
                Canary.log.info("View Timings Report: " + location);
            }

            if (response != null && !response.isEmpty()) {
                Canary.log.info("Timing Response: " + response);
            }
        } catch (IOException ex) {
            this.sender.message(ChatFormat.RED + "Error uploading timings, check your logs for more information");
            if (response != null) {
                Canary.log.fatal(response);
            }
            Canary.log.fatal("Could not paste timings", ex);
        }
    }

    private String getResponse(HttpURLConnection con) throws IOException {
        InputStream is = null;
        try {
            is = con.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            return bos.toString();

        } catch (IOException ex) {
            this.sender.message(ChatFormat.RED + "Error uploading timings, check your logs for more information");
            Canary.log.warn(con.getResponseMessage(), ex);
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}
