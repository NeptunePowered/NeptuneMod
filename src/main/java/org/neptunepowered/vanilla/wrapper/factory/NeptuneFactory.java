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
package org.neptunepowered.vanilla.wrapper.factory;

import net.canarymod.api.factory.AIFactory;
import net.canarymod.api.factory.AttributeFactory;
import net.canarymod.api.factory.ChatComponentFactory;
import net.canarymod.api.factory.EntityFactory;
import net.canarymod.api.factory.Factory;
import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.factory.NBTFactory;
import net.canarymod.api.factory.ObjectFactory;
import net.canarymod.api.factory.PacketFactory;
import net.canarymod.api.factory.PotionFactory;
import net.canarymod.api.factory.StatisticsFactory;

public class NeptuneFactory implements Factory {

    private final ItemFactory itemFactory = new NeptuneItemFactory();
    private final PotionFactory potionFactory = new NeptunePotionFactory();
    private final EntityFactory entityFactory = new NeptuneEntityFactory();
    private final ObjectFactory objectFactory = new NeptuneObjectFactory();
    private final NBTFactory nbtFactory = new NeptuneNBTFactory();
    private final PacketFactory packetFactory = new NeptunePacketFactory();
    private final ChatComponentFactory chatComponentFactory = new NeptuneChatComponentFactory();
    private final AttributeFactory attributeFactory = new NeptuneAttributeFactory();
    private final StatisticsFactory statisticsFactory = new NeptuneStatisticsFactory();
    private final AIFactory aiFactory = new NeptuneAIFactory();

    @Override
    public ItemFactory getItemFactory() {
        return this.itemFactory;
    }

    @Override
    public PotionFactory getPotionFactory() {
        return this.potionFactory;
    }

    @Override
    public EntityFactory getEntityFactory() {
        return this.entityFactory;
    }

    @Override
    public ObjectFactory getObjectFactory() {
        return this.objectFactory;
    }

    @Override
    public NBTFactory getNBTFactory() {
        return this.nbtFactory;
    }

    @Override
    public PacketFactory getPacketFactory() {
        return this.packetFactory;
    }

    @Override
    public ChatComponentFactory getChatComponentFactory() {
        return this.chatComponentFactory;
    }

    @Override
    public AttributeFactory getAttributeFactory() {
        return this.attributeFactory;
    }

    @Override
    public StatisticsFactory getStatisticsFactory() {
        return this.statisticsFactory;
    }

    @Override
    public AIFactory getAIFactory() {
        return this.aiFactory;
    }
}
