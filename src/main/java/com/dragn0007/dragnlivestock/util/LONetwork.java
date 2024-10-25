package com.dragn0007.dragnlivestock.util;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
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


    public static class PlayEmoteRequest {
        public final String emoteName;

        public PlayEmoteRequest( String emoteName) {
            this.emoteName = emoteName;
        }

        public static void encode(PlayEmoteRequest msg, FriendlyByteBuf buffer) {
            buffer.writeUtf(msg.emoteName);
        }

        public static PlayEmoteRequest decode(FriendlyByteBuf buffer) {
            return new PlayEmoteRequest(buffer.readUtf());
        }

        public static void handle(PlayEmoteRequest msg, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            ctx.enqueueWork(() -> {
                ServerPlayer player = ctx.getSender();
                if(player != null) {
                    if(player.getVehicle() instanceof AbstractOHorse oHorse) {
                        int id = oHorse.getId();
                        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> oHorse), new PlayEmoteResponse(id, msg.emoteName));
                    }
                }
            });
            ctx.setPacketHandled(true);
        }
    }


    public static class PlayEmoteResponse {
        public final int id;
        public final String emoteName;

        public PlayEmoteResponse(int id, String emoteName) {
            this.id = id;
            this.emoteName = emoteName;
        }

        public static void encode(PlayEmoteResponse msg, FriendlyByteBuf buffer) {
            buffer.writeInt(msg.id);
            buffer.writeUtf(msg.emoteName);
        }

        public static PlayEmoteResponse decode(FriendlyByteBuf buffer) {
            int id = buffer.readInt();
            String emoteName = buffer.readUtf();
            return new PlayEmoteResponse(id, emoteName);
        }

        public static void handle(PlayEmoteResponse msg, Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            ctx.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if(level != null) {
                    Entity entity = level.getEntity(msg.id);
                    if(entity instanceof AbstractOHorse oHorse) {
                        oHorse.playEmote(msg.emoteName);
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
            INSTANCE.registerMessage(1, PlayEmoteRequest.class, PlayEmoteRequest::encode, PlayEmoteRequest::decode, PlayEmoteRequest::handle);
            INSTANCE.registerMessage(2, PlayEmoteResponse.class, PlayEmoteResponse::encode, PlayEmoteResponse::decode, PlayEmoteResponse::handle);
        });
    }
}
