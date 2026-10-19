package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.player.inventory.container.ContainerInventory;

@Environment(EnvType.CLIENT)
public class ScreenBackpackBlock extends ScreenBackpackBase {
    private final TileEntityBackpack tileEntity;

    public ScreenBackpackBlock(ContainerInventory playerInventory, TileEntityBackpack tileEntity) {
        super(new MenuBackpackBlock(playerInventory, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected String getTitle() {
        return tileEntity.createDisplayStack().getDisplayName();
    }
}
