package com.dragn0007.dragnlivestock.entities.bee;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.util.BeeBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class OBee extends BeeBase implements NeutralMob, FlyingAnimal, IAnimatable {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public OBee(EntityType<? extends OBee> type, Level level) {
		super(type, level);
		this.noCulling = true;
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("flap", ILoopType.EDefaultLoopTypes.LOOP));
		}

		if (!event.isMoving() && !isOnGround()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_flap", ILoopType.EDefaultLoopTypes.LOOP));
		}

		if (isOnGround()) {
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

	// Generates the base texture
	public ResourceLocation getTextureLocation() {
		return OBeeModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}

	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OBee.class, EntityDataSerializers.INT);

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
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, 0);
	}

	public OBee getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return EntityTypes.O_BEE_ENTITY.get().create(serverLevel);
	}
}
