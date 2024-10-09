package com.dragn0007.dragnlivestock.items.custom;

import com.dragn0007.dragnlivestock.items.LOItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class FishOilItem extends Item {

    public MobEffectInstance[] effectInstances;

    public FishOilItem(MobEffectInstance... effectInstances) {
        super(new Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(1).build()).stacksTo(16).tab(LOItemGroup.LIVESTOCK_OVERHAUL_GROUP));
        this.effectInstances = effectInstances;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerEntity, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, playerEntity, hand);
    }
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            for (int i = 0; i < effectInstances.length; i++) {
                pLivingEntity.addEffect(effectInstances[i]);
            }
            pLivingEntity.getItemInHand(InteractionHand.MAIN_HAND).shrink(0);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);

    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);

        for (MobEffectInstance effectInstance : effectInstances) {
            String effectName = effectInstance.getEffect().getDisplayName().getString();
            String amplifier = String.format(" Level %d", effectInstance.getAmplifier() + 1);
            String text = effectName + amplifier;

            tooltip.add(new TextComponent(text).withStyle(ChatFormatting.GOLD));
        }
    }
}

