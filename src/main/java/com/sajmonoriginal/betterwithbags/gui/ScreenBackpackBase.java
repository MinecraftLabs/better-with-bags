package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.lang.I18n;

@Environment(EnvType.CLIENT)
public abstract class ScreenBackpackBase extends ScreenContainerAbstract implements ResponsiveBackpackScreen {
    private static final String GUI_TEXTURE_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/gui/backpack_";
    private static final int SCREEN_MARGIN = 8;

    protected final MenuBackpackBase menu;
    private final String guiTexture;
    private boolean renderingScaled;

    protected ScreenBackpackBase(MenuBackpackBase menu) {
        super(menu);
        this.menu = menu;
        this.xSize = menu.getTier().supportsAttachment() ? 199 : 176;
        this.ySize = 121 + menu.getRows() * 18;
        this.guiTexture = GUI_TEXTURE_BASE + menu.getTier().getName() + ".png";
    }

    protected abstract String getTitle();

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks) {
        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture(guiTexture).bind();

        int rows = menu.getRows();
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(guiLeft, guiTop + rows * 18 + 17, 0, 118, 176, 103);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, rows * 18 + 17 + 7);

        if (menu.getTier().supportsAttachment()) {
            this.drawTexturedModalRect(guiLeft + 174, guiTop + 13, 174, 13, 25, 26);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer() {
        this.fontRenderer.render(getTitle(), 8, 6).setColor(0xFFFFFF).call();
        this.fontRenderer.render(I18n.getInstance().translateKey("container.inventory.name"), 8, this.ySize - 96 + 2).setColor(0x404040).call();
    }

    @Override
    public void tick() {
        super.tick();
        if (!menu.stillValid(this.mc.thePlayer)) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    public boolean betterWithBags$hasResponsiveLayout() {
        return betterWithBags$getGuiScale() < 1.0F;
    }

    @Override
    public float betterWithBags$getGuiScale() {
        float horizontalScale = (this.width - SCREEN_MARGIN * 2) / (float) this.xSize;
        float verticalScale = (this.height - SCREEN_MARGIN * 2) / (float) this.ySize;
        return Math.min(1.0F, Math.min(horizontalScale, verticalScale));
    }

    @Override
    public float betterWithBags$getContentCenterX() {
        return this.width / 2.0F;
    }

    @Override
    public float betterWithBags$getContentCenterY() {
        return this.height / 2.0F;
    }

    @Override
    public boolean betterWithBags$isRenderingScaled() {
        return this.renderingScaled;
    }

    @Override
    public void betterWithBags$setRenderingScaled(boolean scaled) {
        this.renderingScaled = scaled;
    }
}
