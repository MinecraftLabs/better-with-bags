package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.item.ItemStack;
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
        ItemStack namedStack = BackpackItems.getItemForTier(menu.getTier()).getDefaultStack();
        namedStack.setCustomName(tileEntity.getCustomName());
        return namedStack.getDisplayName();
    }
}
