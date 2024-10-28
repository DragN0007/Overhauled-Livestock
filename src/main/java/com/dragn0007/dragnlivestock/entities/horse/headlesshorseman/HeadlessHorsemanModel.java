package com.dragn0007.dragnlivestock.entities.horse.headlesshorseman;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.unicorn.EndUnicorn;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HeadlessHorsemanModel extends AnimatedGeoModel<HeadlessHorseman> {

    public enum Variant {
        HEADLESS_HORSEMAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/horse_headless_horseman.png"));

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/headless_horseman.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/headless_horseman.animation.json");
    @Override
    public ResourceLocation getModelLocation(HeadlessHorseman object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(HeadlessHorseman object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HeadlessHorseman animatable) {
        return ANIMATION;
    }
}

