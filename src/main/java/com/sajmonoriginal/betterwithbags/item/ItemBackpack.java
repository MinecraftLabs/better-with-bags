package com.sajmonoriginal.betterwithbags.item;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BackpackGuis;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.IPlaceable;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

import java.util.ArrayList;
import java.util.List;

public class ItemBackpack extends Item implements IArmorItem<HumanArmorShape> {

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
    public boolean showFullDurability() {
        return true;
    }

    @Override
    public ItemStack onUse(ItemStack itemStack, World world, Player player) {
        if (!player.isSneaking() && !world.isClientSide) {
            BackpackGuis.openHeld(player);
        }
        return itemStack;
    }

    @Override
    public boolean onUseOnBlock(ItemStack itemstack, World world, Player player, TilePosc pos, Side side, double xOffset, double yOffset) {
        if (!player.isSneaking()) {
            if (!world.isClientSide) {
                BackpackGuis.openHeld(player);
            }
            return true;
        }

        Block<?> block = BackpackBlocks.getBlockForTier(this.tier);
        TilePosc placePos = IPlaceable.shouldShiftBlockOutOf(block, world, pos) ? pos.add(side, new TilePos()) : pos;
        if (!IPlaceable.canPlaceBlockDirectlyAtPosition(block, world, placePos, side)) {
            return false;
        }
        if (world.isClientSide) {
            return true;
        }

        Direction facing = player.getHorizontalPlacementDirection(side).opposite();
        if (!world.setBlockTypeDataNotify(placePos, block, facing.id)) {
            return false;
        }

        TileEntity te = world.getTileEntity(placePos);
        if (te instanceof TileEntityBackpack) {
            TileEntityBackpack backpackTE = (TileEntityBackpack) te;
            backpackTE.setTier(this.tier);
            backpackTE.loadFromItem(itemstack);
        }

        world.playBlockSoundEffect(player, placePos.x() + 0.5, placePos.y() + 0.5, placePos.z() + 0.5, block, EnumBlockSoundEffectType.PLACE);
        itemstack.consumeItem(player);
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
        return getWornBackpackStack(player) != null;
    }

    public static ItemBackpack getWornBackpack(Player player) {
        ItemStack backpackStack = getWornBackpackStack(player);
        return backpackStack != null ? (ItemBackpack) backpackStack.getItem() : null;
    }

    public static ItemStack getWornBackpackStack(Player player) {
        ItemStack chestSlot = player.getItemInArmorSlot(HumanArmorShape.CHEST);
        return chestSlot != null && chestSlot.getItem() instanceof ItemBackpack ? chestSlot : null;
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
        backpackStack.setMetadata(0);
    }

    public static void transferContents(ItemStack from, ItemStack to) {
        to.setData(from.getData());
        ItemStack[] source = getBackpackContents(from);
        if (source == null || !(to.getItem() instanceof ItemBackpack)) {
            return;
        }

        ItemStack[] contents = new ItemStack[((ItemBackpack) to.getItem()).getCapacity()];
        List<ItemStack> overflow = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == null) continue;
            if (i < contents.length) {
                contents[i] = source[i];
            } else {
                overflow.add(source[i]);
            }
        }
        for (int i = 0; i < contents.length && !overflow.isEmpty(); i++) {
            if (contents[i] == null) {
                contents[i] = overflow.remove(0);
            }
        }
        setBackpackContents(to, contents);
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

    public static int getFreeSlotCount(ItemStack backpackStack) {
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        int attachmentSlots = backpackStack.getData().containsKey("Attachment") ? 1 : 0;
        return Math.max(0, backpack.getCapacity() - getOccupiedSlotCount(backpackStack) - attachmentSlots);
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
        backpackStack.setMetadata(0);
    }

    public static boolean hasAttachment(ItemStack backpackStack) {
        return getAttachment(backpackStack) != null;
    }
}
