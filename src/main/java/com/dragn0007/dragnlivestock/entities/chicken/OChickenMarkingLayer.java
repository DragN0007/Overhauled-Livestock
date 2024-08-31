package com.dragn0007.dragnlivestock.entities.chicken;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OChickenMarkingLayer extends GeoLayerRenderer<OChicken> {
    public OChickenMarkingLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OChicken entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OChicken)entityLivingBaseIn).getOverlayLocation());
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
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_none.png")),
        BLACK_ROOSTER(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_black_rooster.png")),
        BLUE_ROOSTER(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_blue_rooster.png")),
        HEAD_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_head_black.png")),
        HEAD_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_head_white.png")),
        SPECKLED_BLACK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_speckled_black.png")),
        SPECKLED_BROWN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_speckled_brown.png")),
        SPECKLED_WHITE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/chicken/overlay/overlay_speckled_white.png"));

        //Add new entries to bottom when mod is public, else chickens will change textures during update.

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
