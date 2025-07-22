package com.rinko1231.tdis;

import com.rinko1231.tdis.init.BlockList;
import com.rinko1231.tdis.init.TabInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("tinkersdisassemble")
public class TinkersDisassemble {
    public static final String MODID = "tinkersdisassemble";
    public static final String MODNAME = "Tinkers Disassemble";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);
    public TinkersDisassemble() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockList.BLOCKS.register(modEventBus);
        BlockList.BLOCK_ITEMS.register(modEventBus);
        TabInit.TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static ResourceLocation prefix(String id){
        return  new ResourceLocation(MODID, id);
    }
}
