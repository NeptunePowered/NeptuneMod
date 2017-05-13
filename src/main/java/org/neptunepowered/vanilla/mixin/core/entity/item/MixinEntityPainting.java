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
package org.neptunepowered.vanilla.mixin.core.entity.item;

import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.hanging.Painting;
import net.minecraft.entity.item.EntityPainting;
import org.neptunepowered.vanilla.mixin.core.entity.MixinEntityHanging;
import org.neptunepowered.vanilla.util.converter.ArtConverter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPainting.class)
public abstract class MixinEntityPainting extends MixinEntityHanging implements Painting {

    @Shadow public EntityPainting.EnumArt art;

    @Override
    public ArtType getArtType() {
        return ArtConverter.of(this.art);
    }

    @Override
    public void setArtType(ArtType type) {
        this.art = ArtConverter.of(type);
    }

    @Override
    public int getSizeX() {
        return this.art.sizeX;
    }

    @Override
    public int getSizeY() {
        return this.art.sizeY;
    }

    @Override
    public int getOffsetX() {
        return this.art.offsetX;
    }

    @Override
    public int getOffsetY() {
        return this.art.offsetY;
    }

    @Override
    public String getFqName() {
        return "Painting";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PAINTING;
    }

}
