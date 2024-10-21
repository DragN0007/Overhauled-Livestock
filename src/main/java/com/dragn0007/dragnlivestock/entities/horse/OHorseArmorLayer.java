package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class OHorseArmorLayer extends GeoLayerRenderer<OHorse> {
    public OHorseArmorLayer(IGeoRenderer<OHorse> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, OHorse entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        List<ItemStack> armorSlots = (List<ItemStack>) entity.getArmorSlots();
        if (armorSlots == null || armorSlots.size() <= 2) {
            return;
        }

        ItemStack armorItemStack = armorSlots.get(2);

        if (armorItemStack.isEmpty() || !(armorItemStack.getItem() instanceof HorseArmorItem)) {
            return;
        }

        ResourceLocation resourceLocation = null;

        if (armorItemStack.getItem() == Items.LEATHER_HORSE_ARMOR) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_leather.png");
        } else if (armorItemStack.getItem() == Items.IRON_HORSE_ARMOR) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_iron.png");
        } else if (armorItemStack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_gold.png");
        } else if (armorItemStack.getItem() == Items.DIAMOND_HORSE_ARMOR) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_diamond.png");
        } else if (armorItemStack.getItem() == LOItems.NETHERITE_HORSE_ARMOR.get()) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_netherite.png");
        } else if (armorItemStack.getItem() == LOItems.GRIFFITH_INSPIRED_HORSE_ARMOR.get()) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/armor/horse_armor_griffith_inspired.png");
        } else {
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
