package com.rinko1231.tdis.common.blocks;

import com.rinko1231.tdis.common.IRecipeWithInputs;
import com.rinko1231.tdis.TinkersDisassemble;
import com.rinko1231.tdis.common.IRecipeWithInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.adding.AbstractModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.OverslimeModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
//import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolPartsHook;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.*;

public class DisassemblerBlock extends Block implements SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape TABLE_SHAPE = Shapes.or(
            Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 4.0, 15.0, 4.0),
            Block.box(12.0, 0.0, 0.0, 16.0, 15.0, 4.0),
            Block.box(12.0, 0.0, 12.0, 16.0, 15.0, 16.0),
            Block.box(0.0, 0.0, 12.0, 4.0, 15.0, 16.0)
    ).optimize();

    public DisassemblerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(0.5F).sound(SoundType.WOOD));
        //this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean isWater = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, isWater);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return TABLE_SHAPE;
    }
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
                                 InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            disassembleTool(worldIn, player, handIn);
        }
        return InteractionResult.SUCCESS;
    }
    public void disassembleTool(Level world, Player player, InteractionHand handIn)
    {
        ItemStack stack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        if (!offHandStack.is(Items.APPLE)) return;
        System.out.println(stack.getItem().getClass());
        if (stack.getItem() instanceof IModifiableDisplay)
        {
            ToolStack toolStack = ToolStack.from(stack);
            for (ModifierEntry entry : toolStack.getUpgrades().getModifiers())
            {
                Modifier modifier = entry.getModifier();
                RecipeManager rm = world.getRecipeManager();
                List<ITinkerStationRecipe> recipes = rm.getAllRecipesFor(TinkerRecipeTypes.TINKER_STATION.get());
                List<AbstractModifierRecipe> modifierRecipes = recipes.stream()
                        .filter(input -> input instanceof AbstractModifierRecipe)
                        .map(p -> (AbstractModifierRecipe) p)
                        .toList();
                List<AbstractModifierRecipe> recipesForModifier = modifierRecipes.stream()
                        .filter(i -> i.getDisplayResult()
                                .getModifier()
                                .equals(modifier))
                        .toList();
                if (!recipesForModifier.isEmpty())
                {
                    AbstractModifierRecipe recipe1 = recipesForModifier.get(0);

                    if (recipe1 instanceof IncrementalModifierRecipe)
                    {
                        TinkersDisassemble.LOGGER.info("Skipping incremental modifier return for {}", modifier.getId());

                        // 不返还任何材料，只移除 modifier
                        ModifierEntry modifierEntry = toolStack.getModifiers().getEntry(modifier.getId());
                        toolStack.removeModifier(modifier.getId(), modifierEntry.getLevel());
                    }

                   /* {
                        TinkersDisassemble.LOGGER.info("IncrementalModifierRecipe");

                        List<IncrementalModifierRecipe> incrementalModifierRecipes = recipes.stream()
                                .filter(input -> input instanceof IncrementalModifierRecipe)
                                .map(p -> (IncrementalModifierRecipe) p)
                                .toList();
                        List<IncrementalModifierRecipe> lll = incrementalModifierRecipes.stream()
                                .filter(i -> i.getDisplayResult().getModifier().equals(modifier))
                                .toList();

                        if (!lll.isEmpty())
                        {
                            IncrementalModifierRecipe recipe = lll.get(0);
                            IRecipeWithInput ir = (IRecipeWithInput) recipe;

                            Ingredient input = ir.ba_painting$getInput();
                            int perInput = ir.ba_painting$getAmountPerInput();
                            int neededPerLevel = ir.ba_painting$getNeededPerLevel();

                            int level = entry.getLevel();

                            int totalInputCount = level * neededPerLevel;
                            int totalItemCount = totalInputCount / perInput;

                            ItemStack stack1 = input.getItems()[0].copy();
                            stack1.setCount(totalItemCount);

                            if (!player.addItem(stack1))
                            {
                                player.drop(stack1, false);
                            }

                            // 移除 modifier
                            toolStack.removeModifier(modifier.getId(), level);
                        }
                    }*/

                    else if (recipe1 instanceof ModifierRecipe)
                    {
                        TinkersDisassemble.LOGGER.info("ModifierRecipe");
                        List<ModifierRecipe> modifierRecipes1 = recipes.stream()
                                .filter(input -> input instanceof ModifierRecipe)
                                .map(p -> (ModifierRecipe) p)
                                .toList();
                        List<ModifierRecipe> lll = modifierRecipes1.stream()
                                .filter(i -> i.getDisplayResult()
                                        .getModifier()
                                        .equals(modifier))
                                .toList();
                        ModifierRecipe recipe = lll.get(0);
                        IRecipeWithInputs rwi = (IRecipeWithInputs) recipe;
                        List<SizedIngredient> lsi = rwi.ba_painting$getInputs();
                        for (SizedIngredient si : lsi)
                        {
                            int amount = si.getAmountNeeded();
                            ItemStack stack1 = si.getMatchingStacks()
                                    .get(recipe.toString()
                                            .contains("recapitated") ? 11 : 0);
                            ItemStack stack2 = stack1.copy();
                            stack2.setCount(amount);
                            if (!player.addItem(stack2))
                            {
                                player.drop(stack2, false);
                            }
                        }
                        toolStack.removeModifier(modifier.getId(), 1);
                    }
                    else
                    {
                        TinkersDisassemble.LOGGER.info("Other");
                    }
                }
                else
                {
                    if (modifier instanceof OverslimeModifier om)
                    {
                        List<OverslimeModifierRecipe> modifierRecipes1 = recipes.stream()
                                .filter(input -> input instanceof OverslimeModifierRecipe)
                                .map(p -> (OverslimeModifierRecipe) p)
                                .toList();
                        List<OverslimeModifierRecipe> recipesForModifier1 = modifierRecipes1.stream()
                                .filter(i -> i.getDisplayResult()
                                        .getModifier()
                                        .equals(modifier))
                                .toList();
                        if (!recipesForModifier1.isEmpty())
                        {
                            int cap = toolStack.getStats().getInt(OverslimeModifier.OVERSLIME_STAT);
                            OverslimeModifierRecipe recipe = recipesForModifier1.get(1);
                            IRecipeWithInput ir = ((IRecipeWithInput) recipe);
                            Ingredient input = ir.ba_painting$getInput();
                            ItemStack stack1 = new ItemStack(Items.SLIME_BALL);  // 固定返回绿色黏液球
                            stack1.setCount(cap / 10);
                            if (!player.addItem(stack1))
                            {
                                player.drop(stack1, false);
                            }
                            toolStack.removeModifier(modifier.getId(), 1);
                        }
                    }
                }
          }
            List<IToolPart> components = ToolPartsHook.parts(((IModifiable) stack.getItem()).getToolDefinition()).stream().toList();

            if (!components.isEmpty())
            {

                MaterialNBT materials = toolStack.getMaterials();
                if (!materials.isEmpty())
                {
                    for (int i = 0; i < materials.size(); ++i)
                    {
                        IToolPart requirement = components.get(i);
                        MaterialVariant material = materials.get(i);
                        ToolPartItem partItem = (ToolPartItem) requirement.asItem();
                        if (!player.addItem(partItem.withMaterial(material.getVariant())))
                        {
                            player.drop(partItem.withMaterial(material.getVariant()), false);
                        }
                    }
                }
                player.setItemInHand(handIn, ItemStack.EMPTY);
            }
        }
    }

}
