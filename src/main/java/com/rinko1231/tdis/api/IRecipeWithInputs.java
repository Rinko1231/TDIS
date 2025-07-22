package com.rinko1231.tdis.api;


import slimeknights.mantle.recipe.ingredient.SizedIngredient;

import java.util.List;

public interface IRecipeWithInputs {
    List<SizedIngredient> tic$getInputs();
}
