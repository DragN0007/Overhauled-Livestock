package com.dragn0007.dragnlivestock.util;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LONetwork {
    public static class HandleHorseSpeedRequest {
        private final int speedMod;

        public HandleHorseSpeedRequest(int speedMod) {
            this.speedMod = speedMod;
        }

        public static void encode(HandleHorseSpeedRequest msg, FriendlyByteBuf buffer) {
            buffer.writeInt(msg.speedMod);
        }

        public static HandleHorseSpeedRequest decode(FriendlyByteBuf buffer) {
            return new HandleHorseSpeedRequest(buffer.readInt());
        }

        public static void handle(HandleHorseSpeedRequest msg, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            ctx.enqueueWork(() -> {
                ServerPlayer player = ctx.getSender();
                if(player != null) {
                    if(player.getVehicle() instanceof AbstractOHorse oHorse) {
                        oHorse.handleSpeedRequest(msg.speedMod);
                    }
                }
            });
            ctx.setPacketHandled(true);
        }
    }

    public static class HandleHorseEmoteRequest {
        public final boolean shouldBow;

        public HandleHorseEmoteRequest(boolean shouldBow) {
            this.shouldBow = shouldBow;
        }

        public static void encode(HandleHorseEmoteRequest msg, FriendlyByteBuf buffer) {
            buffer.writeBoolean(msg.shouldBow);
        }

        public static HandleHorseEmoteRequest decode(FriendlyByteBuf buffer) {
            return new HandleHorseEmoteRequest(buffer.readBoolean());
        }

        public static void handle(HandleHorseEmoteRequest msg, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            ctx.enqueueWork(() -> {
                ServerPlayer player = ctx.getSender();
                if(player != null) {
                    if(player.getVehicle() instanceof AbstractOHorse oHorse) {
                        oHorse.setBowing(msg.shouldBow);
                    }
                }
            });
            ctx.setPacketHandled(true);
        }
    }

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LivestockOverhaul.MODID, "lo_network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void commonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            INSTANCE.registerMessage(0, HandleHorseSpeedRequest.class, HandleHorseSpeedRequest::encode, HandleHorseSpeedRequest::decode, HandleHorseSpeedRequest::handle);
            INSTANCE.registerMessage(1, HandleHorseEmoteRequest.class, HandleHorseEmoteRequest::encode, HandleHorseEmoteRequest::decode, HandleHorseEmoteRequest::handle);
        });
    }
}
