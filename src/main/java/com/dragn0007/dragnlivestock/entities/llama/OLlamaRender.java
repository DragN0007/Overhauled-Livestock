package com.dragn0007.dragnlivestock.entities.llama;

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

public class OLlamaRender extends ExtendedGeoEntityRenderer<OLlama> {

    public OLlamaRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OLlamaModel());
        this.addLayer(new OLlamaMarkingLayer(this));
        this.addLayer(new OLlamaCarpetLayer(this));
    }

    @Override
    public void render(GeoModel model, OLlama animatable, float partialTick, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (animatable.hasChest()) {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(false));
            model.getBone("halter").ifPresent(b -> b.setHidden(false));
        } else {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(true));
            model.getBone("halter").ifPresent(b -> b.setHidden(true));
        }

        if(animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        } else {
            poseStack.scale(1F, 1F, 1F);
        }

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureForBone(String boneName, OLlama animatable) {
        return null;
    }

    @Nullable
    @Override
    public ItemStack getHeldItemForBone(String boneName, OLlama animatable) {
        return null;
    }

    @Override
    public ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getHeldBlockForBone(String boneName, OLlama animatable) {
        return null;
    }

    @Override
    public void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OLlama animatable, IBone bone) {
    }

    @Override
    public void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, OLlama animatable) {
    }

    @Override
    public void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OLlama animatable, IBone bone) {
    }

    @Override
    public void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, OLlama animatable) {
    }
}


