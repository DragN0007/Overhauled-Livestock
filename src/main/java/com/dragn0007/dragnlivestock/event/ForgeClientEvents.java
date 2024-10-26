package com.dragn0007.dragnlivestock.event;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import com.dragn0007.dragnlivestock.util.LONetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEvents {
    @SubscribeEvent
    public static void onKeyPressEvent(InputEvent.KeyInputEvent event) {
        if(event.getAction() != GLFW.GLFW_RELEASE) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        if(LivestockOverhaulClientEvent.HORSE_SPEED_UP.getKey().getValue() == event.getKey()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.HandleHorseSpeedRequest(1));
        }

        if(LivestockOverhaulClientEvent.HORSE_SLOW_DOWN.getKey().getValue() == event.getKey()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.HandleHorseSpeedRequest(-1));
        }

        if(LivestockOverhaulClientEvent.HORSE_BOW.getKey().getValue() == event.getKey()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.PlayEmoteRequest("bow"));
        }

        if(LivestockOverhaulClientEvent.HORSE_PIAFFE.getKey().getValue() == event.getKey()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.PlayEmoteRequest("piaffe"));
        }
    }
}
