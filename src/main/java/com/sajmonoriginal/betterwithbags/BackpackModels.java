package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBackpack;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBedroll;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBackpack;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBedroll;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelEmpty;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;

@Environment(EnvType.CLIENT)
public class BackpackModels {

    private static final String MOD_ID = BetterWithBagsMod.MOD_ID;

    public static void initBlockModels(BlockModelDispatcher dispatcher) {
        for (BackpackTier tier : BackpackTier.values()) {
            dispatcher.addDispatch(new BlockModelEmpty<>(BackpackBlocks.getBlockForTier(tier)).setAllTextures((IconCoordinate) null));
        }
        dispatcher.addDispatch(new BlockModelEmpty<>(BackpackBlocks.BEDROLL_BLOCK).setAllTextures(MOD_ID + ":block/bedroll/head_top"));
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
