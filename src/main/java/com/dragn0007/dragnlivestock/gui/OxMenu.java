package com.dragn0007.dragnlivestock.gui;

import com.dragn0007.dragnlivestock.entities.cow.ox.Ox;
import com.dragn0007.dragnlivestock.entities.util.AbstractOHorse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OxMenu extends AbstractContainerMenu {

    public Container container;
    public Ox ox;

    public OxMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, new SimpleContainer(extraData.readInt()), (Ox) inventory.player.level.getEntity(extraData.readInt()));
    }

    public OxMenu(int containerId, Inventory inventory, Container container, Ox ox) {
        super(LOMenuTypes.OX_MENU.get(), containerId);
        this.container = container;
        this.ox = ox;

        int oxSlots = 0;
        this.addSlot(new Slot(this.container, oxSlots++, 8, 18) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(Items.SADDLE) && !this.hasItem() && OxMenu.this.ox.isSaddleable();
            }

            @Override
            public boolean isActive() {
                return OxMenu.this.ox.isSaddleable();
            }
        });

        if (this.ox.hasChest()) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 8; x++) {
                    this.addSlot(new Slot(this.container, oxSlots++, 25 + x * 18, 18 + y * 18));
                }
            }
        }

        int playerSlots = 0;
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, playerSlots++, 8 + x * 18, 142));
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, playerSlots++, 8 + x * 18, 84 + y * 18));
            }
        }
    }

        public boolean stillValid(Player player) {
        return !this.ox.hasInventoryChanged(this.container) && this.container.stillValid(player) && this.ox.isAlive() && this.ox.distanceTo(player) < 8.0F;
    }

    public ItemStack quickMoveStack(Player player, int slotId) {
        Slot slot = this.slots.get(slotId);
        if(!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = slot.getItem();
        ItemStack itemStackCopy = itemStack.copy();
        int containerSize = this.container.getContainerSize();

        if(slotId < containerSize) {
            if(!this.moveItemStackTo(itemStack, containerSize, containerSize + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else if(slotId < containerSize + 36) {
            if(!this.moveItemStackTo(itemStack, 0, containerSize, false)) {
                return ItemStack.EMPTY;
            }
        }

        if(itemStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        }
        slot.setChanged();
        return itemStackCopy;
    }
}