package com.dragn0007.dragnlivestock.entities.rabbit;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ORabbitModel extends AnimatedGeoModel<ORabbit> {

    public enum Variant {
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_black.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_brown.png")),
        GOLD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_gold.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_grey.png")),
        RED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_red.png")),
        TAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_tan.png")),
        WARM_GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_warm_grey.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/rabbit_white.png"));

        //Add new entries to bottom when mod is public, else rabbits will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/rabbit_overhauled.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/rabbit_overhauled.animation.json");

    @Override
    public ResourceLocation getModelLocation(ORabbit object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(ORabbit object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ORabbit animatable) {
        return ANIMATION;
    }
}

