package com.sajmonoriginal.betterwithbags.item;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.item.ItemStack;

public final class BackpackData {
    public static final String CONTENTS = "BackpackInventory";
    public static final String ATTACHMENT = "Attachment";

    private BackpackData() {
    }

    public static ItemStack[] readContents(CompoundTag tag, int size) {
        ItemStack[] contents = new ItemStack[size];
        if (tag.containsKey(CONTENTS)) {
            ListTag itemList = tag.getList(CONTENTS);
            for (int i = 0; i < itemList.tagCount(); i++) {
                CompoundTag itemTag = (CompoundTag) itemList.tagAt(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < size) {
                    contents[slot] = ItemStack.readItemStackFromNbt(itemTag);
                }
            }
        }
        return contents;
    }

    public static void writeContents(CompoundTag tag, ItemStack[] contents) {
        ListTag itemList = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                contents[i].writeToNBT(itemTag);
                itemList.addTag(itemTag);
            }
        }
        tag.put(CONTENTS, itemList);
    }

    public static int countContents(CompoundTag tag) {
        return tag.containsKey(CONTENTS) ? tag.getList(CONTENTS).tagCount() : 0;
    }

    public static ItemStack readAttachment(CompoundTag tag) {
        return tag.containsKey(ATTACHMENT) ? ItemStack.readItemStackFromNbt(tag.getCompound(ATTACHMENT)) : null;
    }

    public static void writeAttachment(CompoundTag tag, ItemStack attachment) {
        if (attachment != null) {
            CompoundTag attachmentTag = new CompoundTag();
            attachment.writeToNBT(attachmentTag);
            tag.put(ATTACHMENT, attachmentTag);
        } else {
            tag.getValue().remove(ATTACHMENT);
        }
    }
}
