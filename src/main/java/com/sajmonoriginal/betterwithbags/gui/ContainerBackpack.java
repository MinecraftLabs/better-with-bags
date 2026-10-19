package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;

public class ContainerBackpack extends MenuAbstract {
    private final BackpackTier tier;
    private final int rows;
    private final int capacity;
    private final BackpackInventoryContainer backpackContainer;
    private final ContainerInventory playerInventory;
    private final int heldSlotIndex;
    private final int heldMenuSlot;

    public ContainerBackpack(ContainerInventory playerInventory, ItemStack backpackStack) {
        this.playerInventory = playerInventory;
        this.heldSlotIndex = playerInventory.getCurrentSlot();
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        this.tier = backpack.getTier();
        this.rows = tier.getRows();
        this.capacity = tier.getSlots();
        this.backpackContainer = new BackpackInventoryContainer(playerInventory, heldSlotIndex);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = row * 9 + col;
                if (slotIndex < capacity) {
                    this.addSlot(new SlotBackpackItem(backpackContainer, slotIndex, 8 + col * 18, 18 + row * 18));
                }
            }
        }

        if (tier.supportsAttachment()) {
            this.addSlot(new SlotAttachment(playerInventory, heldSlotIndex, 178, 18));
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

        Slot heldSlot = this.getSlotFor(playerInventory, heldSlotIndex);
        this.heldMenuSlot = heldSlot != null ? heldSlot.index : -1;
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
    public ItemStack clicked(InventoryAction action, int[] args, Player player) {
        if (args != null && args.length > 0 && touchesHeldBackpack(action, args)) {
            return null;
        }
        return super.clicked(action, args, player);
    }

    private boolean touchesHeldBackpack(InventoryAction action, int[] args) {
        if (action == InventoryAction.DRAG_ITEMS_ALL || action == InventoryAction.DRAG_ITEMS_ONE
            || action == InventoryAction.CREATIVE_DRAG || action == InventoryAction.PICKUP_SIMILAR) {
            return false;
        }
        if (args[0] == heldMenuSlot) {
            return true;
        }
        return action == InventoryAction.HOTBAR_ITEM_SWAP && args.length > 1
            && args[1] >= 1 && args[1] <= 9 && getHotbarSlotId(args[1]) == heldMenuSlot;
    }

    @Override
    public IntList getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        IntList moveSlots = getMoveSlotsUnfiltered(action, slot);
        moveSlots.rem(heldMenuSlot);
        return moveSlots;
    }

    private IntList getMoveSlotsUnfiltered(InventoryAction action, Slot slot) {
        int attachmentSlot = getAttachmentSlot();
        int playerStart = getPlayerSlotStart();
        if (slot.index < capacity) {
            return this.getSlots(slot.index, 1, false);
        }
        if (slot.index == attachmentSlot) {
            return this.getSlots(attachmentSlot, 1, false);
        }
        if (action == InventoryAction.MOVE_ALL) {
            if (slot.index >= playerStart && slot.index < playerStart + 27) {
                return this.getSlots(playerStart, 27, false);
            }
            if (slot.index >= playerStart + 27) {
                return this.getSlots(playerStart + 27, 9, false);
            }
        }
        return this.getSlots(slot.index, 1, false);
    }

    @Override
    public IntList getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
        int attachmentSlot = getAttachmentSlot();
        int playerStart = getPlayerSlotStart();
        if (slot.index < capacity) {
            return this.getSlots(playerStart, 36, false);
        }
        if (slot.index == attachmentSlot) {
            return this.getSlots(playerStart, 36, false);
        }
        if (slot.index >= playerStart) {
            ItemStack stack = slot.getItemStack();
            if (ItemBackpack.isBackpack(stack)) {
                return IntLists.EMPTY_LIST;
            }
            if (stack != null && stack.getItem() instanceof com.sajmonoriginal.betterwithbags.item.ItemBedroll) {
                Slot attSlot = this.getSlot(attachmentSlot);
                if (attSlot != null && attSlot.mayPlace(stack) && !attSlot.hasItem()) {
                    return this.getSlots(attachmentSlot, 1, false);
                }
            }
            return this.getSlots(0, capacity, false);
        }
        return IntLists.EMPTY_LIST;
    }

    @Override
    public void broadcastChanges() {
        if (!isBackpackInSlot()) {
            return;
        }
        backpackContainer.flush();
        super.broadcastChanges();
    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);
        backpackContainer.flush();
    }

    @Override
    public boolean stillValid(Player player) {
        return isBackpackInSlot();
    }

    private boolean isBackpackInSlot() {
        ItemStack current = playerInventory.mainInventory[heldSlotIndex];
        return current != null && current.getItem() instanceof ItemBackpack &&
            ((ItemBackpack) current.getItem()).getTier() == this.tier;
    }
}
