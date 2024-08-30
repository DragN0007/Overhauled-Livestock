package com.dragn0007.dragnlivestock.entities.cow;

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

public class OCowRender extends ExtendedGeoEntityRenderer<OCow> {

    public OCowRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OCowModel());
        this.addLayer(new OCowMarkingLayer(this));
    }

    @Override
    public void render(GeoModel model, OCow animatable, float partialTick, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (animatable.isChested()) {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(false));
            model.getBone("halter").ifPresent(b -> b.setHidden(false));
        } else {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(true));
            model.getBone("halter").ifPresent(b -> b.setHidden(true));
        }

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, OCow animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, OCow animatable) {
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, OCow animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OCow animatable, IBone bone) {
    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, OCow animatable) {
    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OCow animatable, IBone bone) {
    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, OCow animatable) {
    }
}


