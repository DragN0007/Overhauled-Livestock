package com.dragn0007.dragnlivestock.event;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.chicken.OChickenRender;
import com.dragn0007.dragnlivestock.entities.cod.OCod;
import com.dragn0007.dragnlivestock.entities.cod.OCodRender;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.cow.OCowRender;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseRender;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmon;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmonRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class LivestockOverhaulEvent {

    @SubscribeEvent
    public static void entityAttrbiuteCreationEvent(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.O_HORSE_ENTITY.get(), OHorse.createBaseHorseAttributes().build());
        event.put(EntityTypes.O_COW_ENTITY.get(), OCow.createAttributes().build());
        event.put(EntityTypes.O_CHICKEN_ENTITY.get(), OChicken.createAttributes().build());
        event.put(EntityTypes.O_SALMON_ENTITY.get(), OSalmon.createAttributes().build());
        event.put(EntityTypes.O_COD_ENTITY.get(), OCod.createAttributes().build());
    }

    @SubscribeEvent
    public static void clientSetupEvent(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityTypes.O_HORSE_ENTITY.get(), OHorseRender::new);
        EntityRenderers.register(EntityTypes.O_COW_ENTITY.get(), OCowRender::new);
        EntityRenderers.register(EntityTypes.O_CHICKEN_ENTITY.get(), OChickenRender::new);
        EntityRenderers.register(EntityTypes.O_SALMON_ENTITY.get(), OSalmonRender::new);
        EntityRenderers.register(EntityTypes.O_COD_ENTITY.get(), OCodRender::new);
    }
}