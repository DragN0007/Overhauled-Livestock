package com.dragn0007.dragnlivestock.entities.cow.ox;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OxModel extends AnimatedGeoModel<Ox> {

    public enum Variant {
        BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/ox/ox_black.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/ox/ox_brown.png")),
        TAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/ox/ox_tan.png")),
        PALE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/ox/ox_pale.png"));

        //Add new entries to bottom when mod is public, else oxen will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/ox.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/cow_overhaul.animation.json");

    public static final ResourceLocation BABY_MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_cow_overhaul.geo.json");
    @Override
    public ResourceLocation getModelLocation(Ox object) {
        if(object.isBaby())
            return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(Ox object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Ox animatable) {
        return ANIMATION;
    }
}