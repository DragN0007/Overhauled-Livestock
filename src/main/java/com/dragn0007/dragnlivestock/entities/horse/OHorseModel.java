package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OHorseModel extends AnimatedGeoModel<OHorse> {

    public enum Variant {
        BAY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_bay.png")),
        BAY_ROAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_bay_roan.png")),
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_black.png")),
        BLOOD_BAY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_blood_bay.png")),
        BLUE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_blue.png")),
        BLUE_ROAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_blue_roan.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_brown.png")),
        BUCKSKIN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_buckskin.png")),
        CHAMPAGNE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_champagne.png")),
        CHOCOLATE_ROAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_chocolate_roan.png")),
        CHESTNUT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_chestnut.png")),
        CREAMY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_creamy.png")),
        DARK_BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_darkbrown.png")),
        FJORD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_fjord.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_grey.png")),
        GRULLO_DUN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_grullo_dun.png")),
        IVORY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_ivory.png")),
        LIVER_CHESTNUT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_liverchestnut.png")),
        PALAMINO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_palamino.png")),
        PALAMINO_ORANGE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_palamino_orange.png")),
        SEAL_BAY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_seal_bay.png")),
        STRAWBERRY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_strawberry.png")),
        WARM_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_warmblack.png")),
        WARM_GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_warmgrey.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_white.png"));

        //Add new entries to bottom when mod is public, else horses will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

//    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_overhauled.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/horse_overhaul.animation.json");

    public static final ResourceLocation BABY_MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_horse_overhauled.geo.json");
    @Override
    public ResourceLocation getModelLocation(OHorse object) {
        if(object.isBaby())
            return BABY_MODEL;
        return BreedModel.breedFromOrdinal(object.getBreed()).resourceLocation;
    }

    @Override
    public ResourceLocation getTextureLocation(OHorse object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OHorse animatable) {
        return ANIMATION;
    }
}

