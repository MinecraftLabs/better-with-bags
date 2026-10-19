package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemBedroll;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpackItem;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

public abstract class MenuBackpackBase extends MenuAbstract {
    protected final BackpackTier tier;
    protected final int rows;
    protected final int capacity;

    protected MenuBackpackBase(BackpackTier tier) {
        this.tier = tier;
        this.rows = tier.getRows();
        this.capacity = tier.getSlots();
    }

    protected void addBackpackSlots(Container backpack, Slot attachmentSlot, ContainerInventory playerInventory) {
        for (int i = 0; i < capacity; i++) {
            this.addSlot(new SlotBackpackItem(backpack, i, 8 + (i % 9) * 18, 18 + (i / 9) * 18));
        }

        if (tier.supportsAttachment()) {
            this.addSlot(attachmentSlot);
        }

        int playerInvY = 17 + rows * 18 + 22;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }

        int hotbarY = playerInvY + 54 + 4;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, hotbarY));
        }
    }

    public int getRows() {
        return rows;
    }

    public BackpackTier getTier() {
        return tier;
    }

    private int getAttachmentSlot() {
        return tier.supportsAttachment() ? capacity : -1;
    }

    private int getPlayerSlotStart() {
        return tier.supportsAttachment() ? capacity + 1 : capacity;
    }

    @Override
    public IntList getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        int playerStart = getPlayerSlotStart();
        if (action == InventoryAction.MOVE_ALL && slot.index >= playerStart) {
            if (slot.index < playerStart + 27) {
                return this.getSlots(playerStart, 27, false);
            }
            return this.getSlots(playerStart + 27, 9, false);
        }
        return this.getSlots(slot.index, 1, false);
    }

    @Override
    public IntList getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
        int playerStart = getPlayerSlotStart();
        if (slot.index < playerStart) {
            return this.getSlots(playerStart, 36, false);
        }

        ItemStack stack = slot.getItemStack();
        if (ItemBackpack.isBackpack(stack)) {
            return IntLists.EMPTY_LIST;
        }
        if (stack != null && stack.getItem() instanceof ItemBedroll) {
            Slot attachmentSlot = this.getSlot(getAttachmentSlot());
            if (attachmentSlot != null && attachmentSlot.mayPlace(stack) && !attachmentSlot.hasItem()) {
                return this.getSlots(getAttachmentSlot(), 1, false);
            }
        }
        return this.getSlots(0, capacity, false);
    }
}
