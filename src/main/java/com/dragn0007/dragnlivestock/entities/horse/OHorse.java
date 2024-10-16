package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.ai.HorseFollowHerdLeaderGoal;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
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

public class OHorse extends AbstractOHorse implements IAnimatable {
	public static final EntityDataAccessor<ResourceLocation> VARIANT_TEXTURE = SynchedEntityData.defineId(OHorse.class, LivestockOverhaul.RESOURCE_LOCATION);
	public static final EntityDataAccessor<ResourceLocation> OVERLAY_TEXTURE = SynchedEntityData.defineId(OHorse.class, LivestockOverhaul.RESOURCE_LOCATION);
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> BREED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);


	public AnimationFactory factory = GeckoLibUtil.createFactory(this);
	public OHorse leader;
	public int herdSize = 1;

	public OHorse(EntityType<? extends OHorse> type, Level level) {
		super(type, level);
		this.noCulling = true;
	}

	public static AttributeSupplier.Builder createBaseHorseAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.JUMP_STRENGTH)
				.add(Attributes.MAX_HEALTH, 53.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.235F)
				.add(Attributes.ATTACK_DAMAGE, 1D);
	}

	@Override
	public void randomizeAttributes() {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.generateRandomMaxHealth());
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed());
		this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
	}

	@Override
	public void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.7D));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, true));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 0.0F));

		this.goalSelector.addGoal(3, new HorseFollowHerdLeaderGoal(this));
		this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D, AbstractOHorse.class));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, 15.0F, 1.8F, 1.8F, (livingEntity) -> livingEntity instanceof Wolf));
	}

	@Override
	public float generateRandomMaxHealth() {
		return 15.0F + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
	}

	@Override
	public double generateRandomJumpStrength() {
		return (double)0.4F + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
	}

	@Override
	public double generateRandomSpeed() {
		return ((double)0.45F + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
	}

	@Override
	public boolean canBeRiddenInWater(Entity rider) {
		return false;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double movementSpeed = getAttributeValue(Attributes.MOVEMENT_SPEED);
		double animationSpeed = Math.max(0.1, movementSpeed);
		double currentSpeed = this.getDeltaMovement().lengthSqr();
		double speedThreshold = 0.02;

		if (this.isJumping() || !this.isOnGround()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("jump", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
			event.getController().setAnimationSpeed(1.0);
		} else if (event.isMoving()) {
			if (currentSpeed > speedThreshold || this.isAggressive() || this.isSprinting()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.8 * event.getController().getAnimationSpeed() + animationSpeed));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.8 * event.getController().getAnimationSpeed() + animationSpeed));
			}
		} else {
			if(this.isVehicle()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle3", ILoopType.EDefaultLoopTypes.LOOP));
			}
			event.getController().setAnimationSpeed(1.0);
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
	public void registerControllers (AnimationData data){
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
		data.addAnimationController(new AnimationController(this, "attackController", 1, this::attackPredicate));
	}

	public boolean isFollower() {
		return this.leader != null && this.leader.isAlive();
	}

	public void startFollowing(OHorse horse) {
		this.leader = horse;
		horse.addFollower();
	}

	public void stopFollowing() {
		this.leader.removeFollower();
		this.leader = null;
	}

	public void addFollower() {
		this.herdSize++;
	}

	public void removeFollower() {
		this.herdSize--;
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
		stream.limit(this.getMaxHerdSize() - this.herdSize).filter((horse) -> {
			return horse != this;
		}).forEach((horse) -> {
			horse.startFollowing(this);
		});
	}

	@Override
	public void travel(Vec3 travel) {
		if (this.isAlive()) {
			if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
				LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
				this.setYRot(livingentity.getYRot());
				this.yRotO = this.getYRot();
				this.setXRot(livingentity.getXRot() * 0.5F);
				this.setRot(this.getYRot(), this.getXRot());
				this.yBodyRot = this.getYRot();
				this.yHeadRot = this.yBodyRot;
				float sidewaysSpeed = livingentity.xxa * 0.5F;
				float fowardSpeed = livingentity.zza;
				if (fowardSpeed <= 0.0F) {
					fowardSpeed *= 0.25F;
					this.gallopSoundCounter = 0;
				}

				if (this.isSprinting()) { //walk when CTRL is toggled
					fowardSpeed = 0.0F;
				}

				if (this.onGround && this.playerJumpPendingScale == 0.0F && this.isStanding()) {
					sidewaysSpeed = 0.0F;
					fowardSpeed = 0.0F;
				}

				if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
					double d0 = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
					double d1 = d0 + this.getJumpBoostPower();
					Vec3 vec3 = this.getDeltaMovement();
					this.setDeltaMovement(vec3.x, d1, vec3.z);
					this.setIsJumping(true);
					this.hasImpulse = true;
					net.minecraftforge.common.ForgeHooks.onLivingJump(this);
					if (fowardSpeed > 0.0F) {
						float f2 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F));
						float f3 = Mth.cos(this.getYRot() * ((float)Math.PI / 180F));
						this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f2 * this.playerJumpPendingScale, 0.0D, 0.4F * f3 * this.playerJumpPendingScale));
					}

					this.playerJumpPendingScale = 0.0F;
				}

				this.flyingSpeed = this.getSpeed() * 0.1F;
				if (this.isControlledByLocalInstance()) {
					this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
					super.travel(new Vec3(sidewaysSpeed, travel.y, fowardSpeed));
				} else if (livingentity instanceof Player) {
					this.setDeltaMovement(Vec3.ZERO);
				}

				if (this.onGround) {
					this.playerJumpPendingScale = 0.0F;
					this.setIsJumping(false);
				}

				this.calculateEntityAnimation(this, false);
				this.tryCheckInsideBlocks();
			} else {
				this.flyingSpeed = 0.02F;
				super.travel(travel);
			}
		}
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

			if (this.isSaddled() && getModelLocation().equals(BreedModel.STOCK.resourceLocation)) {
				offsetY = 1.3;
			}

			if (this.isSaddled() && getModelLocation().equals(BreedModel.DRAFT.resourceLocation)) {
				offsetY = 1.45;
			}

			if (this.isSaddled() && getModelLocation().equals(BreedModel.WARMBLOOD.resourceLocation)) {
				offsetY = 1.35;
			}

			if (this.isSaddled() && getModelLocation().equals(BreedModel.PONY.resourceLocation)) {
				offsetY = 1.1;
			}

			if (this.isJumping()) {
				offsetY = 1.7;
				offsetZ = -0.9;
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

	@Override
	public SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return SoundEvents.HORSE_AMBIENT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.HORSE_DEATH;
	}

	@Nullable
	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.HORSE_EAT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSource) {
		super.getHurtSound(damageSource);
		return SoundEvents.HORSE_HURT;
	}

	@Override
	public SoundEvent getAngrySound() {
		super.getAngrySound();
		return SoundEvents.HORSE_ANGRY;
	}

	public ResourceLocation getTextureLocation() {
		return this.entityData.get(VARIANT_TEXTURE);
//		return OHorseModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	public ResourceLocation getOverlayLocation() {
		return this.entityData.get(OVERLAY_TEXTURE);
//		return OHorseMarkingLayer.Overlay.overlayFromOrdinal(getOverlayVariant()).resourceLocation;
	}

	public ResourceLocation getModelLocation() {
		return BreedModel.breedFromOrdinal(getBreed()).resourceLocation;
	}

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
		this.entityData.set(VARIANT_TEXTURE, OHorseModel.Variant.variantFromOrdinal(variant).resourceLocation);
		this.entityData.set(VARIANT, variant);
	}

	public void setOverlayVariant(int variant) {
		this.entityData.set(OVERLAY_TEXTURE, OHorseMarkingLayer.Overlay.overlayFromOrdinal(variant).resourceLocation);
		this.entityData.set(OVERLAY, variant);
	}

	public void setBreed(int breed) {
		this.entityData.set(BREED, breed);
	}

	public void setVariantTexture(String variant) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(variant);
		if (resourceLocation == null) {
			resourceLocation = OHorseModel.Variant.BAY.resourceLocation;
		}
		this.entityData.set(VARIANT_TEXTURE, resourceLocation);
	}

	public void setOverlayVariantTexture(String variant) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(variant);
		if (resourceLocation == null) {
			resourceLocation = OHorseMarkingLayer.Overlay.NONE.resourceLocation;
		}
		this.entityData.set(OVERLAY_TEXTURE, resourceLocation);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("Variant")) {
			this.setVariant(tag.getInt("Variant"));
		}

		if (tag.contains("Overlay")) {
			this.setOverlayVariant(tag.getInt("Overlay"));
		}

		if (tag.contains("Variant_Texture")) {
			this.setVariantTexture(tag.getString("Variant_Texture"));
		}

		if (tag.contains("Overlay_Texture")) {
			this.setOverlayVariantTexture(tag.getString("Overlay_Texture"));
		}

		if (tag.contains("Breed")) {
			this.setBreed(tag.getInt("Breed"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", this.getVariant());
		tag.putInt("Overlay", this.getOverlayVariant());
		tag.putString("Variant_Texture", this.getTextureLocation().toString());
		tag.putString("Overlay_Texture", this.getOverlayLocation().toString());
		tag.putInt("Breed", getBreed());
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (data == null) {
			data = new AgeableMob.AgeableMobGroupData(0.2F);
		}
		Random random = new Random();
		this.setVariant(random.nextInt(OHorseModel.Variant.values().length));
		this.setOverlayVariant(random.nextInt(OHorseMarkingLayer.Overlay.values().length));
//		setBreed(random.nextInt(BreedModel.values().length)); breeds shouldnt spawn naturally!

		this.randomizeAttributes();
		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(BREED, 0);
		this.entityData.define(VARIANT_TEXTURE, OHorseModel.Variant.BAY.resourceLocation);
		this.entityData.define(OVERLAY_TEXTURE, OHorseMarkingLayer.Overlay.NONE.resourceLocation);
	}


	public boolean canMate(Animal animal) {
		if (animal == this) {
			return false;
		} else if (!(animal instanceof ODonkey) && !(animal instanceof OHorse)) {
			return false;
		} else {
			return this.canParent() && ((AbstractOHorse)animal).canParent();
		}
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		AbstractOHorse abstracthorse;
		if (ageableMob instanceof ODonkey) {
			abstracthorse = EntityTypes.O_MULE_ENTITY.get().create(serverLevel);
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
}
