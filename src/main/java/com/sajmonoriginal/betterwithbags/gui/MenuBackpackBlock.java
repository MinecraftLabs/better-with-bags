package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotAttachmentBlock;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class MenuBackpackBlock extends MenuBackpackBase {
    private final TileEntityBackpack tileEntity;

    public MenuBackpackBlock(ContainerInventory playerInventory, TileEntityBackpack tileEntity) {
        super(tileEntity.getTier());
        this.tileEntity = tileEntity;
        addBackpackSlots(tileEntity, new SlotAttachmentBlock(tileEntity, 178, 18), playerInventory);
    }

    @Override
    public boolean stillValid(Player player) {
        return tileEntity.stillValid(player);
    }
}
