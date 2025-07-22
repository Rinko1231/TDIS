package com.rinko1231.tdis.init;


import com.rinko1231.tdis.common.blocks.DisassemblerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.rinko1231.tdis.TinkersDisassemble.MODID;

public class BlockList {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> DISASSEMBLER = BLOCKS.register("disassembler", DisassemblerBlock::new);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> DISASSEMBLER_ITEM = BLOCK_ITEMS.register("disassembler", () -> new BlockItem(BlockList.DISASSEMBLER.get(), new Item.Properties()));


}
