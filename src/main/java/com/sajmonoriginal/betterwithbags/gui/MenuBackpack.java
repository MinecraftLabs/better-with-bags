package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotAttachment;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class MenuBackpack extends MenuBackpackBase {
    private final ContainerHeldBackpack backpackContainer;
    private final ContainerInventory playerInventory;
    private final int heldSlotIndex;
    private final int heldMenuSlot;

    public MenuBackpack(ContainerInventory playerInventory, int slotIndex, boolean isArmor) {
        super(getTierAt(playerInventory, slotIndex));
        this.playerInventory = playerInventory;
        this.heldSlotIndex = slotIndex;
        this.backpackContainer = new ContainerHeldBackpack(playerInventory, slotIndex, tier);
        addBackpackSlots(backpackContainer, new SlotAttachment(playerInventory, heldSlotIndex,
            BackpackPanelLayout.ATTACHMENT_SLOT_X, BackpackPanelLayout.MENU_SLOTS_TOP), playerInventory);

        Slot heldSlot = this.getSlotFor(playerInventory, heldSlotIndex);
        this.heldMenuSlot = heldSlot != null ? heldSlot.index : -1;
    }

    private static BackpackTier getTierAt(ContainerInventory playerInventory, int slotIndex) {
        ItemStack stack = playerInventory.mainInventory[slotIndex];
        BackpackTier tier = ItemBackpack.getBackpackTier(stack);
        return tier != null ? tier : BackpackTier.SMALL;
    }

    public ItemStack getBackpackStack() {
        return playerInventory.mainInventory[heldSlotIndex];
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
        IntList moveSlots = super.getMoveSlots(action, slot, target, player);
        moveSlots.rem(heldMenuSlot);
        return moveSlots;
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
        return ItemBackpack.getBackpackTier(playerInventory.mainInventory[heldSlotIndex]) == this.tier;
    }
}
