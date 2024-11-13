package com.dragn0007.dragnlivestock.entities.rabbit;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
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
import java.util.Set;

public class ORabbit extends TamableAnimal implements IAnimatable {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public ORabbit(EntityType<? extends ORabbit> type, Level level) {
		super(type, level);
	}

	@Override
	public Vec3 getLeashOffset() {
		return new Vec3(0D, (double)this.getEyeHeight() * 0.6F, (double)(this.getBbWidth() * 0.6F));
		//              ^ Side offset                      ^ Height offset                   ^ Length offset
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 3.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.16F);
	}

	public static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.MELON_SLICE, Items.APPLE, Items.BEETROOT, Items.GOLDEN_CARROT, Blocks.DANDELION);

	public void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.8F));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(4, new ORabbit.RabbitAvoidEntityGoal(this, Player.class, 8.0F, 2.2D, 2.2D));
		this.goalSelector.addGoal(4, new ORabbit.RabbitAvoidEntityGoal<>(this, Wolf.class, 10.0F, 2.2D, 2.2D));
		this.goalSelector.addGoal(4, new ORabbit.RabbitAvoidEntityGoal<>(this, Monster.class, 4.0F, 2.2D, 2.2D));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(5, new ORabbit.RaidGardenGoal(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
	}

	static class RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
		public final ORabbit rabbit;

		public RabbitAvoidEntityGoal(ORabbit oRabbit, Class<T> tClass, float v, double v1, double v2) {
			super(oRabbit, tClass, v, v1, v2);
			this.rabbit = oRabbit;
		}

		@Override
		public boolean canUse() {
			if (rabbit.isTame()) {
				return false;
			}
			return super.canUse();
		}
	}

	private static final Set<Item> TAME_FOOD = Sets.newHashSet(Items.CARROT, Items.MELON_SLICE, Items.APPLE, Items.BEETROOT, Items.GOLDEN_CARROT);

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();

		if (itemstack.is(LOItems.GENDER_TEST_STRIP.get()) && this.isFemale()) {
			player.playSound(SoundEvents.BEEHIVE_EXIT, 1.0F, 1.0F);
			ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, LOItems.FEMALE_GENDER_TEST_STRIP.get().getDefaultInstance());
			player.setItemInHand(hand, itemstack1);
			return InteractionResult.SUCCESS;
		}

		if (itemstack.is(LOItems.GENDER_TEST_STRIP.get()) && this.isMale()) {
			player.playSound(SoundEvents.BEEHIVE_EXIT, 1.0F, 1.0F);
			ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, LOItems.MALE_GENDER_TEST_STRIP.get().getDefaultInstance());
			player.setItemInHand(hand, itemstack1);
			return InteractionResult.SUCCESS;
		}

		if (this.level.isClientSide) {
			boolean flag = this.isOwnedBy(player) || this.isTame() || TAME_FOOD.contains(itemstack.getItem()) && !this.isTame();
			return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else {
			if (this.isTame()) {
				if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
					this.heal((float)itemstack.getFoodProperties(this).getNutrition());
					if (!player.getAbilities().instabuild) {
						itemstack.shrink(1);
					}

					this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
					return InteractionResult.SUCCESS;
				}

				if (!(item instanceof DyeItem)) {
					InteractionResult interactionresult = super.mobInteract(player, hand);
					if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
						this.setOrderedToSit(!this.isOrderedToSit());
						this.jumping = false;
						this.navigation.stop();
						this.setTarget((LivingEntity)null);
						return InteractionResult.SUCCESS;
					}

					return interactionresult;
				}

			} else if (TAME_FOOD.contains(itemstack.getItem())) {
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}

				if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
					this.tame(player);
					this.navigation.stop();
					this.setTarget((LivingEntity)null);
					this.setOrderedToSit(true);
					this.level.broadcastEntityEvent(this, (byte)7);
				} else {
					this.level.broadcastEntityEvent(this, (byte)6);
				}

				return InteractionResult.SUCCESS;
			}

			return super.mobInteract(player, hand);
		}
	}

	@Override
	public float getStepHeight() {
		return 1F;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double currentSpeed = this.getDeltaMovement().lengthSqr();
		double speedThreshold = 0.01;

		if (event.isMoving()) {
			if (currentSpeed > speedThreshold) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(1.8);
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(1.5);
			}
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
	}

	int moreCarrotTicks;

	boolean wantsMoreFood() {
		return this.moreCarrotTicks == 0;
	}

	static class RaidGardenGoal extends MoveToBlockGoal {
		public final ORabbit rabbit;
		public boolean wantsToRaid;
		public boolean canRaid;

		public RaidGardenGoal(ORabbit p_29782_) {
			super(p_29782_, (double)0.7F, 16);
			this.rabbit = p_29782_;
		}

		public boolean canUse() {
			if (this.nextStartTick <= 0) {
				if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.rabbit.level, this.rabbit)) {
					return false;
				}

				this.canRaid = false;
				this.wantsToRaid = this.rabbit.wantsMoreFood();
				this.wantsToRaid = true;
			}

			return super.canUse();
		}

		public boolean canContinueToUse() {
			return this.canRaid && super.canContinueToUse();
		}

		public void tick() {
			super.tick();
			this.rabbit.getLookControl().setLookAt((double)this.blockPos.getX() + 0.5D, (double)(this.blockPos.getY() + 1), (double)this.blockPos.getZ() + 0.5D, 10.0F, (float)this.rabbit.getMaxHeadXRot());
			if (this.isReachedTarget()) {
				Level level = this.rabbit.level;
				BlockPos blockpos = this.blockPos.above();
				BlockState blockstate = level.getBlockState(blockpos);
				Block block = blockstate.getBlock();
				if (this.canRaid && block instanceof CarrotBlock) {
					int i = blockstate.getValue(CarrotBlock.AGE);
					if (i == 0) {
						level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
						level.destroyBlock(blockpos, true, this.rabbit);
					} else {
						level.setBlock(blockpos, blockstate.setValue(CarrotBlock.AGE, Integer.valueOf(i - 1)), 2);
						level.levelEvent(2001, blockpos, Block.getId(blockstate));
					}

					this.rabbit.moreCarrotTicks = 40;
				}

				this.canRaid = false;
				this.nextStartTick = 10;
			}

		}

		public boolean isValidTarget(LevelReader p_29785_, BlockPos p_29786_) {
			BlockState blockstate = p_29785_.getBlockState(p_29786_);
			if (blockstate.is(Blocks.FARMLAND) && this.wantsToRaid && !this.canRaid) {
				blockstate = p_29785_.getBlockState(p_29786_.above());
				if (blockstate.getBlock() instanceof CarrotBlock && ((CarrotBlock)blockstate.getBlock()).isMaxAge(blockstate)) {
					this.canRaid = true;
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return SoundEvents.RABBIT_AMBIENT;
	}

	public SoundEvent getDeathSound() {
		super.getDeathSound();
		return SoundEvents.RABBIT_DEATH;
	}

	public SoundEvent getHurtSound(DamageSource p_30720_) {
		super.getHurtSound(p_30720_);
		return SoundEvents.RABBIT_HURT;
	}

	public void playStepSound(BlockPos p_28254_, BlockState p_28255_) {
		this.playSound(SoundEvents.RABBIT_JUMP, 0.15F, 1.0F);
	}

	public boolean causeFallDamage(float p_148875_, float p_148876_, DamageSource p_148877_) {
		return false;
	}

	public boolean isFood(ItemStack p_28271_) {
		return FOOD_ITEMS.test(p_28271_);
	}

	// Generates the base texture
	public static final EntityDataAccessor<ResourceLocation> VARIANT_TEXTURE = SynchedEntityData.defineId(ORabbit.class, LivestockOverhaul.RESOURCE_LOCATION);
	public static final EntityDataAccessor<ResourceLocation> OVERLAY_TEXTURE = SynchedEntityData.defineId(ORabbit.class, LivestockOverhaul.RESOURCE_LOCATION);

	public ResourceLocation getTextureLocation() {
		return this.entityData.get(VARIANT_TEXTURE);
	}

	public ResourceLocation getOverlayLocation() {
		return this.entityData.get(OVERLAY_TEXTURE);
	}

	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(ORabbit.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(ORabbit.class, EntityDataSerializers.INT);

	public int getVariant() {
		return this.entityData.get(VARIANT);
	}
	public int getOverlayVariant() {
		return this.entityData.get(OVERLAY);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
		this.entityData.set(VARIANT_TEXTURE, ORabbitModel.Variant.variantFromOrdinal(variant).resourceLocation);
	}
	public void setOverlayVariant(int overlayVariant) {
		this.entityData.set(OVERLAY, overlayVariant);
		this.entityData.set(OVERLAY_TEXTURE, ORabbitMarkingLayer.Overlay.overlayFromOrdinal(overlayVariant).resourceLocation);
	}

	public void setVariantTexture(String variant) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(variant);
		if (resourceLocation == null) {
			resourceLocation = ORabbitModel.Variant.BLACK.resourceLocation;
		}
		this.entityData.set(VARIANT_TEXTURE, resourceLocation);
	}

	public void setOverlayVariantTexture(String variant) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(variant);
		if (resourceLocation == null) {
			resourceLocation = ORabbitMarkingLayer.Overlay.NONE.resourceLocation;
		}
		this.entityData.set(OVERLAY_TEXTURE, resourceLocation);
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

		if (tag.contains("Variant_Texture")) {
			this.setVariantTexture(tag.getString("Variant_Texture"));
		}

		if (tag.contains("Overlay_Texture")) {
			this.setOverlayVariantTexture(tag.getString("Overlay_Texture"));
		}

		if (tag.contains("Gender")) {
			this.setGender(tag.getInt("Gender"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());

		tag.putInt("Overlay", getOverlayVariant());

		tag.putString("Variant_Texture", this.getTextureLocation().toString());

		tag.putString("Overlay_Texture", this.getOverlayLocation().toString());

		tag.putInt("Gender", this.getGender());
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (data == null) {
			data = new AgeableMobGroupData(0.2F);
		}
		Random random = new Random();
		setVariant(random.nextInt(ORabbitModel.Variant.values().length));
		setOverlayVariant(random.nextInt(ORabbitMarkingLayer.Overlay.values().length));
		this.setGender(random.nextInt(ORabbit.Gender.values().length));

		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(VARIANT_TEXTURE, ORabbitModel.Variant.BLACK.resourceLocation);
		this.entityData.define(OVERLAY_TEXTURE, ORabbitMarkingLayer.Overlay.NONE.resourceLocation);
		this.entityData.define(GENDER, 0);
	}

	public enum Gender {
		FEMALE,
		MALE
	}

	public boolean isFemale() {
		return this.getGender() == 0;
	}

	public boolean isMale() {
		return this.getGender() == 1;
	}

	public static final EntityDataAccessor<Integer> GENDER = SynchedEntityData.defineId(ORabbit.class, EntityDataSerializers.INT);

	public int getGender() {
		return this.entityData.get(GENDER);
	}

	public void setGender(int gender) {
		this.entityData.set(GENDER, gender);
	}

	public boolean canParent() {
		return !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
	}

	public boolean canMate(Animal animal) {
		if (animal == this) {
			return false;
		} else if (!(animal instanceof ORabbit)) {
			return false;
		} else {
			if (!LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
				return this.canParent() && ((ORabbit) animal).canParent();
			} else {
				ORabbit partner = (ORabbit) animal;
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
		ORabbit oRabbit = (ORabbit) ageableMob;
		if (ageableMob instanceof ORabbit) {
			ORabbit oRabbit1 = (ORabbit) ageableMob;
			oRabbit = EntityTypes.O_RABBIT_ENTITY.get().create(serverLevel);

			int i = this.random.nextInt(9);
			int variant;
			if (i < 4) {
				variant = this.getVariant();
			} else if (i < 8) {
				variant = oRabbit1.getVariant();
			} else {
				variant = this.random.nextInt(ORabbitModel.Variant.values().length);
			}

			int j = this.random.nextInt(5);
			int overlay;
			if (j < 2) {
				overlay = this.getOverlayVariant();
			} else if (j < 4) {
				overlay = oRabbit1.getOverlayVariant();
			} else {
				overlay = this.random.nextInt(ORabbitMarkingLayer.Overlay.values().length);
			}

			((ORabbit) oRabbit).setVariant(variant);
			((ORabbit) oRabbit).setOverlayVariant(overlay);
		}

		return oRabbit;
	}

	public void positionRider(Entity p_28269_) {
		super.positionRider(p_28269_);
		float f = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
		float f1 = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
		float f2 = 0.1F;
		float f3 = 0.0F;
		p_28269_.setPos(this.getX() + (double)(0.1F * f), this.getY(0.5D) + p_28269_.getMyRidingOffset() + 0.0D, this.getZ() - (double)(0.1F * f1));
		if (p_28269_ instanceof LivingEntity) {
			((LivingEntity)p_28269_).yBodyRot = this.yBodyRot;
		}

	}

}
