package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OHorseMarkingLayer extends GeoLayerRenderer<OHorse> {
    public OHorseMarkingLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OHorse entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OHorse)entityLivingBaseIn).getOverlayLocation());
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
        APPALOOSA(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_appaloosa.png")),
        BALD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_bald.png")),
        BLANKET_APPALOOSA(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_blanket_appaloosa.png")),
        BLAZE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_blaze.png")),
        BLIND_EYE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_blind_eye.png")),
        BLIND_EYES(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_blind_eyes.png")),
        BLUE_EYE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_blue_eye.png")),
        BROWN_EYE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_brown_eye.png")),
        FULL_SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_full_socks.png")),
        FULL_SOCKS_FEATHERING(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_full_socks_feathering.png")),
        GOLD_EYE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_gold_eye.png")),
        HALF_SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_half_socks.png")),
        HALF_SOCKS_FEATHERING(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_half_socks_feathering.png")),
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_none.png")),
        OVERO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_overo.png")),
        OVERO_SPLASH(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_overo_splash.png")),
        PAINT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_paint.png")),
        PINK_BELLY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_pink_belly.png")),
        REVERSED_HALF_SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_reversed_half_socks.png")),
        REVERSED_FULL_SOCKS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_reversed_full_socks.png")),
        REVERSED_FULL_SOCKS_FEATHERING(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_reversed_full_socks_feathering.png")),
        REVERSED_HALF_SOCKS_FEATHERING(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_reversed_half_socks_feathering.png")),
        ROAN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_roan.png")),
        SNIP(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_snip.png")),
        SPLASH_OVERO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_splash_overo.png")),
        SPLASHED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_splashed.png")),
        SPOTTED(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_spotted.png")),
        STAR(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_star.png")),
        TOBIANO(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_tobiano.png")),
        ZORSE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/overlay/overlay_zorse.png"));

        //Add new entries to bottom when mod is public

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
