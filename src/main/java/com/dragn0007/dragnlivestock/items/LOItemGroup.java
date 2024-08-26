package com.dragn0007.dragnlivestock.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class LOItemGroup {

    public static final CreativeModeTab LIVESTOCK_OVERHAUL_GROUP = new CreativeModeTab("overhauled_livestock")
    {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(LOItems.LIVESTOCK_OVERHAUL.get());
        }
    };

}
