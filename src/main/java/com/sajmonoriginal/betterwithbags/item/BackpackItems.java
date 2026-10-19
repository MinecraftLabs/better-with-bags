package com.sajmonoriginal.betterwithbags.item;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.tag.ItemTags;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.EnumMap;
import java.util.Map;

public class BackpackItems {
    private static int currentId = BetterWithBagsMod.itemIdStart;
    private static boolean initialized = false;

    public static Item BACKPACK_SMALL;
    public static Item BACKPACK_MEDIUM;
    public static Item BACKPACK_LARGE;
    public static Item BACKPACK_REINFORCED;
    public static Item BACKPACK_NETHER;
    public static Item BEDROLL;

    private static final Map<BackpackTier, Item> BY_TIER = new EnumMap<>(BackpackTier.class);

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

        BY_TIER.put(BackpackTier.SMALL, BACKPACK_SMALL);
        BY_TIER.put(BackpackTier.MEDIUM, BACKPACK_MEDIUM);
        BY_TIER.put(BackpackTier.LARGE, BACKPACK_LARGE);
        BY_TIER.put(BackpackTier.REINFORCED, BACKPACK_REINFORCED);
        BY_TIER.put(BackpackTier.NETHER, BACKPACK_NETHER);

        BEDROLL = itemBuilder()
            .build(new ItemBedroll("bedroll", itemKey("bedroll"), currentId++));
    }

    public static Item getItemForTier(BackpackTier tier) {
        return BY_TIER.get(tier);
    }
}
