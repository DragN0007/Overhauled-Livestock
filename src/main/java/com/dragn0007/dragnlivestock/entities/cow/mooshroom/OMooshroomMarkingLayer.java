package com.dragn0007.dragnlivestock.entities.cow.mooshroom;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OMooshroomMarkingLayer extends GeoLayerRenderer<OMooshroom> {
    public OMooshroomMarkingLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OMooshroom entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OMooshroom)entityLivingBaseIn).getOverlayLocation());
        matrixStackIn.pushPose();
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
        this.getRenderer().render(
                this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entityLivingBaseIn)),
                entityLivingBaseIn,
                partialTicks,
                renderType,
                matrixStackIn,
                bufferIn,
                bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }

    public enum Overlay {
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_none.png")),
        BLACK_SPECKLED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_black_speckled.png")),
        BLAZE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_blaze.png")),
        DAIRY_PAINT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_dairy_paint.png")),
        OVERO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_overo.png")),
        PAINT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_paint.png")),
        REVERSE_PAINT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_reverse_paint.png")),
        SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_socks.png")),
        SPLASH_STRIPE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_splash_stripe.png")),
        WHITE_SPECKLED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_white_speckled.png")),
        BROWN_FULL(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_reverse_paint.png")),
        RED_FULL(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_socks.png")),
        BROWN_HALF(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_splash_stripe.png")),
        RED_HALF(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_white_speckled.png"));

        //Add new entries to bottom when mod is public, else mooshrooms will change textures during update.

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
