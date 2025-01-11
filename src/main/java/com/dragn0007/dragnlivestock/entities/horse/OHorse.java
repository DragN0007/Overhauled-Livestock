package com.dragn0007.dragnlivestock.entities.horse;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.ai.HorseFollowHerdLeaderGoal;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.entities.mule.OMuleModel;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.event.LivestockOverhaulClientEvent;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.item.ItemStack;
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

public class OHorse extends AbstractOMount implements IAnimatable {

	public AnimationFactory factory = GeckoLibUtil.createFactory(this);
	public OHorse leader;
	public int herdSize = 1;

	public OHorse(EntityType<? extends OHorse> type, Level level) {
		super(type, level);
	}

	@Override
	public Vec3 getLeashOffset() {
		return new Vec3(0D, (double)this.getEyeHeight() * 0.8F, (double)(this.getBbWidth() * 1.2F));
		//              ^ Side offset                      ^ Height offset                   ^ Length offset
	}

	public static AttributeSupplier.Builder createBaseHorseAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.JUMP_STRENGTH)
				.add(Attributes.MAX_HEALTH, 53.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.255F)
				.add(Attributes.ATTACK_DAMAGE, 1D);
	}

	@Override
	public void randomizeAttributes() {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.generateRandomMaxHealth());
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed());
		this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
