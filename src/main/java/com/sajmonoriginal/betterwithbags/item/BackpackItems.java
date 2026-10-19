package com.sajmonoriginal.betterwithbags.item;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.tag.ItemTags;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public class BackpackItems {
    private static int currentId = BetterWithBagsMod.itemIdStart;
    private static boolean initialized = false;

    public static Item BACKPACK_SMALL;
    public static Item BACKPACK_MEDIUM;
    public static Item BACKPACK_LARGE;
    public static Item BACKPACK_REINFORCED;
    public static Item BACKPACK_NETHER;
    public static Item BEDROLL;

    private static String itemKey(String name) {
        return BetterWithBagsMod.MOD_ID + ":item/" + name;
    }

    private static ItemBuilder itemBuilder() {
        return new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .setCreativeInventoryPlacement(
                new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS));
    }

    private static ItemBuilder backpackBuilder() {
        return itemBuilder().addTags(ItemTags.IS_PERSISTENT);
    }

    public void initializeItems() {
        if (initialized) {
            BetterWithBagsMod.LOGGER.warn("BackpackItems.initializeItems() called twice - skipping");
            return;
        }
        initialized = true;

        BACKPACK_SMALL = backpackBuilder()
            .build(new ItemBackpack("backpack.small", itemKey("backpack_small"), currentId++, BackpackTier.SMALL));

        BACKPACK_MEDIUM = backpackBuilder()
            .build(new ItemBackpack("backpack.medium", itemKey("backpack_medium"), currentId++, BackpackTier.MEDIUM));

        BACKPACK_LARGE = backpackBuilder()
            .build(new ItemBackpack("backpack.large", itemKey("backpack_large"), currentId++, BackpackTier.LARGE));

        BACKPACK_REINFORCED = backpackBuilder()
            .build(new ItemBackpack("backpack.reinforced", itemKey("backpack_reinforced"), currentId++, BackpackTier.REINFORCED));

        BACKPACK_NETHER = backpackBuilder()
            .addTags(ItemTags.IS_FIRE_PROOF)
            .build(new ItemBackpack("backpack.nether", itemKey("backpack_nether"), currentId++, BackpackTier.NETHER));

        BEDROLL = itemBuilder()
            .build(new ItemBedroll("bedroll", itemKey("bedroll"), currentId++));
    }

    public static Item getItemForTier(BackpackTier tier) {
        if (tier == BackpackTier.SMALL) return BACKPACK_SMALL;
        if (tier == BackpackTier.MEDIUM) return BACKPACK_MEDIUM;
        if (tier == BackpackTier.LARGE) return BACKPACK_LARGE;
        if (tier == BackpackTier.REINFORCED) return BACKPACK_REINFORCED;
        if (tier == BackpackTier.NETHER) return BACKPACK_NETHER;
        return BACKPACK_SMALL;
    }
}
