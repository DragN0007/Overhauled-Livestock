package com.dragn0007.dragnlivestock.entities.donkey;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ODonkeyModel extends AnimatedGeoModel<ODonkey> {

    public enum Variant {
        DEFAULT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/donkey/donkey_default.png")),
        BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/donkey/donkey_brown.png")),
        PALE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/donkey/donkey_pale.png")),
        STRIPE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/donkey/donkey_stripe.png"));

        //Add new entries to bottom when mod is public, else donkeys will change textures during update.

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/donkey_overhauled.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/horse_overhaul.animation.json");

    public static final ResourceLocation BABY_MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/baby_donkey.geo.json");
//    public static final ResourceLocation BABY_ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/baby_horse.animation.json");
    @Override
    public ResourceLocation getModelLocation(ODonkey object) {
        if(object.isBaby())
            return BABY_MODEL;
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(ODonkey object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ODonkey animatable) {
        return ANIMATION;
    }
}

