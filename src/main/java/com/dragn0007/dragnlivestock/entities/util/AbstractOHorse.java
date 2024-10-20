package com.dragn0007.dragnlivestock.entities.util;

import com.dragn0007.dragnlivestock.entities.llama.OLlama;
import com.dragn0007.dragnlivestock.gui.OHorseMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class AbstractOHorse extends AbstractChestedHorse {
    public static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("3c50e848-b2e3-404a-9879-7550b12dd09b");
    public static final UUID SPRINT_SPEED_MOD_UUID = UUID.fromString("c9379664-01b5-4e19-a7e9-11264453bdce");
    public static final UUID WALK_SPEED_MOD_UUID = UUID.fromString("59b55c98-e39b-45e2-846c-f91f3e9ea861");

    public static final AttributeModifier SPRINT_SPEED_MOD = new AttributeModifier(SPRINT_SPEED_MOD_UUID, "Sprint speed mod", 0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final AttributeModifier WALK_SPEED_MOD = new AttributeModifier(WALK_SPEED_MOD_UUID, "Walk speed mod", -0.7D, AttributeModifier.Operation.MULTIPLY_TOTAL); // KEEP THIS NEGATIVE. It is calculated by adding 1. So -0.1 actually means 0.9

    public static final EntityDataAccessor<Integer> DATA_CARPET_ID = SynchedEntityData.defineId(AbstractOHorse.class, EntityDataSerializers.INT);

    public AbstractOHorse(EntityType<? extends AbstractOHorse> entityType, Level level) {
        super(entityType, level);
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

    public boolean canParent() {
        return !this.isVehicle() && !this.isPassenger() && this.isTamed() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof HorseArmorItem || itemStack.is(ItemTags.CARPETS);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.25D;
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

            InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, hand);
            if(interactionResult.consumesAction()) {
                return interactionResult;
            }

            if(!this.isTamed()) {
                this.makeMad();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if(!this.hasChest() && itemStack.is(Blocks.CHEST.asItem())) {
                this.setChest(true);
                this.playChestEquipsSound();
                if(!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                this.createInventory();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            boolean canSaddle = !this.isBaby() && !this.isSaddled() && itemStack.is(Items.SADDLE);
            if(this.isArmor(itemStack) || canSaddle) {
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
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CARPET_ID, -1);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if(!this.inventory.getItem(1).isEmpty()) {
            compoundTag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }

        if (!this.inventory.getItem(1).isEmpty()) {
            compoundTag.put("DecorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains("ArmorItem", 10)) {
            ItemStack itemStack = ItemStack.of(compoundTag.getCompound("ArmorItem"));
            if(!itemStack.isEmpty() && this.isArmor(itemStack)) {
                this.inventory.setItem(1, itemStack);
            }
        }

        if (compoundTag.contains("DecorItem", 10)) {
            this.inventory.setItem(1, ItemStack.of(compoundTag.getCompound("DecorItem")));
        }

        this.updateContainerEquipment();
    }

    @Override
    public void playGallopSound(SoundType soundType) {
        super.playGallopSound(soundType);
        if(this.random.nextInt(10) == 0) {
            this.playSound(SoundEvents.HORSE_BREATHE, soundType.getVolume() * 0.6f, soundType.getPitch());
        }

        ItemStack itemStack = this.inventory.getItem(1);
        if(this.isArmor(itemStack)) {
            itemStack.onHorseArmorTick(this.level, this);
        }
    }

    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    public void setArmor(ItemStack itemStack) {
        this.setItemSlot(EquipmentSlot.CHEST, itemStack);
        this.setDropChance(EquipmentSlot.CHEST, 0f);
    }

    public void setArmorEquipment(ItemStack itemStack) {
        this.setArmor(itemStack);
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);

            if (itemStack.getItem() instanceof HorseArmorItem horseArmorItem) {
                int protection = horseArmorItem.getProtection();
                if (protection > 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(
                            new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double) protection, AttributeModifier.Operation.ADDITION)
                    );
                }
            }
        }
    }

    @Override
    public void updateContainerEquipment() {
       super.updateContainerEquipment();
       this.setArmorEquipment(this.inventory.getItem(1));
       this.setDropChance(EquipmentSlot.CHEST, 0f);
        if (!this.level.isClientSide) {
            super.updateContainerEquipment();
            this.setCarpet(getDyeColor(this.inventory.getItem(1)));
        }
    }

    public void setCarpet(@Nullable DyeColor p_30772_) {
        this.entityData.set(DATA_CARPET_ID, p_30772_ == null ? -1 : p_30772_.getId());
    }

    @Nullable
    public static DyeColor getDyeColor(ItemStack p_30836_) {
        Block block = Block.byItem(p_30836_.getItem());
        return block instanceof WoolCarpetBlock ? ((WoolCarpetBlock)block).getColor() : null;
    }

    @Nullable
    public DyeColor getCarpet() {
        int i = this.entityData.get(DATA_CARPET_ID);
        return i == -1 ? null : DyeColor.byId(i);
    }

    @Override
    public void containerChanged(Container container) {
        ItemStack prevArmor = this.getArmor();
        super.containerChanged(container);
        ItemStack newArmor = this.getArmor();
        if(this.tickCount > 20 && this.isArmor(newArmor) && prevArmor != newArmor) {
            this.playSound(SoundEvents.HORSE_ARMOR, 0.5f, 1f);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPRINT_SPEED_MOD);
        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(WALK_SPEED_MOD);
        return super.getDismountLocationForPassenger(livingEntity);
    }

    public void handleSpeedRequest(int speedMod) {
            AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);

            if (speedMod == -1 && movementSpeed.hasModifier(SPRINT_SPEED_MOD)) {
                movementSpeed.removeModifier(SPRINT_SPEED_MOD);
            } else if (speedMod == -1 && !movementSpeed.hasModifier(WALK_SPEED_MOD)) {
                movementSpeed.addTransientModifier(WALK_SPEED_MOD);
            } else if (speedMod == 1 && movementSpeed.hasModifier(WALK_SPEED_MOD)) {
                movementSpeed.removeModifier(WALK_SPEED_MOD);
            } else if (speedMod == 1 && !movementSpeed.hasModifier(SPRINT_SPEED_MOD)) {
                movementSpeed.addTransientModifier(SPRINT_SPEED_MOD);
            }
    }
}