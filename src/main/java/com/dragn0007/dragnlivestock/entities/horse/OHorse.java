package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.entities.Chestable;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class OHorse extends AbstractOHorse implements IAnimatable, Chestable, Saddleable {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> ARMORED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.BOOLEAN);

	public OHorse(EntityType<? extends OHorse> type, Level level) {
		super(type, level);
		this.noCulling = true;
		this.updateInventory();
	}

	public static AttributeSupplier.Builder createBaseHorseAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.JUMP_STRENGTH)
				.add(Attributes.MAX_HEALTH, 53.0D)
				.add(Attributes.MOVEMENT_SPEED, (double)0.3F);
	}

	protected void randomizeAttributes() {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)this.generateRandomMaxHealth());
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed());
		this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
	}

	protected float generateRandomMaxHealth() {
		return 15.0F + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
	}

	protected double generateRandomJumpStrength() {
		return (double)0.4F + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
	}

	protected double generateRandomSpeed() {
		return ((double)0.45F + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.7D));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, true));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 0.0F));

		this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
	}

	public SimpleContainer inventory;
	private LazyOptional<?> itemHandler = null;

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double movementSpeed = getAttributeValue(Attributes.MOVEMENT_SPEED);
		double animationSpeed = Math.max(0.1, movementSpeed);

		String animationName;

		if (isJumping()) {
			animationName = "jump";
			event.getController().setAnimation(
					new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP)
			);
			event.getController().setAnimationSpeed(1.0);
		} else if (event.isMoving()) {
			if (isVehicle() || isAggressive() || isSprinting() || isSwimming()) {
				animationName = "run";
				event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.8 * event.getController().getAnimationSpeed() + animationSpeed));
			} else {
				animationName = "walk";
				event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.7 * event.getController().getAnimationSpeed() + animationSpeed));
			}
		} else {
			if (isVehicle()) {
				animationName = "idle";
				event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP));
			} else {
				animationName = "idle3";
				event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP));
			}
			event.getController().setAnimationSpeed(1.0);
		}

		return PlayState.CONTINUE;
	}

