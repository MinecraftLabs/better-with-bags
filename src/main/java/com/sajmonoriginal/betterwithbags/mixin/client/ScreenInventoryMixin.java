package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.gui.BackpackInventoryPanel;
import com.sajmonoriginal.betterwithbags.gui.BackpackPanelLayout;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveScreens;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventory.class, remap = false)
public abstract class ScreenInventoryMixin extends ScreenContainerAbstract implements ResponsiveBackpackScreen {

    @Unique
    private BackpackTier bwb$currentTier = null;
    @Unique
    private boolean bwb$renderingScaled = false;

    protected ScreenInventoryMixin(MenuAbstract menu) {
        super(menu);
    }

    @Unique
    private BackpackTier bwb$getWornTier() {
        if (this.mc == null || this.mc.thePlayer == null) return null;
        return ItemBackpack.getBackpackTier(ItemBackpack.getWornBackpackStack(this.mc.thePlayer));
    }

    @Unique
    private void bwb$refreshBackpack() {
        ((BackpackMenuCapable) this.inventorySlots).betterWithBags$refreshBackpackSlots();
        this.bwb$currentTier = bwb$getWornTier();
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bwb$adjustHeight(CallbackInfo ci) {
        bwb$refreshBackpack();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwb$checkBackpackChange(CallbackInfo ci) {
        if (bwb$getWornTier() != this.bwb$currentTier) {
            bwb$refreshBackpack();
        }
    }

    @Unique
    private int bwb$getContentWidth() {
        int panelWidth = this.bwb$currentTier != null
            ? BackpackPanelLayout.getContentWidth(this.bwb$currentTier) : BackpackPanelLayout.PANEL_WIDTH;
        return Math.max(this.xSize, panelWidth);
    }

    @Unique
    private int bwb$getContentHeight() {
        return BackpackPanelLayout.getContentHeight(this.bwb$currentTier != null ? this.bwb$currentTier.getRows() : 0);
    }

    @Unique
    private boolean bwb$contentOverflowsCurrentPosition() {
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;
        int margin = ResponsiveScreens.SCREEN_MARGIN;
        return guiLeft < margin ||
            guiLeft + bwb$getContentWidth() > this.width - margin ||
            guiTop < margin ||
            guiTop + bwb$getContentHeight() > this.height - margin;
    }

    @Override
    public boolean betterWithBags$hasResponsiveLayout() {
        return this.bwb$currentTier != null && bwb$contentOverflowsCurrentPosition();
    }

    @Override
    public float betterWithBags$getGuiScale() {
        return ResponsiveScreens.getFitScale(this, bwb$getContentWidth(), bwb$getContentHeight());
    }

    @Override
    public float betterWithBags$getContentCenterX() {
        return (this.width - this.xSize) / 2 + bwb$getContentWidth() / 2;
    }

    @Override
    public float betterWithBags$getContentCenterY() {
        return (this.height - this.ySize) / 2 + bwb$getContentHeight() / 2;
    }

    @Override
    public boolean betterWithBags$isRenderingScaled() {
        return this.bwb$renderingScaled;
    }

    @Override
    public void betterWithBags$setRenderingScaled(boolean scaled) {
        this.bwb$renderingScaled = scaled;
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"))
    private void bwb$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        if (this.bwb$currentTier != null) {
            BackpackInventoryPanel.draw(this, this.bwb$currentTier, ItemBackpack.getWornBackpackStack(this.mc.thePlayer));
        }
    }
}
