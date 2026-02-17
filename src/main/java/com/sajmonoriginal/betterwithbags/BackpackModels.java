package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.model.ItemModelBackpack;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBackpack;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBedroll;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBackpack;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBedroll;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelEmpty;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class BackpackModels implements ModelEntrypoint {

    private static final String MOD_ID = BetterWithBagsMod.MOD_ID;

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_SMALL, new BlockModelEmpty<>(BackpackBlocks.BACKPACK_BLOCK_SMALL));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_MEDIUM, new BlockModelEmpty<>(BackpackBlocks.BACKPACK_BLOCK_MEDIUM));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_LARGE, new BlockModelEmpty<>(BackpackBlocks.BACKPACK_BLOCK_LARGE));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_REINFORCED, new BlockModelEmpty<>(BackpackBlocks.BACKPACK_BLOCK_REINFORCED));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_NETHER, new BlockModelEmpty<>(BackpackBlocks.BACKPACK_BLOCK_NETHER));
        dispatcher.addDispatch(BackpackBlocks.BEDROLL_BLOCK, new BlockModelEmpty<>(BackpackBlocks.BEDROLL_BLOCK));
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_SMALL, null)
            .setIcon(MOD_ID + ":item/backpack_small"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_MEDIUM, null)
            .setIcon(MOD_ID + ":item/backpack_medium"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_LARGE, null)
            .setIcon(MOD_ID + ":item/backpack_large"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_REINFORCED, null)
            .setIcon(MOD_ID + ":item/backpack_reinforced"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_NETHER, null)
            .setIcon(MOD_ID + ":item/backpack_nether"));

        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.BEDROLL, null)
            .setIcon(MOD_ID + ":item/bedroll"));

        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_LEATHER, null)
            .setIcon(MOD_ID + ":item/miners_helmet_leather"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_LEATHER_ALT, null)
            .setIcon(MOD_ID + ":item/miners_helmet_leather_alt"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_LEATHER_CANDLE, null)
            .setIcon(MOD_ID + ":item/miners_helmet_leather_candle"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_LEATHER_CANDLE_ALT, null)
            .setIcon(MOD_ID + ":item/miners_helmet_leather_candle_alt"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_BRICK, null)
            .setIcon(MOD_ID + ":item/miners_helmet_brick"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_BRICK_ALT, null)
            .setIcon(MOD_ID + ":item/miners_helmet_brick_alt"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_BRICK_CANDLE, null)
            .setIcon(MOD_ID + ":item/miners_helmet_brick_candle"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_BRICK_CANDLE_ALT, null)
            .setIcon(MOD_ID + ":item/miners_helmet_brick_candle_alt"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_GOLD, null)
            .setIcon(MOD_ID + ":item/miners_helmet_gold"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_GOLD_ALT, null)
            .setIcon(MOD_ID + ":item/miners_helmet_gold_alt"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_GOLD_CANDLE, null)
            .setIcon(MOD_ID + ":item/miners_helmet_gold_candle"));
        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.MINERS_HELMET_GOLD_CANDLE_ALT, null)
            .setIcon(MOD_ID + ":item/miners_helmet_gold_candle_alt"));

        for (BackpackTier tier : BackpackTier.values()) {
            Block<?> block = BackpackBlocks.getBlockForTier(tier);
            dispatcher.addDispatch(new ItemModelPlacedBackpack(block.asItem(), tier));
        }

        dispatcher.addDispatch(new ItemModelPlacedBedroll(BackpackBlocks.BEDROLL_BLOCK.asItem()));
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        ModelHelper.setTileEntityModel(TileEntityBackpack.class, TileEntityRendererBackpack::new);
        ModelHelper.setTileEntityModel(TileEntityBedroll.class, TileEntityRendererBedroll::new);
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
