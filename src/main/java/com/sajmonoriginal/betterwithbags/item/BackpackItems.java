package com.sajmonoriginal.betterwithbags.item;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

public class BackpackItems {
    private static int currentId = BetterWithBagsMod.itemIdStart;
    private static boolean initialized = false;

    public static Item BACKPACK_SMALL;
    public static Item BACKPACK_MEDIUM;
    public static Item BACKPACK_LARGE;
    public static Item BACKPACK_REINFORCED;
    public static Item BACKPACK_NETHER;
    public static Item BEDROLL;
    public static Item MINERS_HELMET_LEATHER;
    public static Item MINERS_HELMET_LEATHER_ALT;
    public static Item MINERS_HELMET_LEATHER_CANDLE;
    public static Item MINERS_HELMET_LEATHER_CANDLE_ALT;
    public static Item MINERS_HELMET_BRICK;
    public static Item MINERS_HELMET_BRICK_ALT;
    public static Item MINERS_HELMET_BRICK_CANDLE;
    public static Item MINERS_HELMET_BRICK_CANDLE_ALT;
    public static Item MINERS_HELMET_GOLD;
    public static Item MINERS_HELMET_GOLD_ALT;
    public static Item MINERS_HELMET_GOLD_CANDLE;
    public static Item MINERS_HELMET_GOLD_CANDLE_ALT;

    private static String itemKey(String name) {
        return BetterWithBagsMod.MOD_ID + ":item/" + name;
    }

    public void initializeItems() {
        if (initialized) {
            BetterWithBagsMod.LOGGER.warn("BackpackItems.initializeItems() called twice - skipping");
            return;
        }
        initialized = true;

        BACKPACK_SMALL = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.small", itemKey("backpack_small"), currentId++, BackpackTier.SMALL));

        BACKPACK_MEDIUM = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.medium", itemKey("backpack_medium"), currentId++, BackpackTier.MEDIUM));

        BACKPACK_LARGE = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.large", itemKey("backpack_large"), currentId++, BackpackTier.LARGE));

        BACKPACK_REINFORCED = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.reinforced", itemKey("backpack_reinforced"), currentId++, BackpackTier.REINFORCED));

        BACKPACK_NETHER = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.nether", itemKey("backpack_nether"), currentId++, BackpackTier.NETHER));

        BEDROLL = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBedroll("bedroll", itemKey("bedroll"), currentId++));

        MINERS_HELMET_LEATHER = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_leather", itemKey("miners_helmet_leather"), currentId++, HelmetVariant.LEATHER_LAMP));

        MINERS_HELMET_LEATHER_ALT = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_leather_alt", itemKey("miners_helmet_leather_alt"), currentId++, HelmetVariant.LEATHER_LAMP_ALT));

        MINERS_HELMET_LEATHER_CANDLE = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_leather_candle", itemKey("miners_helmet_leather_candle"), currentId++, HelmetVariant.LEATHER_CANDLE));

        MINERS_HELMET_LEATHER_CANDLE_ALT = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_leather_candle_alt", itemKey("miners_helmet_leather_candle_alt"), currentId++, HelmetVariant.LEATHER_CANDLE_ALT));

        MINERS_HELMET_BRICK = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_brick", itemKey("miners_helmet_brick"), currentId++, HelmetVariant.BRICK_LAMP));

        MINERS_HELMET_BRICK_ALT = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_brick_alt", itemKey("miners_helmet_brick_alt"), currentId++, HelmetVariant.BRICK_LAMP_ALT));

        MINERS_HELMET_BRICK_CANDLE = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_brick_candle", itemKey("miners_helmet_brick_candle"), currentId++, HelmetVariant.BRICK_CANDLE));

        MINERS_HELMET_BRICK_CANDLE_ALT = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_brick_candle_alt", itemKey("miners_helmet_brick_candle_alt"), currentId++, HelmetVariant.BRICK_CANDLE_ALT));

        MINERS_HELMET_GOLD = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_gold", itemKey("miners_helmet_gold"), currentId++, HelmetVariant.GOLD_LAMP));

        MINERS_HELMET_GOLD_ALT = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_gold_alt", itemKey("miners_helmet_gold_alt"), currentId++, HelmetVariant.GOLD_LAMP_ALT));

        MINERS_HELMET_GOLD_CANDLE = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_gold_candle", itemKey("miners_helmet_gold_candle"), currentId++, HelmetVariant.GOLD_CANDLE));

        MINERS_HELMET_GOLD_CANDLE_ALT = new ItemBuilder(BetterWithBagsMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemMinersHelmet("miners_helmet_gold_candle_alt", itemKey("miners_helmet_gold_candle_alt"), currentId++, HelmetVariant.GOLD_CANDLE_ALT));

        BetterWithBagsMod.LOGGER.info("Registered {} items (IDs {}-{})", currentId - BetterWithBagsMod.itemIdStart, BetterWithBagsMod.itemIdStart, currentId - 1);
        BetterWithBagsMod.LOGGER.info("Candle alt items: leather={} (id={}), brick={} (id={}), gold={} (id={})",
            MINERS_HELMET_LEATHER_CANDLE_ALT != null ? MINERS_HELMET_LEATHER_CANDLE_ALT.getKey() : "NULL", MINERS_HELMET_LEATHER_CANDLE_ALT != null ? MINERS_HELMET_LEATHER_CANDLE_ALT.id : -1,
            MINERS_HELMET_BRICK_CANDLE_ALT != null ? MINERS_HELMET_BRICK_CANDLE_ALT.getKey() : "NULL", MINERS_HELMET_BRICK_CANDLE_ALT != null ? MINERS_HELMET_BRICK_CANDLE_ALT.id : -1,
            MINERS_HELMET_GOLD_CANDLE_ALT != null ? MINERS_HELMET_GOLD_CANDLE_ALT.getKey() : "NULL", MINERS_HELMET_GOLD_CANDLE_ALT != null ? MINERS_HELMET_GOLD_CANDLE_ALT.id : -1);
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
