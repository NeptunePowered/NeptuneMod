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

import static org.neptunepowered.vanilla.util.ExtraObjects.nullable;

import net.canarymod.CanaryDeserializeException;
import net.canarymod.api.inventory.Enchantment;
import net.canarymod.serialize.Serializer;
import org.neptunepowered.vanilla.inventory.NeptuneEnchantment;
import org.neptunepowered.vanilla.util.SerializerConstants;

import java.util.regex.Pattern;

/**
 * An implementation of {@link Serializer} for {@link NeptuneEnchantment},
 * that is compatible with the implementation found in CanaryMod.
 */
public class EnchantmentSerializer extends NeptuneSerializer<NeptuneEnchantment> {

    public static final Serializer<NeptuneEnchantment> INSTANCE = new EnchantmentSerializer();

    private static final Pattern FIELD_PATTERN = Pattern.compile(";", Pattern.LITERAL);

    @Override
    public NeptuneEnchantment deserialize(final String data) throws CanaryDeserializeException {
        final String[] fields = FIELD_PATTERN.split(data);
        if (fields.length != 2) {
            throw this.newDeserializeException("Failed to deserialise Enchantment: Got " + fields.length + " fields, expected 2");
        }

        try {
            final Enchantment.Type type = Enchantment.Type.fromId(Integer.parseInt(fields[0]));
            return new NeptuneEnchantment(type, Short.parseShort(fields[1]));
        } catch (final NumberFormatException ex) {
            throw this.newDeserializeException("Failed to deserialise Enchantment: " + ex.getMessage());
        }
    }

    @Override
    public String serialize(final NeptuneEnchantment object) {
        return nullable(object, () -> object.getType().getId() + ";" + object.getLevel());
    }

    @Override
    public String getType() {
        return SerializerConstants.ENCHANTMENT;
    }

}
