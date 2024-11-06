package com.dragn0007.dragnlivestock.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class LOTags {

    public static class Items {

        public static final TagKey<Item> RAW_CHICKEN = forgeTag("raw_chicken");
        public static final TagKey<Item> RAW_PORK = forgeTag("raw_pork");
        public static final TagKey<Item> RAW_MUTTON = forgeTag("raw_mutton");
        public static final TagKey<Item> RAW_BEEF = forgeTag("raw_beef");
        public static final TagKey<Item> RAW_RABBIT = forgeTag("raw_rabbit");
        public static final TagKey<Item> COOKED_CHICKEN = forgeTag("cooked_chicken");
        public static final TagKey<Item> COOKED_PORK = forgeTag("cooked_pork");
        public static final TagKey<Item> COOKED_MUTTON = forgeTag("cooked_mutton");
        public static final TagKey<Item> COOKED_BEEF = forgeTag("cooked_beef");
        public static final TagKey<Item> COOKED_RABBIT = forgeTag("cooked_rabbit");

        public static final TagKey<Item> RAW_HORSE = forgeTag("raw_horse");
        public static final TagKey<Item> RAW_LLAMA = forgeTag("raw_llama");
        public static final TagKey<Item> RAW_UNICORN = forgeTag("raw_unicorn");
        public static final TagKey<Item> COOKED_HORSE = forgeTag("cooked_horse");
        public static final TagKey<Item> COOKED_LLAMA = forgeTag("cooked_llama");
        public static final TagKey<Item> COOKED_UNICORN = forgeTag("cooked_unicorn");

        public static final TagKey<Item> RAW_MEATS = forgeTag("raw_meats");
        public static final TagKey<Item> COOKED_MEATS = forgeTag("cooked_meats");

        public static final TagKey<Item> ROE = forgeTag("roe");
        public static final TagKey<Item> CHEESE = forgeTag("cheese");
        public static final TagKey<Item> MILK = forgeTag("milk");
        public static final TagKey<Item> EGG = forgeTag("egg");


        public static TagKey<Item> forgeTag (String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Entity_Types {
        public static final TagKey<EntityType<?>> HORSES = forgeTag("horses");
        public static final TagKey<EntityType<?>> RHG_HORSES = forgeTag("rhg_horses");
        public static final TagKey<EntityType<?>> SWEM_HORSES = forgeTag("swem_horses");

        public static TagKey<EntityType<?>> forgeTag(String name) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", name));
        }
    }

}
