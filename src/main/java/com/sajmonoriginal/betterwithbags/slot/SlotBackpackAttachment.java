package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemBedroll;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotBackpackAttachment extends Slot {
    private final ContainerInventory playerInventory;

    public SlotBackpackAttachment(ContainerInventory inventory, int x, int y) {
        super(new DummyContainer(), 0, x, y);
        this.playerInventory = inventory;
    }

    private ItemStack getBackpackStack() {
        Player player = playerInventory.player;
        if (player == null) return null;
        return ItemBackpack.getWornBackpackStack(player);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return null;
        return ItemBackpack.getAttachment(backpackStack);
    }

    @Override
    public void set(ItemStack stack) {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return;
        ItemBackpack.setAttachment(backpackStack, stack);
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getItemStack();
        if (current == null) return null;

        ItemStack result;
        if (current.stackSize <= amount) {
            result = current.copy();
            set(null);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                set(null);
            } else {
                ItemStack backpackStack = getBackpackStack();
                if (backpackStack != null) {
                    ItemBackpack.setAttachment(backpackStack, current);
                }
            }
        }
        this.setChanged();
        return result;
    }

    @Override
    public boolean hasItem() {
        return getItemStack() != null;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isLocked() {
        return !isActive();
    }

    @Override
    public boolean enableDragAndPickup() {
        return isActive();
    }

    @Override
    public boolean allowItemInteraction() {
        return isActive();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (!isActive()) return false;
        if (stack == null) return false;
        return stack.getItem() instanceof ItemBedroll;
    }

    public boolean isActive() {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return false;
        if (!(backpackStack.getItem() instanceof ItemBackpack)) return false;

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        BackpackTier tier = backpack.getTier();
        return tier.supportsAttachment();
    }

    private static class DummyContainer implements Container {
        @Override
        public int getContainerSize() { return 1; }

        @Override
        public ItemStack getItem(int slot) { return null; }

        @Override
        public ItemStack removeItem(int slot, int amount) { return null; }

        @Override
        public void setItem(int slot, ItemStack stack) { }

        @Override
        public String getNameTranslationKey() { return ""; }

        @Override
        public int getMaxStackSize() { return 1; }

        @Override
        public void setChanged() { }

        @Override
        public boolean stillValid(Player player) { return true; }

        @Override
        public void sort() { }

        @Override
        public boolean locked(int slot) { return false; }
    }
}
