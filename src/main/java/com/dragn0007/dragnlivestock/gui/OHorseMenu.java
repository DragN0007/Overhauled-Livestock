package com.dragn0007.dragnlivestock.gui;

import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.IContainerFactory;

public class OHorseMenu extends AbstractContainerMenu {

    public Container container;
    public AbstractOHorse oHorse;

    public OHorseMenu(int containerId, Inventory inventory, Container container, AbstractOHorse abstractOHorse) {
        super(LOMenuTypes.O_HORSE_MENU.get(), containerId);
        this.container = container;
        this.oHorse = abstractOHorse;
    int i = 3;
      container.startOpen(inventory.player);
    int j = -18;
      this.addSlot(new Slot(container, 0, 8, 18) {
        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.is(Items.SADDLE) && !this.hasItem() && abstractOHorse.isSaddleable();
        }

        public boolean isActive() {
            return abstractOHorse.isSaddleable();
        }
    });

      this.addSlot(new Slot(container, 1, 8, 36) {
        public boolean mayPlace(ItemStack p_39690_) {
            return abstractOHorse.isArmor(p_39690_);
        }

        public boolean isActive() {
            return abstractOHorse.canWearArmor();
        }

        public int getMaxStackSize() {
            return 1;
        }
    });

      if (abstractOHorse.isChested()) {
        for(int k = 0; k < 3; ++k) {
            for(int l = 0; l < abstractOHorse.getInventoryColumns(); ++l) {
                this.addSlot(new Slot(container, 2 + l + k * abstractOHorse.getInventoryColumns(), 80 + l * 18, 18 + k * 18));
            }
        }
    }

      for(int i1 = 0; i1 < 3; ++i1) {
        for(int k1 = 0; k1 < 9; ++k1) {
            this.addSlot(new Slot(inventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
        }
    }

      for(int j1 = 0; j1 < 9; ++j1) {
        this.addSlot(new Slot(inventory, j1, 8 + j1 * 18, 142));
    }

}

    public boolean stillValid(Player p_39661_) {
        return !this.oHorse.hasInventoryChanged(this.container) && this.container.stillValid(p_39661_) && this.oHorse.isAlive() && this.oHorse.distanceTo(p_39661_) < 8.0F;
    }

    private boolean hasChest(AbstractOHorse p_150578_) {
        return p_150578_ instanceof AbstractOHorse && ((AbstractOHorse)p_150578_).hasChest();
    }

    public ItemStack quickMoveStack(Player p_39665_, int p_39666_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_39666_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.container.getContainerSize();
            if (p_39666_ < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (p_39666_ >= j && p_39666_ < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_39666_ >= i && p_39666_ < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player p_39663_) {
        super.removed(p_39663_);
        this.container.stopOpen(p_39663_);
    }

    public static IContainerFactory<OHorseMenu> create() {
        return (windowId, inv, data) -> {
            int containerSize = data.readInt();
            AbstractOHorse abstractOHorse = (AbstractOHorse) inv.player.level.getEntity(data.readInt());
            return new OHorseMenu(windowId, inv, new SimpleContainer(containerSize), abstractOHorse);
        };
    }
}