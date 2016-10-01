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

import static co.aikar.timings.TimingsManager.FULL_SERVER_TICK;
import static co.aikar.timings.TimingsManager.MINUTE_REPORTS;

import co.aikar.util.JSONUtil;
import co.aikar.util.LoadingMap;
import co.aikar.util.MRUMapCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.canarymod.Canary;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.TileEntity;
import org.neptunepowered.vanilla.interfaces.minecraft.world.chunk.IMixinChunk;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TimingHistory {

    public static long lastMinuteTime;
    public static long timedTicks;
    public static long playerTicks;
    public static long entityTicks;
    public static long tileEntityTicks;
    public static long activatedEntityTicks;
    static int worldIdPool = 1;
    static Map<String, Integer> worldMap = LoadingMap.newHashMap((input) -> worldIdPool++);
    final long endTime;
    final long startTime;
    final long totalTicks;
    // Represents all time spent running the server this history
    final long totalTime;
    final MinuteReport[] minuteReports;

    final TimingHistoryEntry[] entries;
    final Set<BlockType> blockTypeSet = Sets.newHashSet();
    final Set<EntityType> entityTypeSet = Sets.newHashSet();
    final JsonObject worlds;

    TimingHistory() {
        this.endTime = System.currentTimeMillis() / 1000;
        this.startTime = TimingsManager.historyStart / 1000;
        if (timedTicks % 1200 != 0 || MINUTE_REPORTS.isEmpty()) {
            this.minuteReports = MINUTE_REPORTS.toArray(new MinuteReport[MINUTE_REPORTS.size() + 1]);
            this.minuteReports[this.minuteReports.length - 1] = new MinuteReport();
        } else {
            this.minuteReports = MINUTE_REPORTS.toArray(new MinuteReport[MINUTE_REPORTS.size()]);
        }
        long ticks = 0;
        for (MinuteReport mp : this.minuteReports) {
            ticks += mp.ticksRecord.timed;
        }
        this.totalTicks = ticks;
        this.totalTime = FULL_SERVER_TICK.record.totalTime;
        this.entries = new TimingHistoryEntry[TimingsManager.HANDLERS.size()];

        int i = 0;
        for (TimingHandler handler : TimingsManager.HANDLERS) {
            this.entries[i++] = new TimingHistoryEntry(handler);
        }

        final Map<EntityType, Counter> entityCounts = MRUMapCache.of(LoadingMap.of(Maps.newHashMap(), Counter.loader()));
        final Map<BlockType, Counter> tileEntityCounts = MRUMapCache.of(LoadingMap.of(Maps.newHashMap(), Counter.loader()));
        // Information about all loaded chunks/entities
        this.worlds = JSONUtil.mapArrayToObject(Canary.getServer().getWorldManager().getAllWorlds(), (world) -> {
            return JSONUtil.singleObjectPair(String.valueOf(worldMap.get(world.getName())), JSONUtil.mapArray(world.getLoadedChunks(), (chunk) -> {
                entityCounts.clear();
                tileEntityCounts.clear();

                for (Entity entity : ((IMixinChunk) chunk).getEntities()) {
                    if (entity.getEntityType() == null) {
                        Canary.log.error("Entity is not registered {}", entity);
                        continue;
                    }
                    entityCounts.get(entity.getEntityType()).increment();
                }

                for (TileEntity tileEntity : ((IMixinChunk) chunk).getTileEntities()) {
                    tileEntityCounts.get(tileEntity.getBlock().getType()).increment();
                }

                if (tileEntityCounts.isEmpty() && entityCounts.isEmpty()) {
                    return null;
                }
                return JSONUtil.arrayOf(
                        chunk.getX(),
                        chunk.getZ(),
                        JSONUtil.mapArrayToObject(entityCounts.entrySet(), (entry) -> {
                            if (entry.getKey() == EntityType.GENERIC_ENTITY) {
                                return null;
                            }
                            this.entityTypeSet.add(entry.getKey());
                            return JSONUtil.singleObjectPair(entry.getKey().getEntityID(), entry.getValue().count());
                        }),
                        JSONUtil.mapArrayToObject(tileEntityCounts.entrySet(), (entry) -> {
                            this.blockTypeSet.add(entry.getKey());
                            return JSONUtil.singleObjectPair(entry.getKey().getId(), entry.getValue().count());
                        }));
            }));
        });

    }

    public static void resetTicks(boolean fullReset) {
        if (fullReset) {
            // Non full is simply for 1 minute reports
            timedTicks = 0;
        }
        lastMinuteTime = System.nanoTime();
        playerTicks = 0;
        tileEntityTicks = 0;
        entityTicks = 0;
        activatedEntityTicks = 0;
    }

    JsonObject export() {
        return JSONUtil.objectBuilder()
                .add("s", this.startTime)
                .add("e", this.endTime)
                .add("tk", this.totalTicks)
                .add("tm", this.totalTime)
                .add("w", this.worlds)
                .add("h", JSONUtil.mapArray(this.entries, (entry) -> entry.data.count == 0 ? null : entry.export()))
                .add("mp", JSONUtil.mapArray(this.minuteReports, MinuteReport::export))
                .build();
    }

    static class MinuteReport {

        final long time = System.currentTimeMillis() / 1000;

        final TicksRecord ticksRecord = new TicksRecord();
        final PingRecord pingRecord = new PingRecord();
        final TimingData fst = TimingsManager.FULL_SERVER_TICK.minuteData.clone();
        final double tps = 1E9 / (System.nanoTime() - lastMinuteTime) * this.ticksRecord.timed;
        final double usedMemory = TimingsManager.FULL_SERVER_TICK.avgUsedMemory;
        final double freeMemory = TimingsManager.FULL_SERVER_TICK.avgFreeMemory;
        final double loadAvg = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

        public JsonArray export() {
            return JSONUtil.arrayOf(
                    this.time,
                    Math.round(this.tps * 100D) / 100D,
                    Math.round(this.pingRecord.avg * 100D) / 100D,
                    this.fst.export(),
                    JSONUtil.arrayOf(this.ticksRecord.timed,
                            this.ticksRecord.player,
                            this.ticksRecord.entity,
                            this.ticksRecord.activatedEntity,
                            this.ticksRecord.tileEntity),
                    this.usedMemory,
                    this.freeMemory,
                    this.loadAvg);
        }
    }

    static class TicksRecord {

        final long timed;
        final long player;
        final long entity;
        final long tileEntity;
        final long activatedEntity;

        TicksRecord() {
            this.timed = timedTicks - (TimingsManager.MINUTE_REPORTS.size() * 1200);
            this.player = playerTicks;
            this.entity = entityTicks;
            this.tileEntity = tileEntityTicks;
            this.activatedEntity = activatedEntityTicks;
        }

    }

    static class PingRecord {

        final double avg;

        PingRecord() {
            final Collection<Player> onlinePlayers = Canary.getServer().getPlayerList();
            int totalPing = 0;
            for (Player player : onlinePlayers) {
                totalPing += player.getPing();
            }
            this.avg = onlinePlayers.isEmpty() ? 0 : totalPing / onlinePlayers.size();
        }
    }

    static class Counter {

        int count = 0;
        private static final Function<?, Counter> LOADER = new LoadingMap.Feeder<Counter>() {

            @Override
            public Counter apply() {
                return new Counter();
            }
        };

        @SuppressWarnings("unchecked")
        static <T> Function<T, Counter> loader() {
            return (Function<T, Counter>) LOADER;
        }

        public int increment() {
            return ++this.count;
        }

        public int count() {
            return this.count;
        }
    }
}
