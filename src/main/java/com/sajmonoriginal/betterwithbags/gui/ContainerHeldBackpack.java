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

    public ContainerHeldBackpack(ContainerInventory playerInventory, int slotIndex) {
        this.playerInventory = playerInventory;
        this.slotIndex = slotIndex;
        ItemStack backpackStack = playerInventory.mainInventory[slotIndex];
        this.tier = ((ItemBackpack) backpackStack.getItem()).getTier();
        this.contents = ItemBackpack.getBackpackContents(backpackStack);
    }

    @Override
    protected ItemStack getBackpackStack() {
        ItemStack current = playerInventory.mainInventory[slotIndex];
        if (current != null && current.getItem() instanceof ItemBackpack
            && ((ItemBackpack) current.getItem()).getTier() == tier) {
            return current;
        }
        return null;
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
