package com.dragn0007.dragnlivestock.spawn;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreatureSpawnGeneration {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {

//        addEntityToSpecificBiomes(event, EntityTypes.OVERWORLD_UNICORN_ENTITY.get(),
//                1, 1, 1,
//                Biomes.MEADOW
//        );
//
//        addEntityToSpecificBiomes(event, EntityTypes.NETHER_UNICORN_ENTITY.get(),
//                1, 1, 1,
//                Biomes.BASALT_DELTAS
//        );
//
//        addEntityToSpecificBiomes(event, EntityTypes.END_UNICORN_ENTITY.get(),
//                1, 1, 1,
//                Biomes.END_HIGHLANDS
//        );
    }

    @SafeVarargs
    public static void addEntityToSpecificBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount, ResourceKey<Biome>... biomes) {
        boolean isBiomeSelected = Arrays.stream(biomes).map(ResourceKey::location)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if (isBiomeSelected && shouldSpawnEntity()) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    public static boolean shouldSpawnEntity() {
        int randomChance = new Random().nextInt(100);
        return randomChance < 10;
    }

    public static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
        base.add(new MobSpawnSettings.SpawnerData(type,weight, minCount, maxCount));
    }
}
