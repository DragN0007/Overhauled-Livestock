package com.dragn0007.dragnlivestock.entities.unicorn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OverworldUnicornHornLayer extends GeoLayerRenderer<OverworldUnicorn> {
    public OverworldUnicornHornLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OverworldUnicorn entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OverworldUnicorn)entityLivingBaseIn).getOverlayLocation());
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
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_none.png")),
        BLUE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_blue.png")),
        DIAMOND(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_diamond.png")),
        EMERALD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_emerald.png")),
        GREEN(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_green.png")),
        LAPIS(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_lapis.png")),
        PEARL(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_pearl.png")),
        PINK(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_pink.png")),
        YELLOW(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_yellow.png"));

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
