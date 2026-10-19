package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import net.minecraft.core.item.ItemStack;

public class SlotAttachmentBlock extends SlotAttachmentBase {
    private final TileEntityBackpack tileEntity;

    public SlotAttachmentBlock(TileEntityBackpack tileEntity, int x, int y) {
        super(x, y);
        this.tileEntity = tileEntity;
    }

    @Override
    protected ItemStack getAttachment() {
        return tileEntity.getAttachment();
    }

    @Override
    protected void setAttachment(ItemStack stack) {
        tileEntity.setAttachment(stack);
    }

    @Override
    protected boolean acceptsAttachment() {
        return true;
    }
}
