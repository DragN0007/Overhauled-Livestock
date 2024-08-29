package com.dragn0007.dragnlivestock.client.menu;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OLMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, LivestockOverhaul.MODID);

    public static final RegistryObject<MenuType<OHorseMenu>> O_HORSE_MENU = MENU_TYPES.register("o_horse_menu", () -> new MenuType<>(OHorseMenu.create()));


    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
