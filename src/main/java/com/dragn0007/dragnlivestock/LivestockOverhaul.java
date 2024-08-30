package com.dragn0007.dragnlivestock;

import com.dragn0007.dragnlivestock.blocks.LOBlocks;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import static com.dragn0007.dragnlivestock.LivestockOverhaul.MODID;

@Mod(MODID)
public class LivestockOverhaul
{

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "dragnlivestock";

    public LivestockOverhaul()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOItems.register(eventBus);
        LOBlocks.register(eventBus);
        EntityTypes.ENTITY_TYPES.register(eventBus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
