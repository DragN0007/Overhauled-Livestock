package com.dragn0007.dragnlivestock.event;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.util.LONetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LivestockOverhaulClientEvent {

    public static final KeyMapping HORSE_SPEED_UP = new KeyMapping("key.dragnlivestock.horse_speed_up", InputConstants.KEY_LCONTROL, "key.dragnlivestock.categories.dragnlivestock");
    public static final KeyMapping HORSE_SLOW_DOWN = new KeyMapping("key.dragnlivestock.horse_slow_down", InputConstants.KEY_LALT, "key.dragnlivestock.categories.dragnlivestock");
    public static final KeyMapping HORSE_BOW = new KeyMapping("key.dragnlivestock.horse_bow", InputConstants.KEY_B, "key.dragnlivestock.categories.dragnlivestock");

    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(HORSE_SPEED_UP);
        ClientRegistry.registerKeyBinding(HORSE_SLOW_DOWN);
        ClientRegistry.registerKeyBinding(HORSE_BOW);
    }
}