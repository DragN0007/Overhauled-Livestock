package com.dragn0007.dragnlivestock.entities.sheep;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OSheepHornLayer extends GeoLayerRenderer<OSheep> {
    public OSheepHornLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OSheep entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OSheep)entityLivingBaseIn).getHornsLocation());
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

    public enum HornOverlay {
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/none.png")),
        CURLY(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/horns/overlay_horns_curly.png")),
        SHORT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/horns/overlay_horns_short.png")),
        LONG(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/horns/overlay_horns_long.png"));

        public final ResourceLocation resourceLocation;
        HornOverlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static HornOverlay hornOverlayFromOrdinal(int overlay) { return HornOverlay.values()[overlay % HornOverlay.values().length];
        }
    }

}
