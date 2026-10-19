package com.sajmonoriginal.betterwithbags.network;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BagScreens;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class OpenBackpackScreenMessage implements NetworkMessage {
    private int windowId;
    private boolean placed;
    private BackpackTier tier = BackpackTier.SMALL;
    private int slot = -1;
    private String customName = "";

    public OpenBackpackScreenMessage() {
    }

    public static OpenBackpackScreenMessage held(int windowId, int slot, BackpackTier tier) {
        OpenBackpackScreenMessage message = new OpenBackpackScreenMessage();
        message.windowId = windowId;
        message.placed = false;
        message.tier = tier;
        message.slot = slot;
        return message;
    }

    public static OpenBackpackScreenMessage placed(int windowId, BackpackTier tier, String customName) {
        OpenBackpackScreenMessage message = new OpenBackpackScreenMessage();
        message.windowId = windowId;
        message.placed = true;
        message.tier = tier;
        message.customName = customName == null ? "" : customName;
        return message;
    }

    @Override
    public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
        packet.writeInt(windowId);
        packet.writeBoolean(placed);
        packet.writeEnumConstant(tier);
        packet.writeInt(slot);
        packet.writeString(customName);
    }

    @Override
    public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
        windowId = packet.readInt();
        placed = packet.readBoolean();
        tier = packet.readEnumConstant(BackpackTier.class);
        slot = packet.readInt();
        customName = packet.readString();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleClientEnv(NetworkContext context) {
        Player player = context.player;
        if (!(player instanceof BagScreens)) {
            return;
        }

        BagScreens screens = (BagScreens) player;
        if (placed) {
            TileEntityBackpack tileEntity = new TileEntityBackpack();
            tileEntity.setTier(tier);
            tileEntity.setCustomName(customName);
            screens.betterWithBags$displayBackpackBlockScreen(tileEntity);
        } else {
            ItemStack stack = slot >= 0 && slot < player.inventory.mainInventory.length
                ? player.inventory.mainInventory[slot] : null;
            if (slot != player.inventory.getCurrentSlot() || stack == null
                || !(stack.getItem() instanceof ItemBackpack)
                || ((ItemBackpack) stack.getItem()).getTier() != tier) {
                screens.betterWithBags$closeBackpackScreen();
                return;
            }
            screens.betterWithBags$displayBackpackScreen(stack);
        }
        player.containerMenu.containerId = windowId;
    }
}
