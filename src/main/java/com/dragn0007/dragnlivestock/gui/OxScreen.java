package com.dragn0007.dragnlivestock.gui;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.cow.ox.Ox;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OxScreen extends AbstractContainerScreen<OxMenu> {
    public static final ResourceLocation OX_INVENTORY_LOCATION = new ResourceLocation(LivestockOverhaul.MODID, "textures/gui/ox.png");
    public final Ox ox;

    public OxScreen(OxMenu oxMenu, Inventory inventory, Component component) {
        super(oxMenu, inventory, component);
        this.ox = oxMenu.ox;
    }

    public void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, OX_INVENTORY_LOCATION);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

        if (this.ox.hasChest()) {
            this.blit(poseStack, x + 25, y + 17, 0, this.imageHeight, 145, 54);
        }

        if (this.ox.isSaddleable()) {
            this.blit(poseStack, x + 7, y + 17, 18, this.imageHeight + 54, 18, 18);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}