//		this.getAttribute(LOAttributes.ENDURANCE).setBaseValue(this.generateRandomEndurance());
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
		this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D, AbstractOMount.class));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, 15.0F, 1.8F, 1.8F, (livingEntity) -> livingEntity instanceof Wolf));
	}

	public float generateRandomMaxHealth() {
		float baseHealth;
		if (getModelLocation().equals(BreedModel.MUSTANG.resourceLocation)) {
			baseHealth = 16.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.ARDENNES.resourceLocation)) {
			baseHealth = 20.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.KLADRUBER.resourceLocation)) {
			baseHealth = 16.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.FJORD.resourceLocation)) {
			baseHealth = 14.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.THOROUGHBRED.resourceLocation)) {
			baseHealth = 13.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.FRIESIAN.resourceLocation)) {
			baseHealth = 18.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.IRISH_COB.resourceLocation)) {
			baseHealth = 18.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.AMERICAN_QUARTER.resourceLocation)) {
			baseHealth = 16.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.PERCHERON.resourceLocation)) {
			baseHealth = 24.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.SELLE_FRANCAIS.resourceLocation)) {
			baseHealth = 17.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.MARWARI.resourceLocation)) {
			baseHealth = 18.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.MONGOLIAN.resourceLocation)) {
			baseHealth = 20.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.SHIRE.resourceLocation)) {
			baseHealth = 24.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		if (getModelLocation().equals(BreedModel.AKHAL_TEKE.resourceLocation)) {
			baseHealth = 15.0F;
			return baseHealth + this.random.nextInt(3) + this.random.nextInt(5);
		}
		return 15.0F + (float) this.random.nextInt(4) + (float) this.random.nextInt(5);
	}

	public double generateRandomJumpStrength() {
		double baseStrength = 0.4F;
		double multiplier = this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.25D;

		if (getModelLocation().equals(BreedModel.MUSTANG.resourceLocation)) {
			baseStrength = 0.5F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.ARDENNES.resourceLocation)) {
			baseStrength = 0.3F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.KLADRUBER.resourceLocation)) {
			baseStrength = 0.4F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.FJORD.resourceLocation)) {
			baseStrength = 0.35F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.THOROUGHBRED.resourceLocation)) {
			baseStrength = 0.35F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.FRIESIAN.resourceLocation)) {
			baseStrength = 0.35F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.IRISH_COB.resourceLocation)) {
			baseStrength = 0.3F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.AMERICAN_QUARTER.resourceLocation)) {
			baseStrength = 0.4F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.SELLE_FRANCAIS.resourceLocation)) {
			baseStrength = 0.55F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.PERCHERON.resourceLocation)) {
			baseStrength = 0.25F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.MARWARI.resourceLocation)) {
			baseStrength = 0.4F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.MONGOLIAN.resourceLocation)) {
			baseStrength = 0.3F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.SHIRE.resourceLocation)) {
			baseStrength = 0.2F;
			return baseStrength + multiplier;
		}
		if (getModelLocation().equals(BreedModel.AKHAL_TEKE.resourceLocation)) {
			baseStrength = 0.3F;
			return baseStrength + multiplier;
		}
		return baseStrength + this.random.nextDouble() * 0.15D;
	}

	public double generateRandomSpeed() {
		double baseSpeed = 0.0F;
		double multiplier = (this.random.nextDouble() * 0.1D + this.random.nextDouble() * 0.1D + this.random.nextDouble() * 0.1D) * 0.30D;

		if (getModelLocation().equals(BreedModel.MUSTANG.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.ARDENNES.resourceLocation)) {
			baseSpeed = 0.15F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.KLADRUBER.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.FJORD.resourceLocation)) {
			baseSpeed = 0.15F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.THOROUGHBRED.resourceLocation)) {
			baseSpeed = 0.25F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.FRIESIAN.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.IRISH_COB.resourceLocation)) {
			baseSpeed = 0.15F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.AMERICAN_QUARTER.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.SELLE_FRANCAIS.resourceLocation)) {
			baseSpeed = 0.15F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.PERCHERON.resourceLocation)) {
			baseSpeed = 0.15F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.MARWARI.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.MONGOLIAN.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.SHIRE.resourceLocation)) {
			baseSpeed = 0.2F;
			return baseSpeed + multiplier;
		}
		if (getModelLocation().equals(BreedModel.AKHAL_TEKE.resourceLocation)) {
			baseSpeed = 0.25F;
			return baseSpeed + multiplier;
		}
		return baseSpeed + multiplier;
	}

	public double generateRandomEndurance() {
		return ((double)0.45F + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
	}

	@Override
	public boolean canBeRiddenInWater(Entity rider) {
		return false;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double movementSpeed = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
		double animationSpeed = Math.max(0.1, movementSpeed);

		if(this.isJumping() || !this.isOnGround()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("jump", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
			event.getController().setAnimationSpeed(1.0);
		} else if(event.isMoving()) {
			if(this.isAggressive() || (this.isVehicle() && this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPRINT_SPEED_MOD))) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("sprint", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.82 * event.getController().getAnimationSpeed() + animationSpeed));

			} else if (this.isVehicle() && !this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(WALK_SPEED_MOD) && !this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPRINT_SPEED_MOD)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.8 * event.getController().getAnimationSpeed() + animationSpeed));

			} else if (this.isVehicle() && LivestockOverhaulClientEvent.HORSE_SPANISH_WALK_TOGGLE.isDown() && this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(WALK_SPEED_MOD)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("spanish_walk", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.82 * event.getController().getAnimationSpeed() + animationSpeed));

			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(Math.max(0.1, 0.82 * event.getController().getAnimationSpeed() + animationSpeed));
			}
		} else {
			if (this.isVehicle()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle3", ILoopType.EDefaultLoopTypes.LOOP));
			}
			event.getController().setAnimationSpeed(1.0);
		}
		return PlayState.CONTINUE;
	}

	public <T extends IAnimatable> PlayState attackPredicate(AnimationEvent<T> event) {
		if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
			event.getController().markNeedsReload();
			event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
			this.swinging = false;
		}
		return PlayState.CONTINUE;
	}

	public <T extends IAnimatable> PlayState emotePredicate(AnimationEvent<T> event) {
		if(event.isMoving() || !this.shouldEmote) {
            event.getController().markNeedsReload();
			event.getController().setAnimation(new AnimationBuilder().clearAnimations());
			this.shouldEmote = false;
			return PlayState.STOP;
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers (AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
		data.addAnimationController(new AnimationController<>(this, "attackController", 1, this::attackPredicate));
		data.addAnimationController(new AnimationController<>(this, "emoteController", 5, this::emotePredicate));
	}

	@Override
	public void playEmote(String emoteName, ILoopType.EDefaultLoopTypes loopType) {
		AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, this.getId(), "emoteController");
        controller.markNeedsReload();
		controller.setAnimation(new AnimationBuilder().clearAnimations().addAnimation(emoteName, loopType));
		this.shouldEmote = true;
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

			if (getModelLocation().equals(BreedModel.MUSTANG.resourceLocation)) {
				offsetY = 1.0;
			}

			if (getModelLocation().equals(BreedModel.ARDENNES.resourceLocation)) {
				offsetY = 1.1;
			}

			if (getModelLocation().equals(BreedModel.KLADRUBER.resourceLocation)) {
				offsetY = 1.15;
			}

			if (getModelLocation().equals(BreedModel.FJORD.resourceLocation)) {
				offsetY = 0.85;
			}

			if (getModelLocation().equals(BreedModel.THOROUGHBRED.resourceLocation)) {
				offsetY = 1.2;
			}

			if (getModelLocation().equals(BreedModel.FRIESIAN.resourceLocation)) {
				offsetY = 1.25;
			}

			if (getModelLocation().equals(BreedModel.IRISH_COB.resourceLocation)) {
				offsetY = 0.95;
			}

			if (getModelLocation().equals(BreedModel.AMERICAN_QUARTER.resourceLocation)) {
				offsetY = 1.0;
			}

			if (getModelLocation().equals(BreedModel.PERCHERON.resourceLocation)) {
				offsetY = 1.43;
			}

			if (getModelLocation().equals(BreedModel.SELLE_FRANCAIS.resourceLocation)) {
				offsetY = 1.1;
			}

			if (getModelLocation().equals(BreedModel.MARWARI.resourceLocation)) {
				offsetY = 1.1;
			}

			if (getModelLocation().equals(BreedModel.MONGOLIAN.resourceLocation)) {
				offsetY = 0.85;
			}

			if (getModelLocation().equals(BreedModel.SHIRE.resourceLocation)) {
				offsetY = 1.5;
			}

			if (getModelLocation().equals(BreedModel.AKHAL_TEKE.resourceLocation)) {
				offsetY = 1.1;
			}

			if (this.isJumping()) {
//				offsetY = 1.7;
				offsetZ = -0.6;
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

	public static final EntityDataAccessor<ResourceLocation> VARIANT_TEXTURE = SynchedEntityData.defineId(OHorse.class, LivestockOverhaul.RESOURCE_LOCATION);
	public static final EntityDataAccessor<ResourceLocation> OVERLAY_TEXTURE = SynchedEntityData.defineId(OHorse.class, LivestockOverhaul.RESOURCE_LOCATION);
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> BREED = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);

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

		if (tag.contains("Gender")) {
			this.setGender(tag.getInt("Gender"));
		}

		if (tag.contains("Mane")) {
			this.setManeType(tag.getInt("Mane"));
		}

		if (tag.contains("Tail")) {
			this.setTailType(tag.getInt("Tail"));
		}

		this.createInventory();
		if (this.hasChest()) {
			ListTag listtag = tag.getList("Items", 10);

			for(int i = 0; i < listtag.size(); ++i) {
				CompoundTag compoundtag = listtag.getCompound(i);
				int j = compoundtag.getByte("Slot") & 255;
				if (j >= 2 && j < this.inventory.getContainerSize()) {
					this.inventory.setItem(j, ItemStack.of(compoundtag));
				}
			}
		}

		this.updateContainerEquipment();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", this.getVariant());
		tag.putInt("Overlay", this.getOverlayVariant());
		tag.putString("Variant_Texture", this.getTextureLocation().toString());
		tag.putString("Overlay_Texture", this.getOverlayLocation().toString());
		tag.putInt("Breed", this.getBreed());
		tag.putInt("Gender", this.getGender());
		tag.putInt("Mane", this.getManeType());
		tag.putInt("Tail", this.getTailType());

		if (this.hasChest()) {
			ListTag listtag = new ListTag();

			for(int i = 2; i < this.inventory.getContainerSize(); ++i) {
				ItemStack itemstack = this.inventory.getItem(i);
				if (!itemstack.isEmpty()) {
					CompoundTag compoundtag = new CompoundTag();
					compoundtag.putByte("Slot", (byte)i);
					itemstack.save(compoundtag);
					listtag.add(compoundtag);
				}
			}

			tag.put("Items", listtag);
		}
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
		this.setGender(random.nextInt(Gender.values().length));

		if (spawnType == MobSpawnType.SPAWN_EGG || LivestockOverhaulCommonConfig.NATURAL_HORSE_BREEDS.get()) {
			this.setBreed(random.nextInt(BreedModel.values().length));
		}

		this.randomizeAttributes();
		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(BREED, 0);
		this.entityData.define(GENDER, 0);
		this.entityData.define(VARIANT_TEXTURE, OHorseModel.Variant.BAY.resourceLocation);
		this.entityData.define(OVERLAY_TEXTURE, OHorseMarkingLayer.Overlay.NONE.resourceLocation);
		this.entityData.define(MANE_TYPE, 0);
		this.entityData.define(TAIL_TYPE, 0);
	}

	public boolean canMate(Animal animal) {
		if (animal == this) {
			return false;
		} else if (!(animal instanceof ODonkey) && !(animal instanceof OHorse)) {
			return false;
		} else {
			if (!LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
				return this.canParent() && ((AbstractOMount) animal).canParent();
			} else {
				AbstractOMount partner = (AbstractOMount) animal;
				if (this.canParent() && partner.canParent() && this.getGender() != partner.getGender()) {
					return true;
				}

				boolean partnerIsFemale = partner.isFemale();
				boolean partnerIsMale = partner.isMale();
				if (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get() && this.canParent() && partner.canParent()
						&& ((isFemale() && partnerIsMale) || (isMale() && partnerIsFemale))) {
					return isFemale();
				}
			}
		}
		return false;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		AbstractOMount abstracthorse;
		if (ageableMob instanceof ODonkey) {
			ODonkey donkey = (ODonkey) ageableMob;

			abstracthorse = EntityTypes.O_MULE_ENTITY.get().create(serverLevel);

			int overlayChance = this.random.nextInt(9);
			int selectedOverlay;

			if (overlayChance < 4) {
				selectedOverlay = this.getOverlayVariant();
			} else if (overlayChance < 8) {
				selectedOverlay = donkey.getOverlayVariant();
			} else {
				selectedOverlay = this.random.nextInt(OHorseMarkingLayer.Overlay.values().length);
			}

			((OMule) abstracthorse).setOverlayVariant(selectedOverlay);
			((OMule) abstracthorse).setVariant(random.nextInt(OMuleModel.Variant.values().length));

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

			int gender;
			gender = this.random.nextInt(OHorse.Gender.values().length);

			((OHorse) abstracthorse).setVariant(variant);
			((OHorse) abstracthorse).setOverlayVariant(overlay);
			((OHorse) abstracthorse).setBreed(breed);
			((OHorse) abstracthorse).setGender(gender);

			if (this.random.nextInt(4) == 0) {
				int randomJumpStrength = (int) Math.max(horse.generateRandomJumpStrength(), this.random.nextInt(10) + 10);
				((OHorse) abstracthorse).generateRandomJumpStrength();

				int betterSpeed = (int) Math.max(horse.getSpeed(), this.random.nextInt(10) + 20);
				((OHorse) abstracthorse).setSpeed(betterSpeed);

				int betterHealth = (int) Math.max(horse.getHealth(), this.random.nextInt(20) + 40);
				((OHorse) abstracthorse).setHealth(betterHealth);
			} else {
				((OHorse) abstracthorse).setSpeed(horse.getSpeed());
				((OHorse) abstracthorse).setHealth(horse.getHealth());
			}
		}

		this.setOffspringAttributes(ageableMob, abstracthorse);
		return abstracthorse;
	}

	public enum Mane {
		DEFAULT,
		BUTTONS,
		SHORT;

		public Mane next() {
			return Mane.values()[(this.ordinal() + 1) % Mane.values().length];
		}
	}

	public boolean hasDefaultMane() {
		return this.getManeType() == 0;
	}

	public boolean hasButtonMane() {
		return this.getManeType() == 1;
	}

	public boolean hasShortMane() {
		return this.getManeType() == 2;
	}

	public enum Tail {
		DEFAULT,
		SHORT,
		LONG,
		TUCKED;

		public Tail next() {
			return Tail.values()[(this.ordinal() + 1) % Tail.values().length];
		}
	}

	public boolean hasDefaultTail() {
		return this.getTailType() == 0;
	}

	public boolean hasLongTail() {
		return this.getTailType() == 1;
	}

	public boolean hasShortTail() {
		return this.getTailType() == 2;
	}

	public boolean hasTuckedTail() {
		return this.getTailType() == 3;
	}

	public static final EntityDataAccessor<Integer> MANE_TYPE = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);

	public int getManeType() {
		return this.entityData.get(MANE_TYPE);
	}

	public void setManeType(int mane) {
		this.entityData.set(MANE_TYPE, mane);
	}

	public static final EntityDataAccessor<Integer> TAIL_TYPE = SynchedEntityData.defineId(OHorse.class, EntityDataSerializers.INT);

	public int getTailType() {
		return this.entityData.get(TAIL_TYPE);
	}

	public void setTailType(int tail) {
		this.entityData.set(TAIL_TYPE, tail);
	}

}
