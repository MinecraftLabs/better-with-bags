package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import net.minecraft.core.item.ItemStack;

public interface BagScreens {
    void betterWithBags$displayBackpackScreen(ItemStack backpackStack);
    void betterWithBags$displayBackpackBlockScreen(TileEntityBackpack tileEntity);
    void betterWithBags$closeBackpackScreen();
}
