package com.sajmonoriginal.betterwithbags.item;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IPlaceable;
import net.minecraft.core.item.ItemBed;
import net.minecraft.core.item.ItemPlaceablePair;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

public class ItemBedroll extends ItemPlaceablePair {

    public ItemBedroll(String name, String textureKey, int id) {
        super(name, textureKey, id, BackpackBlocks.BEDROLL_BLOCK, BackpackBlocks.BEDROLL_BLOCK);
        this.maxStackSize = 1;
    }

    @Override
    protected PlacementResult getPlacement(ItemStack selfStack, World world, Player player, TilePosc blockPos, Side side,
                                           double xHit, double yHit, PlacementResult result) {
        Direction dir = ItemBed.getDirection(player, side);
        result.dataA = result.dataB = dir.legacyIndex();
        result.dataB = BlockLogicBed.IS_HEAD.set(result.dataB, 1);
        dir.getOffset(result.offsetB);
        result.offsetB.mul(-1);
        return result;
    }

    @Override
    public boolean canPlaceDirectlyAtPosition(ItemStack selfStack, World world, Player player, TilePosc blockPos, Side side,
                                              double xHit, double yHit) {
        Direction dir = ItemBed.getDirection(player, side);
        return IPlaceable.canPlaceBlockDirectlyAtPosition(this.blockA, world, blockPos, side)
            && IPlaceable.canPlaceBlockDirectlyAtPosition(this.blockB, world, blockPos.sub(dir, new TilePos()), side);
    }
}
