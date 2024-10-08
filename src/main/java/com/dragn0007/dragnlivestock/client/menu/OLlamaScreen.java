package com.dragn0007.dragnlivestock.client.menu;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OLlamaScreen extends AbstractContainerScreen<OLlamaMenu> {

    protected static final ResourceLocation SADDLED = new ResourceLocation(LivestockOverhaul.MODID, "textures/gui/o_horse_gui.png");
    protected static final ResourceLocation CHESTED = new ResourceLocation(LivestockOverhaul.MODID, "textures/gui/o_horse_gui_chested.png");


    protected final ResourceLocation texture;

    public OLlamaScreen(OLlamaMenu oHorseMenu, Inventory inventory, Component component) {
        super(oHorseMenu, inventory, component);
        this.texture = oHorseMenu.OLlama.isChested() ? CHESTED : SADDLED;
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

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
