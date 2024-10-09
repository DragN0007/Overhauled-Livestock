package com.dragn0007.dragnlivestock.entities.util;

import com.dragn0007.dragnlivestock.entities.Chestable;
import com.dragn0007.dragnlivestock.gui.OHorseMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.UUID;

public class AbstractOHorse extends AbstractHorse implements Saddleable, Chestable {

    public static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(AbstractOHorse.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(AbstractOHorse.class, EntityDataSerializers.BOOLEAN);

    public static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("3c50e848-b2e3-404a-9879-7550b12dd09b");


    public AbstractOHorse(EntityType<? extends AbstractOHorse> entityType, Level level) {
        super(entityType, level);
        this.canGallop = false;
        this.createInventory();
    }

    @Override
    public int getInventorySize() {
        //NOTE (EVNGLX): 2 slots for armor and saddle, 17 TOTAL slots because +15 for chest inventory
        return this.isChested() ? 17 : 2;
    }

    @Override
    public void openInventory(Player player) {
        if(player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, new SimpleMenuProvider((containerId, inventory, p) -> {
                return new OHorseMenu(containerId, inventory, this.inventory, this);
            }, this.getDisplayName()), (data) -> {
                data.writeInt(this.getInventorySize());
                data.writeInt(this.getId());
            });
        }


    }

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof HorseArmorItem;
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.25D;
    }

    @Override
    public void dropEquipment() {
        super.dropEquipment();
        if (this.isChested()) {
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Blocks.CHEST);
            }
            this.setChested(false);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!this.isBaby()) {
            if(this.isTamed() && player.isSecondaryUseActive()) {
                this.openInventory(player);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if(this.isVehicle()) {
                return super.mobInteract(player, hand);
            }
        }

        if(!itemStack.isEmpty()) {
            if(this.isFood(itemStack)) {
                return this.fedFood(player, itemStack);
            }

            if(!this.isTamed()) {
                this.makeMad();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if(!this.isChested() && itemStack.is(Items.CHEST)) {
                this.setChested(true);
                this.equipChest(SoundSource.NEUTRAL);
                if(!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.createInventory();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if(itemStack.getItem() instanceof HorseArmorItem horseArmorItem) {
                if(this.isArmored()) {
                    this.spawnAtLocation(this.inventory.getItem(1));
                }

                this.setArmorItem(horseArmorItem.getDefaultInstance());
                this.equipArmor(SoundSource.NEUTRAL);

                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if(!this.isBaby() && !this.isSaddled() && itemStack.is(Items.SADDLE)) {
                this.openInventory(player);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }

        if(this.isBaby()) {
            return super.mobInteract(player, hand);
        } else {
            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHESTED, false);
        this.entityData.define(SADDLED, false);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Saddled", this.isSaddled());
        compoundTag.putBoolean("Chested", this.isChested());

        if(this.isChested()) {
            ListTag listTag = new ListTag();

            for(int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemStack = this.inventory.getItem(i);
                if(!itemStack.isEmpty()) {
                    CompoundTag tag = new CompoundTag();
                    tag.putByte("Slot", (byte) i);
                    itemStack.save(tag);
                    listTag.add(tag);
                }
            }
            compoundTag.put("Items", listTag);
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        if(compoundTag.contains("Chested")) {
            this.setChested(compoundTag.getBoolean("Chested"));
        }

        if(compoundTag.contains("Saddled")) {
            this.setSaddled(compoundTag.getBoolean("Saddled"));
        }

        this.createInventory();
        if(this.isChested()) {
            ListTag listTag = compoundTag.getList("Items", 10);

            for(int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                int j = tag.getByte("Slot") & 255;
                if(j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.of(tag));
                }
            }
        }
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource) {
        if(soundSource != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5F, 1F);
        }
    }

    @Override
    public boolean isChestable() {
        return this.isAlive() && !this.isBaby() && this.isTamed();
    }

    @Override
    public void equipChest(@Nullable SoundSource soundSource) {
        if(soundSource != null) {
            this.level.playSound(null, this, SoundEvents.MULE_CHEST, soundSource, 0.5F, 1F);
        }
    }

    @Override
    public boolean isChested() {
        return this.entityData.get(CHESTED);
    }

    public void setChested(boolean chested) {
        this.entityData.set(CHESTED, chested);
    }

    public boolean isArmored() {
        return !this.inventory.getItem(1).isEmpty();
    }

    public void equipArmor(@Nullable SoundSource soundSource) {
        if(soundSource != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_ARMOR, soundSource, 0.5F, 1F);
        }
    }

    public void setArmorItem(ItemStack armorItem) {
        this.inventory.setItem(1, armorItem);
    }

    public boolean isArmorable() {
        return this.isAlive() && !this.isBaby() && this.isTamed();
    }

    @Override
    public void containerChanged(Container container) {
        if(this.tickCount > 20) {
            if (!this.isSaddled() && this.isSaddleable()) {
                this.playSound(SoundEvents.HORSE_SADDLE, 0.5f, 1f);
            }

            if(!this.isArmored() && this.isArmorable()) {
                this.playSound(SoundEvents.HORSE_ARMOR, 0.5f, 1f);
                int armorAmount = ((HorseArmorItem)(this.inventory.getItem(1).getItem())).getProtection();
                if(armorAmount != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", armorAmount, AttributeModifier.Operation.ADDITION));
                }
            } else {
                this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            }
        }
    }
}