package com.dragn0007.dragnlivestock.entities.llama;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OLlamaMarkingLayer extends GeoLayerRenderer<OLlama> {
    public OLlamaMarkingLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OLlama entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OLlama)entityLivingBaseIn).getOverlayLocation());
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
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/none.png")),
        BUTT_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_butt_black.png")),
        BUTT_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_butt_white.png")),
        FRONT_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_front_black.png")),
        FRONT_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_front_white.png")),
        HALVED_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_halved_black.png")),
        HALVED_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_halved_white.png")),
        SPLASH_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_splash_black.png")),
        SPLASH_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_splash_white.png")),
        SPOT_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_spot_black.png")),
        SPOT_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/overlay/overlay_spot_white.png"));

        //Add new entries to bottom when mod is public, else llamas will change textures during update.

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
