package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventorySorter;
import net.minecraft.core.player.inventory.container.Container;

public abstract class ContainerBackpackContents implements Container {
    protected ItemStack[] contents = new ItemStack[0];

    protected abstract ItemStack getBackpackStack();

    protected void refresh() {
    }

    @Override
    public int getContainerSize() {
        refresh();
        return contents.length;
    }

    @Override
    public ItemStack getItem(int slot) {
        refresh();
        return slot >= 0 && slot < contents.length ? contents[slot] : null;
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
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        flush();
    }

    public void flush() {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack != null) {
            ItemBackpack.setBackpackContents(backpackStack, contents);
        }
    }

    @Override
    public void sort() {
        refresh();
        InventorySorter.sortInventory(contents);
        setChanged();
    }
}
