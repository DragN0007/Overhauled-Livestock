package com.dragn0007.dragnlivestock.spawn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID)
public class LOWorldEvents {

    @SubscribeEvent
    public static void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
        if(LivestockOverhaulCommonConfig.FAILSAFE_REPLACER.get()) {
            return;
        }

        Holder<? extends ConfiguredFeature<?, ?>> holder = event.getFeature();
        if(holder != null) {
            ConfiguredFeature<?, ?> configuredFeature = holder.value();

            if(configuredFeature.config() instanceof TreeConfiguration treeConfiguration) {
                float probability = (0.002f + 0.02f + 0.05f + 1f) / 4f;
                if (holder.equals(TreeFeatures.SUPER_BIRCH_BEES_0002) || holder.equals(TreeFeatures.OAK_BEES_0002) || holder.equals(TreeFeatures.BIRCH_BEES_0002) || holder.equals(TreeFeatures.FANCY_OAK_BEES_0002)) {
                    probability = 0.002f;
                } else if(holder.equals(TreeFeatures.OAK_BEES_002) || holder.equals(TreeFeatures.BIRCH_BEES_002) || holder.equals(TreeFeatures.FANCY_OAK_BEES_002)) {
                    probability = 0.02f;
                } else if(holder.equals(TreeFeatures.OAK_BEES_005) || holder.equals(TreeFeatures.BIRCH_BEES_005) || holder.equals(TreeFeatures.FANCY_OAK_BEES_005)) {
                    probability = 0.05f;
                } else if(holder.equals(TreeFeatures.SUPER_BIRCH_BEES) || holder.equals(TreeFeatures.FANCY_OAK_BEES)) {
                    probability = 1f;
                }

                List<TreeDecorator> replacedDecorators = new ArrayList<>();
                for(TreeDecorator treeDecorator : treeConfiguration.decorators) {
                    if(treeDecorator instanceof BeehiveDecorator) {
                        float finalProbability = probability;
                        replacedDecorators.add(new BeehiveDecorator(finalProbability) {
                            private static final Direction[] SPAWN_DIRECTIONS = Direction.Plane.HORIZONTAL.stream().filter((direction) -> direction != Direction.SOUTH.getOpposite()).toArray(Direction[]::new);

                            @Override
                            public void place(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> consumer, Random random, List<BlockPos> posList1, List<BlockPos> posList2) {
                                if (!(random.nextFloat() >= finalProbability)) {
                                    int i = !posList2.isEmpty() ? Math.max(posList2.get(0).getY() - 1, posList1.get(0).getY() + 1) : Math.min(posList1.get(0).getY() + 1 + random.nextInt(3), posList1.get(posList1.size() - 1).getY());
                                    List<BlockPos> list = posList1.stream().filter((blockPos) -> blockPos.getY() == i).flatMap((blockPos) -> Stream.of(SPAWN_DIRECTIONS).map(blockPos::relative)).collect(Collectors.toList());
                                    if (!list.isEmpty()) {
                                        Collections.shuffle(list);
                                        Optional<BlockPos> optional = list.stream().filter((blockPos) -> Feature.isAir(reader, blockPos) && Feature.isAir(reader, blockPos.relative(Direction.SOUTH))).findFirst();
                                        if (optional.isPresent()) {
                                            consumer.accept(optional.get(), Blocks.BEE_NEST.defaultBlockState().setValue(BeehiveBlock.FACING, Direction.SOUTH));
                                            reader.getBlockEntity(optional.get(), BlockEntityType.BEEHIVE).ifPresent((beehiveBlockEntity) -> {
                                                int j = 2 + random.nextInt(2);

                                                for(int k = 0; k < j; ++k) {
                                                    CompoundTag compoundtag = new CompoundTag();
                                                    compoundtag.putString("id", EntityTypes.O_BEE_ENTITY.getId().toString());
                                                    beehiveBlockEntity.storeBee(compoundtag, random.nextInt(599), false);
                                                }

                                            });
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        replacedDecorators.add(treeDecorator);
                    }
                }

                TreeConfiguration.TreeConfigurationBuilder replacedTreeConfigurationBuilder = new TreeConfiguration.TreeConfigurationBuilder(
                        treeConfiguration.trunkProvider,
                        treeConfiguration.trunkPlacer,
                        treeConfiguration.foliageProvider,
                        treeConfiguration.foliagePlacer,
                        treeConfiguration.minimumSize
                ).dirt(treeConfiguration.dirtProvider).decorators(replacedDecorators);

                if(treeConfiguration.forceDirt) {
                    replacedTreeConfigurationBuilder = replacedTreeConfigurationBuilder.forceDirt();
                }

                if(treeConfiguration.ignoreVines) {
                    replacedTreeConfigurationBuilder = replacedTreeConfigurationBuilder.ignoreVines();
                }

                ConfiguredFeature<?, ?> replacedConfiguredFeature = new ConfiguredFeature<>(Feature.TREE, replacedTreeConfigurationBuilder.build());
                Holder<ConfiguredFeature<?, ?>> replacedHolder = new Holder.Direct<>(replacedConfiguredFeature);
                event.setFeature(replacedHolder);
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoadEvent(ChunkEvent.Load event) {
        if(LivestockOverhaulCommonConfig.FAILSAFE_REPLACER.get()) {
            return;
        }

        Set<BlockPos> positions = event.getChunk().getBlockEntitiesPos();
        for(BlockPos pos : positions) {
            BlockEntity blockEntity = event.getChunk().getBlockEntity(pos);
            if(blockEntity instanceof BeehiveBlockEntity beehiveBlockEntity) {
                CompoundTag tag = beehiveBlockEntity.serializeNBT();
                if(tag.contains("Replaced") && tag.getBoolean("Replaced")) {
                    return;
                }

                BeehiveBlockEntity newBeehiveBlockEntity = new BeehiveBlockEntity(pos, beehiveBlockEntity.getBlockState());
                CompoundTag nbt = tag.copy();
                nbt.putBoolean("Replaced", true);

                ListTag bees = nbt.getList("Bees", 10);
                ListTag oBees = new ListTag();

                for(int i = 0; i < bees.size(); i++) {
                    CompoundTag oldTag = bees.getCompound(i);
                    CompoundTag newTag = new CompoundTag();

                    CompoundTag idTag = new CompoundTag();
                    idTag.putString("id", EntityTypes.O_BEE_ENTITY.getId().toString());

                    newTag.put("EntityData", idTag);
                    newTag.putInt("TicksInHive", oldTag.getInt("TicksInHive"));
                    newTag.putInt("MinOccupationTicks", oldTag.getInt("MinOccupationTicks"));
                    oBees.add(newTag);
                }

                nbt.put("Bees", oBees);
                newBeehiveBlockEntity.deserializeNBT(nbt);
                ((LevelChunk)event.getChunk()).addAndRegisterBlockEntity(newBeehiveBlockEntity);
            }
        }
    }

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        CreatureSpawnGeneration.onEntitySpawn(event);
    }
}
