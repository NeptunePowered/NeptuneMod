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
package org.neptunepowered.vanilla.serialize;

import static org.neptunepowered.vanilla.util.ExtraObjects.ifttt;
import static org.neptunepowered.vanilla.util.ExtraObjects.nullable;

import net.canarymod.CanaryDeserializeException;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.serialize.Serializer;
import net.minecraft.item.ItemStack;
import org.neptunepowered.vanilla.util.SerializerConstants;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An implementation of {@link Serializer} for {@link ItemStack},
 * that is compatible with the implementation found in CanaryMod.
 */
public class ItemSerializer extends NeptuneSerializer<ItemStack> {

    public static final Serializer<ItemStack> INSTANCE = new ItemSerializer();

    private static final String FIELD_DELIMITER = "#";
    private static final String ITEM_DELIMITER = ";";
    private static final String LORE_DELIMITER = ",";

    private static final Pattern FIELD_PATTERN = Pattern.compile(FIELD_DELIMITER, Pattern.LITERAL);
    private static final Pattern ITEM_PATTERN = Pattern.compile(ITEM_DELIMITER, Pattern.LITERAL);
    private static final Pattern LORE_PATTERN = Pattern.compile(LORE_DELIMITER, Pattern.LITERAL);

    @Override
    public ItemStack deserialize(final String data) throws CanaryDeserializeException {
        return null;
    }

    @Override
    public String serialize(final ItemStack object) {
        return nullable(object, () -> {
            final Item canary = (Item) object;
            final StringBuilder builder = new StringBuilder();

            // Item data
            builder.append(getTypeOfStack(object).getId()).append(ITEM_DELIMITER)
                    .append(object.getItemDamage()).append(ITEM_DELIMITER)
                    .append(object.stackSize).append(ITEM_DELIMITER)
                    .append(canary.getSlot());
            ifttt(canary::hasLore, () -> builder.append(ITEM_DELIMITER)
                    .append(Arrays.stream(canary.getLore()).collect(Collectors.joining(LORE_DELIMITER))));
            ifttt(object::hasDisplayName, () -> builder.append(ITEM_DELIMITER)
                    .append(object.getDisplayName()));

            // Item enchantments
            // TODO: implement

            return builder.toString();
        });
    }

    @Override
    public String getType() {
        return SerializerConstants.ITEM;
    }

    private static ItemType getTypeOfStack(final ItemStack stack) {
        final int id = net.minecraft.item.Item.getIdFromItem(stack.getItem());
        final int data = stack.getItemDamage();
        return ItemType.fromIdAndData(id, data);
    }

}
