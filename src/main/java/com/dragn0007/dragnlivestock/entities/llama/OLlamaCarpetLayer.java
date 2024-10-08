package com.dragn0007.dragnlivestock.entities.llama;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class OLlamaCarpetLayer extends GeoLayerRenderer<OLlama> {
    private static final ResourceLocation[] TEXTURE_LOCATION = new ResourceLocation[]{
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/white.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/orange.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/magenta.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/light_blue.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/yellow.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/lime.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/pink.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/gray.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/light_gray.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/cyan.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/purple.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/blue.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/brown.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/green.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/red.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/llama/carpet/black.png")
    };

    public OLlamaCarpetLayer(IGeoRenderer<OLlama> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OLlama entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        DyeColor dyeColor = entity.getSwag();
        ResourceLocation resourceLocation = null;

        if (dyeColor != null) {
            resourceLocation = TEXTURE_LOCATION[dyeColor.getId()];
        }

        if (resourceLocation == null) {
            return;
        }

        RenderType renderType = RenderType.entityCutout(resourceLocation);
        matrixStackIn.pushPose();
        this.getRenderer().render(
                this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entity)),
                entity,
                partialTicks,
                renderType,
                matrixStackIn,
                bufferIn,
                bufferIn.getBuffer(renderType),
                packedLightIn,
                OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f
        );
        matrixStackIn.popPose();
    }
}
