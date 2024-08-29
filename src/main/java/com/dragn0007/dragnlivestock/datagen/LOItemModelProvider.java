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
    }

    private ItemModelBuilder simpleSpriteBlockItem(Block block) {
        return withExistingParent(block.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"block/" + block.getRegistryName().getPath()));
    }

    private ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"item/" + item.getRegistryName().getPath()));
    }

    private ItemModelBuilder itemNameBlockItem(Item item, String getTextureName) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"block/" + getTextureName));
    }

    private ItemModelBuilder advancedItem(Item item, String getTextureName) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"item/" + getTextureName));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(LivestockOverhaul.MODID,"item/" + item.getRegistryName().getPath()));
    }
}