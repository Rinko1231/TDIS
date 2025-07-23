package com.rinko1231.tdis.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class DisassembleConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> itemDisassembleBlacklist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> toolDisassembleBlacklist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> disassemblingBlockSet;

    static {
        BUILDER.push("Config");

        itemDisassembleBlacklist = BUILDER
                .comment("items that will not be returned through Disassembling.")
                .defineList("itemDisassembleBlacklist", List.of("minecraft:quartz"),
                        element -> element instanceof String);
        toolDisassembleBlacklist = BUILDER
                .comment("Tools that can not be Disassembled.")
                .defineList("toolDisassembleBlacklist",
                        List.of("tconstruct:plate_shield"),
                element -> element instanceof String);
        disassemblingBlockSet = BUILDER
                .comment("Blocks that can be used for Disassembling.")
                .defineList("disassemblingBlockSet", List.of("minecraft:smooth_stone"),
                element -> element instanceof String);

        SPEC = BUILDER.build();
    }

    public static void setup() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "ToolDisassembleConfig.toml");
    }


}