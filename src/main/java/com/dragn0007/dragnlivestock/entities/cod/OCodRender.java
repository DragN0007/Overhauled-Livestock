package com.dragn0007.dragnlivestock.entities.cod;

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

public class OCodRender extends ExtendedGeoEntityRenderer<OCod> {

    public OCodRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OCodModel());
    }

    @Override
    public void render(GeoModel model, OCod animatable, float partialTick, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureForBone(String boneName, OCod animatable) {
        return null;
    }

    @Nullable
    @Override
    public ItemStack getHeldItemForBone(String boneName, OCod animatable) {
        return null;
    }

    @Override
    public ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getHeldBlockForBone(String boneName, OCod animatable) {
        return null;
    }

    @Override
    public void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OCod animatable, IBone bone) {
    }

    @Override
    public void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, OCod animatable) {
    }

    @Override
    public void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OCod animatable, IBone bone) {
    }

    @Override
    public void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, OCod animatable) {
    }
}


