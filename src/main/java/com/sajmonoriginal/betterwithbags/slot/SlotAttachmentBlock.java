package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemBedroll;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotAttachmentBlock extends Slot {
    private final TileEntityBackpack tileEntity;

    public SlotAttachmentBlock(TileEntityBackpack tileEntity, int x, int y) {
        super(null, 0, x, y);
        this.tileEntity = tileEntity;
    }

    @Override
    public ItemStack getItemStack() {
        return tileEntity.getAttachment();
    }

    @Override
    public void set(ItemStack stack) {
        tileEntity.setAttachment(stack);
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getItemStack();
        if (current == null) {
            return null;
        }

        ItemStack result;
        if (current.stackSize <= amount) {
            result = current.copy();
            set(null);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                set(null);
            } else {
                tileEntity.setAttachment(current);
            }
        }
        this.setChanged();
        return result;
    }

    @Override
    public boolean hasItem() {
        return getItemStack() != null;
    }

    @Override
    public void setChanged() {
        tileEntity.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        return stack.getItem() instanceof ItemBedroll;
    }
}
