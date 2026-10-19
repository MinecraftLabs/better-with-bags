package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotBackpackItem extends Slot {

    public SlotBackpackItem(Container container, int slotIndex, int x, int y) {
        super(container, slotIndex, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        return !ItemBackpack.isBackpack(stack);
    }
}
