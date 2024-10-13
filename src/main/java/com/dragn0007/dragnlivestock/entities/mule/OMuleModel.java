package com.dragn0007.dragnlivestock.entities.mule;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OMuleModel extends AnimatedGeoModel<OMule> {

    public enum Variant {
        DEFAULT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mule/mule_default.png")),
        GREY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mule/mule_grey.png")),
        SMOKEY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/mule/mule_smokey.png"));

        //Add new entries to bottom when mod is public, else mules will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/mule_overhauled.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/horse_overhaul.animation.json");

    public static final ResourceLocation BABY_MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_donkey.geo.json");
    public static final ResourceLocation BABY_ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/baby_horse.animation.json");
    @Override
    public ResourceLocation getModelLocation(OMule object) {
        if(object.isBaby())
            return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OMule object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OMule animatable) {
        if(animatable.isBaby())
            return BABY_ANIMATION;
        return ANIMATION;
    }
}

