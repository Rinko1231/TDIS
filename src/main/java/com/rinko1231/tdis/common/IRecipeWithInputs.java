package com.rinko1231.tdis.common;

//import net.minecraft.item.crafting.Ingredient;
//import slimeknights.mantle.recipe.SizedIngredient;

import slimeknights.mantle.recipe.ingredient.SizedIngredient;

import java.util.List;

public interface IRecipeWithInputs {
    List<SizedIngredient> ba_painting$getInputs();
}
