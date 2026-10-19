package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class ContainerHeldBackpack extends ContainerBackpackContents {
    private final ContainerInventory playerInventory;
    private final int slotIndex;
    private final BackpackTier tier;

    public ContainerHeldBackpack(ContainerInventory playerInventory, int slotIndex, BackpackTier tier) {
        this.playerInventory = playerInventory;
        this.slotIndex = slotIndex;
        this.tier = tier;
        ItemStack backpackStack = getBackpackStack();
        this.contents = backpackStack != null ? ItemBackpack.getBackpackContents(backpackStack) : new ItemStack[tier.getSlots()];
    }

    @Override
    protected ItemStack getBackpackStack() {
        ItemStack current = playerInventory.mainInventory[slotIndex];
        return ItemBackpack.getBackpackTier(current) == tier ? current : null;
    }

    @Override
    public String getNameTranslationKey() {
        return tier.getNameTranslationKey();
    }

    @Override
    public boolean stillValid(Player player) {
        return getBackpackStack() != null;
    }
}
