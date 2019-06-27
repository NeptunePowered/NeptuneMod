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

import static java.lang.Integer.parseInt;
import static org.neptunepowered.vanilla.util.ExtraObjects.conditionNull;
import static org.neptunepowered.vanilla.util.ExtraObjects.ifttt;
import static org.neptunepowered.vanilla.util.ExtraObjects.nullable;

import net.canarymod.CanaryDeserializeException;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.serialize.Serializer;
import net.minecraft.item.ItemStack;
import org.neptunepowered.vanilla.inventory.NeptuneEnchantment;
import org.neptunepowered.vanilla.util.SerializerConstants;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An implementation of {@link Serializer} for {@link ItemStack},
 * that is compatible with the implementation found in CanaryMod.
 */
public class ItemSerializer extends NeptuneSerializer<ItemStack> {

    public static final Serializer<ItemStack> INSTANCE = new ItemSerializer();

    private static final String FIELD_DELIMITER = "#";
    private static final String ITEM_DELIMITER = ";";
    private static final String LORE_DELIMITER = ",";
    private static final String ENCHANTMENT_DELIMITER = ",";

    private static final Pattern FIELD_PATTERN = Pattern.compile(FIELD_DELIMITER, Pattern.LITERAL);
    private static final Pattern ITEM_PATTERN = Pattern.compile(ITEM_DELIMITER, Pattern.LITERAL);
    private static final Pattern LORE_PATTERN = Pattern.compile(LORE_DELIMITER, Pattern.LITERAL);
    private static final Pattern ENCHANTMENT_PATTERN = Pattern.compile(ENCHANTMENT_DELIMITER, Pattern.LITERAL);

    @Override
    public ItemStack deserialize(final String data) throws CanaryDeserializeException {
        final String[] fields = FIELD_PATTERN.split(data);
        final String[] item = ITEM_PATTERN.split(fields[0]);
        final String[] enchantments = conditionNull(fields.length >= 2, () -> ENCHANTMENT_PATTERN.split(fields[1]));

        if (item.length < 4) {
            throw this.newDeserializeException("Could not deserialize Item. Expected fields 4. Found: " + item.length);
        }
        final int itemId = parseInt(item[0]);
        final int itemDamage = parseInt(item[1]);
        final int itemAmount = parseInt(item[2]);
        final int itemSlot = parseInt(item[3]);

        final ItemStack stack = new ItemStack(net.minecraft.item.Item.getItemById(itemId), itemAmount, itemDamage);
        final Item canary = (Item) stack;
        canary.setSlot(itemSlot);

        if (item.length >= 5) {
            stack.setStackDisplayName(item[4]);
        }
        if (item.length >= 6) {
            final String[] lore = LORE_PATTERN.split(item[5]);
            canary.setLore(lore);
        }

        nullable(enchantments, enchants -> Stream.of(enchants)
                .map(EnchantmentSerializer.INSTANCE::deserialize)
                .map(NeptuneEnchantment::createEnchantmentData)
                .forEach(enchantmentData -> stack.addEnchantment(enchantmentData.enchantmentobj, enchantmentData.enchantmentLevel)));

        return stack;
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
                    .append(String.join(LORE_DELIMITER, canary.getLore())));
            ifttt(object::hasDisplayName, () -> builder.append(ITEM_DELIMITER)
                    .append(object.getDisplayName()));

            // Item enchantments
            nullable(canary.getEnchantments(), enchantments -> {
                if (enchantments.length == 0) return;
                builder.append(FIELD_DELIMITER);
                builder.append(Stream.of(enchantments).map(NeptuneEnchantment.class::cast)
                        .map(EnchantmentSerializer.INSTANCE::serialize)
                        .collect(Collectors.joining(ENCHANTMENT_DELIMITER)));
            });

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
