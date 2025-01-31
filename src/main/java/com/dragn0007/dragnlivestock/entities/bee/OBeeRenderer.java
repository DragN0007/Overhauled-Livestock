package com.dragn0007.dragnlivestock.entities.bee;

import com.dragn0007.dragnlivestock.entities.chicken.OChickenMarkingLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class OBeeRenderer extends GeoEntityRenderer<OBee> {

    public OBeeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OBeeModel());
        this.addLayer(new BeePollenLayer(this));
    }

    @Override
    public void render(OBee animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if(animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        } else {
            poseStack.scale(1F, 1F, 1F);
        }

        if(!animatable.isBaby()) {

            if (animatable.getVariant() == 0) {
                poseStack.scale(1F, 1F, 1F);
            }

            if (animatable.getVariant() == 1) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            }

            if (animatable.getVariant() == 2) {
                poseStack.scale(1F, 1F, 1F);
            }

            if (animatable.getVariant() == 3) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            }

            if (animatable.getVariant() == 4) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            }

            if (animatable.getVariant() == 5) {
                poseStack.scale(0.8F, 0.8F, 0.8F);
            }

            if (animatable.getVariant() == 6) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            }

            if (animatable.getVariant() == 7) {
                poseStack.scale(1.1F, 1.1F, 1.1F);
            }
        }

        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}


