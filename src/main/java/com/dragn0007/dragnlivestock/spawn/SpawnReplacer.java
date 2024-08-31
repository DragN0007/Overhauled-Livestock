package com.dragn0007.dragnlivestock.spawn;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.world.entity.Entity;
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

                //try to take on as many identifiers from the vanilla horse possible
                oHorse.setCustomName(vanillaHorse.getCustomName());
                oHorse.setOwnerUUID(vanillaHorse.getOwnerUUID());
                oHorse.setAge(vanillaHorse.getAge());

                //set attributes randomly, not attached to the vanilla horse (oHorses have some different attribute values)
                OHorse.createBaseHorseAttributes();

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
                event.setCanceled(true);
            }
        }
    }

}