package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class ContainerWornBackpack extends ContainerBackpackContents {
    private final ContainerInventory playerInventory;
    private ItemStack trackedBackpackStack;

    public ContainerWornBackpack(ContainerInventory playerInventory) {
        this.playerInventory = playerInventory;
        refresh();
    }

    @Override
    protected void refresh() {
        Player player = playerInventory.player;
        ItemStack backpackStack = player != null ? ItemBackpack.getWornBackpackStack(player) : null;
        if (backpackStack != trackedBackpackStack) {
            trackedBackpackStack = backpackStack;
            contents = backpackStack != null ? ItemBackpack.getBackpackContents(backpackStack) : new ItemStack[0];
        }
    }

    @Override
    protected ItemStack getBackpackStack() {
        return trackedBackpackStack;
    }

    @Override
    public String getNameTranslationKey() {
        BackpackTier tier = ItemBackpack.getBackpackTier(trackedBackpackStack);
        return (tier != null ? tier : BackpackTier.SMALL).getNameTranslationKey();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
