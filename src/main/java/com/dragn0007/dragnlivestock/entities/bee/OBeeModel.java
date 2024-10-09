package com.dragn0007.dragnlivestock.entities.bee;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OBeeModel extends AnimatedGeoModel<OBee> {

    public enum Variant {
        BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/bee.png"));

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) { return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/overhauled_bee.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/o_bee.animation.json");
    public static final ResourceLocation POLLINATED_TEXTURE = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/bee_nectar.png");

    @Override
    public ResourceLocation getModelLocation(OBee object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OBee object) {
        if (object.hasNectar()) {
            return POLLINATED_TEXTURE;
        }
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OBee animatable) {
        return ANIMATION;
    }
}
