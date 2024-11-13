package com.dragn0007.dragnlivestock.entities.rabbit;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class ORabbitMarkingLayer extends GeoLayerRenderer<ORabbit> {
    public ORabbitMarkingLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ORabbit entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((ORabbit)entityLivingBaseIn).getOverlayLocation());
        matrixStackIn.pushPose();
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
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
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_none.png")),
        BLOTCHED_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_blotched_black.png")),
        BLOTCHED_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_blotched_white.png")),
        DALMATION(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_dalmation.png")),
        HALF_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_half_black.png")),
        HALF_WHTIE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_half_black.png")),
        ENDED_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_ended_black.png")),
        ENDED_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_ended_white.png")),
        PINTO_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_pinto_black.png")),
        PINTO_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_pinto_white.png")),
        SPECKLED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_speckled.png")),
        STRIPED_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_striped_black.png")),
        STRIPED_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_striped_white.png")),
        TIPPED_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_tipped_black.png")),
        TIPPED_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/rabbit/overlay/overlay_tipped_white.png"));

        //Add new entries to bottom when mod is public, else rabbits will change textures during update.

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
