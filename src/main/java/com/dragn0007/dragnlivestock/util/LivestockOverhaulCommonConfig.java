package com.dragn0007.dragnlivestock.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class LivestockOverhaulCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue REPLACE_HORSES;

    static {
        BUILDER.push("Spawning");

        REPLACE_HORSES = BUILDER.comment("Should vanilla horses be replaced by OHorse on-spawn?")
                .define("Replace Vanilla Horses", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
