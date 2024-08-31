package com.dragn0007.dragnlivestock.entities.chicken;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OChickenModel extends AnimatedGeoModel<OChicken> {

    public enum Variant {
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_black.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_brown.png")),
        CREAM(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_cream.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_grey.png")),
        ROOSTER(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_rooster.png")),
        TAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_tan.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/chicken_white.png"));

        //Add new entries to bottom when mod is public, else chickens will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/chicken_overhauled.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/chicken_overhauled.animation.json");
    public static final ResourceLocation BABY_MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_chicken_overhauled.geo.json");
    public static final ResourceLocation BABY_TEXTURE = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/baby_chicken.png");

    @Override
    public ResourceLocation getModelLocation(OChicken object) {
        if(object.isBaby())
            return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OChicken object) {
        if(object.isBaby())
            return BABY_TEXTURE;
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OChicken animatable) {
        return ANIMATION;
    }
}