//	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//		double movementSpeed = getAttributeValue(Attributes.MOVEMENT_SPEED);
//		double animationSpeed = Math.max(0.1, movementSpeed);
//
//		if (isJumping()) {
//			event.getController().setAnimation(
//					new AnimationBuilder().addAnimation("jump", ILoopType.EDefaultLoopTypes.LOOP)
//			);
//			event.getController().setAnimationSpeed(1.0);
//		} else if (event.isMoving()) {
//			if (isVehicle() || isAggressive() || isSprinting() || isSwimming()) {
//				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
//				event.getController().setAnimationSpeed(Math.max(0.1, 0.8 * event.getController().getAnimationSpeed() + animationSpeed));
//			} else {
//				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
//				event.getController().setAnimationSpeed(Math.max(0.1, 0.7 * event.getController().getAnimationSpeed() + animationSpeed));
//			}
//		} else {
//			if (isVehicle()) {
//				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
//			} else {
//				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle3", ILoopType.EDefaultLoopTypes.LOOP));
//			}
//			event.getController().setAnimationSpeed(1.0);
//		}
//
//		return PlayState.CONTINUE;
//	}

	private PlayState attackPredicate(AnimationEvent event) {
		if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
			event.getController().markNeedsReload();
			event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
			this.swinging = false;
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers (AnimationData data){
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
		data.addAnimationController(new AnimationController(this, "attackController", 1, this::attackPredicate));
	}

	//ground tie
	@Override
	public void tick() {
		super.tick();
		if (this.isSaddled() && !this.isVehicle() || this.isLeashed()) {
			this.getNavigation().stop();
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public void positionRider(Entity entity) {
		if (this.hasPassenger(entity)) {
			double offsetX;
			double offsetY;
			double offsetZ;

			if (this.isSaddled()) {
				offsetX = 0;
				offsetY = 1.3;
				offsetZ = -0.1;
			} else {
				offsetX = 0;
				offsetY = 1.1;
				offsetZ = -0.2;
			}

			double radYaw = Math.toRadians(this.getYRot());

			double offsetXRotated = offsetX * Math.cos(radYaw) - offsetZ * Math.sin(radYaw);
			double offsetYRotated = offsetY;
			double offsetZRotated = offsetX * Math.sin(radYaw) + offsetZ * Math.cos(radYaw);

			double x = this.getX() + offsetXRotated;
			double y = this.getY() + offsetYRotated;
			double z = this.getZ() + offsetZRotated;

			entity.setPos(x, y, z);
		}
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);

		if (this.isBaby()) {
			return super.mobInteract(player, hand);
		}

		if (!this.isTamed() && this.isFood(itemstack)) {
			return this.fedFood(player, itemstack);
		}

		if (this.isTamed() && player.isSecondaryUseActive()) {
			this.openInventory(player);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}

		if (this.isVehicle()) {
			return super.mobInteract(player, hand);
		}

		if (!itemstack.isEmpty()) {
			if (!this.isTamed()) {
				this.makeMad();
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}

			if (itemstack.is(Items.SADDLE) && !this.isSaddled() && this.isSaddleable()) {
				this.setSaddled(true);
				this.updateInventory();
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}

			if (itemstack.is(Items.CHEST) && this.isChestable() && !this.isChested()) {
				this.setChested(true);
				this.equipChest(SoundSource.NEUTRAL);
				this.updateInventory();
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}

			InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
			if (interactionresult.consumesAction()) {
				return interactionresult;
			}
		}

		this.doPlayerRide(player);
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

	protected void playGallopSound(SoundType p_30709_) {
		super.playGallopSound(p_30709_);
		if (this.random.nextInt(10) == 0) {
			this.playSound(SoundEvents.HORSE_BREATHE, p_30709_.getVolume() * 0.6F, p_30709_.getPitch());
		}

		ItemStack stack = this.inventory.getItem(1);
		if (isArmor(stack)) stack.onHorseArmorTick(level, this);
	}

	@Override
	public boolean isSaddleable() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	public void equipSaddle(@Nullable SoundSource soundSource) {
		this.inventory.setItem(0, new ItemStack(Items.SADDLE));
		if (soundSource != null) {
			this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5f, 1.0f);
		}
	}

	@Override
	public boolean isSaddled() {
		return this.entityData.get(SADDLED);
	}

	private void setSaddled(boolean saddled) {
		this.entityData.set(SADDLED, saddled);
	}

	private void updateInventory() {
		SimpleContainer tempInventory = this.inventory;
		this.inventory = new SimpleContainer(this.getInventorySize());

		if(tempInventory != null) {
			tempInventory.removeListener(this);
			int maxSize = Math.min(tempInventory.getContainerSize(), this.inventory.getContainerSize());

			for(int i = 0; i < maxSize; i++) {
				ItemStack itemStack = tempInventory.getItem(i);
				if(!itemStack.isEmpty()) {
					this.inventory.setItem(i, itemStack.copy());
				}
			}
		}
		this.inventory.addListener(this);
		this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if(this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.itemHandler != null) {
			return itemHandler.cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if(this.itemHandler != null) {
			LazyOptional<?> oldHandler = this.itemHandler;
			this.itemHandler = null;
			oldHandler.invalidate();
		}
	}

	@Override
	protected void dropEquipment() {
		if(!this.level.isClientSide) {
			super.dropEquipment();
			if(this.isChested()) {
				this.spawnAtLocation(Items.CHEST);
			}
			Containers.dropContents(this.level, this, this.inventory);
		}
	}

	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return SoundEvents.HORSE_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		super.getDeathSound();
		return SoundEvents.HORSE_DEATH;
	}

	@Nullable
	protected SoundEvent getEatingSound() {
		return SoundEvents.HORSE_EAT;
	}

	protected SoundEvent getHurtSound(DamageSource p_30720_) {
		super.getHurtSound(p_30720_);
		return SoundEvents.HORSE_HURT;
	}

	protected SoundEvent getAngrySound() {
		super.getAngrySound();
		return SoundEvents.HORSE_ANGRY;
	}

	public ItemStack getArmor() {
		return this.getItemBySlot(EquipmentSlot.CHEST);
	}

	private void setArmor(ItemStack stack) {
		this.setItemSlot(EquipmentSlot.CHEST, stack);
		this.setDropChance(EquipmentSlot.CHEST, 0.0F);
	}

	// Generates the base texture
	public ResourceLocation getTextureLocation() {
		return OHorseModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	public ResourceLocation getOverlayLocation() {
		return OHorseMarkingLayer.Overlay.overlayFromOrdinal(getOverlayVariant()).resourceLocation;
	}

	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);

	public int getVariant() {
		return this.entityData.get(VARIANT);
	}

	public int getOverlayVariant() {
		return this.entityData.get(OVERLAY);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}

	public void setOverlayVariant(int variant) {
		this.entityData.set(OVERLAY, variant);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("Variant")) {
			setVariant(tag.getInt("Variant"));
		}

		if (tag.contains("Overlay")) {
			setOverlayVariant(tag.getInt("Overlay"));
		}

		if (tag.contains("Chested")) {
			this.setChested(tag.getBoolean("Chested"));
		}

		if (tag.contains("Saddled")) {
			this.setSaddled(tag.getBoolean("Saddled"));
		}

		if (tag.contains("ArmorItem", 10)) {
			ItemStack itemstack = ItemStack.of(tag.getCompound("ArmorItem"));
			if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
				this.inventory.setItem(1, itemstack);
			}
		}

		this.setTamed(tag.getBoolean("Tame"));
		UUID uuid;
		if (tag.hasUUID("Owner")) {
			uuid = tag.getUUID("Owner");
		} else {
			String s = tag.getString("Owner");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}

		if (uuid != null) {
			this.setOwnerUUID(uuid);
		}

		if (tag.contains("SaddleItem", 10)) {
			ItemStack itemstack = ItemStack.of(tag.getCompound("SaddleItem"));
			if (itemstack.is(Items.SADDLE)) {
				this.inventory.setItem(0, itemstack);
			}
		}

		this.updateContainerEquipment();
		this.updateInventory();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());

		tag.putInt("Overlay", getOverlayVariant());

		tag.putBoolean("Chested", this.isChested());

		tag.putBoolean("Saddled", this.isSaddled());

		tag.putBoolean("Tame", this.isTamed());
		if (this.getOwnerUUID() != null) {
			tag.putUUID("Owner", this.getOwnerUUID());
		}

		if (!this.inventory.getItem(0).isEmpty()) {
			tag.put("SaddleItem", this.inventory.getItem(0).save(new CompoundTag()));
		}

		if (!this.inventory.getItem(1).isEmpty()) {
			tag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
		}
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (data == null) {
			data = new AgeableMob.AgeableMobGroupData(0.2F);
		}
		Random random = new Random();
		setVariant(random.nextInt(OHorseModel.Variant.values().length));
		setOverlayVariant(random.nextInt(OHorseMarkingLayer.Overlay.values().length));

		this.randomizeAttributes();
		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(CHESTED, false);
		this.entityData.define(SADDLED, false);
	}

	protected void updateContainerEquipment() {
		if (!this.level.isClientSide) {
			super.updateContainerEquipment();
			this.setDropChance(EquipmentSlot.CHEST, 0.0F);
		}
	}


