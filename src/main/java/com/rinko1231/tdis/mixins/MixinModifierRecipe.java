package com.rinko1231.tdis.mixins;

import com.rinko1231.tdis.common.IRecipeWithInputs;
//import net.minecraft.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
//import slimeknights.mantle.recipe.SizedIngredient;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;

import java.util.List;

@Mixin(ModifierRecipe.class)
public class MixinModifierRecipe implements IRecipeWithInputs {
    @Final
    @Shadow(remap = false)
    protected List<SizedIngredient> inputs;

    @Override
    public List<SizedIngredient> ba_painting$getInputs() {
        return inputs;
    }
}
