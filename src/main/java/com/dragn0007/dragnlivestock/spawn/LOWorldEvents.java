package com.dragn0007.dragnlivestock.spawn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber (modid = LivestockOverhaul.MODID)
public class LOWorldEvents {
    @SubscribeEvent
            public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
    CreatureSpawnGeneration.onEntitySpawn(event);
    }
}
