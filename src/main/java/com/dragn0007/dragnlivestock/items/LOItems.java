package com.dragn0007.dragnlivestock.items;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LOItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LivestockOverhaul.MODID);

   //SPAWN EGGS
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

    public static final RegistryObject<Item> OVERWORLD_UNICORN_SPAWN_EGG = ITEMS.register("overworld_unicorn_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.OVERWORLD_UNICORN_ENTITY, 0xfef4f4, 0xccbfbf, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
    public static final RegistryObject<Item> NETHER_UNICORN_SPAWN_EGG = ITEMS.register("nether_unicorn_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.NETHER_UNICORN_ENTITY, 0x222539, 0x090a12, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));
 public static final RegistryObject<Item> END_UNICORN_SPAWN_EGG = ITEMS.register("end_unicorn_spawn_egg",
         () -> new ForgeSpawnEggItem(EntityTypes.END_UNICORN_ENTITY, 0xb4ac79, 0xdee6a4, new Item.Properties().stacksTo(64).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP)));


    //MOD ITEM TABS (UNOBTAINABLE)
    public static final RegistryObject<Item> LIVESTOCK_OVERHAUL = ITEMS.register("livestock_overhaul",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}