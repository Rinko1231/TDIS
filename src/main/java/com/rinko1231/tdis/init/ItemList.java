package com.rinko1231.tdis.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.rinko1231.tdis.ToolDisassemble.MODID;

public class ItemList {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> DISASSEMBLING_TOOL = ITEMS.register("disassembling_tool", () -> new Item(new Item.Properties()));

}