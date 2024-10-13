package com.dragn0007.dragnlivestock.datagen;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.items.LOItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class LOItemModelProvider extends ItemModelProvider {
    public LOItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, LivestockOverhaul.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(LOItems.LIVESTOCK_OVERHAUL.get());

        simpleItem(LOItems.BRAND_TAG.get());
        simpleItem(LOItems.NETHERITE_HORSE_ARMOR.get());

        simpleItem(LOItems.SHEEP_MILK_BUCKET.get());
        simpleItem(LOItems.LLAMA_MILK_BUCKET.get());

        simpleItem(LOItems.CHEESE.get());
        simpleItem(LOItems.SHEEP_CHEESE.get());
        simpleItem(LOItems.LLAMA_CHEESE.get());

        simpleItem(LOItems.BEEF_RIB_STEAK.get());
        simpleItem(LOItems.BEEF_SIRLOIN_STEAK.get());
        simpleItem(LOItems.COOKED_BEEF_RIB_STEAK.get());
        simpleItem(LOItems.COOKED_BEEF_SIRLOIN_STEAK.get());

        simpleItem(LOItems.HORSE.get());
        simpleItem(LOItems.HORSE_RIB_STEAK.get());
        simpleItem(LOItems.HORSE_SIRLOIN_STEAK.get());
        simpleItem(LOItems.COOKED_HORSE.get());
        simpleItem(LOItems.COOKED_HORSE_RIB_STEAK.get());
        simpleItem(LOItems.COOKED_HORSE_SIRLOIN_STEAK.get());

        simpleItem(LOItems.LLAMA.get());
        simpleItem(LOItems.LLAMA_RIB.get());
        simpleItem(LOItems.LLAMA_LOIN.get());
        simpleItem(LOItems.COOKED_LLAMA.get());
        simpleItem(LOItems.COOKED_LLAMA_RIB.get());
        simpleItem(LOItems.COOKED_LLAMA_LOIN.get());

        simpleItem(LOItems.MUTTON_LOIN.get());
        simpleItem(LOItems.MUTTON_RIB.get());
        simpleItem(LOItems.COOKED_MUTTON_LOIN.get());
        simpleItem(LOItems.COOKED_MUTTON_RIB.get());

        simpleItem(LOItems.PORK_TENDERLOIN.get());
        simpleItem(LOItems.PORK_RIB_CHOP.get());
        simpleItem(LOItems.COOKED_PORK_TENDERLOIN.get());
        simpleItem(LOItems.COOKED_PORK_RIB_CHOP.get());

        simpleItem(LOItems.CHICKEN_THIGH.get());
        simpleItem(LOItems.COOKED_CHICKEN_THIGH.get());

        simpleItem(LOItems.RABBIT_THIGH.get());
        simpleItem(LOItems.COOKED_RABBIT_THIGH.get());

        simpleItem(LOItems.FISH_OIL.get());
        simpleItem(LOItems.ROE.get());

        simpleItem(LOItems.UNICORN.get());
        simpleItem(LOItems.UNICORN_RIB_STEAK.get());
        simpleItem(LOItems.UNICORN_SIRLOIN_STEAK.get());
        simpleItem(LOItems.COOKED_UNICORN.get());
        simpleItem(LOItems.COOKED_UNICORN_RIB_STEAK.get());
        simpleItem(LOItems.COOKED_UNICORN_SIRLOIN_STEAK.get());

        simpleItem(LOItems.OVERWORLD_UNICORN_HORN.get());
        simpleItem(LOItems.NETHER_UNICORN_HORN.get());
        simpleItem(LOItems.END_UNICORN_HORN.get());
    }

    protected ItemModelBuilder simpleSpriteBlockItem(Block block) {
        return withExistingParent(block.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"block/" + block.getRegistryName().getPath()));
    }

    protected ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"item/" + item.getRegistryName().getPath()));
    }

    protected ItemModelBuilder itemNameBlockItem(Item item, String getTextureName) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"block/" + getTextureName));
    }

    protected ItemModelBuilder advancedItem(Item item, String getTextureName) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"item/" + getTextureName));
    }

    protected ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"item/" + item.getRegistryName().getPath()));
    }
}