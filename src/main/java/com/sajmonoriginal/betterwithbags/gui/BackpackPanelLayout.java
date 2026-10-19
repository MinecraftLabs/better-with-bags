package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;

public final class BackpackPanelLayout {
    public static final int INVENTORY_HEIGHT = 166;
    public static final int ROW_HEIGHT = 18;
    public static final int PANEL_WIDTH = 176;
    public static final int PANEL_WIDTH_WITH_ATTACHMENT = 210;
    public static final int PANEL_OVERLAP = 3;
    public static final int PANEL_TITLE_HEIGHT = 14;
    public static final int PANEL_BOTTOM_HEIGHT = 7;
    public static final int FIRST_SLOT_Y = INVENTORY_HEIGHT + PANEL_TITLE_HEIGHT + 1;
    public static final int ATTACHMENT_SLOT_X = 178;

    private BackpackPanelLayout() {
    }

    public static int getContentWidth(BackpackTier tier) {
        return tier.supportsAttachment() ? PANEL_WIDTH_WITH_ATTACHMENT : PANEL_WIDTH;
    }

    public static int getContentHeight(int rows) {
        return INVENTORY_HEIGHT + PANEL_TITLE_HEIGHT + rows * ROW_HEIGHT + PANEL_BOTTOM_HEIGHT;
    }
}
