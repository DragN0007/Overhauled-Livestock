package com.dragn0007.dragnlivestock.spawn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.horse.Horse;
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



    }
}