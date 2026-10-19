package com.sajmonoriginal.betterwithbags.item;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;

public class ItemBackpack extends Item implements IArmorItem {

    private final BackpackTier tier;

    public ItemBackpack(String name, String textureKey, int id, BackpackTier tier) {
        super(name, textureKey, id);
        this.tier = tier;
        this.maxStackSize = 1;
        this.setMaxDamage(tier.getSlots() + 1);
    }

    public BackpackTier getTier() {
        return tier;
    }

    public int getCapacity() {
        return tier.getSlots();
    }

    public int getRows() {
        return tier.getRows();
    }

    @Override
    public ItemStack getDefaultStack() {
        return new ItemStack(this, 1, this.tier.getSlots());
    }

    @Override
    public boolean showFullDurability() {
        return true;
    }

    @Override
    public ItemStack onUse(ItemStack itemStack, World world, Player player) {
        if (!player.isSneaking()) {
            if (!world.isClientSide) {
                ((com.sajmonoriginal.betterwithbags.gui.BagScreens) player).betterWithBags$displayBackpackScreen(itemStack);
            }
        }
        return itemStack;
    }

    @Override
    public boolean onUseOnBlock(ItemStack itemstack, World world, Player player, TilePosc pos, Side side, double xOffset, double yOffset) {
        if (!player.isSneaking()) {
            if (!world.isClientSide) {
                ((com.sajmonoriginal.betterwithbags.gui.BagScreens) player).betterWithBags$displayBackpackScreen(itemstack);
            }
            return true;
        }

        int placeX = pos.x() + side.offsetX();
        int placeY = pos.y() + side.offsetY();
        int placeZ = pos.z() + side.offsetZ();

        if (!world.isAirBlock(placeX, placeY, placeZ)) {
            return false;
        }

        if (world.isClientSide) {
            return true;
        }

        int blockId = BackpackBlocks.getBlockForTier(this.tier).id();
        Direction facing = player.getHorizontalPlacementDirection(side).opposite();
        world.setBlockAndMetadataWithNotify(placeX, placeY, placeZ, blockId, facing.id);

        TileEntity te = world.getTileEntity(placeX, placeY, placeZ);
        if (te instanceof TileEntityBackpack) {
            TileEntityBackpack backpackTE = (TileEntityBackpack) te;
            backpackTE.setTier(this.tier);

            ItemStack[] contents = getBackpackContents(itemstack);
            if (contents != null) {
                for (int i = 0; i < contents.length; i++) {
                    backpackTE.setItem(i, contents[i]);
                }
            }

            ItemStack attachment = getAttachment(itemstack);
            if (attachment != null) {
                backpackTE.setAttachment(attachment);
            }
        }

        itemstack.stackSize--;
        return true;
    }

    @Override
    public ArmorMaterial getArmorMaterial() {
        return null;
    }

    @Override
    public int armorPieceProtection() {
        return 0;
    }

    @Override
    public HumanArmorShape getArmorShape() {
        return HumanArmorShape.CHEST;
    }

    public static boolean isBackpack(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        if (item instanceof ItemBackpack) return true;
        return item instanceof ItemBlock
            && BackpackBlocks.getTierForBlockId(((ItemBlock<?>) item).getBlock().id()) != null;
    }

    public static boolean isWearingBackpack(Player player) {
        ItemStack chestSlot = player.inventory.armorInventory[HumanArmorShape.CHEST.getSlotIndex()];
        return chestSlot != null && chestSlot.getItem() instanceof ItemBackpack;
    }

    public static ItemBackpack getWornBackpack(Player player) {
        ItemStack chestSlot = player.inventory.armorInventory[HumanArmorShape.CHEST.getSlotIndex()];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            return (ItemBackpack) chestSlot.getItem();
        }
        return null;
    }

    public static ItemStack getWornBackpackStack(Player player) {
        ItemStack chestSlot = player.inventory.armorInventory[HumanArmorShape.CHEST.getSlotIndex()];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            return chestSlot;
        }
        return null;
    }

    public static BackpackTier getWornTier(Player player) {
        ItemBackpack backpack = getWornBackpack(player);
        return backpack != null ? backpack.getTier() : null;
    }

    public static ItemStack[] getBackpackContents(ItemStack backpackStack) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return null;
        }

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        int capacity = backpack.getCapacity();
        ItemStack[] contents = new ItemStack[capacity];

        CompoundTag tag = backpackStack.getData();
        if (tag.containsKey("BackpackInventory")) {
            ListTag itemList = tag.getList("BackpackInventory");
            for (int i = 0; i < itemList.tagCount(); i++) {
                CompoundTag itemTag = (CompoundTag) itemList.tagAt(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < capacity) {
                    contents[slot] = ItemStack.readItemStackFromNbt(itemTag);
                }
            }
        }

        syncFillLevel(backpackStack, contents);

        return contents;
    }

    public static void setBackpackContents(ItemStack backpackStack, ItemStack[] contents) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return;
        }

        CompoundTag tag = backpackStack.getData();

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
        syncFillLevel(backpackStack, contents);
    }

    public static void setBackpackSlot(ItemStack backpackStack, int slot, ItemStack item) {
        ItemStack[] contents = getBackpackContents(backpackStack);
        if (contents != null && slot >= 0 && slot < contents.length) {
            contents[slot] = item;
            setBackpackContents(backpackStack, contents);
        }
    }

    public static boolean hasItems(ItemStack backpackStack) {
        ItemStack[] contents = getBackpackContents(backpackStack);
        if (contents == null) return false;
        for (ItemStack item : contents) {
            if (item != null) return true;
        }
        return false;
    }

    public static int getOccupiedSlotCount(ItemStack backpackStack) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return 0;
        }
        CompoundTag tag = backpackStack.getData();
        if (tag.containsKey("BackpackInventory")) {
            return tag.getList("BackpackInventory").tagCount();
        }
        return 0;
    }

    private static void syncFillLevel(ItemStack backpackStack, ItemStack[] contents) {
        int occupiedSlots = 0;
        for (ItemStack content : contents) {
            if (content != null) {
                occupiedSlots++;
            }
        }

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        int attachmentSlots = backpackStack.getData().containsKey("Attachment") ? 1 : 0;
        backpackStack.setMetadata(Math.max(0, backpack.getCapacity() - occupiedSlots - attachmentSlots));
    }

    public static ItemStack getAttachment(ItemStack backpackStack) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return null;
        }
        CompoundTag tag = backpackStack.getData();
        if (tag.containsKey("Attachment")) {
            return ItemStack.readItemStackFromNbt(tag.getCompound("Attachment"));
        }
        return null;
    }

    public static void setAttachment(ItemStack backpackStack, ItemStack attachment) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return;
        }
        CompoundTag tag = backpackStack.getData();
        if (attachment != null) {
            CompoundTag attachmentTag = new CompoundTag();
            attachment.writeToNBT(attachmentTag);
            tag.put("Attachment", attachmentTag);
        } else {
            tag.getValue().remove("Attachment");
        }
        getBackpackContents(backpackStack);
    }

    public static boolean hasAttachment(ItemStack backpackStack) {
        return getAttachment(backpackStack) != null;
    }
}
