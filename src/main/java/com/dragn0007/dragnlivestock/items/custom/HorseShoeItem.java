package com.dragn0007.dragnlivestock.items.custom;

import net.minecraft.world.item.Item;

public class HorseShoeItem extends Item {
   private final int protection;

   public HorseShoeItem(int i, Properties properties) {
      super(properties);
      this.protection = i;
   }

   public int getProtection() {
      return this.protection;
   }
}