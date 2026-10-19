package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.gui.ContainerWornBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotBackpack extends Slot {
    private final ContainerInventory playerInventory;
    private final int backpackSlotIndex;

    public SlotBackpack(ContainerWornBackpack container, ContainerInventory inventory, int backpackSlot, int x, int y) {
        super(container, backpackSlot, x, y);
        this.playerInventory = inventory;
        this.backpackSlotIndex = backpackSlot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        if (ItemBackpack.isBackpack(stack)) return false;
        if (!isActive()) return false;
        return true;
    }

    public boolean isActive() {
        Player player = playerInventory.player;
        if (player == null) return false;
        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(player);
        if (backpackStack == null) return false;
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        return backpackSlotIndex < backpack.getCapacity();
    }
}
