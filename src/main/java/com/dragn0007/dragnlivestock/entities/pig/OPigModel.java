package com.dragn0007.dragnlivestock.entities.pig;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OPigModel extends AnimatedGeoModel<OPig> {

    public enum Variant {
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/pig/pig_black.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/pig/pig_brown.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/pig/pig_grey.png")),
        LIGHT_GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/pig/pig_light_grey.png")),
        PINK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/pig/pig_pink.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/pig/pig_white.png"));

        //Add new entries to bottom when mod is public, else pigs will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/pig_overhaul.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/pig_overhaul.animation.json");

    @Override
    public ResourceLocation getModelLocation(OPig object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OPig object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OPig animatable) {
        return ANIMATION;
    }
}

