package com.dragn0007.dragnlivestock.entities.cow.mooshroom;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Random;

public class OMooshroom extends OCow implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public OMooshroom(EntityType<? extends OCow> type, Level level) {
        super(type, level);
    }

    public float getWalkTargetValue(BlockPos p_28933_, LevelReader p_28934_) {
        return p_28934_.getBlockState(p_28933_.below()).is(Blocks.MYCELIUM) ? 10.0F : p_28934_.getBrightness(p_28933_) - 0.5F;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.BOWL) && !this.isBaby() &&
                (!LivestockOverhaulCommonConfig.GENDERS_AFFECT_BIPRODUCTS.get() ||
                        (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BIPRODUCTS.get() && this.isFemale))) {

            player.playSound(SoundEvents.MOOSHROOM_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, Items.MUSHROOM_STEW.getDefaultInstance());
            player.setItemInHand(hand, itemstack1);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    // Generates the base texture
    public ResourceLocation getTextureLocation() {
        return OMooshroomModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
    }

    public ResourceLocation getOverlayLocation() {
        return OMooshroomMarkingLayer.Overlay.overlayFromOrdinal(getOverlayVariant()).resourceLocation;
    }

    public ResourceLocation getHornsLocation() {
        return OMooshroomHornLayer.HornOverlay.hornOverlayFromOrdinal(getHornVariant()).resourceLocation;
    }

    public ResourceLocation getMushroomsLocation() {
        return OMooshroomMushroomLayer.Overlay.overlayFromOrdinal(getMushroomVariant()).resourceLocation;
    }

    public ResourceLocation getUddersLocation() {
        return OMooshroomUdderLayer.Overlay.overlayFromOrdinal(getUdderVariant()).resourceLocation;
    }

    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(OMooshroom.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> OVERLAY = SynchedEntityData.defineId(OMooshroom.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> HORNS = SynchedEntityData.defineId(OMooshroom.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> MUSHROOMS = SynchedEntityData.defineId(OMooshroom.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> UDDERS = SynchedEntityData.defineId(OMooshroom.class, EntityDataSerializers.INT);

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }
    public int getOverlayVariant() {
        return this.entityData.get(OVERLAY);
    }
    public int getHornVariant() {
        return this.entityData.get(HORNS);
    }
    public int getMushroomVariant() {
        return this.entityData.get(MUSHROOMS);
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
    public void setMushroomVariant(int mushroomVariant) {
        this.entityData.set(MUSHROOMS, mushroomVariant);
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

        if (tag.contains("Mushrooms")) {
            setMushroomVariant(tag.getInt("Mushrooms"));
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

        tag.putInt("Mushrooms", getMushroomVariant());

        tag.putInt("Udders", getUdderVariant());
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if (data == null) {
            data = new AgeableMobGroupData(0.2F);
        }
        Random random = new Random();
        setVariant(random.nextInt(OMooshroomModel.Variant.values().length));
        setOverlayVariant(random.nextInt(OMooshroomMarkingLayer.Overlay.values().length));
        setHornVariant(random.nextInt(OMooshroomHornLayer.HornOverlay.values().length));
        setMushroomVariant(random.nextInt(OMooshroomMushroomLayer.Overlay.values().length));
        setUdderVariant(random.nextInt(OMooshroomUdderLayer.Overlay.values().length));

        return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(OVERLAY, 0);
        this.entityData.define(HORNS, 0);
        this.entityData.define(MUSHROOMS, 0);
        this.entityData.define(UDDERS, 0);
    }

    boolean isFemale = this.getUddersLocation().equals(OMooshroomUdderLayer.Overlay.FEMALE.resourceLocation);
    boolean isMale = this.getUddersLocation().equals(OMooshroomUdderLayer.Overlay.MALE.resourceLocation);

    public boolean canParent() {
        return !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    public boolean canMate(Animal animal) {
        if (animal == this) {
            return false;
        } else if (!(animal instanceof OMooshroom)) {
            return false;
        } else {
            OMooshroom partner = (OMooshroom) animal;

            if (!this.canParent() || !partner.canParent()) {
                return false;
            }

            if (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
                boolean partnerIsFemale = partner.getUddersLocation().equals(OMooshroomUdderLayer.Overlay.FEMALE.resourceLocation);
                boolean partnerIsMale = partner.getUddersLocation().equals(OMooshroomUdderLayer.Overlay.MALE.resourceLocation);

                boolean isFemale = this.getUddersLocation().equals(OMooshroomUdderLayer.Overlay.FEMALE.resourceLocation);
                boolean isMale = this.getUddersLocation().equals(OMooshroomUdderLayer.Overlay.MALE.resourceLocation);

                return (isFemale && partnerIsMale) || (isMale && partnerIsFemale);
            } else {
                return true;
            }
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        OMooshroom oMooshroom = (OMooshroom) ageableMob;
        if (ageableMob instanceof OMooshroom) {
            OMooshroom oMooshroom1 = (OMooshroom) ageableMob;
            oMooshroom = EntityTypes.O_MOOSHROOM_ENTITY.get().create(serverLevel);

            int i = this.random.nextInt(9);
            int variant;
            if (i < 4) {
                variant = this.getVariant();
            } else if (i < 8) {
                variant = oMooshroom1.getVariant();
            } else {
                variant = this.random.nextInt(OMooshroomModel.Variant.values().length);
            }

            int j = this.random.nextInt(5);
            int overlay;
            if (j < 2) {
                overlay = this.getOverlayVariant();
            } else if (j < 4) {
                overlay = oMooshroom1.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(OMooshroomMarkingLayer.Overlay.values().length);
            }

            int k = this.random.nextInt(5);
            int horns;
            if (k < 2) {
                horns = this.getHornVariant();
            } else if (k < 4) {
                horns = oMooshroom1.getHornVariant();
            } else {
                horns = this.random.nextInt(OMooshroomHornLayer.HornOverlay.values().length);
            }

            int l = this.random.nextInt(5);
            int mushrooms;
            if (l < 2) {
                mushrooms = this.getMushroomVariant();
            } else if (l < 4) {
                mushrooms = oMooshroom1.getMushroomVariant();
            } else {
                mushrooms = this.random.nextInt(OMooshroomMushroomLayer.Overlay.values().length);
            }

            int udders;
            udders = this.random.nextInt(OMooshroomUdderLayer.Overlay.values().length);

            oMooshroom.setVariant(variant);
            oMooshroom.setOverlayVariant(overlay);
            oMooshroom.setHornVariant(horns);
            oMooshroom.setMushroomVariant(mushrooms);
            oMooshroom.setUdderVariant(udders);
        }

        return oMooshroom;
    }
}
