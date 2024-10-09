package com.dragn0007.dragnlivestock.entities.unicorn;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Random;

public class OverworldUnicorn extends OHorse implements IAnimatable {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public OverworldUnicorn(EntityType<? extends OverworldUnicorn> type, Level level) {
		super(type, level);
		this.noCulling = true;
		this.xpReward = 50;
	}

	public static AttributeSupplier.Builder createBaseHorseAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.JUMP_STRENGTH)
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.250F)
				.add(Attributes.ATTACK_DAMAGE, 4D)
				.add(Attributes.ATTACK_KNOCKBACK, 2F);
	}

	@Override
	protected void randomizeAttributes() {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.generateRandomMaxHealth());
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed());
		this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
	}

	@Override
	public float generateRandomMaxHealth() {
		return 24.0F + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
	}

	@Override
	public double generateRandomJumpStrength() {
		return (double)0.8F + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D;
	}

	@Override
	public double generateRandomSpeed() {
		return ((double)0.65F + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.7D));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, true));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 0.0F));

		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, 15.0F, 1.8F, 1.8F, livingEntity
				-> livingEntity instanceof Wolf
		));
	}

	@Override
	public boolean hurt(DamageSource damageSource, float v) {
		if(damageSource.isMagic() || damageSource.isExplosion()) {
			return false;
		}
		return super.hurt(damageSource, v);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.level.addParticle(ParticleTypes.MYCELIUM, this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
	}

	@Override
	public boolean causeFallDamage(float f1, float f2, DamageSource damageSource) {
		return false;
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
			double offsetX = 0;
			double offsetY = 1.4;
			double offsetZ = -0.2;

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
	public void setVariant(int variant) {
		this.entityData.set(VARIANT_TEXTURE, OverworldUnicornModel.Variant.variantFromOrdinal(variant).resourceLocation);
		this.entityData.set(VARIANT, variant);
	}

	@Override
	public void setOverlayVariant(int variant) {
		this.entityData.set(OVERLAY_TEXTURE, OverworldUnicornHornLayer.Overlay.overlayFromOrdinal(variant).resourceLocation);
		this.entityData.set(OVERLAY, variant);
	}

	@Override
	public void setVariantTexture(String variant) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(variant);
		if (resourceLocation == null) {
			resourceLocation = OverworldUnicornModel.Variant.OVERWORLD.resourceLocation;
		}
		this.entityData.set(VARIANT_TEXTURE, resourceLocation);
	}

	@Override
	public void setOverlayVariantTexture(String variant) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(variant);
		if (resourceLocation == null) {
			resourceLocation = OverworldUnicornHornLayer.Overlay.NONE.resourceLocation;
		}
		this.entityData.set(OVERLAY_TEXTURE, resourceLocation);
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (data == null) {
			data = new AgeableMobGroupData(0.2F);
		}
		Random random = new Random();
		setVariant(random.nextInt(OverworldUnicornModel.Variant.values().length));
		setOverlayVariant(random.nextInt(OverworldUnicornHornLayer.Overlay.values().length));

		this.randomizeAttributes();
		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}

	@Override
	public boolean canMate(Animal animal) {
		return false;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return null;
	}

}
