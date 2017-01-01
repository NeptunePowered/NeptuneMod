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
package org.neptunepowered.vanilla.util.converter;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.canarymod.api.entity.hanging.Painting;
import net.minecraft.entity.item.EntityPainting;

public final class ArtConverter {

    private static BiMap<EntityPainting.EnumArt, Painting.ArtType> map =
            ImmutableBiMap.<EntityPainting.EnumArt, Painting.ArtType>builder()
                    .put(EntityPainting.EnumArt.KEBAB, Painting.ArtType.Kebab)
                    .put(EntityPainting.EnumArt.ALBAN, Painting.ArtType.Alban)
                    .put(EntityPainting.EnumArt.AZTEC, Painting.ArtType.Aztec)
                    .put(EntityPainting.EnumArt.AZTEC_2, Painting.ArtType.Aztec2)
                    .put(EntityPainting.EnumArt.BOMB, Painting.ArtType.Bomb)
                    .put(EntityPainting.EnumArt.PLANT, Painting.ArtType.Plant)
                    .put(EntityPainting.EnumArt.WASTELAND, Painting.ArtType.Wasteland)
                    .put(EntityPainting.EnumArt.POOL, Painting.ArtType.Pool)
                    .put(EntityPainting.EnumArt.COURBET, Painting.ArtType.Courbet)
                    .put(EntityPainting.EnumArt.SEA, Painting.ArtType.Sea)
                    .put(EntityPainting.EnumArt.SUNSET, Painting.ArtType.Sunset)
                    .put(EntityPainting.EnumArt.CREEBET, Painting.ArtType.Creebet)
                    .put(EntityPainting.EnumArt.WANDERER, Painting.ArtType.Wanderer)
                    .put(EntityPainting.EnumArt.GRAHAM, Painting.ArtType.Graham)
                    .put(EntityPainting.EnumArt.MATCH, Painting.ArtType.Match)
                    .put(EntityPainting.EnumArt.BUST, Painting.ArtType.Bust)
                    .put(EntityPainting.EnumArt.STAGE, Painting.ArtType.Stage)
                    .put(EntityPainting.EnumArt.VOID, Painting.ArtType.Void)
                    .put(EntityPainting.EnumArt.SKULL_AND_ROSES, Painting.ArtType.SkullAndRoses)
                    .put(EntityPainting.EnumArt.WITHER, Painting.ArtType.Wither)
                    .put(EntityPainting.EnumArt.FIGHTERS, Painting.ArtType.Fighters)
                    .put(EntityPainting.EnumArt.POINTER, Painting.ArtType.Pointer)
                    .put(EntityPainting.EnumArt.PIGSCENE, Painting.ArtType.Pigscene)
                    .put(EntityPainting.EnumArt.BURNING_SKULL, Painting.ArtType.BurningSkull)
                    .put(EntityPainting.EnumArt.SKELETON, Painting.ArtType.Skeleton)
                    .put(EntityPainting.EnumArt.DONKEY_KONG, Painting.ArtType.DonkeyKong)
                    .build();

    private ArtConverter() {
    }

    public static Painting.ArtType of(EntityPainting.EnumArt artType) {
        return map.get(artType);
    }

    public static EntityPainting.EnumArt of(Painting.ArtType art) {
        return map.inverse().get(art);
    }
    
}
