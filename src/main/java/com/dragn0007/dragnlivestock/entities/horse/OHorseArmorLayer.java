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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class OHorseArmorLayer extends GeoLayerRenderer<OHorse> {
    public OHorseArmorLayer(IGeoRenderer<OHorse> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OHorse entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        List<ItemStack> armorSlots = new ArrayList<>();
        entity.getArmorSlots().forEach(armorSlots::add);

        ItemStack armorItemStack = armorSlots.size() > 1 ? armorSlots.get(1) : ItemStack.EMPTY;

        ResourceLocation resourceLocation = null;

        if (!armorItemStack.isEmpty() && armorItemStack.getItem() instanceof HorseArmorItem) {
            HorseArmorItem armorItem = (HorseArmorItem) armorItemStack.getItem();
            resourceLocation = armorItem.getTexture();
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
