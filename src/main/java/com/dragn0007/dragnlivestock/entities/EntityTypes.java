package com.dragn0007.dragnlivestock.entities;

import com.dragn0007.dragnlivestock.entities.bee.OBee;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.cod.OCod;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dragn0007.dragnlivestock.LivestockOverhaul.MODID;

public class EntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static final RegistryObject<EntityType<OHorse>> O_HORSE_ENTITY = ENTITY_TYPES.register("o_horse_entity",
            () -> EntityType.Builder.of(OHorse::new,
                    MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"o_horse").toString()));

    public static final RegistryObject<EntityType<OCow>> O_COW_ENTITY = ENTITY_TYPES.register("o_cow_entity",
            () -> EntityType.Builder.of(OCow::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"o_cow").toString()));

    public static final RegistryObject<EntityType<OChicken>> O_CHICKEN_ENTITY = ENTITY_TYPES.register("o_chicken_entity",
            () -> EntityType.Builder.of(OChicken::new,
                            MobCategory.CREATURE)
                    .sized(1f,1f)
                    .build(new ResourceLocation(MODID,"o_chicken").toString()));

    public static final RegistryObject<EntityType<OSalmon>> O_SALMON_ENTITY = ENTITY_TYPES.register("o_salmon_entity",
            () -> EntityType.Builder.of(OSalmon::new,
                            MobCategory.CREATURE)
                    .sized(0.7f, 0.4f)
                    .build(new ResourceLocation(MODID,"o_salmon").toString()));

    public static final RegistryObject<EntityType<OCod>> O_COD_ENTITY = ENTITY_TYPES.register("o_cod_entity",
            () -> EntityType.Builder.of(OCod::new,
                            MobCategory.CREATURE)
                    .sized(0.5f, 0.3f)
                    .build(new ResourceLocation(MODID,"o_cod").toString()));

    public static final RegistryObject<EntityType<OBee>> O_BEE_ENTITY = ENTITY_TYPES.register("o_bee_entity",
            () -> EntityType.Builder.of(OBee::new,
                            MobCategory.CREATURE)
                    .sized(0.3f, 0.3f)
                    .build(new ResourceLocation(MODID,"o_bee").toString()));
}

