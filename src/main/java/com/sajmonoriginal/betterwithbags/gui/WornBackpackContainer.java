package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class WornBackpackContainer implements Container {
    private final ContainerInventory playerInventory;
    private ItemStack[] contents;
    private ItemStack trackedBackpackStack;
    private int capacity;

    public WornBackpackContainer(ContainerInventory playerInventory) {
        this.playerInventory = playerInventory;
        this.contents = new ItemStack[0];
        this.capacity = 0;
        refresh();
    }

    private void refresh() {
        Player player = playerInventory.player;
        if (player == null) {
            trackedBackpackStack = null;
            capacity = 0;
            contents = new ItemStack[0];
            return;
        }

        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(player);
        if (backpackStack != trackedBackpackStack) {
            trackedBackpackStack = backpackStack;
            if (backpackStack != null) {
                ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
                capacity = backpack.getCapacity();
                ItemStack[] nbtContents = ItemBackpack.getBackpackContents(backpackStack);
                contents = nbtContents != null ? nbtContents : new ItemStack[capacity];
            } else {
                capacity = 0;
                contents = new ItemStack[0];
            }
        }
    }

    @Override
    public int getContainerSize() {
        refresh();
        return capacity;
    }

    @Override
    public ItemStack getItem(int slot) {
        refresh();
        if (slot >= 0 && slot < contents.length) {
            return contents[slot];
        }
        return null;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        refresh();
        if (slot < 0 || slot >= contents.length || contents[slot] == null) {
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
        refresh();
        if (slot >= 0 && slot < contents.length) {
            contents[slot] = stack;
            setChanged();
        }
    }

    @Override
    public String getNameTranslationKey() {
        return trackedBackpackStack != null
            ? ((ItemBackpack) trackedBackpackStack.getItem()).getTier().getNameTranslationKey()
            : BackpackTier.SMALL.getNameTranslationKey();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        if (trackedBackpackStack != null) {
            ItemBackpack.setBackpackContents(trackedBackpackStack, contents);
        }
    }

    public void flush() {
        if (trackedBackpackStack != null) {
            ItemBackpack.setBackpackContents(trackedBackpackStack, contents);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void sort() {
    }
}
