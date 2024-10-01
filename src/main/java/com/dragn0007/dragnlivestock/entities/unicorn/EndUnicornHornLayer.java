package com.dragn0007.dragnlivestock.entities.unicorn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class EndUnicornHornLayer extends GeoLayerRenderer<EndUnicorn> {
    public EndUnicornHornLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EndUnicorn entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((EndUnicorn)entityLivingBaseIn).getOverlayLocation());
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
        END_CRYSTAL(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_endcrystal.png")),
        ENDER_DRAGON(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_enderdragon.png")),
        ENDER_EYE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_endereye.png")),
        ENDER_PEARL(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_enderpearl.png")),
        END_GATEWAY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_endgateway.png")),
        END_ROD(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_endrod.png")),
        PURPUR(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_purpur.png")),
        VOID(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/unicorn/horn/overlay_horn_void.png"));

        public final ResourceLocation resourceLocation;
        Overlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static Overlay overlayFromOrdinal(int overlay) { return Overlay.values()[overlay % Overlay.values().length];
        }
    }

}
