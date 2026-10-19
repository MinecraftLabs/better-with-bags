package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.catalyst.Catalyst;

public final class BackpackGuis {
    public static final String HELD = BetterWithBagsMod.MOD_ID + ":gui/backpack";
    public static final String PLACED = BetterWithBagsMod.MOD_ID + ":gui/backpack_placed";

    private BackpackGuis() {
    }

    public static void openHeld(Player player) {
        Catalyst.displayGui(player, player.inventory, player.inventory.getCurrentSlot(), false, HELD);
    }

    public static void openPlaced(Player player, TileEntityBackpack tileEntity) {
        Catalyst.displayGui(player, tileEntity, PLACED);
    }
}
