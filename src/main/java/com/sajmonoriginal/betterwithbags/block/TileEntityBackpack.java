package com.sajmonoriginal.betterwithbags.block;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;

import java.util.Arrays;

public class TileEntityBackpack extends TileEntity implements Container {

    private ItemStack[] contents;
    private BackpackTier tier;
    private ItemStack attachment;

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

    public void clearContents() {
        Arrays.fill(contents, null);
        attachment = null;
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
        return "container.backpack." + tier.getName();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        if (worldObj == null) {
            return true;
        }
        if (worldObj.getTileEntity(tilePos) != this) {
            return false;
        }
        return player.distanceToSqr(tilePos.x + 0.5, tilePos.y + 0.5, tilePos.z + 0.5) <= 64.0;
    }

    @Override
    public void sort() {
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

        this.contents = new ItemStack[tier.getSlots()];

        if (tag.containsKey("BackpackInventory")) {
            ListTag itemList = tag.getList("BackpackInventory");
            for (int i = 0; i < itemList.tagCount(); i++) {
                CompoundTag itemTag = (CompoundTag) itemList.tagAt(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < contents.length) {
                    contents[slot] = ItemStack.readItemStackFromNbt(itemTag);
                }
            }
        }

        this.attachment = null;
        if (tag.containsKey("Attachment")) {
            this.attachment = ItemStack.readItemStackFromNbt(tag.getCompound("Attachment"));
        }
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
        tag.putString("BackpackTier", tier.name());

        ListTag itemList = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                contents[i].writeToNBT(itemTag);
                itemList.addTag(itemTag);
            }
        }
        tag.put("BackpackInventory", itemList);

        if (attachment != null) {
            CompoundTag attachmentTag = new CompoundTag();
            attachment.writeToNBT(attachmentTag);
            tag.put("Attachment", attachmentTag);
        }
    }

    @Override
    public void dropContents(World world, int x, int y, int z) {

    }

    @Override
    public Packet getDescriptionPacket() {
        CompoundTag tag = new CompoundTag();
        writeToNBT(tag);
        tag.getValue().remove("BackpackInventory");
        PacketTileEntityData packet = new PacketTileEntityData(tag);
        packet.isChunkDataPacket = true;
        return packet;
    }

}
