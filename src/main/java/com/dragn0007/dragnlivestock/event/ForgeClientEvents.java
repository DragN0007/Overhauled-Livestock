package com.dragn0007.dragnlivestock.event;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.util.LONetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.core.builder.ILoopType;

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
            LONetwork.INSTANCE.sendToServer(new LONetwork.PlayEmoteRequest("bow", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }

        if(LivestockOverhaulClientEvent.HORSE_PIAFFE.getKey().getValue() == event.getKey()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.PlayEmoteRequest("piaffe", ILoopType.EDefaultLoopTypes.LOOP));
        }

        if (event.getAction() == InputConstants.RELEASE && event.getKey() == LivestockOverhaulClientEvent.HORSE_WAVE.getKey().getValue()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.PlayEmoteRequest("wave", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }

        if (event.getAction() == InputConstants.RELEASE && event.getKey() == LivestockOverhaulClientEvent.HORSE_LEVADE.getKey().getValue()) {
            LONetwork.INSTANCE.sendToServer(new LONetwork.PlayEmoteRequest("levade", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
    }
}
