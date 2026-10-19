package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.item.ItemBedroll;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

public abstract class SlotAttachmentBase extends Slot {
    private static final Container EMPTY_CONTAINER = new EmptyContainer();

    protected SlotAttachmentBase(int x, int y) {
        super(EMPTY_CONTAINER, 0, x, y);
    }

    protected abstract ItemStack getAttachment();

    protected abstract void setAttachment(ItemStack stack);

    protected abstract boolean acceptsAttachment();

    @Override
    public ItemStack getItemStack() {
        return getAttachment();
    }

    @Override
    public void set(ItemStack stack) {
        setAttachment(stack);
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getAttachment();
        if (current == null || amount <= 0) {
            return null;
        }
        ItemStack result = current.splitStack(Math.min(amount, current.stackSize));
        setAttachment(current.stackSize > 0 ? current : null);
        this.setChanged();
        return result;
    }

    @Override
    public boolean hasItem() {
        return getAttachment() != null;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemBedroll && acceptsAttachment();
    }

    private static class EmptyContainer implements Container {
        @Override
        public int getContainerSize() { return 1; }

        @Override
        public ItemStack getItem(int slot) { return null; }

        @Override
        public ItemStack removeItem(int slot, int amount) { return null; }

        @Override
        public void setItem(int slot, ItemStack stack) { }

        @Override
        public String getNameTranslationKey() { return ""; }

        @Override
        public int getMaxStackSize() { return 1; }

        @Override
        public void setChanged() { }

        @Override
        public boolean stillValid(Player player) { return true; }

        @Override
        public void sort() { }

        @Override
        public boolean locked(int slot) { return false; }
    }
}
