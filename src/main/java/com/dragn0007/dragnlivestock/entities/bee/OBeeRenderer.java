package com.dragn0007.dragnlivestock.entities.bee;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class OBeeRenderer extends GeoEntityRenderer<OBee> {

    public OBeeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OBeeModel());
    }

    @Override
    public void render(OBee animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        } else {
            poseStack.scale(1F, 1F, 1F);
        }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}


