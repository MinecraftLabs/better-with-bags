package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemBedroll;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotAttachment extends Slot {
    private final ContainerInventory playerInventory;
    private final int slotIndex;

    public SlotAttachment(ContainerInventory playerInventory, int slotIndex, int x, int y) {
        super(null, 0, x, y);
        this.playerInventory = playerInventory;
        this.slotIndex = slotIndex;
    }

    private ItemStack getBackpackStack() {
        return playerInventory.mainInventory[slotIndex];
    }

    @Override
    public ItemStack getItemStack() {
        return ItemBackpack.getAttachment(getBackpackStack());
    }

    @Override
    public void set(ItemStack stack) {
        ItemBackpack.setAttachment(getBackpackStack(), stack);
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getItemStack();
        if (current == null) {
            return null;
        }

        ItemStack result;
        if (current.stackSize <= amount) {
            result = current.copy();
            set(null);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                set(null);
            } else {
                ItemBackpack.setAttachment(getBackpackStack(), current);
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
    public void setChanged() {
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        boolean isValidItem = stack.getItem() instanceof ItemBedroll;
        if (!isValidItem) return false;
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return false;
        if (!(backpackStack.getItem() instanceof ItemBackpack)) return false;

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        com.sajmonoriginal.betterwithbags.item.BackpackTier tier = backpack.getTier();
        return tier == com.sajmonoriginal.betterwithbags.item.BackpackTier.LARGE ||
               tier == com.sajmonoriginal.betterwithbags.item.BackpackTier.REINFORCED ||
               tier == com.sajmonoriginal.betterwithbags.item.BackpackTier.NETHER;
    }
}
