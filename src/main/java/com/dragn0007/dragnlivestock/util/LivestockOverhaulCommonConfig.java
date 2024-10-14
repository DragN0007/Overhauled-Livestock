package com.dragn0007.dragnlivestock.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class LivestockOverhaulCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue REPLACE_HORSES;
    public static final ForgeConfigSpec.BooleanValue REPLACE_MULES;
    public static final ForgeConfigSpec.BooleanValue REPLACE_DONKEYS;
    public static final ForgeConfigSpec.BooleanValue REPLACE_COWS;
    public static final ForgeConfigSpec.BooleanValue REPLACE_CHICKENS;
    public static final ForgeConfigSpec.BooleanValue REPLACE_SHEEP;
    public static final ForgeConfigSpec.BooleanValue REPLACE_PIGS;
    public static final ForgeConfigSpec.BooleanValue REPLACE_LLAMAS;
    public static final ForgeConfigSpec.BooleanValue REPLACE_RABBITS;
    public static final ForgeConfigSpec.BooleanValue REPLACE_SALMON;
    public static final ForgeConfigSpec.BooleanValue REPLACE_COD;
    public static final ForgeConfigSpec.BooleanValue REPLACE_BEES;
    public static final ForgeConfigSpec.BooleanValue FAILSAFE_REPLACER;

    static {
        BUILDER.push("Spawning");

        REPLACE_HORSES = BUILDER.comment("Should vanilla horses be replaced by O-Horses?")
                .define("Replace Vanilla Horses", true);

        REPLACE_MULES = BUILDER.comment("Should vanilla mules be replaced by O-Mules?")
                .define("Replace Vanilla Mules", true);

        REPLACE_DONKEYS = BUILDER.comment("Should vanilla donkeys be replaced by O-Donkeys?")
                .define("Replace Vanilla Donkeys", true);

        REPLACE_COWS = BUILDER.comment("Should vanilla cows be replaced by O-Cows?")
                .define("Replace Vanilla Cows", true);

        REPLACE_CHICKENS = BUILDER.comment("Should vanilla chickens be replaced by O-Chickens?")
                .define("Replace Vanilla Chickens", true);

        REPLACE_SHEEP = BUILDER.comment("Should vanilla sheep be replaced by O-Sheep?")
                .define("Replace Vanilla Sheep", true);

        REPLACE_PIGS = BUILDER.comment("Should vanilla pigs be replaced by O-Pigs?")
                .define("Replace Vanilla Pigs", true);

        REPLACE_LLAMAS = BUILDER.comment("Should vanilla llamas be replaced by O-Llamas?")
                .define("Replace Vanilla Llamas", true);

        REPLACE_RABBITS = BUILDER.comment("Should vanilla rabbits be replaced by O-Rabbits?")
                .define("Replace Vanilla Rabbits", true);

        REPLACE_SALMON = BUILDER.comment("Should vanilla  be replaced by O-Salmon?")
                .define("Replace Vanilla Salmon", true);

        REPLACE_COD = BUILDER.comment("Should vanilla cod be replaced by O-Cod?")
                .define("Replace Vanilla Cod", true);

        REPLACE_BEES = BUILDER.comment("Should vanilla bees be replaced by O-Bees?")
                .define("Replace Vanilla Bees", true);
        BUILDER.pop();

        BUILDER.push("Uninstalling");
        FAILSAFE_REPLACER =
                BUILDER.comment("Should all O-Animals be converted back into a vanilla counterpart? " +
                                "WARNING: This should be used *sparingly*, and only when needed/ if this mod is about to be removed.")
                        .define("Failsafe O-Animal -> Vanilla Animal Converter", false);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
