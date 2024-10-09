package com.dragn0007.dragnlivestock.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrandTagItem extends Item {
   public BrandTagItem(Item.Properties properties) {
      super(properties);
   }

   public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
      if (itemStack.hasCustomHoverName() && !(entity instanceof Player)) {
         if (!player.level.isClientSide && entity.isAlive()) {
            entity.setCustomName(itemStack.getHoverName());
            if (entity instanceof Mob) {
               ((Mob)entity).setPersistenceRequired();
            }

            itemStack.shrink(1);
         }

         return InteractionResult.sidedSuccess(player.level.isClientSide);
      } else {
         return InteractionResult.PASS;
      }
   }

   @Override
   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(new TranslatableComponent("tooltip.dragnlivestock.brand_tag.tooltip").withStyle(ChatFormatting.GRAY));
   }
}