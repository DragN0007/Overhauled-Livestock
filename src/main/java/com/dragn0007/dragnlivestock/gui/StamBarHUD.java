package com.dragn0007.dragnlivestock.gui;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

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