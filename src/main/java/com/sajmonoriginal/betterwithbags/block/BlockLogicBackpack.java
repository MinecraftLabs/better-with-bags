package com.sajmonoriginal.betterwithbags.block;

import com.sajmonoriginal.betterwithbags.gui.BagScreens;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
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
    public void onPlacedByMob(World world, TilePosc pos, Side side, Mob mob, double xHit, double yHit) {
        Direction dir = mob.getHorizontalPlacementDirection(side).opposite();
        world.setBlockDataNotify(pos, dir.id);
    }

    @Override
    public void onPlacedOnSide(World world, TilePosc pos, Side side, double xHit, double yHit) {
        if (!side.isHorizontal()) {
            side = Side.SOUTH;
        }
        world.setBlockDataNotify(pos, side.direction().id);
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
                pickUpBackpack(world, pos, player, backpackTE);
            }
            return true;
        }

        if (!world.isClientSide) {
            ((BagScreens) player).betterWithBags$displayBackpackBlockScreen(backpackTE);
        }
        return true;
    }

    private void pickUpBackpack(World world, TilePosc pos, Player player, TileEntityBackpack backpackTE) {
        ItemStack backpackItem = backpackTE.takeAsItem();
        world.setBlockTypeNotify(pos, Blocks.AIR);
        player.inventory.insertItem(backpackItem, false);
        if (backpackItem.stackSize > 0) {
            player.dropPlayerItem(backpackItem);
        }
        world.playSoundAtEntity(null, player, "random.pop", 0.2f,
            ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause cause, TilePosc pos, int meta, TileEntity tileEntity) {
        if (cause == EnumDropCause.PICK_BLOCK) {
            return new ItemStack[]{BackpackItems.getItemForTier(this.tier).getDefaultStack()};
        }
        if (tileEntity == null) {
            tileEntity = world.getTileEntity(pos);
        }
        if (tileEntity instanceof TileEntityBackpack) {
            TileEntityBackpack backpackTE = (TileEntityBackpack) tileEntity;
            if (backpackTE.isTaken()) {
                return null;
            }
            syncTier(backpackTE);
            return new ItemStack[]{backpackTE.takeAsItem()};
        }
        return new ItemStack[]{BackpackItems.getItemForTier(this.tier).getDefaultStack()};
    }
}
