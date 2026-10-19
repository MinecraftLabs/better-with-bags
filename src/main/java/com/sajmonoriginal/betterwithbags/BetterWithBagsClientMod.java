package com.sajmonoriginal.betterwithbags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

@Environment(EnvType.CLIENT)
public class BetterWithBagsClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Key key = Key.of(BetterWithBagsMod.MOD_ID);
        ClientEvents.BLOCK_MODEL_RELOAD.listen(key, BackpackModels::initBlockModels);
        ClientEvents.ITEM_MODEL_RELOAD.listen(key, BackpackModels::initItemModels);
        ClientEvents.ENTITY_RENDERER_RELOAD.listen(key, dispatcher -> BackpackModels.loadBwbGeoModels());
        ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(key, BackpackModels::initTileEntityModels);
        BetterWithBagsMod.LOGGER.info("Better With Bags client initialized!");
    }
}
