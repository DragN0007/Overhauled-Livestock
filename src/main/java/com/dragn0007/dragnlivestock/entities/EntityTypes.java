package com.dragn0007.dragnlivestock.entities;

import com.dragn0007.dragnlivestock.entities.bee.OBee;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.cod.OCod;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.llama.OLlama;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.entities.pig.OPig;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbit;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmon;
import com.dragn0007.dragnlivestock.entities.sheep.OSheep;
import com.dragn0007.dragnlivestock.entities.unicorn.EndUnicorn;
import com.dragn0007.dragnlivestock.entities.unicorn.NetherUnicorn;
import com.dragn0007.dragnlivestock.entities.unicorn.OverworldUnicorn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dragn0007.dragnlivestock.LivestockOverhaul.MODID;

public class EntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static final RegistryObject<EntityType<OHorse>> O_HORSE_ENTITY = ENTITY_TYPES.register("o_horse",
            () -> EntityType.Builder.of(OHorse::new,
                    MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"o_horse").toString()));

    public static final RegistryObject<EntityType<ODonkey>> O_DONKEY_ENTITY = ENTITY_TYPES.register("o_donkey",
            () -> EntityType.Builder.of(ODonkey::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"o_donkey").toString()));

    public static final RegistryObject<EntityType<OMule>> O_MULE_ENTITY = ENTITY_TYPES.register("o_mule",
            () -> EntityType.Builder.of(OMule::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"o_mule").toString()));

    public static final RegistryObject<EntityType<OCow>> O_COW_ENTITY = ENTITY_TYPES.register("o_cow",
            () -> EntityType.Builder.of(OCow::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"o_cow").toString()));

    public static final RegistryObject<EntityType<OChicken>> O_CHICKEN_ENTITY = ENTITY_TYPES.register("o_chicken",
            () -> EntityType.Builder.of(OChicken::new,
                            MobCategory.CREATURE)
                    .sized(1f,1f)
                    .build(new ResourceLocation(MODID,"o_chicken").toString()));

    public static final RegistryObject<EntityType<OSalmon>> O_SALMON_ENTITY = ENTITY_TYPES.register("o_salmon",
            () -> EntityType.Builder.of(OSalmon::new,
                            MobCategory.CREATURE)
                    .sized(0.7f, 0.4f)
                    .build(new ResourceLocation(MODID,"o_salmon").toString()));

    public static final RegistryObject<EntityType<OCod>> O_COD_ENTITY = ENTITY_TYPES.register("o_cod",
            () -> EntityType.Builder.of(OCod::new,
                            MobCategory.CREATURE)
                    .sized(0.5f, 0.3f)
                    .build(new ResourceLocation(MODID,"o_cod").toString()));

    public static final RegistryObject<EntityType<OBee>> O_BEE_ENTITY = ENTITY_TYPES.register("o_bee",
            () -> EntityType.Builder.of(OBee::new,
                            MobCategory.CREATURE)
                    .sized(0.3f, 0.3f)
                    .build(new ResourceLocation(MODID,"o_bee").toString()));

    public static final RegistryObject<EntityType<ORabbit>> O_RABBIT_ENTITY = ENTITY_TYPES.register("o_rabbit",
            () -> EntityType.Builder.of(ORabbit::new,
                            MobCategory.CREATURE)
                    .sized(0.7f, 0.7f)
                    .build(new ResourceLocation(MODID,"o_rabbit").toString()));

    public static final RegistryObject<EntityType<OLlama>> O_LLAMA_ENTITY = ENTITY_TYPES.register("o_llama",
            () -> EntityType.Builder.of(OLlama::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.5f)
                    .build(new ResourceLocation(MODID,"o_llama").toString()));

    public static final RegistryObject<EntityType<OSheep>> O_SHEEP_ENTITY = ENTITY_TYPES.register("o_sheep",
            () -> EntityType.Builder.of(OSheep::new,
                            MobCategory.CREATURE)
                    .sized(1f,1f)
                    .build(new ResourceLocation(MODID,"o_sheep").toString()));

    public static final RegistryObject<EntityType<OPig>> O_PIG_ENTITY = ENTITY_TYPES.register("o_pig",
            () -> EntityType.Builder.of(OPig::new,
                            MobCategory.CREATURE)
                    .sized(1f,1f)
                    .build(new ResourceLocation(MODID,"o_pig").toString()));


    public static final RegistryObject<EntityType<OverworldUnicorn>> OVERWORLD_UNICORN_ENTITY = ENTITY_TYPES.register("overworld_unicorn",
            () -> EntityType.Builder.of(OverworldUnicorn::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"overworld_unicorn").toString()));

    public static final RegistryObject<EntityType<NetherUnicorn>> NETHER_UNICORN_ENTITY = ENTITY_TYPES.register("nether_unicorn",
            () -> EntityType.Builder.of(NetherUnicorn::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"nether_unicorn").toString()));

    public static final RegistryObject<EntityType<EndUnicorn>> END_UNICORN_ENTITY = ENTITY_TYPES.register("end_unicorn",
            () -> EntityType.Builder.of(EndUnicorn::new,
                            MobCategory.CREATURE)
                    .sized(2f,2f)
                    .build(new ResourceLocation(MODID,"end_unicorn").toString()));
}

