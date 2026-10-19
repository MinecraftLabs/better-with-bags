package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class SlotBackpackAttachment extends SlotAttachmentBase {
    private final ContainerInventory playerInventory;

    public SlotBackpackAttachment(ContainerInventory inventory, int x, int y) {
        super(x, y);
        this.playerInventory = inventory;
    }

    private ItemStack getBackpackStack() {
        Player player = playerInventory.player;
        return player != null ? ItemBackpack.getWornBackpackStack(player) : null;
    }

    @Override
    protected ItemStack getAttachment() {
        return ItemBackpack.getAttachment(getBackpackStack());
    }

    @Override
    protected void setAttachment(ItemStack stack) {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack != null) {
            ItemBackpack.setAttachment(backpackStack, stack);
            playerInventory.player.setItemInArmorSlot(HumanArmorShape.CHEST, backpackStack.copy());
        }
    }

    @Override
    protected boolean acceptsAttachment() {
        return isActive();
    }

    @Override
    public boolean isLocked() {
        return !isActive();
    }

    @Override
    public boolean enableDragAndPickup() {
        return isActive();
    }

    @Override
    public boolean allowItemInteraction() {
        return isActive();
    }

    public boolean isActive() {
        BackpackTier tier = ItemBackpack.getBackpackTier(getBackpackStack());
        return tier != null && tier.supportsAttachment();
    }
}
