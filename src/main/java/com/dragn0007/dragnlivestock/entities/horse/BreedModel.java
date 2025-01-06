package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;

public enum BreedModel {
    MUSTANG(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_overhauled.geo.json")),
    ARDENNES(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_ardennes.geo.json")),
    KLADRUBER(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_kladruber.geo.json")),
    FJORD(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_fjord.geo.json")),
    THOROUGHBRED(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_thoroughbred.geo.json")),
    FRIESIAN(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_friesian.geo.json")),
    IRISH_COB(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_irish_cob.geo.json")),
    AMERICAN_QUARTER(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_american_quarter.geo.json")),
    PERCHERON(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_percheron.geo.json")),
    SELLE_FRANCAIS(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_selle_francais.geo.json")),
    MARWARI(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_marwari.geo.json")),
    MONGOLIAN(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_mongolian.geo.json")),
    SHIRE(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_shire.geo.json")),
    AKHAL_TEKE(new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_ahkal_teke.geo.json"));

    public final ResourceLocation resourceLocation;

    BreedModel(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public static BreedModel breedFromOrdinal(int ordinal) {
        return BreedModel.values()[ordinal % BreedModel.values().length];
    }

}


