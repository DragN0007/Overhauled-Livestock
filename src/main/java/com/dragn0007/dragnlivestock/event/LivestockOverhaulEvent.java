package com.dragn0007.dragnlivestock.event;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.bee.OBee;
import com.dragn0007.dragnlivestock.entities.bee.OBeeRender;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.chicken.OChickenRender;
import com.dragn0007.dragnlivestock.entities.cod.OCod;
import com.dragn0007.dragnlivestock.entities.cod.OCodRender;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.cow.OCowRender;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseRender;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbit;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbitRender;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmon;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmonRender;
import com.dragn0007.dragnlivestock.entities.unicorn.*;
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
        event.put(EntityTypes.O_BEE_ENTITY.get(), OBee.createAttributes().build());
        event.put(EntityTypes.O_RABBIT_ENTITY.get(), ORabbit.createAttributes().build());

        event.put(EntityTypes.OVERWORLD_UNICORN_ENTITY.get(), OverworldUnicorn.createBaseHorseAttributes().build());
        event.put(EntityTypes.NETHER_UNICORN_ENTITY.get(), NetherUnicorn.createBaseHorseAttributes().build());
        event.put(EntityTypes.END_UNICORN_ENTITY.get(), EndUnicorn.createBaseHorseAttributes().build());
    }

    @SubscribeEvent
    public static void clientSetupEvent(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityTypes.O_HORSE_ENTITY.get(), OHorseRender::new);
        EntityRenderers.register(EntityTypes.O_COW_ENTITY.get(), OCowRender::new);
        EntityRenderers.register(EntityTypes.O_CHICKEN_ENTITY.get(), OChickenRender::new);
        EntityRenderers.register(EntityTypes.O_SALMON_ENTITY.get(), OSalmonRender::new);
        EntityRenderers.register(EntityTypes.O_COD_ENTITY.get(), OCodRender::new);
        EntityRenderers.register(EntityTypes.O_BEE_ENTITY.get(), OBeeRender::new);
        EntityRenderers.register(EntityTypes.O_RABBIT_ENTITY.get(), ORabbitRender::new);

        EntityRenderers.register(EntityTypes.OVERWORLD_UNICORN_ENTITY.get(), OverworldUnicornRender::new);
        EntityRenderers.register(EntityTypes.NETHER_UNICORN_ENTITY.get(), NetherUnicornRender::new);
        EntityRenderers.register(EntityTypes.END_UNICORN_ENTITY.get(), EndUnicornRender::new);
    }
}