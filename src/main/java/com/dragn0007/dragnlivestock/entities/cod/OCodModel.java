package com.dragn0007.dragnlivestock.entities.cod;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OCodModel extends AnimatedGeoModel<OCod> {

    public enum Variant {
        COD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/fish/cod.png"));

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/overhauled_cod.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/o_fish.animation.json");

    @Override
    public ResourceLocation getModelLocation(OCod object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OCod object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OCod animatable) {
        return ANIMATION;
    }
}

