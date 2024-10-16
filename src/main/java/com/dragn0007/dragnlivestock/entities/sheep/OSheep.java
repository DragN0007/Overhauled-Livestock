package com.dragn0007.dragnlivestock.entities.sheep;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OSheep extends Animal implements Shearable, net.minecraftforge.common.IForgeShearable, IAnimatable {

	public static final int EAT_ANIMATION_TICKS = 40;

	public static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(OSheep.class, EntityDataSerializers.BYTE);

	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OSheep.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OSheep.class, EntityDataSerializers.INT);

	public static final Map<DyeColor, OSheepWoolLayer.Overlay> OVERLAY_MAP = new HashMap<>() {{
		put(DyeColor.WHITE, OSheepWoolLayer.Overlay.WHITE);
	}};

	public static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (p_29841_) -> {
		p_29841_.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
	});

	public static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR = Maps.<DyeColor, float[]>newEnumMap(Arrays.stream(DyeColor.values()).collect(Collectors.toMap((p_29868_) -> p_29868_, OSheep::createSheepColor)));

	public static float[] getColorArray(DyeColor p_29830_) {
		return COLORARRAY_BY_COLOR.get(p_29830_);
	}

	public static float[] createSheepColor(DyeColor p_29866_) {
		if (p_29866_ == DyeColor.WHITE) {
			return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
		} else {
			float[] afloat = p_29866_.getTextureDiffuseColors();
			return new float[]{afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
		}
	}

	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public OSheep(EntityType<? extends OSheep> type, Level level) {
		super(type, level);
		this.noCulling = true;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 8.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.18F);
	}

	public static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT);

	public void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.8F));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(8, new EatGrassGoal(this));
	}

	public OSheep leader;
	public int herdSize = 1;

	public boolean isFollower() {
		return this.leader != null && this.leader.isAlive();
	}

	public OSheep startFollowing(OSheep cow) {
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
			List<? extends OSheep> list = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
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

	public void addFollowers(Stream<? extends OSheep> p_27534_) {
		p_27534_.limit((long)(this.getMaxHerdSize() - this.herdSize)).filter((cow) -> {
			return cow != this;
		}).forEach((cow) -> {
			cow.startFollowing(this);
		});
	}

	public class EatGrassGoal extends Goal {
		public final OSheep sheep;
		public int eatAnimationTick;

		public EatGrassGoal(OSheep sheep) {
			this.sheep = sheep;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return sheep.isOnGround() && sheep.getRandom().nextInt(100) < 10;
		}

		@Override
		public void start() {
			eatAnimationTick = 0;
			sheep.getNavigation().stop();
		}

		@Override
		public void tick() {
			eatAnimationTick++;
			if (eatAnimationTick >= EAT_ANIMATION_TICKS) {
				sheep.eatGrass();
				eatAnimationTick = 0;
			}
		}

		@Override
		public boolean canContinueToUse() {
			return eatAnimationTick < EAT_ANIMATION_TICKS;
		}
	}

	public void eatGrass() {
		if (this.isBaby()) {
			this.ageUp(60);
		}
		this.setSheared(false);
	}

	public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
		return readyForShearing();
	}

	@Override
	@Nonnull
	public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
		world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
		this.gameEvent(GameEvent.SHEAR, player);
		if (!world.isClientSide) {
			this.setSheared(true);
			int i = 1 + this.random.nextInt(3);

			List<ItemStack> items = new ArrayList<>();
			for (int j = 0; j < i; ++j) {
				items.add(new ItemStack(ITEM_BY_DYE.get(this.getColor())));
			}
			return items;
		}
		return Collections.emptyList();
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);

		if (itemstack.getItem() == Items.SHEARS) {
			if (!this.level.isClientSide && this.readyForShearing()) {
				List<ItemStack> drops = this.onSheared(player, itemstack, this.level, this.blockPosition(), 0);
				for (ItemStack drop : drops) {
					this.spawnAtLocation(drop);
				}
				itemstack.hurtAndBreak(1, player, (p_29923_) -> {
					p_29923_.broadcastBreakEvent(hand);
				});
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}

		if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
			ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, LOItems.SHEEP_MILK_BUCKET.get().getDefaultInstance());
			player.setItemInHand(hand, itemstack1);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		} else {
			return super.mobInteract(player, hand);
		}
	}

	public void shear(SoundSource p_29819_) {
		this.level.playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, p_29819_, 1.0F, 1.0F);
		this.setSheared(true);
		int i = 1 + this.random.nextInt(3);

		for(int j = 0; j < i; ++j) {
			ItemEntity itementity = this.spawnAtLocation(ITEM_BY_DYE.get(this.getColor()), 1);
			if (itementity != null) {
				itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
			}
		}
	}

	public boolean readyForShearing() {
		return this.isAlive() && !this.isSheared() && !this.isBaby();
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
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
				event.getController().setAnimationSpeed(1.2);
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

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public SoundEvent getAmbientSound() {
		return SoundEvents.SHEEP_AMBIENT;
	}

	public SoundEvent getDeathSound() {
		return SoundEvents.SHEEP_DEATH;
	}

	public SoundEvent getHurtSound(DamageSource p_30720_) {
		return SoundEvents.SHEEP_HURT;
	}

	public void playStepSound(BlockPos p_28254_, BlockState p_28255_) {
		this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
	}

	public boolean isFood(ItemStack p_28271_) {
		return FOOD_ITEMS.test(p_28271_);
	}

	public ResourceLocation getTextureLocation() {
		return OSheepModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	public ResourceLocation getOverlayLocation() {
		return OSheepWoolLayer.Overlay.overlayFromOrdinal(getOverlayVariant()).resourceLocation;
	}

	public int getVariant() {
		return this.entityData.get(VARIANT);
	}

	public int getOverlayVariant() {
		return this.entityData.get(OVERLAY);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}

	public void setOverlayVariant(int overlayVariant) {
		this.entityData.set(OVERLAY, overlayVariant);
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
		this.setSheared(tag.getBoolean("Sheared"));
		this.setColor(DyeColor.byId(tag.getByte("Color")));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());
		tag.putInt("Overlay", getOverlayVariant());
		tag.putBoolean("Sheared", this.isSheared());
		tag.putByte("Color", (byte)this.getColor().getId());
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(OVERLAY, 0);
		this.entityData.define(DATA_WOOL_ID, (byte)0);
	}

	public boolean canParent() {
		return !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
	}

	public boolean canMate(Animal animal) {
		return this.canParent() && ((OSheep) animal).canParent();
	}

	public OSheep getBreedOffspring(ServerLevel p_149044_, AgeableMob p_149045_) {
		return EntityTypes.O_SHEEP_ENTITY.get().create(p_149044_);
	}

	public DyeColor getColor() {
		return DyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 15);
	}

	public void setColor(DyeColor color) {
		byte b0 = this.entityData.get(DATA_WOOL_ID);
		this.entityData.set(DATA_WOOL_ID, (byte)(b0 & 240 | color.getId() & 15));
	}

	public boolean isSheared() {
		return (this.entityData.get(DATA_WOOL_ID) & 16) != 0;
	}

	public void setSheared(boolean p_29879_) {
		byte b0 = this.entityData.get(DATA_WOOL_ID);
		if (p_29879_) {
			this.entityData.set(DATA_WOOL_ID, (byte)(b0 | 16));
		} else {
			this.entityData.set(DATA_WOOL_ID, (byte)(b0 & -17));
		}
	}

	public static DyeColor getRandomSheepColor(Random random) {
		int i = random.nextInt(100);
		return DyeColor.WHITE;
	}

	public void ate() {
		this.setSheared(false);
		if (this.isBaby()) {
			this.ageUp(60);
		}
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance p_29836_, MobSpawnType p_29837_, @Nullable SpawnGroupData p_29838_, @Nullable CompoundTag p_29839_) {
		Random random = new Random();
		DyeColor randomColor = getRandomSheepColor(random);
		this.setColor(randomColor);
		setVariant(random.nextInt(OSheepModel.Variant.values().length));
//		setOverlayVariant(random.nextInt(OSheepWoolLayer.Overlay.values().length));
		return super.finalizeSpawn(serverLevelAccessor, p_29836_, p_29837_, p_29838_, p_29839_);
	}
}
