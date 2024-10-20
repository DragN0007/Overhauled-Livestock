package com.dragn0007.dragnlivestock.event;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.bee.OBee;
import com.dragn0007.dragnlivestock.entities.bee.OBeeRenderer;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.chicken.OChickenRender;
import com.dragn0007.dragnlivestock.entities.cod.OCod;
import com.dragn0007.dragnlivestock.entities.cod.OCodRender;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.cow.OCowRender;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkeyRender;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseRender;
import com.dragn0007.dragnlivestock.entities.llama.OLlama;
import com.dragn0007.dragnlivestock.entities.llama.OLlamaRender;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.entities.mule.OMuleRender;
import com.dragn0007.dragnlivestock.entities.pig.OPig;
import com.dragn0007.dragnlivestock.entities.pig.OPigRender;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbit;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbitRender;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmon;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmonRender;
import com.dragn0007.dragnlivestock.entities.sheep.OSheep;
import com.dragn0007.dragnlivestock.entities.sheep.OSheepRender;
import com.dragn0007.dragnlivestock.entities.unicorn.*;
import com.dragn0007.dragnlivestock.gui.LOMenuTypes;
import com.dragn0007.dragnlivestock.gui.OHorseScreen;
import com.dragn0007.dragnlivestock.util.LONetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LivestockOverhaulClientEvent {

    public static final KeyMapping HORSE_DECREASE_SPEED = new KeyMapping("key.dragnlivestock.horse_decrease_speed", InputConstants.KEY_LALT, "key.dragnlivestock.categories.dragnlivestock");
    public static final KeyMapping HORSE_INCREASE_SPEED = new KeyMapping("key.dragnlivestock.horse_increase_speed", InputConstants.KEY_LCONTROL, "key.dragnlivestock.categories.dragnlivestock");

    public static class LivestockOverhaulClientEvents {
        @SubscribeEvent
        public static void registerKeyBindings(FMLClientSetupEvent event) {
            ClientRegistry.registerKeyBinding(HORSE_DECREASE_SPEED);
            ClientRegistry.registerKeyBinding(HORSE_INCREASE_SPEED);
        }
    }

    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onKeyPressEvent(InputEvent.KeyInputEvent event) {
            if(event.getAction() != GLFW.GLFW_RELEASE) {
                return;
            }

            Player player = Minecraft.getInstance().player;
            if(player == null) return;

            if(HORSE_INCREASE_SPEED.getKey().getValue() == event.getKey()) {
                while(HORSE_INCREASE_SPEED.consumeClick()) {
                    LONetwork.INSTANCE.sendToServer(new LONetwork.HandleHorseSpeedRequest(1));
                }
            }

            if(HORSE_DECREASE_SPEED.getKey().getValue() == event.getKey()) {
                while(HORSE_DECREASE_SPEED.consumeClick()) {
                    LONetwork.INSTANCE.sendToServer(new LONetwork.HandleHorseSpeedRequest(-1));
                }
            }
        }
    }
}