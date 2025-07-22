package com.rinko1231.tdis.common;

//import net.minecraft.item.crafting.Ingredient;

import net.minecraft.world.item.crafting.Ingredient;

public interface IRecipeWithInput {
    Ingredient ba_painting$getInput();
    int ba_painting$getAmountPerInput();
    int ba_painting$getNeededPerLevel();
}
