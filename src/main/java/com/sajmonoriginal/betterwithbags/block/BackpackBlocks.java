package com.sajmonoriginal.betterwithbags.block;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;

import java.util.EnumMap;
import java.util.Map;

public class BackpackBlocks {

    public static Block<?> BACKPACK_BLOCK_SMALL;
    public static Block<?> BACKPACK_BLOCK_MEDIUM;
    public static Block<?> BACKPACK_BLOCK_LARGE;
    public static Block<?> BACKPACK_BLOCK_REINFORCED;
    public static Block<?> BACKPACK_BLOCK_NETHER;
    public static Block<?> BEDROLL_BLOCK;

    private static final Map<BackpackTier, Block<?>> BY_TIER = new EnumMap<>(BackpackTier.class);

    public void initializeBlocks() {
        int id = BetterWithBagsMod.blockIdStart;

        BACKPACK_BLOCK_SMALL = buildBackpackBlock("backpack_block_small", id++, BackpackTier.SMALL);

        BEDROLL_BLOCK = new BlockBuilder(BetterWithBagsMod.MOD_ID)
            .setHardness(0.3f)
            .setResistance(0.5f)
            .setBlockSound(BlockSounds.CLOTH)
            .setTileEntity(TileEntityBedroll::new)
            .setUseInternalLight()
            .setLightOpacity(0)
            .build("bedroll", id++, BlockLogicBedroll::new);

        BACKPACK_BLOCK_MEDIUM = buildBackpackBlock("backpack_block_medium", id++, BackpackTier.MEDIUM);
        BACKPACK_BLOCK_LARGE = buildBackpackBlock("backpack_block_large", id++, BackpackTier.LARGE);
        BACKPACK_BLOCK_REINFORCED = buildBackpackBlock("backpack_block_reinforced", id++, BackpackTier.REINFORCED);
        BACKPACK_BLOCK_NETHER = buildBackpackBlock("backpack_block_nether", id++, BackpackTier.NETHER);

        TileEntityDispatcher.addMapping(
            TileEntityBackpack.class,
            new NamespaceID(BetterWithBagsMod.MOD_ID, "backpack")
        );

        TileEntityDispatcher.addMapping(
            TileEntityBedroll.class,
            new NamespaceID(BetterWithBagsMod.MOD_ID, "bedroll")
        );
    }

    private Block<?> buildBackpackBlock(String key, int id, BackpackTier tier) {
        Block<?> block = new BlockBuilder(BetterWithBagsMod.MOD_ID)
            .setHardness(0.6f)
            .setResistance(1.0f)
            .setBlockSound(BlockSounds.CLOTH)
            .setTileEntity(TileEntityBackpack::new)
            .setUseInternalLight()
            .setLightOpacity(0)
            .build(key, id, built -> new BlockLogicBackpack(built, tier));
        BY_TIER.put(tier, block);
        return block;
    }

    public static Block<?> getBlockForTier(BackpackTier tier) {
        return BY_TIER.get(tier);
    }

    public static BackpackTier getTierForBlock(Block<?> block) {
        return block != null && block.getLogic() instanceof BlockLogicBackpack
            ? ((BlockLogicBackpack) block.getLogic()).getTier() : null;
    }
}
