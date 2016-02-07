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
package org.neptunepowered.common.wrapper.inventory.recipes;

import net.canarymod.api.inventory.CraftingMatrix;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.recipes.Recipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import org.neptunepowered.common.util.Wrapper;

public abstract class NeptuneRecipe extends Wrapper<IRecipe> implements Recipe {

    public NeptuneRecipe(IRecipe handle) {
        super(handle);
    }

    @Override
    public Item getResult() {
        return (Item) this.getHandle().getRecipeOutput();
    }

    @Override
    public int getRecipeSize() {
        return this.getHandle().getRecipeSize();
    }

    @Override
    public boolean matchesMatrix(CraftingMatrix matrix) {
        return false;
    }

    public static NeptuneRecipe of(IRecipe recipe) {
        if (recipe instanceof ShapedRecipes) {
            return new NeptuneShapedRecipe((ShapedRecipes) recipe);
        } else if(recipe instanceof ShapelessRecipes) {
            return new NeptuneShapelessRecipe((ShapelessRecipes) recipe);
        }
        return new NeptuneRecipe(recipe) {
            @Override
            public boolean isShapeless() {
                return false;
            }

            @Override
            public boolean isShaped() {
                return false;
            }
        };
    }
}
