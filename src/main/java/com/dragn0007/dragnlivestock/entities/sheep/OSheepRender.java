package com.dragn0007.dragnlivestock.entities.sheep;

import com.dragn0007.dragnlivestock.entities.rabbit.ORabbitMarkingLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

public class OSheepRender extends ExtendedGeoEntityRenderer<OSheep> {

    public OSheepRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OSheepModel());
        this.addLayer(new OSheepWoolLayer(this));
    }

    @Override
    public void render(GeoModel model, OSheep animatable, float partialTick, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if(animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        } else {
            poseStack.scale(1F, 1F, 1F);
        }

        if (!animatable.isBaby() && animatable.isSheared()) {
                model.getBone("Wool1").ifPresent(b -> b.setHidden(true));
                model.getBone("Wool2").ifPresent(b -> b.setHidden(true));
            } else {
                model.getBone("Wool1").ifPresent(b -> b.setHidden(false));
                model.getBone("Wool2").ifPresent(b -> b.setHidden(false));
            }

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, OSheep animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, OSheep animatable) {
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, OSheep animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OSheep animatable, IBone bone) {
    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, OSheep animatable) {
    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OSheep animatable, IBone bone) {
    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, OSheep animatable) {
    }
}


