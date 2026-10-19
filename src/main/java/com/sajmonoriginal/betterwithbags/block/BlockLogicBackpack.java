package com.sajmonoriginal.betterwithbags.block;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

public class BlockLogicBackpack extends BlockLogic {

    private final BackpackTier tier;

    public BlockLogicBackpack(Block<?> block, BackpackTier tier) {
        super(block, Materials.CLOTH);
        this.tier = tier;
        setBlockBounds(0.1875, 0.0, 0.1875, 0.8125, 0.625, 0.8125);
    }

    public BackpackTier getTier() {
        return tier;
    }

    @Override
    public boolean isCubeShaped() {
        return false;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, Side side, Mob mob, double xOffset, double yOffset) {
        Direction dir = mob.getHorizontalPlacementDirection(side).opposite();
        world.setBlockMetadataWithNotify(x, y, z, dir.id);
    }

    @Override
    public void onBlockPlacedOnSide(World world, int x, int y, int z, Side side, double xOffset, double yOffset) {
        if (!side.isHorizontal()) {
            side = Side.SOUTH;
        }
        Direction dir = side.direction();
        world.setBlockMetadataWithNotify(x, y, z, dir.id);
    }

    @Override
    public void onPlacedByWorld(World world, TilePosc pos) {
        super.onPlacedByWorld(world, pos);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityBackpack) {
            syncTier((TileEntityBackpack) te);
        }
    }

    private void syncTier(TileEntityBackpack backpackTE) {
        if (backpackTE.getTier() != this.tier) {
            backpackTE.setTier(this.tier);
        }
    }

    @Override
    public boolean onInteracted(World world, TilePosc pos, Player player, Side side, double hitX, double hitY) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityBackpack)) {
            return false;
        }

        TileEntityBackpack backpackTE = (TileEntityBackpack) te;
        syncTier(backpackTE);

        if (player.isSneaking()) {
            if (!world.isClientSide) {
                TilePos tilePos = new TilePos(pos);
                pickUpBackpack(world, tilePos.x, tilePos.y, tilePos.z, player, backpackTE);
            }
            return true;
        }

        if (!world.isClientSide) {
            ((com.sajmonoriginal.betterwithbags.gui.BagScreens) player).betterWithBags$displayBackpackBlockScreen(backpackTE);
        }
        return true;
    }

    private void pickUpBackpack(World world, int x, int y, int z, Player player, TileEntityBackpack backpackTE) {
        ItemStack backpackItem = takeItemFromTileEntity(backpackTE);
        world.setBlockWithNotify(x, y, z, 0);
        player.inventory.insertItem(backpackItem, false);
        if (backpackItem.stackSize > 0) {
            player.dropPlayerItem(backpackItem);
        }
        world.playSoundAtEntity(null, player, "random.pop", 0.2f,
            ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
    }

    private ItemStack takeItemFromTileEntity(TileEntityBackpack backpackTE) {
        ItemStack stack = new ItemStack(BackpackItems.getItemForTier(this.tier));

        ItemStack[] contents = new ItemStack[backpackTE.getContainerSize()];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = backpackTE.getItem(i);
        }
        ItemBackpack.setBackpackContents(stack, contents);

        if (backpackTE.hasAttachment()) {
            ItemBackpack.setAttachment(stack, backpackTE.getAttachment());
        }
        stack.setCustomName(backpackTE.getCustomName());

        backpackTE.clearContents();
        return stack;
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause cause, TilePosc pos, int meta, TileEntity tileEntity) {
        if (cause != EnumDropCause.PICK_BLOCK && tileEntity instanceof TileEntityBackpack) {
            return new ItemStack[]{takeItemFromTileEntity((TileEntityBackpack) tileEntity)};
        }
        return new ItemStack[]{BackpackItems.getItemForTier(this.tier).getDefaultStack()};
    }
}
