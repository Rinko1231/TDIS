package com.rinko1231.tdis.api;


import net.minecraft.world.item.crafting.Ingredient;

public interface IRecipeWithInput {
    Ingredient tic$getInput();
    int tic$getAmountPerInput();
    int tic$getNeededPerLevel();
}
