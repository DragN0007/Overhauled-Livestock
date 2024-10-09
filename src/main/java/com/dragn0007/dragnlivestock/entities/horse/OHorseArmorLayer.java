package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class OHorseArmorLayer extends GeoLayerRenderer<OHorse> {
    private static final ResourceLocation[] TEXTURE_LOCATION = new ResourceLocation[]{
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_leather.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_iron.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_gold.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_diamond.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_netherite.png")
    };

    public OHorseArmorLayer(IGeoRenderer<OHorse> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OHorse entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        //dont mind all of this its just random BS

        HorseArmorItem armorItem = (HorseArmorItem) entity.getArmorSlots();
        ResourceLocation resourceLocation = null;

        if (armorItem != null) {
            resourceLocation = TEXTURE_LOCATION[armorItem.getId(Item.byId(1))];
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
