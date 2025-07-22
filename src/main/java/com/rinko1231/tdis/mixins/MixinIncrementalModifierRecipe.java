package com.rinko1231.tdis.mixins;

import com.rinko1231.tdis.api.IRecipeWithInput;

import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipe;

@Mixin(IncrementalModifierRecipe.class)
public class MixinIncrementalModifierRecipe implements IRecipeWithInput {
    @Final
    @Shadow(remap = false)
    private Ingredient input;
    @Final
    @Shadow(remap = false)
    private int amountPerInput;
    @Final
    @Shadow(remap = false)
    private int neededPerLevel;
    public Ingredient tic$getInput(){
        return input;
    }
    public int tic$getAmountPerInput(){
        return amountPerInput;
    }
    public int tic$getNeededPerLevel(){
        return neededPerLevel;
    }
}
