package com.dragn0007.dragnlivestock.entities.util;

import com.dragn0007.dragnlivestock.entities.Chestable;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class AbstractOHorse extends AbstractHorse implements Saddleable, Chestable {

    protected static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(AbstractOHorse.class, EntityDataSerializers.BOOLEAN);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    public static final int INV_CHEST_COUNT = 15;

    public AbstractOHorse(EntityType<? extends AbstractOHorse> p_30485_, Level p_30486_) {
        super(p_30485_, p_30486_);
        this.canGallop = false;
    }

    private SlotAccess createEquipmentSlotAccess(final int p_149503_, final Predicate<ItemStack> p_149504_) {
        return new SlotAccess() {
            public ItemStack get() {
                return AbstractOHorse.this.inventory.getItem(p_149503_);
            }

            public boolean set(ItemStack p_149528_) {
                if (!p_149504_.test(p_149528_)) {
                    return false;
                } else {
                    AbstractOHorse.this.inventory.setItem(p_149503_, p_149528_);
                    AbstractOHorse.this.updateContainerEquipment();
                    return true;
                }
            }
        };
    }

    @Override
    public void openInventory(Player p_30621_) {
        return;
    }

    public boolean isWearingArmor() {
        return !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    public boolean isArmor(ItemStack p_30645_) {
        return false;
    }

    public boolean canWearArmor() {
        return true;
    }

    public SlotAccess getSlot(int p_149514_) {
        int i = p_149514_ - 400;
        if (i >= 0 && i < 2 && i < this.inventory.getContainerSize()) {
            if (i == 0) {
                return this.createEquipmentSlotAccess(i, (p_149518_) -> {
                    return p_149518_.isEmpty() || p_149518_.is(Items.SADDLE);
                });
            }

            if (i == 1) {
                if (!this.canWearArmor()) {
                    return SlotAccess.NULL;
                }

                return this.createEquipmentSlotAccess(i, (p_149516_) -> {
                    return p_149516_.isEmpty() || this.isArmor(p_149516_);
                });
            }
        }

        int j = p_149514_ - 500 + 2;
        return j >= 2 && j < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, j) : super.getSlot(p_149514_);
    }

    protected void randomizeAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)this.generateRandomMaxHealth());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_CHEST, false);
    }

    public void addAdditionalSaveData(CompoundTag p_30716_) {
        super.addAdditionalSaveData(p_30716_);
        if (!this.inventory.getItem(1).isEmpty()) {
            p_30716_.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }

    }

    private void setArmorEquipment(ItemStack p_30735_) {
        this.setArmor(p_30735_);
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(p_30735_)) {
                int i = ((HorseArmorItem)p_30735_.getItem()).getProtection();
                if (i != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)i, AttributeModifier.Operation.ADDITION));
                }
            }
        }

    }

    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    private void setArmor(ItemStack p_30733_) {
        this.setItemSlot(EquipmentSlot.CHEST, p_30733_);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

    public void readAdditionalSaveData(CompoundTag p_30711_) {
        super.readAdditionalSaveData(p_30711_);
        if (p_30711_.contains("ArmorItem", 10)) {
            ItemStack itemstack = ItemStack.of(p_30711_.getCompound("ArmorItem"));
            if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
                this.inventory.setItem(1, itemstack);
            }
        }

        this.updateContainerEquipment();
    }

    public static AttributeSupplier.Builder createBaseChestedHorseAttributes() {
        return createBaseHorseAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.175F).add(Attributes.JUMP_STRENGTH, 0.5D);
    }

    public boolean hasChest() {
        return this.entityData.get(DATA_ID_CHEST);
    }

    public void setChest(boolean p_30505_) {
        this.entityData.set(DATA_ID_CHEST, p_30505_);
    }

    protected int getInventorySize() {
        return this.hasChest() ? 17 : super.getInventorySize();
    }

    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.25D;
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.hasChest()) {
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Blocks.CHEST);
            }

            this.setChest(false);
        }

    }

    public InteractionResult mobInteract(Player p_30493_, InteractionHand p_30494_) {
        ItemStack itemstack = p_30493_.getItemInHand(p_30494_);
        if (!this.isBaby()) {
            if (this.isTamed() && p_30493_.isSecondaryUseActive()) {
                this.openInventory(p_30493_);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (this.isVehicle()) {
                return super.mobInteract(p_30493_, p_30494_);
            }
        }

        if (!itemstack.isEmpty()) {
            if (this.isFood(itemstack)) {
                return this.fedFood(p_30493_, itemstack);
            }

            if (!this.isTamed()) {
                this.makeMad();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (!this.isChested() && itemstack.is(Blocks.CHEST.asItem())) {
                this.setChested(true);
                this.playChestEquipsSound();
                if (!p_30493_.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.createInventory();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (!this.isBaby() && !this.isSaddled() && itemstack.is(Items.SADDLE)) {
                this.openInventory(p_30493_);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }

        if (this.isBaby()) {
            return super.mobInteract(p_30493_, p_30494_);
        } else {
            this.doPlayerRide(p_30493_);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public int getInventoryColumns() {
        return 5;
    }

    @Override
    public boolean isChestable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void equipChest(@Nullable SoundSource soundSource) {
        if(soundSource != null) {
            this.level.playSound(null, this, SoundEvents.MULE_CHEST, soundSource, 0.5f, 1f);
        }
    }

    @Override
    public boolean isChested() {
        return this.entityData.get(CHESTED);
    }

    protected void setChested(boolean chested) {
        this.entityData.set(CHESTED, chested);
    }

    @Override
    public void containerChanged(Container container) {
        boolean flag = this.isSaddled();
        if(this.tickCount > 20 && !flag && this.isSaddleable()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5f, 1f);
        }
    }

}