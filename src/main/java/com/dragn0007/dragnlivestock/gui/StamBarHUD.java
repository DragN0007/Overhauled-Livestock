package com.dragn0007.dragnlivestock.gui;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.checkerframework.checker.signature.qual.Identifier;

@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StamBarHUD {

//    @SubscribeEvent
//    public static void onFMLClientSetupEvent(FMLClientSetupEvent event) {
//        OverlayRegistry.registerOverlayTop("horse_stamina_bar", (gui, poseStack, partialTick, width, height) -> {
//            Minecraft minecraft = Minecraft.getInstance();
//            Player player = (Player) minecraft.getCameraEntity();
//
//            if (!minecraft.options.hideGui && player instanceof LocalPlayer && player.getVehicle() instanceof AbstractOHorse oHorse) {
//                    ResourceLocation texture = (new ResourceLocation(LivestockOverhaul.MODID, "textures/gui/horse_stamina_bar.png"));
//                    int x = (width / 2) + 100;
//                    int y = height - 61;
//
//                    gui.setupOverlayRenderState(true, false, texture);
//                    gui.blit(poseStack, x, y, 0, 0, 34, 61);
//            }
//        });
//    }
}