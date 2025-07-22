package com.rinko1231.tdis.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.rinko1231.tdis.ToolDisassemble.MODID;

public class TabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> DIS_TAB = TABS.register(MODID, () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("item_group." + MODID))
            // Set icon of creative tab
            .icon(() -> new ItemStack(ItemList.DISASSEMBLING_TOOL.get()))
            // Add default items to tab
            .displayItems((params, output) -> {
                ItemList.ITEMS.getEntries().forEach(it -> output.accept(it.get()));
            })
            .build()
    );
}