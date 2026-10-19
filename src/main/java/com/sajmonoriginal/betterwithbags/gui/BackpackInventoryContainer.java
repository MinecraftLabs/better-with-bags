package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class BackpackInventoryContainer implements Container {
    private final ContainerInventory playerInventory;
    private final int slotIndex;
    private final BackpackTier tier;
    private final int capacity;
    private final ItemStack[] contents;

    public BackpackInventoryContainer(ContainerInventory playerInventory, int slotIndex) {
        this.playerInventory = playerInventory;
        this.slotIndex = slotIndex;
        ItemStack backpackStack = playerInventory.mainInventory[slotIndex];
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        this.tier = backpack.getTier();
        this.capacity = backpack.getCapacity();

        ItemStack[] nbtContents = ItemBackpack.getBackpackContents(backpackStack);
        this.contents = nbtContents != null ? nbtContents : new ItemStack[capacity];
    }

    private ItemStack getBackpackStack() {
        ItemStack current = playerInventory.mainInventory[slotIndex];
        if (current != null && current.getItem() instanceof ItemBackpack
            && ((ItemBackpack) current.getItem()).getTier() == tier) {
            return current;
        }
        return null;
    }

    @Override
    public int getContainerSize() {
        return capacity;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < capacity) {
            return contents[slot];
        }
        return null;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot < 0 || slot >= capacity || contents[slot] == null) {
            return null;
        }

        ItemStack result;
        if (contents[slot].stackSize <= amount) {
            result = contents[slot];
            contents[slot] = null;
        } else {
            result = contents[slot].splitStack(amount);
            if (contents[slot].stackSize == 0) {
                contents[slot] = null;
            }
        }
        setChanged();
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < capacity) {
            contents[slot] = stack;
            setChanged();
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.backpack";
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        flush();
    }

    public void flush() {
        ItemStack current = getBackpackStack();
        if (current != null) {
            ItemBackpack.setBackpackContents(current, contents);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return getBackpackStack() != null;
    }

    @Override
    public void sort() {
    }
}
