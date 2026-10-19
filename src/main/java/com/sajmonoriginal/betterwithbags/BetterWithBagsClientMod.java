package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BackpackGuis;
import com.sajmonoriginal.betterwithbags.gui.ContainerHeldBackpack;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpack;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpackBlock;
import com.sajmonoriginal.betterwithbags.gui.ScreenBackpack;
import com.sajmonoriginal.betterwithbags.gui.ScreenBackpackBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.entry.ItemGuiEntry;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

@Environment(EnvType.CLIENT)
public class BetterWithBagsClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Key key = Key.of(BetterWithBagsMod.MOD_ID);
        ClientEvents.BLOCK_MODEL_RELOAD.listen(key, BackpackModels::initBlockModels);
        ClientEvents.ITEM_MODEL_RELOAD.listen(key, BackpackModels::initItemModels);
        ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(key, BackpackModels::initTileEntityModels);
        Catalyst.GUIS.register(BackpackGuis.HELD, new ItemGuiEntry<>(ContainerHeldBackpack.class, MenuBackpack.class, ScreenBackpack::new));
        Catalyst.GUIS.register(BackpackGuis.PLACED, new TileGuiEntry<>(TileEntityBackpack.class, MenuBackpackBlock.class, ScreenBackpackBlock::new));
        BetterWithBagsMod.LOGGER.info("Better With Bags client initialized!");
    }
}
