package com.rinko1231.tdis.mixins;

import com.rinko1231.tdis.common.IRecipeWithInput;
//import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.tconstruct.library.recipe.modifiers.adding.OverslimeModifierRecipe;

@Mixin(OverslimeModifierRecipe.class)
public class MixinOverslimeModifierRecipe implements IRecipeWithInput {
    @Final
    @Shadow(remap = false)
    private Ingredient ingredient;
    @Override
    public Ingredient ba_painting$getInput() {
        return ingredient;
    }

    @Override
    public int ba_painting$getAmountPerInput() {
        return 0;
    }

    @Override
    public int ba_painting$getNeededPerLevel() {
        return 0;
    }
}
