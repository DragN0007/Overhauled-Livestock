package com.dragn0007.dragnlivestock.entities.cow.mooshroom;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OMooshroomHornLayer extends GeoLayerRenderer<OMooshroom> {
    public OMooshroomHornLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OMooshroom entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = RenderType.entityCutout(((OMooshroom)entityLivingBaseIn).getHornsLocation());
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
        NONE(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/overlay/overlay_none.png")),
        SHORT(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/horn_overlay/overlay_short_horns.png")),
        MEDIUM(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/horn_overlay/overlay_medium_horns.png")),
        LONG(new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/cow/horn_overlay/overlay_long_horns.png"));

        //Add new entries to bottom when mod is public, else mooshrooms will change textures during update.

        public final ResourceLocation resourceLocation;
        HornOverlay(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public static HornOverlay hornOverlayFromOrdinal(int hornOverlay) { return HornOverlay.values()[hornOverlay % HornOverlay.values().length];
        }
    }

}
