package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;

public enum BreedModel {
    STOCK(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_overhauled.geo.json")),
    DRAFT(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_ardennes.geo.json")),
    WARMBLOOD(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_kladruber.geo.json")),
    PONY(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_fjord.geo.json"));

    public final ResourceLocation resourceLocation;

    BreedModel(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public static BreedModel breedFromOrdinal(int ordinal) {
        return BreedModel.values()[ordinal % BreedModel.values().length];
    }

}

