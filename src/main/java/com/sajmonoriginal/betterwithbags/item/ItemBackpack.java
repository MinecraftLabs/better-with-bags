package com.sajmonoriginal.betterwithbags.item;

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

    public static BackpackTier getBackpackTier(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemBackpack ? ((ItemBackpack) stack.getItem()).getTier() : null;
    }

    public static boolean isBackpack(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        if (item instanceof ItemBackpack) return true;
        return item instanceof ItemBlock
            && BackpackBlocks.getTierForBlock(((ItemBlock<?>) item).getBlock()) != null;
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
        return BackpackData.readContents(backpackStack.getData(), ((ItemBackpack) backpackStack.getItem()).getCapacity());
    }

    public static void setBackpackContents(ItemStack backpackStack, ItemStack[] contents) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return;
        }
        BackpackData.writeContents(backpackStack.getData(), contents);
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
        return BackpackData.countContents(backpackStack.getData());
    }

    public static int getFreeSlotCount(ItemStack backpackStack) {
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        int attachmentSlots = backpackStack.getData().containsKey(BackpackData.ATTACHMENT) ? 1 : 0;
        return Math.max(0, backpack.getCapacity() - getOccupiedSlotCount(backpackStack) - attachmentSlots);
    }

    public static ItemStack getAttachment(ItemStack backpackStack) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return null;
        }
        return BackpackData.readAttachment(backpackStack.getData());
    }

    public static void setAttachment(ItemStack backpackStack, ItemStack attachment) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return;
        }
        BackpackData.writeAttachment(backpackStack.getData(), attachment);
        backpackStack.setMetadata(0);
    }

    public static boolean hasAttachment(ItemStack backpackStack) {
        return getAttachment(backpackStack) != null;
    }
}
