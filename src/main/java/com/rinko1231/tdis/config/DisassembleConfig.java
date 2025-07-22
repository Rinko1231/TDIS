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
                .define("itemDisassembleBlacklist", List.of("minecraft:quartz", "notNull:doNotDelete"));
        toolDisassembleBlacklist = BUILDER
                .comment("Tools that can not be Disassembled.")
                .comment("Plate Shield has a duplicate bug, do not remove it.")
                .define("toolDisassembleBlacklist", List.of("tconstruct:plate_shield"));
        disassemblingBlockSet = BUILDER
                .comment("Blocks that can be used for Disassembling.")
                .define("disassemblingBlockSet", List.of("minecraft:smooth_stone"));

        SPEC = BUILDER.build();
    }

    public static void setup() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "ToolDisassembleConfig.toml");
    }


}