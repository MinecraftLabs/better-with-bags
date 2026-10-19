package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class SlotAttachment extends SlotAttachmentBase {
    private final ContainerInventory playerInventory;
    private final int slotIndex;

    public SlotAttachment(ContainerInventory playerInventory, int slotIndex, int x, int y) {
        super(x, y);
        this.playerInventory = playerInventory;
        this.slotIndex = slotIndex;
    }

    private ItemStack getBackpackStack() {
        return playerInventory.mainInventory[slotIndex];
    }

    @Override
    protected ItemStack getAttachment() {
        return ItemBackpack.getAttachment(getBackpackStack());
    }

    @Override
    protected void setAttachment(ItemStack stack) {
        ItemBackpack.setAttachment(getBackpackStack(), stack);
    }

    @Override
    protected boolean acceptsAttachment() {
        ItemStack backpackStack = getBackpackStack();
        return backpackStack != null && backpackStack.getItem() instanceof ItemBackpack
            && ((ItemBackpack) backpackStack.getItem()).getTier().supportsAttachment();
    }
}
