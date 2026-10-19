package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.item.ItemStack;

import static com.sajmonoriginal.betterwithbags.gui.BackpackPanelLayout.*;

@Environment(EnvType.CLIENT)
public final class BackpackInventoryPanel {
    private static final String TEXTURE_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/gui/backpack_extension_";

    private BackpackInventoryPanel() {
    }

    public static void draw(ScreenContainerAbstract screen, BackpackTier tier, ItemStack backpackStack) {
        int guiLeft = (screen.width - screen.xSize) / 2;
        int guiTop = (screen.height - screen.ySize) / 2;
        int panelY = guiTop + INVENTORY_HEIGHT - PANEL_OVERLAP;
        int panelHeight = PANEL_OVERLAP + PANEL_TITLE_HEIGHT + tier.getRows() * ROW_HEIGHT + PANEL_BOTTOM_HEIGHT;

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().textureManager.loadTexture(TEXTURE_BASE + tier.getName() + ".png").bind();
        screen.drawTexturedModalRect(guiLeft, panelY, 0, 0, PANEL_WIDTH, panelHeight);
        if (tier.supportsAttachment()) {
            screen.drawTexturedModalRect(guiLeft + 174, panelY + 13, 174, 13, 36, 26);
        }

        String title = backpackStack != null ? backpackStack.getDisplayName() : "";
        screen.fontRenderer.render(title, guiLeft + 8, panelY + PANEL_OVERLAP + 4).setColor(0xFFFFFF).call();
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
