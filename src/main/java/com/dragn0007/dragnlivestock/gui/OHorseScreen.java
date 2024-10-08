package com.dragn0007.dragnlivestock.gui;

import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OHorseScreen extends AbstractContainerScreen<OHorseMenu> {

    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/horse.png");
    public final AbstractOHorse horse;
    private float xMouse;
    private float yMouse;

    public OHorseScreen(OHorseMenu oHorseMenu, Inventory inventory, Component component) {
        super(oHorseMenu, inventory, component);
        this.horse = oHorseMenu.oHorse;
        this.imageWidth = 175;
        this.imageHeight = 221;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    protected void renderBg(PoseStack p_98821_, float p_98822_, int p_98823_, int p_98824_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HORSE_INVENTORY_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_98821_, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (this.horse.hasChest()) {
            this.blit(p_98821_, i + 79, j + 17, 0, this.imageHeight, horse.getInventoryColumns() * 18, 54);
        }

        if (this.horse.isSaddleable()) {
            this.blit(p_98821_, i + 7, j + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }

        if (this.horse.canWearArmor()) {
                this.blit(p_98821_, i + 7, j + 35, 0, this.imageHeight + 54, 18, 18);
            }

        InventoryScreen.renderEntityInInventory(i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.horse);
    }
}
