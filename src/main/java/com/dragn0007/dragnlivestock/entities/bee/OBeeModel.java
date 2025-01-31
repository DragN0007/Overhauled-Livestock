package com.dragn0007.dragnlivestock.entities.bee;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OBeeModel extends AnimatedGeoModel<OBee> {

    public enum Variant {
        BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/bumble_bee.png")),
        ASHY_MINING_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/ashy_mining_bee.png")),
        GARDEN_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/garden_bumble_bee.png")),
        HONEY_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/honey_bee.png")),
        RED_MASON_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/red_mason_bee.png")),
        RED_TAILED_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/red_tailed_bumble_bee.png")),
        TAWNY_MINING_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/tawny_mining_bee.png")),
        TREE_BEE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/bee/tree_bumble_bee.png")),
        ;

        public final ResourceLocation resourceLocation;
        Variant(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Variant variantFromOrdinal(int variant) {
            return Variant.values()[variant % Variant.values().length];
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(LivestockOverhaul.MODID, "geo/overhauled_bee.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(LivestockOverhaul.MODID, "animations/o_bee.animation.json");

    @Override
    public ResourceLocation getModelLocation(OBee oBee) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OBee oBee) {
        return oBee.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OBee oBee) {
        return ANIMATION;
    }
}

