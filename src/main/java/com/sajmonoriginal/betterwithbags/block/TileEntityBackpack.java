package com.sajmonoriginal.betterwithbags.block;

import com.mojang.nbt.tags.CompoundTag;
import com.sajmonoriginal.betterwithbags.item.BackpackData;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.player.inventory.InventorySorter;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import java.util.Arrays;

public class TileEntityBackpack extends TileEntity implements Container {

    private ItemStack[] contents;
    private BackpackTier tier;
    private ItemStack attachment;
    private CompoundTag itemData = new CompoundTag();
    private boolean taken;

    public TileEntityBackpack() {
        this.tier = BackpackTier.SMALL;
        this.contents = new ItemStack[tier.getSlots()];
        this.attachment = null;
    }

    public BackpackTier getTier() {
        return tier;
    }

    public void setTier(BackpackTier tier) {
        this.tier = tier;
        ItemStack[] oldContents = this.contents;
        this.contents = new ItemStack[tier.getSlots()];
        if (oldContents != null) {
            System.arraycopy(oldContents, 0, this.contents, 0, Math.min(oldContents.length, this.contents.length));
        }
    }

    public ItemStack getAttachment() {
        return attachment;
    }

    public void setAttachment(ItemStack attachment) {
        this.attachment = attachment;
        setChanged();
    }

    public boolean hasAttachment() {
        return attachment != null;
    }

    public ItemStack createDisplayStack() {
        ItemStack stack = new ItemStack(BackpackItems.getItemForTier(tier));
        stack.setData(itemData);
        return stack;
    }

    public boolean isEmpty() {
        if (attachment != null) return false;
        for (ItemStack stack : contents) {
            if (stack != null) return false;
        }
        return true;
    }

    public void loadFromItem(ItemStack backpackStack) {
        ItemStack[] stored = ItemBackpack.getBackpackContents(backpackStack);
        if (stored != null) {
            System.arraycopy(stored, 0, contents, 0, Math.min(stored.length, contents.length));
        }
        attachment = ItemBackpack.getAttachment(backpackStack);
        itemData = backpackStack.getData().clone();
        itemData.getValue().remove(BackpackData.CONTENTS);
        itemData.getValue().remove(BackpackData.ATTACHMENT);
        setChanged();
    }

    public ItemStack takeAsItem() {
        ItemStack stack = createDisplayStack();
        ItemBackpack.setBackpackContents(stack, contents);
        ItemBackpack.setAttachment(stack, attachment);

        Arrays.fill(contents, null);
        attachment = null;
        taken = true;
        return stack;
    }

    public boolean isTaken() {
        return taken;
    }

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    @Override
    public ItemStack getItem(int slot) {
        return contents[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (contents[slot] != null) {
            if (contents[slot].stackSize <= amount) {
                ItemStack stack = contents[slot];
                contents[slot] = null;
                setChanged();
                return stack;
            }
            ItemStack split = contents[slot].splitStack(amount);
            if (contents[slot].stackSize == 0) {
                contents[slot] = null;
            }
            setChanged();
            return split;
        }
        return null;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        contents[slot] = stack;
        if (stack != null && stack.stackSize > getMaxStackSize()) {
            stack.stackSize = getMaxStackSize();
        }
        setChanged();
    }

    @Override
    public String getNameTranslationKey() {
        return tier.getNameTranslationKey();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        if (worldObj.getTileEntity(tilePos) != this) {
            return false;
        }
        return player.distanceToSqr(tilePos.x + 0.5, tilePos.y + 0.5, tilePos.z + 0.5) <= 64.0;
    }

    @Override
    public void sort() {
        InventorySorter.sortInventory(contents);
        setChanged();
    }

    @Override
    public void readAdditionalData(CompoundTag tag) {
        if (tag.containsKey("BackpackTier")) {
            String tierName = tag.getString("BackpackTier");
            try {
                this.tier = BackpackTier.valueOf(tierName);
            } catch (IllegalArgumentException e) {
                this.tier = BackpackTier.SMALL;
            }
        }

        if (tag.containsKey(BackpackData.CONTENTS)) {
            this.contents = BackpackData.readContents(tag, tier.getSlots());
        } else {
            setTier(tier);
        }
        this.attachment = BackpackData.readAttachment(tag);

        this.itemData = tag.containsKey("ItemData") ? tag.getCompound("ItemData").clone() : new CompoundTag();
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
        tag.putString("BackpackTier", tier.name());

        BackpackData.writeContents(tag, contents);
        BackpackData.writeAttachment(tag, attachment);

        tag.put("ItemData", itemData.clone());
    }

    @Override
    public void dropContents(World world, int x, int y, int z) {
        if (!taken && !isEmpty()) {
            world.dropItem(new TilePos(x, y, z), takeAsItem());
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        CompoundTag tag = new CompoundTag();
        writeToNBT(tag);
        tag.getValue().remove(BackpackData.CONTENTS);
        PacketTileEntityData packet = new PacketTileEntityData(tag);
        packet.isChunkDataPacket = true;
        return packet;
    }

}
