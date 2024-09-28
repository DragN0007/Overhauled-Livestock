package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.client.menu.OHorseMenu;
import com.dragn0007.dragnlivestock.entities.Armorable;
import com.dragn0007.dragnlivestock.entities.Chestable;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.ai.HorseFollowHerdLeaderGoal;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
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
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

public class OHorse extends AbstractOHorse implements IAnimatable, Chestable, Saddleable, Armorable {
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
				.add(Attributes.MOVEMENT_SPEED, (double)0.235F);
	}

	protected void randomizeAttributes() {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)this.generateRandomMaxHealth());
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed());
		this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
	}

	public float generateRandomMaxHealth() {
		return 15.0F + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
	}

	public double generateRandomJumpStrength() {
		return (double)0.4F + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
	}

	public double generateRandomSpeed() {
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

		this.goalSelector.addGoal(3, new HorseFollowHerdLeaderGoal(this));
		this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D, AbstractOHorse.class));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
	}

	@Override
	public boolean canBeRiddenInWater(Entity rider) {
		return false;
	}

	public SimpleContainer inventory;
	private LazyOptional<?> itemHandler = null;
	private OHorse leader;
	private int herdSize = 1;

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double movementSpeed = getAttributeValue(Attributes.MOVEMENT_SPEED);
		double animationSpeed = Math.max(0.1, movementSpeed);

		if (isJumping()) {
			event.getController().setAnimation(
					new AnimationBuilder().addAnimation("jump", ILoopType.EDefaultLoopTypes.LOOP));

			event.getController().setAnimationSpeed(1.0);

		} else if (event.isMoving()) {
			if (isVehicle() || isAggressive() || isSprinting() || isSwimming()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.8 * event.getController().getAnimationSpeed() + animationSpeed));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.7 * event.getController().getAnimationSpeed() + animationSpeed));
			}
		} else {
			if (isVehicle()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle3", ILoopType.EDefaultLoopTypes.LOOP));
			}
			event.getController().setAnimationSpeed(1.0);
		}

		return PlayState.CONTINUE;
	}

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

	public boolean isFollower() {
		return this.leader != null && this.leader.isAlive();
	}

	public OHorse startFollowing(OHorse horse) {
		this.leader = horse;
		horse.addFollower();
		return horse;
	}

	public void stopFollowing() {
		this.leader.removeFollower();
		this.leader = null;
	}

	private void addFollower() {
		++this.herdSize;
	}

	private void removeFollower() {
		--this.herdSize;
	}

	public boolean canBeFollowed() {
		return this.hasFollowers() && this.herdSize < this.getMaxHerdSize();
	}

	public int getMaxHerdSize() {
		return 3;
	}

	public boolean hasFollowers() {
		return this.herdSize > 1;
	}

	public boolean inRangeOfLeader() {
		return this.distanceToSqr(this.leader) <= 121.0D;
	}

	public void pathToLeader() {
		if (this.isFollower()) {
			this.getNavigation().moveTo(this.leader, 1.0D);
		}

	}

	public void addFollowers(Stream<? extends OHorse> stream) {
		stream.limit((long)(this.getMaxHerdSize() - this.herdSize)).filter((horse) -> {
			return horse != this;
		}).forEach((horse) -> {
			horse.startFollowing(this);
		});
	}

	//ground tie
	@Override
	public void tick() {
		super.tick();
		if (this.isSaddled() && !this.isVehicle() || this.isLeashed()) {
			this.getNavigation().stop();
		}

		if (this.hasFollowers() && this.level.random.nextInt(200) == 1) {
			List<? extends OHorse> list = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
			if (list.size() <= 1) {
				this.herdSize = 1;
			}
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public void positionRider(Entity entity) {
		if (this.hasPassenger(entity)) {
			double offsetX = 0;
			double offsetY = 1.1;
			double offsetZ = -0.2;

			if (this.isSaddled() && getModelLocation().equals(BreedModel.STOCK)) {
				offsetY = 1.3;
			}

			if (this.isSaddled() && getModelLocation().equals(BreedModel.DRAFT)) {
				offsetY = 1.6;
			}

			if (this.isSaddled() && getModelLocation().equals(BreedModel.WARMBLOOD)) {
				offsetY = 1.4;
			}

			if (this.isSaddled() && getModelLocation().equals(BreedModel.PONY)) {
				offsetY = 1.0;
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

		if (this.isFood(itemstack) && this.isTamed()) {
			if (this.getHealth() < this.getMaxHealth()) {
				this.usePlayerItem(player, hand, itemstack);
				this.heal(itemstack.getFoodProperties(this).getNutrition());
				this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
		}

		if (this.isFood(itemstack) && this.isTamed()) {
			if (this.canFallInLove() && !this.level.isClientSide) {
				this.usePlayerItem(player, hand, itemstack);
				this.setInLove(player);
				this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
				return InteractionResult.SUCCESS;
			}
		}

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

			if (!this.level.isClientSide) {
				if (player.isShiftKeyDown()) {
					NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider((containerId, inventory, serverPlayer) -> {
						return new OHorseMenu(containerId, inventory, this.inventory, this);
					}, this.getDisplayName()), (data) -> {
						data.writeInt(this.getInventorySize());
						data.writeInt(this.getId());
					});
					return InteractionResult.SUCCESS;
				}

				InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
				if (interactionresult.consumesAction()) {
					return interactionresult;
				}
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

	// Generates the base texture
	public ResourceLocation getTextureLocation() {
		return OHorseModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	public ResourceLocation getOverlayLocation() {
		return OHorseMarkingLayer.Overlay.overlayFromOrdinal(getOverlayVariant()).resourceLocation;
	}

	public ResourceLocation getModelLocation() {
		return BreedModel.breedFromOrdinal(getBreed()).resourceLocation;
	}

	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BREED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);

	public int getVariant() {
		return this.entityData.get(VARIANT);
	}

	public int getOverlayVariant() {
		return this.entityData.get(OVERLAY);
	}
	public int getBreed() {
		return this.entityData.get(BREED);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}
	public void setOverlayVariant(int variant) {
		this.entityData.set(OVERLAY, variant);
	}
	public void setBreed(int breed) {
		this.entityData.set(BREED, breed);
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

		if (tag.contains("Breed")) {
			setBreed(tag.getInt("Breed"));
		}

		if (tag.contains("Chested")) {
			this.setChested(tag.getBoolean("Chested"));
		}

		if (tag.contains("Saddled")) {
			this.setSaddled(tag.getBoolean("Saddled"));
		}

		if (tag.contains("Armored")) {
			this.setArmored(tag.getBoolean("Armored"));
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

		tag.putInt("Breed", getBreed());

		tag.putBoolean("Chested", this.isChested());

		tag.putBoolean("Saddled", this.isSaddled());

		tag.putBoolean("Armored", this.isArmored());

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
		setBreed(random.nextInt(BreedModel.values().length));

		this.randomizeAttributes();
		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(BREED, 0);
		this.entityData.define(CHESTED, false);
		this.entityData.define(SADDLED, false);
		this.entityData.define(ARMORED, false);
	}

	protected void updateContainerEquipment() {
		if (!this.level.isClientSide) {
			super.updateContainerEquipment();
			this.setDropChance(EquipmentSlot.CHEST, 0.0F);
		}
	}

	protected boolean canParent() {
		return !this.isVehicle() && !this.isPassenger() && this.isTamed() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
	}

	public boolean canMate(Animal animal) {
		if (animal == this) {
			return false;
		} else if (!(animal instanceof Donkey) && !(animal instanceof OHorse)) {
			return false;
		} else {
			return this.canParent() && ((OHorse) animal).canParent();
		}
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		AbstractHorse abstracthorse;
		if (ageableMob instanceof Donkey) {
			abstracthorse = EntityType.MULE.create(serverLevel);
		} else {
			OHorse horse = (OHorse) ageableMob;
			abstracthorse = EntityTypes.O_HORSE_ENTITY.get().create(serverLevel);

			int i = this.random.nextInt(9);
			int variant;
			if (i < 4) {
				variant = this.getVariant();
			} else if (i < 8) {
				variant = horse.getVariant();
			} else {
				variant = this.random.nextInt(OHorseModel.Variant.values().length);
			}

			int j = this.random.nextInt(5);
			int overlay;
			if (j < 2) {
				overlay = this.getOverlayVariant();
			} else if (j < 4) {
				overlay = horse.getOverlayVariant();
			} else {
				overlay = this.random.nextInt(OHorseMarkingLayer.Overlay.values().length);
			}

			int k = this.random.nextInt(5);
			int breed;
			if (k < 2) {
				breed = this.getBreed();
			} else if (k < 4) {
				breed = horse.getBreed();
			} else {
				breed = this.random.nextInt(BreedModel.values().length);
			}

			((OHorse) abstracthorse).setVariant(variant);
			((OHorse) abstracthorse).setOverlayVariant(overlay);
			((OHorse) abstracthorse).setBreed(breed);
		}

		this.setOffspringAttributes(ageableMob, abstracthorse);
		return abstracthorse;
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

	private void setChested(boolean chested) {
		this.entityData.set(CHESTED, chested);
	}

	@Override
	public void containerChanged(Container container) {
		boolean flag = this.isSaddled();
		if (!this.level.isClientSide) {
			this.setSaddled(!this.inventory.getItem(0).isEmpty());
		}
		if (this.tickCount > 20 && !flag && this.isSaddleable()) {
			this.playSound(SoundEvents.HORSE_SADDLE, 0.5f, 1f);
		}

		if (!this.level.isClientSide) {
			this.setArmored(!this.inventory.getItem(0).isEmpty());
		}
		if (this.tickCount > 20 && !flag && this.isArmorable()) {
			this.playSound(SoundEvents.HORSE_ARMOR, 0.5f, 1f);
		}
	}

	@Override
	public boolean isArmorable() {
		return this.isAlive() && !this.isBaby() && this.isTamed();
	}

	@Override
	public void equipArmor(@Nullable SoundSource soundSource) {
		if (soundSource != null) {
			this.level.playSound(null, this, SoundEvents.HORSE_ARMOR, soundSource, 0.5f, 1f);
		}
	}

	@Override
	public boolean isArmored() {
		return this.entityData.get(ARMORED);
	}

	public void setArmored(boolean armored) {
		this.entityData.set(ARMORED, armored);
		this.getAttribute(Attributes.ARMOR).setBaseValue(this.getAttribute(Attributes.ARMOR).getBaseValue() + 10);
	}
}
