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
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.dependency.Key;

import java.util.Properties;

public class BetterWithBagsMod implements ModInitializer {
    public static final String MOD_ID = HalpLibe.registerMod("betterwithbags");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int itemIdStart;
    public static int blockIdStart;

    static {
        Properties prop = new Properties();
        prop.setProperty("starting_item_id", "29000");
        prop.setProperty("starting_block_id", "4000");
        ConfigHandler config = new ConfigHandler(MOD_ID, prop);
        itemIdStart = config.getInt("starting_item_id");
        blockIdStart = config.getInt("starting_block_id");
        config.updateConfig();
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
        new BackpackItems().initializeItems();
        new BackpackBlocks().initializeBlocks();
        LOGGER.info("Registered items starting at ID {} and blocks starting at ID {}", itemIdStart, blockIdStart);
    }
}
