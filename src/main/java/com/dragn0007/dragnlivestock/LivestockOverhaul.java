package com.dragn0007.dragnlivestock;

import com.dragn0007.dragnlivestock.blocks.LOBlocks;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.dragn0007.dragnlivestock.spawn.SpawnReplacer;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFMLCommonSetupEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LivestockOverhaulCommonConfig.SPEC, "livestock-overhaul-common.toml");

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SpawnReplacer());
    }

    public static final EntityDataSerializer<ResourceLocation> RESOURCE_LOCATION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, ResourceLocation resourceLocation) {
            buf.writeResourceLocation(resourceLocation);
        }

        @Override
        public ResourceLocation read(FriendlyByteBuf buf) {
            return buf.readResourceLocation();
        }

        @Override
        public ResourceLocation copy(ResourceLocation resourceLocation) {
            return resourceLocation;
        }
    };

    static {
        EntityDataSerializers.registerSerializer(RESOURCE_LOCATION);
    }

    private void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        LONetwork.init();
    }
}
