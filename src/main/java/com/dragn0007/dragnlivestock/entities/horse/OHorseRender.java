package com.dragn0007.dragnlivestock.entities.horse;

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

public class OHorseRender extends ExtendedGeoEntityRenderer<OHorse> {

    public OHorseRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OHorseModel());
        this.addLayer(new OHorseMarkingLayer(this));
        this.addLayer(new OHorseCarpetLayer(this));
        this.addLayer(new OHorseArmorLayer(this));
    }

    @Override
    public void render(GeoModel model, OHorse animatable, float partialTick, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!animatable.isBaby()) {
            if (animatable.hasChest()) {
                model.getBone("saddlebags").ifPresent(b -> b.setHidden(false));
            } else {
                model.getBone("saddlebags").ifPresent(b -> b.setHidden(true));
            }

            if (animatable.isSaddled()) {
                model.getBone("saddle").ifPresent(b -> b.setHidden(false));
                model.getBone("saddle2").ifPresent(b -> b.setHidden(false));
                model.getBone("extras").ifPresent(b -> b.setHidden(false));
                model.getBone("front_right_shoe").ifPresent(b -> b.setHidden(false));
                model.getBone("front_left_shoe").ifPresent(b -> b.setHidden(false));
                model.getBone("back_right_shoe").ifPresent(b -> b.setHidden(false));
                model.getBone("back_left_shoe").ifPresent(b -> b.setHidden(false));
            } else {
                model.getBone("saddle").ifPresent(b -> b.setHidden(true));
                model.getBone("saddle2").ifPresent(b -> b.setHidden(true));
                model.getBone("extras").ifPresent(b -> b.setHidden(true));
                model.getBone("front_right_shoe").ifPresent(b -> b.setHidden(true));
                model.getBone("front_left_shoe").ifPresent(b -> b.setHidden(true));
                model.getBone("back_right_shoe").ifPresent(b -> b.setHidden(true));
                model.getBone("back_left_shoe").ifPresent(b -> b.setHidden(true));
            }

            if (animatable.isWearingArmor()) {
                model.getBone("body_armor").ifPresent(b -> b.setHidden(false));
                model.getBone("neck_armor").ifPresent(b -> b.setHidden(false));
                model.getBone("head_armor").ifPresent(b -> b.setHidden(false));
            } else {
                model.getBone("body_armor").ifPresent(b -> b.setHidden(true));
                model.getBone("neck_armor").ifPresent(b -> b.setHidden(true));
                model.getBone("head_armor").ifPresent(b -> b.setHidden(true));
            }
        }

        if (animatable.isBaby()) {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(true));
            model.getBone("saddle").ifPresent(b -> b.setHidden(true));
            model.getBone("saddle2").ifPresent(b -> b.setHidden(true));
            model.getBone("front_right_shoe").ifPresent(b -> b.setHidden(true));
            model.getBone("front_left_shoe").ifPresent(b -> b.setHidden(true));
            model.getBone("back_right_shoe").ifPresent(b -> b.setHidden(true));
            model.getBone("back_left_shoe").ifPresent(b -> b.setHidden(true));
            model.getBone("body_armor").ifPresent(b -> b.setHidden(true));
            model.getBone("neck_armor").ifPresent(b -> b.setHidden(true));
            model.getBone("head_armor").ifPresent(b -> b.setHidden(true));
        }

        if (animatable.hasDefaultMane()) {
            model.getBone("roached_mane").ifPresent(b -> b.setHidden(false));
            model.getBone("button_mane").ifPresent(b -> b.setHidden(true));
        }

        if (animatable.hasButtonMane()) {
            model.getBone("roached_mane").ifPresent(b -> b.setHidden(true));
            model.getBone("button_mane").ifPresent(b -> b.setHidden(false));
        }

        if (animatable.hasShortMane()) {
            model.getBone("roached_mane").ifPresent(b -> b.setHidden(false));
            model.getBone("button_mane").ifPresent(b -> b.setHidden(true));
            model.getBone("mane").ifPresent(b -> b.setScaleZ(0.5F));
        }

        if (animatable.hasDefaultTail()) {
            model.getBone("tail").ifPresent(b -> b.setScaleX(1.0F));
            model.getBone("tail").ifPresent(b -> b.setScaleY(1.0F));
            model.getBone("tail").ifPresent(b -> b.setScaleZ(1.0F));
            model.getBone("tail_bottom").ifPresent(b -> b.setHidden(false));
        }

        if (animatable.hasLongTail()) {
            model.getBone("tail").ifPresent(b -> b.setScaleX(1.0F));
            model.getBone("tail").ifPresent(b -> b.setScaleY(1.3F));
            model.getBone("tail").ifPresent(b -> b.setScaleZ(1.0F));
            model.getBone("tail_bottom").ifPresent(b -> b.setHidden(false));
        }

        if (animatable.hasShortTail()) {
            model.getBone("tail").ifPresent(b -> b.setScaleX(1.0F));
            model.getBone("tail").ifPresent(b -> b.setScaleY(0.6F));
            model.getBone("tail").ifPresent(b -> b.setScaleZ(1.0F));
            model.getBone("tail_bottom").ifPresent(b -> b.setHidden(false));
        }

        if (animatable.hasTuckedTail()) {
            model.getBone("tail").ifPresent(b -> b.setScaleY(0.7F));
            model.getBone("tail").ifPresent(b -> b.setScaleX(1.1F));
            model.getBone("tail").ifPresent(b -> b.setScaleZ(1.0F));
            model.getBone("tail_bottom").ifPresent(b -> b.setHidden(true));
        }

        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureForBone(String boneName, OHorse animatable) {
        return null;
    }

    @Nullable
    @Override
    public ItemStack getHeldItemForBone(String boneName, OHorse animatable) {
        return null;
    }

    @Override
    public ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getHeldBlockForBone(String boneName, OHorse animatable) {
        return null;
    }

    @Override
    public void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OHorse animatable, IBone bone) {
    }

    @Override
    public void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, OHorse animatable) {
    }

    @Override
    public void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, OHorse animatable, IBone bone) {
    }

    @Override
    public void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, OHorse animatable) {
    }
}


