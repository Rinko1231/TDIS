package com.rinko1231.tdis;

import com.rinko1231.tdis.api.IRecipeWithInput;
import com.rinko1231.tdis.api.IRecipeWithInputs;
import com.rinko1231.tdis.config.DisassembleConfig;
import com.rinko1231.tdis.init.ItemList;
import com.rinko1231.tdis.init.TabInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.adding.AbstractModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.OverslimeModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolPartsHook;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.List;

@Mod("tooldisassemble")
public class ToolDisassemble {
    public static final String MODID = "tooldisassemble";
    public static final String MODNAME = "Tool Disassemble";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);
    public ToolDisassemble() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemList.ITEMS.register(modEventBus);
        TabInit.TABS.register(modEventBus);
        DisassembleConfig.setup();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        Level world = event.getLevel();
        if (world.isClientSide) return;

        Player player = event.getEntity();
        ItemStack mainItem = player.getMainHandItem();
        if (!mainItem.is(ItemList.DISASSEMBLING_TOOL.get())) return;

        BlockPos pos = event.getPos();
        Block clickBlock = world.getBlockState(pos).getBlock();
        String BlockId = ForgeRegistries.BLOCKS.getKey(clickBlock).toString();
        if (!DisassembleConfig.disassemblingBlockSet.get().contains(BlockId)) return;

        boolean hasModifiableTool = false;
        boolean hasDisassemblableTool = false;

        for (int i = 0; i < 9; i++) {
            ItemStack stackDis = player.getInventory().getItem(i);

            if (!stackDis.isEmpty() && stackDis.getItem() instanceof IModifiableDisplay) {
                hasModifiableTool = true;
                String stackID = ForgeRegistries.ITEMS.getKey(stackDis.getItem()).toString();
                if (!DisassembleConfig.toolDisassembleBlacklist.get().contains(stackID)) {
                    // 拆解第一个可拆的工具
                    disassembleTool(world, player, i, pos);
                    player.swing(InteractionHand.MAIN_HAND);
                    hasDisassemblableTool = true;
                    break;
                }
            }
        }

        // 如果存在工具，但全是黑名单，才提示
        if (hasModifiableTool && !hasDisassemblableTool) {
            player.displayClientMessage(Component.translatable("info.tooldisassemble.failed2"), true);
        }
    }


    public void disassembleTool(Level world, Player player, int Slot, BlockPos pos) {


        ItemStack stack = player.getInventory().getItem(Slot);


        if (stack.getDamageValue() !=0)
        {
            player.displayClientMessage(Component.translatable("info.tooldisassemble.failed"),true);
            return;
        }

        System.out.println(stack.getItem().getClass());

        if (stack.getItem() instanceof IModifiableDisplay) {
            ToolStack toolStack = ToolStack.from(stack);
            for (ModifierEntry entry : toolStack.getUpgrades().getModifiers()) {
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
                if (!recipesForModifier.isEmpty()) {
                    AbstractModifierRecipe recipe1 = recipesForModifier.get(0);

                    if (recipe1 instanceof IncrementalModifierRecipe)
 {
                        List<IncrementalModifierRecipe> matchingRecipes = recipes.stream()
                                .filter(r -> r instanceof IncrementalModifierRecipe irm && irm.getDisplayResult().getModifier().equals(modifier))
                                .map(r -> (IncrementalModifierRecipe) r)
                                .toList();

                        if (!matchingRecipes.isEmpty()) {
                            IncrementalModifierRecipe recipe = matchingRecipes.get(0);
                            IRecipeWithInput ir = (IRecipeWithInput) recipe;

                            Ingredient input = ir.tic$getInput();
                            int perInput = ir.tic$getAmountPerInput();//9
                            int perLevel = ((IRecipeWithInput) recipe).tic$getNeededPerLevel();//45
                            int level = entry.getLevel();
// 2. 添加当前等级已投入但未满的部分
                            int amount = entry.getAmount(entry.getLevel() - 1);
                            if (level ==1 && amount ==0)
                            {
                                //player.displayClientMessage(Component.literal("匠魂数组折磨人！"),true);
                                ItemStack stack1 = input.getItems()[0].copyWithCount(perLevel/perInput);
                                String itemId = ForgeRegistries.ITEMS.getKey(stack1.getItem()).toString();

                                if (!(DisassembleConfig.itemDisassembleBlacklist.get().contains(itemId))) {
                                if (!player.addItem(stack1))
                                {
                                    player.drop(stack1, false);
                                }}
                                toolStack.removeModifier(modifier.getId(), entry.getLevel());
                            }
                                else {
                            while (level >= 1)
                            {
                                while (amount >= perInput)
                                {
                                    amount -= perInput;
                                    ItemStack stack1 = input.getItems()[0].copy();
                                    String itemIdx = ForgeRegistries.ITEMS.getKey(stack1.getItem()).toString();

                                    if (!(DisassembleConfig.itemDisassembleBlacklist.get().contains(itemIdx))) {
                                    if (!player.addItem(stack1))
                                    {
                                        player.drop(stack1, false);
                                    }}
                                }
                                toolStack.removeModifier(modifier.getId(), 1);
                                level--;
                                amount = perLevel;
                            } }

                        }
                    } else if (recipe1 instanceof ModifierRecipe) {
                        ToolDisassemble.LOGGER.info("ModifierRecipe");
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
                        List<SizedIngredient> lsi = rwi.tic$getInputs();
                        for (SizedIngredient si : lsi) {
                            int amount = si.getAmountNeeded();
                            ItemStack stack1 = si.getMatchingStacks()
                                    .get(recipe.toString()
                                            .contains("recapitated") ? 11 : 0);
                            ItemStack stack2 = stack1.copy();
                            stack2.setCount(amount);

                            String itemId2 = ForgeRegistries.ITEMS.getKey(stack1.getItem()).toString();

                            if (!(DisassembleConfig.itemDisassembleBlacklist.get().contains(itemId2))) {
                                if (!player.addItem(stack2)) {
                                    player.drop(stack2, false);
                                }
                            }

                        }
                        toolStack.removeModifier(modifier.getId(), 1);
                    } else {
                        ToolDisassemble.LOGGER.info("Other");
                    }
                } else {
                    if (modifier instanceof OverslimeModifier overslimeModifier) {
                        List<OverslimeModifierRecipe> modifierRecipes1 = recipes.stream()
                                .filter(input -> input instanceof OverslimeModifierRecipe)
                                .map(p -> (OverslimeModifierRecipe) p)
                                .toList();
                        List<OverslimeModifierRecipe> recipesForModifier1 = modifierRecipes1.stream()
                                .filter(i -> i.getDisplayResult()
                                        .getModifier()
                                        .equals(modifier))
                                .toList();
                        if (!recipesForModifier1.isEmpty()) {
                            int cap = toolStack.getStats().getInt(OverslimeModifier.OVERSLIME_STAT);
                            OverslimeModifierRecipe recipe = recipesForModifier1.get(1);
                            IRecipeWithInput ir = ((IRecipeWithInput) recipe);
                            Ingredient input = ir.tic$getInput();
                            ItemStack stack1 = new ItemStack(Items.SLIME_BALL);  // 固定返回绿色黏液球
                            stack1.setCount(cap / 10);

                            String itemId3 = ForgeRegistries.ITEMS.getKey(stack1.getItem()).toString();

                            if (!(DisassembleConfig.itemDisassembleBlacklist.get().contains(itemId3))) {
                                if (!player.addItem(stack1)) {
                                    player.drop(stack1, false);
                                }
                            }
                            toolStack.removeModifier(modifier.getId(), 1);
                        }
                    }
                }
            }
            List<IToolPart> components = ToolPartsHook.parts(((IModifiable) stack.getItem()).getToolDefinition()).stream().toList();

            if (!components.isEmpty()) {

                MaterialNBT materials = toolStack.getMaterials();
                if (!materials.isEmpty()) {
                    for (int i = 0; i < materials.size(); ++i) {
                        IToolPart requirement = components.get(i);
                        MaterialVariant material = materials.get(i);
                        ToolPartItem partItem = (ToolPartItem) requirement.asItem();
                        if (!player.addItem(partItem.withMaterial(material.getVariant()))) {
                            player.drop(partItem.withMaterial(material.getVariant()), false);
                        }
                    }
                }
                player.level().playSound((Player)null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 2.0F);
                player.level().playSound((Player)null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 2.0F);

                player.getInventory().setItem(Slot, ItemStack.EMPTY);
            }
        }
    }


}
