package com.dragn0007.dragnlivestock.entities.sheep;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.chicken.OChickenMarkingLayer;
import com.dragn0007.dragnlivestock.entities.chicken.OChickenModel;
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
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class OSheep extends Animal implements Shearable, net.minecraftforge.common.IForgeShearable, IAnimatable {
	private static final int EAT_ANIMATION_TICKS = 40;

	//TODO

	private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(OSheep.class, EntityDataSerializers.BYTE);
	private static final Map<DyeItem, ResourceLocation> COLOR_MAP = new HashMap<>() {{
		put(DyeItem.byColor(DyeColor.BLACK), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/black.png"));
		put(DyeItem.byColor(DyeColor.BLUE), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/blue.png"));
		put(DyeItem.byColor(DyeColor.BROWN), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/brown.png"));
		put(DyeItem.byColor(DyeColor.CYAN), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/cyan.png"));
		put(DyeItem.byColor(DyeColor.GRAY), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/dark_grey.png"));
		put(DyeItem.byColor(DyeColor.LIGHT_BLUE), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/light_blue.png"));
		put(DyeItem.byColor(DyeColor.LIGHT_GRAY), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/light_grey.png"));
		put(DyeItem.byColor(DyeColor.LIME), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/lime_green.png"));
		put(DyeItem.byColor(DyeColor.MAGENTA), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/magenta.png"));
		put(DyeItem.byColor(DyeColor.ORANGE), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/orange.png"));
		put(DyeItem.byColor(DyeColor.PINK), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/pink.png"));
		put(DyeItem.byColor(DyeColor.PURPLE), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/purple.png"));
		put(DyeItem.byColor(DyeColor.RED), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/red.png"));
		put(DyeItem.byColor(DyeColor.WHITE), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/white.png"));
		put(DyeItem.byColor(DyeColor.GREEN), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/green.png"));
		put(DyeItem.byColor(DyeColor.YELLOW), new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/sheep/wool/yellow.png"));
	}};

	private static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR = Maps.<DyeColor, float[]>newEnumMap(Arrays.stream(DyeColor.values()).collect(Collectors.toMap((p_29868_) -> {
		return p_29868_;
	}, OSheep::createSheepColor)));

	public static float[] getColorArray(DyeColor p_29830_) {
		return COLORARRAY_BY_COLOR.get(p_29830_);
	}

	private int eatAnimationTick;
	private EatBlockGoal eatBlockGoal;

	private static float[] createSheepColor(DyeColor p_29866_) {
		if (p_29866_ == DyeColor.WHITE) {
			return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
		} else {
			float[] afloat = p_29866_.getTextureDiffuseColors();
			float f = 0.75F;
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

	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT);

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.8F));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, this.eatBlockGoal);
	}

	protected void customServerAiStep() {
		this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
		super.customServerAiStep();
	}

	public void handleEntityEvent(byte p_29814_) {
		if (p_29814_ == 10) {
			this.eatAnimationTick = 40;
		} else {
			super.handleEntityEvent(p_29814_);
		}

	}

	public InteractionResult mobInteract(Player p_29853_, InteractionHand p_29854_) {
		ItemStack itemstack = p_29853_.getItemInHand(p_29854_);
		if (false && itemstack.getItem() == Items.SHEARS) { //Forge: Moved to onSheared
			if (!this.level.isClientSide && this.readyForShearing()) {
				this.shear(SoundSource.PLAYERS);
				this.gameEvent(GameEvent.SHEAR, p_29853_);
				itemstack.hurtAndBreak(1, p_29853_, (p_29822_) -> {
					p_29822_.broadcastBreakEvent(p_29854_);
				});
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		} else {
			return super.mobInteract(p_29853_, p_29854_);
		}
	}

	public void shear(SoundSource p_29819_) {
		this.level.playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, p_29819_, 1.0F, 1.0F);
		this.setSheared(true);
		int i = 1 + this.random.nextInt(3);

		for(int j = 0; j < i; ++j) {
			ItemEntity itementity = this.spawnAtLocation((ItemLike) COLOR_MAP.get(this.getColor()), 1);
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

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		double currentSpeed = this.getDeltaMovement().lengthSqr();
		double speedThreshold = 0.01;

		if (!isOnGround()) {
			event.getController().setAnimation(
					new AnimationBuilder().addAnimation("flap", ILoopType.EDefaultLoopTypes.LOOP));

			event.getController().setAnimationSpeed(1.0);

		} else if (event.isMoving()) {
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

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public ResourceLocation getDefaultLootTable() {
		if (this.isSheared()) {
			return this.getType().getDefaultLootTable();
		} else {
			switch(this.getColor()) {
				case WHITE:
				default:
					return BuiltInLootTables.SHEEP_WHITE;
				case ORANGE:
					return BuiltInLootTables.SHEEP_ORANGE;
				case MAGENTA:
					return BuiltInLootTables.SHEEP_MAGENTA;
				case LIGHT_BLUE:
					return BuiltInLootTables.SHEEP_LIGHT_BLUE;
				case YELLOW:
					return BuiltInLootTables.SHEEP_YELLOW;
				case LIME:
					return BuiltInLootTables.SHEEP_LIME;
				case PINK:
					return BuiltInLootTables.SHEEP_PINK;
				case GRAY:
					return BuiltInLootTables.SHEEP_GRAY;
				case LIGHT_GRAY:
					return BuiltInLootTables.SHEEP_LIGHT_GRAY;
				case CYAN:
					return BuiltInLootTables.SHEEP_CYAN;
				case PURPLE:
					return BuiltInLootTables.SHEEP_PURPLE;
				case BLUE:
					return BuiltInLootTables.SHEEP_BLUE;
				case BROWN:
					return BuiltInLootTables.SHEEP_BROWN;
				case GREEN:
					return BuiltInLootTables.SHEEP_GREEN;
				case RED:
					return BuiltInLootTables.SHEEP_RED;
				case BLACK:
					return BuiltInLootTables.SHEEP_BLACK;
			}
		}
	}

	public void aiStep() {
		super.aiStep();

		Vec3 vec3 = this.getDeltaMovement();
		if (!this.onGround && vec3.y < 0.0D) {
			this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
		}

		if (this.level.isClientSide) {
			this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
		}
	}

	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return SoundEvents.SHEEP_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		super.getDeathSound();
		return SoundEvents.SHEEP_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource p_30720_) {
		super.getHurtSound(p_30720_);
		return SoundEvents.SHEEP_HURT;
	}

	protected void playStepSound(BlockPos p_28254_, BlockState p_28255_) {
		this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
	}

	public boolean causeFallDamage(float p_148875_, float p_148876_, DamageSource p_148877_) {
		return false;
	}

	public boolean isFood(ItemStack p_28271_) {
		return FOOD_ITEMS.test(p_28271_);
	}

	// Generates the base texture
	public ResourceLocation getTextureLocation() {
		return OChickenModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OSheep.class, EntityDataSerializers.INT);

	public int getVariant() {
		return this.entityData.get(VARIANT);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("Variant")) {
			setVariant(tag.getInt("Variant"));
		}

		this.setSheared(tag.getBoolean("Sheared"));
		this.setColor(DyeColor.byId(tag.getByte("Color")));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());
		tag.putBoolean("Sheared", this.isSheared());
		tag.putByte("Color", (byte)this.getColor().getId());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
		this.entityData.define(DATA_WOOL_ID, (byte)0);
	}

	protected boolean canParent() {
		return !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
	}

	public boolean canMate(Animal animal) {
			return this.canParent() && ((OSheep) animal).canParent();
	}

	public OSheep getBreedOffspring(ServerLevel p_149044_, AgeableMob p_149045_) {
		OSheep sheep = (OSheep)p_149045_;
		OSheep sheep1 = EntityTypes.O_SHEEP_ENTITY.get().create(p_149044_);
		return sheep1;
	}

	public DyeColor getColor() {
		return DyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 15);
	}

	public void setColor(DyeColor p_29856_) {
		byte b0 = this.entityData.get(DATA_WOOL_ID);
		this.entityData.set(DATA_WOOL_ID, (byte)(b0 & 240 | p_29856_.getId() & 15));
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
	public static DyeColor getRandomSheepColor(Random p_29843_) {
		int i = p_29843_.nextInt(100);
		if (i < 5) {
			return DyeColor.BLACK;
		} else if (i < 10) {
			return DyeColor.GRAY;
		} else if (i < 15) {
			return DyeColor.LIGHT_GRAY;
		} else if (i < 18) {
			return DyeColor.BROWN;
		} else {
			return p_29843_.nextInt(500) == 0 ? DyeColor.PINK : DyeColor.WHITE;
		}
	}

	public void ate() {
		this.setSheared(false);
		if (this.isBaby()) {
			this.ageUp(60);
		}

	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_29835_, DifficultyInstance p_29836_, MobSpawnType p_29837_, @Nullable SpawnGroupData p_29838_, @Nullable CompoundTag p_29839_) {
		this.setColor(getRandomSheepColor(p_29835_.getRandom()));
		return super.finalizeSpawn(p_29835_, p_29836_, p_29837_, p_29838_, p_29839_);
	}

	@Override
	public boolean isShearable(@javax.annotation.Nonnull ItemStack item, Level world, BlockPos pos) {
		return readyForShearing();
	}

	@javax.annotation.Nonnull
	@Override
	public java.util.List<ItemStack> onSheared(@Nullable Player player, @javax.annotation.Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
		world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
		this.gameEvent(GameEvent.SHEAR, player);
		if (!world.isClientSide) {
			this.setSheared(true);
			int i = 1 + this.random.nextInt(3);

			java.util.List<ItemStack> items = new java.util.ArrayList<>();
			for (int j = 0; j < i; ++j) {
				items.add(new ItemStack((ItemLike) COLOR_MAP.get(this.getColor())));
			}
			return items;
		}
		return java.util.Collections.emptyList();
	}
}
