package com.dragn0007.dragnlivestock.spawn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.bee.OBee;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.cod.OCod;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.llama.OLlama;
import com.dragn0007.dragnlivestock.entities.pig.OPig;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbit;
import com.dragn0007.dragnlivestock.entities.salmon.OSalmon;
import com.dragn0007.dragnlivestock.entities.sheep.OSheep;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LivestockOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnReplacer {

    // This class falls under the LGPL license, as stated in the CODE_LICENSE.txt
    // Some of this code was referenced from Realistic Horse Genetics. Please check them out, too! :)
    // https://github.com/sekelsta/horse-colors  |  https://www.curseforge.com/minecraft/mc-mods/realistic-horse-genetics

    @SubscribeEvent
    public static void onSpawn(EntityJoinWorldEvent event) {

        //Horse
        if (LivestockOverhaulCommonConfig.REPLACE_HORSES.get() && event.getEntity() instanceof Horse) {
            Horse vanillaHorse = (Horse) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillaHorse.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            OHorse oHorse = EntityTypes.O_HORSE_ENTITY.get().create(event.getWorld());
            if (oHorse != null) {
                oHorse.copyPosition(vanillaHorse);
                Entity entity = event.getEntity();

                //try to take on as many identifiers from the vanilla horse possible
                oHorse.setCustomName(vanillaHorse.getCustomName());
                oHorse.setOwnerUUID(vanillaHorse.getOwnerUUID());
                oHorse.setAge(vanillaHorse.getAge());
                oHorse.setHealth(vanillaHorse.getHealth());
                oHorse.setSpeed(vanillaHorse.getSpeed());
                oHorse.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(oHorse.generateRandomJumpStrength());

                //set random variants on-spawn
                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oHorse.setVariant(randomVariant);

                int randomOverlayVariant = event.getWorld().getRandom().nextInt(31);
                oHorse.setOverlayVariant(randomOverlayVariant);

                //discard vanilla horse once it's been successfully replaced on client and server
                if (event.getWorld().isClientSide) {
                    vanillaHorse.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oHorse);
                vanillaHorse.remove(Entity.RemovalReason.DISCARDED);

                vanillaHorse.getPersistentData().putBoolean("O-Replaced", true);

                //debug only. annoying to see it spam the console
//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla horse with an O-Horse!");

                event.setCanceled(true);
            }
        }

        //Cow
        if (LivestockOverhaulCommonConfig.REPLACE_COWS.get() && event.getEntity() instanceof Cow) {
            Cow vanillacow = (Cow) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillacow.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            OCow oCow = EntityTypes.O_COW_ENTITY.get().create(event.getWorld());
            if (oCow != null) {
                oCow.copyPosition(vanillacow);
                Entity entity = event.getEntity();

                oCow.setCustomName(vanillacow.getCustomName());
                oCow.setAge(vanillacow.getAge());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oCow.setVariant(randomVariant);

                int randomOverlayVariant = event.getWorld().getRandom().nextInt(31);
                oCow.setOverlayVariant(randomOverlayVariant);

                if (event.getWorld().isClientSide) {
                    vanillacow.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oCow);
                vanillacow.remove(Entity.RemovalReason.DISCARDED);

                vanillacow.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla cow with an O-Cow!");

                event.setCanceled(true);
            }
        }

        //Chicken
        if (LivestockOverhaulCommonConfig.REPLACE_CHICKENS.get() && event.getEntity() instanceof Chicken) {
            Chicken vanillachicken = (Chicken) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillachicken.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            OChicken oChicken = EntityTypes.O_CHICKEN_ENTITY.get().create(event.getWorld());
            if (oChicken != null) {
                oChicken.copyPosition(vanillachicken);
                Entity entity = event.getEntity();

                oChicken.setCustomName(vanillachicken.getCustomName());
                oChicken.setAge(vanillachicken.getAge());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oChicken.setVariant(randomVariant);

                int randomOverlayVariant = event.getWorld().getRandom().nextInt(31);
                oChicken.setOverlayVariant(randomOverlayVariant);

                if (event.getWorld().isClientSide) {
                    vanillachicken.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oChicken);
                vanillachicken.remove(Entity.RemovalReason.DISCARDED);

                vanillachicken.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla chicken with an O-Chicken!");

                event.setCanceled(true);
            }
        }

        //Salmon
        OSalmon oSalmon = EntityTypes.O_SALMON_ENTITY.get().create(event.getWorld());
        if (LivestockOverhaulCommonConfig.REPLACE_SALMON.get() && event.getEntity() instanceof Salmon) {
            Salmon vanillasalmon = (Salmon) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillasalmon.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            if (oSalmon != null) {
                oSalmon.copyPosition(vanillasalmon);
                Entity entity = event.getEntity();

                oSalmon.setCustomName(vanillasalmon.getCustomName());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oSalmon.setVariant(randomVariant);

                if (event.getWorld().isClientSide) {
                    vanillasalmon.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oSalmon);
                vanillasalmon.remove(Entity.RemovalReason.DISCARDED);

                vanillasalmon.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla salmon with an O-Salmon!");

                event.setCanceled(true);
            }
        }

        //Cod
        OCod oCod = EntityTypes.O_COD_ENTITY.get().create(event.getWorld());
        if (LivestockOverhaulCommonConfig.REPLACE_COD.get() && event.getEntity() instanceof Cod) {
            Cod vanillacod = (Cod) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillacod.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            if (oCod != null) {
                oCod.copyPosition(vanillacod);
                Entity entity = event.getEntity();

                oCod.setCustomName(vanillacod.getCustomName());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oCod.setVariant(randomVariant);

                if (event.getWorld().isClientSide) {
                    vanillacod.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oCod);
                vanillacod.remove(Entity.RemovalReason.DISCARDED);

                vanillacod.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla cod with an O-Cod!");

                event.setCanceled(true);
            }
        }

        //Bee
        if (LivestockOverhaulCommonConfig.REPLACE_BEES.get() && event.getEntity() instanceof Bee vanillaBee) {
            OBee oBee = EntityTypes.O_BEE_ENTITY.get().create(event.getWorld());

            vanillaBee.remove(Entity.RemovalReason.DISCARDED);
            event.getWorld().addFreshEntity(oBee);

                oBee.copyPosition(vanillaBee);

                oBee.setCustomName(vanillaBee.getCustomName());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oBee.setVariant(randomVariant);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla bee with an O-Bee!");

            event.setCanceled(true);
        }

        //Rabbit
        ORabbit oRabbit = EntityTypes.O_RABBIT_ENTITY.get().create(event.getWorld());
        if (LivestockOverhaulCommonConfig.REPLACE_RABBITS.get() && event.getEntity() instanceof Rabbit) {
            Rabbit vanillarabbit = (Rabbit) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillarabbit.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            if (oRabbit != null) {
                oRabbit.copyPosition(vanillarabbit);
                Entity entity = event.getEntity();

                oRabbit.setCustomName(vanillarabbit.getCustomName());
                oRabbit.setAge(vanillarabbit.getAge());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oRabbit.setVariant(randomVariant);

                if (event.getWorld().isClientSide) {
                    vanillarabbit.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oRabbit);
                vanillarabbit.remove(Entity.RemovalReason.DISCARDED);

                vanillarabbit.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla rabbit with an O-Rabbit!");

                event.setCanceled(true);
            }
        }

        //Sheep
        OSheep oSheep = EntityTypes.O_SHEEP_ENTITY.get().create(event.getWorld());
        if (LivestockOverhaulCommonConfig.REPLACE_SHEEP.get() && event.getEntity() instanceof Sheep) {
            Sheep vanillasheep = (Sheep) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillasheep.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            if (oSheep != null) {
                oSheep.copyPosition(vanillasheep);
                Entity entity = event.getEntity();

                oSheep.setCustomName(vanillasheep.getCustomName());
                oSheep.setAge(vanillasheep.getAge());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oSheep.setVariant(randomVariant);

                if (event.getWorld().isClientSide) {
                    vanillasheep.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oSheep);
                vanillasheep.remove(Entity.RemovalReason.DISCARDED);

                vanillasheep.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla sheep with an O-Sheep!");

                event.setCanceled(true);
            }
        }

        //Llama
        OLlama oLlama = EntityTypes.O_LLAMA_ENTITY.get().create(event.getWorld());
        if (LivestockOverhaulCommonConfig.REPLACE_LLAMAS.get() && event.getEntity() instanceof Llama) {
            Llama vanillallama = (Llama) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillallama.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            if (oLlama != null) {
                oLlama.copyPosition(vanillallama);
                Entity entity = event.getEntity();

                oLlama.setCustomName(vanillallama.getCustomName());
                oLlama.setOwnerUUID(vanillallama.getOwnerUUID());
                oLlama.setAge(vanillallama.getAge());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oLlama.setVariant(randomVariant);

                if (event.getWorld().isClientSide) {
                    vanillallama.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oLlama);
                vanillallama.remove(Entity.RemovalReason.DISCARDED);

                vanillallama.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla llama with an O-Llama!");

                event.setCanceled(true);
            }
        }

        //Llama
        OPig oPig = EntityTypes.O_PIG_ENTITY.get().create(event.getWorld());
        if (LivestockOverhaulCommonConfig.REPLACE_PIGS.get() && event.getEntity() instanceof Pig) {
            Pig vanillapig = (Pig) event.getEntity();

            if (event.getWorld().isClientSide) {
                return;
            }

            if (vanillapig.getPersistentData().getBoolean("O-Replaced")) {
                return;
            }

            if (oPig != null) {
                oPig.copyPosition(vanillapig);
                Entity entity = event.getEntity();

                oPig.setCustomName(vanillapig.getCustomName());
                oPig.setAge(vanillapig.getAge());

                int randomVariant = event.getWorld().getRandom().nextInt(23);
                oPig.setVariant(randomVariant);

                if (event.getWorld().isClientSide) {
                    vanillapig.remove(Entity.RemovalReason.DISCARDED);
                }

                event.getWorld().addFreshEntity(oPig);
                vanillapig.remove(Entity.RemovalReason.DISCARDED);

                vanillapig.getPersistentData().putBoolean("O-Replaced", true);

//                    System.out.println("[Livestock Overhaul]: Replaced a vanilla pig with an O-Llama!");

                event.setCanceled(true);
            }
        }


    }
}