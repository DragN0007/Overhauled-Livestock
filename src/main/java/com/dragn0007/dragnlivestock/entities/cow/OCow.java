package com.dragn0007.dragnlivestock.entities.cow;

import com.dragn0007.dragnlivestock.entities.Chestable;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.ai.CattleFollowHerdLeaderGoal;
import com.dragn0007.dragnlivestock.entities.cow.ox.Ox;
import com.dragn0007.dragnlivestock.entities.cow.ox.OxModel;
import com.dragn0007.dragnlivestock.util.LOTags;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class OCow extends Animal implements IAnimatable {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(OCow.class, EntityDataSerializers.BOOLEAN);

	public OCow leader;
	public int herdSize = 1;

	public OCow(EntityType<? extends OCow> type, Level level) {
		super(type, level);
	}

	@Override
	public Vec3 getLeashOffset() {
		return new Vec3(0D, (double)this.getEyeHeight() * 1.0F, (double)(this.getBbWidth() * 0.9F));
		//              ^ Side offset                      ^ Height offset                   ^ Length offset
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 14.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.17F);
	}

	public void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(3, new CattleFollowHerdLeaderGoal(this));

		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, 15.0F, 1.8F, 1.8F, livingEntity -> {
			boolean isHorse = livingEntity.getType().is(LOTags.Entity_Types.HORSES);
			boolean isRHGHorse = livingEntity.getType().is(LOTags.Entity_Types.RHG_HORSES);
			boolean isSWEMHorse = livingEntity.getType().is(LOTags.Entity_Types.SWEM_HORSES);
			boolean isWolf = livingEntity instanceof Wolf;
			return isHorse || isRHGHorse || isSWEMHorse || isWolf;
		}));
	}

	@Override
	public float getStepHeight() {
		return 1F;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double currentSpeed = this.getDeltaMovement().lengthSqr(); //grabbing the speed of the cow to see if it should run the "run" animation. particularly for avoiding the OHorse.
		double speedThreshold = 0.01;

		if (event.isMoving()) {
			if (currentSpeed > speedThreshold) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
			}
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
		}

		return PlayState.CONTINUE;
	}

	public PlayState attackPredicate(AnimationEvent event) {
		if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
			event.getController().markNeedsReload();
			event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
			this.swinging = false;
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
		data.addAnimationController(new AnimationController(this, "attackController", 1, this::attackPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public boolean isFollower() {
		return this.leader != null && this.leader.isAlive();
	}

	public OCow startFollowing(OCow cow) {
		this.leader = cow;
		cow.addFollower();
		return cow;
	}

	public void stopFollowing() {
		this.leader.removeFollower();
		this.leader = null;
	}

	public void addFollower() {
		++this.herdSize;
	}

	public void removeFollower() {
		--this.herdSize;
	}

	public boolean canBeFollowed() {
		return this.hasFollowers() && this.herdSize < this.getMaxHerdSize();
	}

	public void tick() {
		super.tick();
		if (this.hasFollowers() && this.level.random.nextInt(200) == 1) {
			List<? extends OCow> list = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
			if (list.size() <= 1) {
				this.herdSize = 1;
			}
		}

	}

	public int getMaxHerdSize() {
		return 8;
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

	public void addFollowers(Stream<? extends OCow> p_27534_) {
		p_27534_.limit((long)(this.getMaxHerdSize() - this.herdSize)).filter((cow) -> {
			return cow != this;
		}).forEach((cow) -> {
			cow.startFollowing(this);
		});
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(Items.BUCKET) && !this.isBaby() &&
				(!LivestockOverhaulCommonConfig.GENDERS_ENABLED.get() ||
						(LivestockOverhaulCommonConfig.GENDERS_ENABLED.get() &&
								getUddersLocation().equals(OCowUdderLayer.Overlay.FEMALE.resourceLocation)))) {

			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
			ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, Items.MILK_BUCKET.getDefaultInstance());
			player.setItemInHand(hand, itemstack1);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		} else {
			return super.mobInteract(player, hand);
		}
	}

	public SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return SoundEvents.COW_AMBIENT;
	}

	public SoundEvent getDeathSound() {
		super.getDeathSound();
		return SoundEvents.COW_DEATH;
	}

	public SoundEvent getHurtSound(DamageSource p_30720_) {
		super.getHurtSound(p_30720_);
		return SoundEvents.COW_HURT;
	}

	public void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
		this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
	}

	// Generates the base texture
	public ResourceLocation getTextureLocation() {
		return OCowModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	public ResourceLocation getOverlayLocation() {
		return OCowMarkingLayer.Overlay.overlayFromOrdinal(getOverlayVariant()).resourceLocation;
	}

	public ResourceLocation getHornsLocation() {
		return OCowHornLayer.HornOverlay.hornOverlayFromOrdinal(getHornVariant()).resourceLocation;
	}

	public ResourceLocation getUddersLocation() {
		return OCowUdderLayer.Overlay.overlayFromOrdinal(getUdderVariant()).resourceLocation;
	}

	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OCow.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OCow.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> HORNS = SynchedEntityData.defineId(OCow.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> UDDERS = SynchedEntityData.defineId(OCow.class, EntityDataSerializers.INT);

	public int getVariant() {
		return this.entityData.get(VARIANT);
	}
	public int getOverlayVariant() {
		return this.entityData.get(OVERLAY);
	}
	public int getHornVariant() {
		return this.entityData.get(HORNS);
	}
	public int getUdderVariant() {
		return this.entityData.get(UDDERS);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}
	public void setOverlayVariant(int overlayVariant) {
		this.entityData.set(OVERLAY, overlayVariant);
	}
	public void setHornVariant(int hornVariant) {
		this.entityData.set(HORNS, hornVariant);
	}
	public void setUdderVariant(int udderVariant) {
		this.entityData.set(UDDERS, udderVariant);
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

		if (tag.contains("Horns")) {
			setHornVariant(tag.getInt("Horns"));
		}

		if (tag.contains("Udders")) {
			setUdderVariant(tag.getInt("Udders"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());

		tag.putInt("Overlay", getOverlayVariant());

		tag.putInt("Horns", getHornVariant());

		tag.putInt("Udders", getUdderVariant());
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (data == null) {
			data = new AgeableMobGroupData(0.2F);
		}
		Random random = new Random();
		setVariant(random.nextInt(OCowModel.Variant.values().length));
		setOverlayVariant(random.nextInt(OCowMarkingLayer.Overlay.values().length));
		setHornVariant(random.nextInt(OCowHornLayer.HornOverlay.values().length));
		setUdderVariant(random.nextInt(OCowUdderLayer.Overlay.values().length));

		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(HORNS, 0);
		this.entityData.define(UDDERS, 0);
		this.entityData.define(CHESTED, false);
	}

	public boolean canParent() {
		return !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
	}

	public boolean canMate(Animal animal) {
			return this.canParent() && ((OCow) animal).canParent();
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		OCow oCow = (OCow) ageableMob;

		if (ageableMob instanceof OCow) {
			OCow oCow1 = (OCow) ageableMob;

			if (this.random.nextInt(5) == 0) {
				Ox oX = EntityTypes.OX_ENTITY.get().create(serverLevel);
				if (oX != null) {
					oX.setVariant(this.random.nextInt(OxModel.Variant.values().length));
					return oX;
				}

			} else {
				oCow = EntityTypes.O_COW_ENTITY.get().create(serverLevel);

				int i = this.random.nextInt(9);
				int variant;
				if (i < 4) {
					variant = this.getVariant();
				} else if (i < 8) {
					variant = oCow1.getVariant();
				} else {
					variant = this.random.nextInt(OCowModel.Variant.values().length);
				}

				int j = this.random.nextInt(5);
				int overlay;
				if (j < 2) {
					overlay = this.getOverlayVariant();
				} else if (j < 4) {
					overlay = oCow1.getOverlayVariant();
				} else {
					overlay = this.random.nextInt(OCowMarkingLayer.Overlay.values().length);
				}

				int k = this.random.nextInt(5);
				int horns;
				if (k < 2) {
					horns = this.getHornVariant();
				} else if (k < 4) {
					horns = oCow1.getHornVariant();
				} else {
					horns = this.random.nextInt(OCowHornLayer.HornOverlay.values().length);
				}

				int udders;
				udders = this.random.nextInt(OCowUdderLayer.Overlay.values().length);

				oCow.setVariant(variant);
				oCow.setOverlayVariant(overlay);
				oCow.setHornVariant(horns);
				oCow.setUdderVariant(udders);
			}
		}

		return oCow;
	}

}
