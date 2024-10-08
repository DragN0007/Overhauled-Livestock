package com.dragn0007.dragnlivestock.entities.sheep;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OSheepModel extends AnimatedGeoModel<OSheep> {

    public enum Variant {
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/sheep_black.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/sheep_brown.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/sheep_white.png"));

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/sheep_overhaul.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/sheep_overhaul.animation.json");

    @Override
    public ResourceLocation getModelLocation(OSheep object) {
        return MODEL;
    }

    public ResourceLocation getTextureLocation(OSheep object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OSheep animatable) {
        return ANIMATION;
    }
}

