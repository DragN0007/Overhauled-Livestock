package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OHorseModel extends AnimatedGeoModel<OHorse> {

    public enum Variant {
        BAY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_bay.png")),
        BAY_ROAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_bay_roan.png")),
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_black.png")),
        BLUE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_blue.png")),
        BLUE_ROAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_blue_roan.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_brown.png")),
        BUCKSKIN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_buckskin.png")),
        CHAMPAGNE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_champagne.png")),
        CHESTNUT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_chestnut.png")),
        CREAMY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_creamy.png")),
        DARKBROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_darkbrown.png")),
        FJORD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_fjord.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_grey.png")),
        IVORY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_ivory.png")),
        LIVER_CHESTNUT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_liver_chestnut.png")),
        PALAMINO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_palamino.png")),
        STRAWBERRY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_strawberry.png")),
        WARM_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_warm_black.png")),
        WARM_GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_warm_grey.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_white.png"));

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public enum Overlay {
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_none.png")),
        APPALOOSA(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_appaloosa.png")),
        BLANKET_APPALOOSA(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_blanket_appaloosa.png")),
        HALF_SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_half_socks.png")),
        REVERSED_HALF_SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_reversed_half_socks.png")),
        HALF_SOCKS_FEATHERING(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_half_socks_feathering.png")),
        REVERSED_HALF_SOCKS_FEATHERING(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_reversed_half_socks_feathering.png")),
        OVERO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_overo.png")),
        PAINT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_paint.png")),
        SPLASHED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_splashed.png")),
        SPOTTED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay_spotted.png"));

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

    public static final ResourceLocation model = new ResourceLocation(LivestockOverhaul.MODID, "geo/horse_overhauled.geo.json");
    public static final ResourceLocation animation = new ResourceLocation(LivestockOverhaul.MODID, "animations/horse_overhaul.animation.json");



    public static final ResourceLocation baby_model = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_horse_overhauled.geo.json");
    @Override
    public ResourceLocation getModelLocation(OHorse object) {
        if(object.isBaby())
            return baby_model;
        return model;
    }

    @Override
    public ResourceLocation getTextureLocation(OHorse object) {
        return object.getTextureLocation();
    }

    public ResourceLocation getOverlayLocation(OHorse object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OHorse animatable) {
        return animation;
    }

}

