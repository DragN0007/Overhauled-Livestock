package com.dragn0007.dragnlivestock.entities.unicorn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OverworldUnicornModel extends AnimatedGeoModel<OverworldUnicorn> {

    public enum Variant {
        OVERWORLD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/unicorn_overworld.png"));

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/unicorn.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/horse_overhaul.animation.json");
    @Override
    public ResourceLocation getModelLocation(OverworldUnicorn object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OverworldUnicorn object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OverworldUnicorn animatable) {
        return ANIMATION;
    }
}

