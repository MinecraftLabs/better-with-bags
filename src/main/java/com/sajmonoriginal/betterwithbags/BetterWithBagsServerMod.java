package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BackpackGuis;
import com.sajmonoriginal.betterwithbags.gui.ContainerHeldBackpack;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpack;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpackBlock;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.GuiEntry;

@Environment(EnvType.SERVER)
public class BetterWithBagsServerMod implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        Catalyst.GUIS.register(BackpackGuis.HELD, new GuiEntry<>(ContainerHeldBackpack.class, MenuBackpack.class));
        Catalyst.GUIS.register(BackpackGuis.PLACED, new GuiEntry<>(TileEntityBackpack.class, MenuBackpackBlock.class));
    }
}
