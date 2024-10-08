package com.dragn0007.dragnlivestock.entities.llama;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OLlamaModel extends AnimatedGeoModel<OLlama> {

    public enum Variant {
        ASH(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_ash.png")),
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_black.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_brown.png")),
        CHOCOLATE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_chocolate.png")),
        COCOA(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_cocoa.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_grey.png")),
        TAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_tan.png")),
        WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/llama_white.png"));

        //Add new entries to bottom when mod is public, else llamas will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/llama_overhauled.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/llama_overhauled.animation.json");

    @Override
    public ResourceLocation getModelLocation(OLlama object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OLlama object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OLlama animatable) {
        return ANIMATION;
    }
}

