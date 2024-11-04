package com.dragn0007.dragnlivestock.entities.cow.mooshroom;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OMooshroomModel extends AnimatedGeoModel<OMooshroom> {

    public enum Variant {
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/cow_white.png")),
        ROSE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/cow_rose.png")),
        PINK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/cow_pink.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mooshroom/mooshroom_brown.png")),
        RED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mooshroom/mooshroom_red.png")),
        HIGHLAND_BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mooshroom/highland_mooshroom_brown.png")),
        HIGHLAND_RED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mooshroom/highland_mooshroom_red.png"));

        //Add new entries to bottom when mod is public, else mooshrooms will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/mooshroom_overhaul.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/cow_overhaul.animation.json");

    public static final ResourceLocation BABY_MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_cow_overhaul.geo.json");
    @Override
    public ResourceLocation getModelLocation(OMooshroom object) {
        if(object.isBaby())
            return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OMooshroom object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OMooshroom animatable) {
        return ANIMATION;
    }
}

