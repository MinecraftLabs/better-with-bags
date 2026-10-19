package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.network.OpenBackpackScreenMessage;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.dependency.Key;
import turniplabs.halplibe.util.toml.Toml;

public class BetterWithBagsMod implements ModInitializer {
    public static final String MOD_ID = HalpLibe.registerMod("betterwithbags");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int itemIdStart;
    public static int blockIdStart;

    static {
        Toml defaults = new Toml("Better With Bags");
        defaults.addEntry("starting_item_id", "First item ID", 29000);
        defaults.addEntry("starting_block_id", "First block ID", 4000);
        TomlConfigHandler config = new TomlConfigHandler(MOD_ID, defaults);
        itemIdStart = config.getInt("starting_item_id");
        blockIdStart = config.getInt("starting_block_id");
    }

    @Override
    public void onInitialize() {
        Key key = Key.of(MOD_ID);
        CommonEvents.BEFORE_GAME_START.listen(key, BetterWithBagsMod::registerContent);
        CommonEvents.RECIPES_NAMESPACE_INIT.listen(key, BackpackRecipes::initNamespaces);
        CommonEvents.RECIPES_READY.listen(key, BackpackRecipes::registerRecipes);
        NetworkHandler.registerNetworkMessage(OpenBackpackScreenMessage::new);
        LOGGER.info("Better With Bags initialized!");
    }

    private static void registerContent() {
        BackpackRecipes.registerRecipeTypes();
        new BackpackBlocks().initializeBlocks();
        new BackpackItems().initializeItems();
        LOGGER.info("Registered items starting at ID {} and blocks starting at ID {}", itemIdStart, blockIdStart);
    }
}
