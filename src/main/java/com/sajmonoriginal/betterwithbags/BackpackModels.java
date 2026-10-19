package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.model.BlockModelBag;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBackpack;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBedroll;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBackpack;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBedroll;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;

@Environment(EnvType.CLIENT)
public class BackpackModels {

    private static final String MOD_ID = BetterWithBagsMod.MOD_ID;

    public static void initBlockModels(BlockModelDispatcher dispatcher) {
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_SMALL, new BlockModelBag<>(BackpackBlocks.BACKPACK_BLOCK_SMALL));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_MEDIUM, new BlockModelBag<>(BackpackBlocks.BACKPACK_BLOCK_MEDIUM));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_LARGE, new BlockModelBag<>(BackpackBlocks.BACKPACK_BLOCK_LARGE));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_REINFORCED, new BlockModelBag<>(BackpackBlocks.BACKPACK_BLOCK_REINFORCED));
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK_NETHER, new BlockModelBag<>(BackpackBlocks.BACKPACK_BLOCK_NETHER));
        dispatcher.addDispatch(BackpackBlocks.BEDROLL_BLOCK, new BlockModelBag<>(BackpackBlocks.BEDROLL_BLOCK));
    }

    public static void initItemModels(ItemModelDispatcher dispatcher) {
        for (BackpackTier tier : BackpackTier.values()) {
            dispatcher.addDispatch(new ItemModelStandard(BackpackItems.getItemForTier(tier), false)
                .setIcon(MOD_ID + ":item/backpack_" + tier.getName()));
        }

        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.BEDROLL, false)
            .setIcon(MOD_ID + ":item/bedroll"));

        for (BackpackTier tier : BackpackTier.values()) {
            Block<?> block = BackpackBlocks.getBlockForTier(tier);
            dispatcher.addDispatch(new ItemModelPlacedBackpack(block.asItem(), tier));
        }

        dispatcher.addDispatch(new ItemModelPlacedBedroll(BackpackBlocks.BEDROLL_BLOCK.asItem()));
    }

    public static void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        dispatcher.assignRenderer(TileEntityBackpack.class, new TileEntityRendererBackpack());
        dispatcher.assignRenderer(TileEntityBedroll.class, new TileEntityRendererBedroll());
    }
}
