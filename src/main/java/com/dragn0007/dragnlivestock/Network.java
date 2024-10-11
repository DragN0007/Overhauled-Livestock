package com.dragn0007.dragnlivestock;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class Network {

    public static class ToggleMovementRequest {
        private final int id;

        public ToggleMovementRequest(int id) {
            this.id = id;
        }

        public static void encode(ToggleMovementRequest msg, FriendlyByteBuf buffer) {
            buffer.writeInt(msg.id);
        }

        public static ToggleMovementRequest decode(FriendlyByteBuf buffer) {
            return new ToggleMovementRequest(buffer.readInt());
        }

        public static void handle(ToggleMovementRequest msg, Supplier<NetworkEvent.Context> context) {
            ServerPlayer player = context.get().getSender();
        }
    }

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LivestockOverhaul.MODID, "lo_network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, ToggleMovementRequest.class, ToggleMovementRequest::encode, ToggleMovementRequest::decode, ToggleMovementRequest::handle);
    }
}
