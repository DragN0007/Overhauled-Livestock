package com.dragn0007.dragnlivestock.items;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.items.custom.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LOItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LivestockOverhaul.MODID);

    //Spawn Eggs
    public static final RegistryObject<Item> O_HORSE_SPAWN_EGG = ITEMS.register("o_horse_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_HORSE_ENTITY, 0x53250e, 0x281003, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_COW_SPAWN_EGG = ITEMS.register("o_cow_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_COW_ENTITY, 0x4f402e, 0xdbdbdb, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_CHICKEN_SPAWN_EGG = ITEMS.register("o_chicken_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_CHICKEN_ENTITY, 0xc8623d, 0x423434, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_SALMON_SPAWN_EGG = ITEMS.register("o_salmon_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_SALMON_ENTITY, 0xab3533, 0x5b511c, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_COD_SPAWN_EGG = ITEMS.register("o_cod_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_COD_ENTITY, 0x92715a, 0xb6966b, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_BEE_SPAWN_EGG = ITEMS.register("o_bee_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_BEE_ENTITY, 0xe4ae3b, 0xe4ae3b, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_RABBIT_SPAWN_EGG = ITEMS.register("o_rabbit_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_RABBIT_ENTITY, 0xa48d73, 0x524839, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_SHEEP_SPAWN_EGG = ITEMS.register("o_sheep_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_SHEEP_ENTITY, 0xc7c7c7, 0xdccbc2, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_LLAMA_SPAWN_EGG = ITEMS.register("o_llama_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_LLAMA_ENTITY, 0xccb37c, 0xfff3d8, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_PIG_SPAWN_EGG = ITEMS.register("o_pig_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_PIG_ENTITY, 0xb29595, 0xd3bbbb, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_DONKEY_SPAWN_EGG = ITEMS.register("o_donkey_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_DONKEY_ENTITY, 0x8b7867, 0x655749, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_MULE_SPAWN_EGG = ITEMS.register("o_mule_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_MULE_ENTITY, 0x502c1a, 0x381f17, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> O_MOOSHROOM_SPAWN_EGG = ITEMS.register("o_mooshroom_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.O_MOOSHROOM_ENTITY, 0xbf2425, 0xcabcbc, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> OX_SPAWN_EGG = ITEMS.register("ox_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.OX_ENTITY, 0xa19477, 0x826b54, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> OVERWORLD_UNICORN_SPAWN_EGG = ITEMS.register("overworld_unicorn_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.OVERWORLD_UNICORN_ENTITY, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> NETHER_UNICORN_SPAWN_EGG = ITEMS.register("nether_unicorn_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.NETHER_UNICORN_ENTITY, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> END_UNICORN_SPAWN_EGG = ITEMS.register("end_unicorn_spawn_egg",
         () -> new ForgeSpawnEggItem(EntityTypes.END_UNICORN_ENTITY, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    //Misc
    public static final RegistryObject<Item> BRAND_TAG = ITEMS.register("brand_tag",
            () -> new BrandTagItem(new Item.Properties()));
    public static final RegistryObject<Item> GENDER_TEST_STRIP = ITEMS.register("gender_test_strip",
            () -> new GenderTestKit(new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> MALE_GENDER_TEST_STRIP = ITEMS.register("male_gender_test_strip",
            () -> new MaleGenderTestKit(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FEMALE_GENDER_TEST_STRIP = ITEMS.register("female_gender_test_strip",
            () -> new FemaleGenderTestKit(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor",
            () -> new HorseArmorItem(15, "netherite", (new Item.Properties()).stacksTo(1).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> GRIFFITH_INSPIRED_HORSE_ARMOR = ITEMS.register("griffith_inspired_horse_armor",
            () -> new HorseArmorItem(15, "griffth", (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> FERTILIZED_EGG = ITEMS.register("fertilized_egg",
            () -> new FertilizedEggItem((new Item.Properties()).stacksTo(1).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> EGG = ITEMS.register("egg",
            () -> new Item((new Item.Properties()).stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> MANE_SCISSORS = ITEMS.register("mane_scissors",
            () -> new HairScissorItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TAIL_SCISSORS = ITEMS.register("tail_scissors",
            () -> new HairScissorItem(new Item.Properties().stacksTo(1)));


    //Food/ Items
    public static final RegistryObject<Item> SHEEP_MILK_BUCKET = ITEMS.register("sheep_milk_bucket",
         () -> new MilkBucketItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(1).build()).craftRemainder(Items.BUCKET).stacksTo(1).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> LLAMA_MILK_BUCKET = ITEMS.register("llama_milk_bucket",
            () -> new MilkBucketItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(1).build()).craftRemainder(Items.BUCKET).stacksTo(1).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> COW_MILK_JUG = ITEMS.register("cow_milk_jug",
            () -> new Item(new Item.Properties().tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> SHEEP_MILK_JUG = ITEMS.register("sheep_milk_jug",
            () -> new Item(new Item.Properties().tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> LLAMA_MILK_JUG = ITEMS.register("llama_milk_jug",
            () -> new Item(new Item.Properties().tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> SHEEP_CHEESE = ITEMS.register("sheep_cheese",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> LLAMA_CHEESE = ITEMS.register("llama_cheese",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> EGG_SALAD = ITEMS.register("egg_salad",
            () -> new BowlFoodItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).build()).stacksTo(1).craftRemainder(Items.BOWL).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> BEEF_RIB_STEAK = ITEMS.register("beef_rib_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> BEEF_SIRLOIN_STEAK = ITEMS.register("beef_sirloin_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_BEEF_RIB_STEAK = ITEMS.register("cooked_beef_rib_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_BEEF_SIRLOIN_STEAK = ITEMS.register("cooked_beef_sirloin_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> HORSE = ITEMS.register("horse",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> HORSE_RIB_STEAK = ITEMS.register("horse_rib_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> HORSE_SIRLOIN_STEAK = ITEMS.register("horse_sirloin_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_HORSE = ITEMS.register("cooked_horse",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_HORSE_RIB_STEAK = ITEMS.register("cooked_horse_rib_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_HORSE_SIRLOIN_STEAK = ITEMS.register("cooked_horse_sirloin_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> LLAMA = ITEMS.register("llama",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> LLAMA_RIB = ITEMS.register("llama_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> LLAMA_LOIN = ITEMS.register("llama_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_LLAMA = ITEMS.register("cooked_llama",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_LLAMA_RIB = ITEMS.register("cooked_llama_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_LLAMA_LOIN = ITEMS.register("cooked_llama_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> MUTTON_RIB = ITEMS.register("mutton_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> MUTTON_LOIN = ITEMS.register("mutton_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_MUTTON_RIB = ITEMS.register("cooked_mutton_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_MUTTON_LOIN = ITEMS.register("cooked_mutton_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> PORK_RIB_CHOP = ITEMS.register("pork_rib_chop",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> PORK_TENDERLOIN = ITEMS.register("pork_tenderloin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_PORK_RIB_CHOP = ITEMS.register("cooked_pork_rib_chop",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_PORK_TENDERLOIN = ITEMS.register("cooked_pork_tenderloin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> CHICKEN_THIGH = ITEMS.register("chicken_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_CHICKEN_THIGH = ITEMS.register("cooked_chicken_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> RABBIT_THIGH = ITEMS.register("rabbit_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_RABBIT_THIGH = ITEMS.register("cooked_rabbit_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> FISH_OIL = ITEMS.register("fish_oil",
            () -> new FishOilItem(
                    new MobEffectInstance(MobEffects.REGENERATION, 500, 0, true, false)
            ));
    public static final RegistryObject<Item> ROE = ITEMS.register("roe",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> UNICORN = ITEMS.register("unicorn",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).saturationMod(1).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> UNICORN_RIB_STEAK = ITEMS.register("unicorn_rib_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> UNICORN_SIRLOIN_STEAK = ITEMS.register("unicorn_sirloin_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_UNICORN = ITEMS.register("cooked_unicorn",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_UNICORN_RIB_STEAK = ITEMS.register("cooked_unicorn_rib_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> COOKED_UNICORN_SIRLOIN_STEAK = ITEMS.register("cooked_unicorn_sirloin_steak",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build()).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));

    public static final RegistryObject<Item> OVERWORLD_UNICORN_HORN = ITEMS.register("overworld_unicorn_horn",
            () -> new UnicornHornItem(
                    new MobEffectInstance(MobEffects.REGENERATION, 2880, 1, true, false),
                    new MobEffectInstance(MobEffects.LUCK, 2880, 2, true, false),
                    new MobEffectInstance(MobEffects.SLOW_FALLING, 2880, 1, true, false)
            ));

    public static final RegistryObject<Item> NETHER_UNICORN_HORN = ITEMS.register("nether_unicorn_horn",
            () -> new UnicornHornItem(
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2880, 1, true, false),
                    new MobEffectInstance(MobEffects.ABSORPTION, 2880, 2, true, false),
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2880, 1, true, false)
            ));

    public static final RegistryObject<Item> END_UNICORN_HORN = ITEMS.register("end_unicorn_horn",
            () -> new UnicornHornItem(
                    new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 2880, 1, true, false),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 2880, 1, true, false),
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2880, 2, true, false)
            ));


    //Mod Item Tab Icon (UNOBTAINABLE)
    public static final RegistryObject<Item> LIVESTOCK_OVERHAUL = ITEMS.register("livestock_overhaul",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}