//	public boolean canMate(Animal animal) {
//		if (animal == this) {
//			return false;
//		} else if (!(animal instanceof Donkey) && !(animal instanceof HorseReplaced)) {
//			return false;
//		} else {
//			return this.canParent() && ((AbstractHorse)animal).canParent();
//		}
//	}
//
//	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
//		AbstractHorse abstracthorse;
//		if (ageableMob instanceof Donkey) {
//			abstracthorse = EntityType.MULE.create(serverLevel);
//		} else {
//			HorseReplaced horse = (HorseReplaced)ageableMob;
//			abstracthorse = EntityTypes.HORSE_REPLACED_ENTITY.create(serverLevel);
//			int i = this.random.nextInt(9);
//			Variant variant;
//			if (i < 4) {
//				variant = this.getVariant();
//			} else if (i < 8) {
//				variant = horse.getVariant();
//			} else {
//				variant = Util.getRandom(Variant.values(), this.random);
//			}
//
//			int j = this.random.nextInt(5);
//			HorseReplacedModel.Overlay markings;
//			if (j < 2) {
//				markings = this.getOverlay();
//			} else if (j < 4) {
//				markings = horse.getOverlay();
//			} else {
//				markings = Util.getRandom(HorseReplacedModel.Overlay.values(), this.random);
//			}
//
//			((HorseReplaced)abstracthorse).setVariantAndMarkings(variant, markings);
//		}
//
//		this.setOffspringAttributes(ageableMob, abstracthorse);
//		return abstracthorse;
//	}

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

	private void setChested(boolean chested) {
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
