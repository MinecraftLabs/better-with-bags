package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.gui.BackpackInventoryPanel;
import com.sajmonoriginal.betterwithbags.gui.BackpackPanelLayout;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventory.class, remap = false)
public abstract class ScreenInventoryMixin extends ScreenContainerAbstract implements ResponsiveBackpackScreen {

    @Unique
    private static final int SCREEN_MARGIN = 8;

    @Unique
    private int bwb$backpackRows = 0;
    @Unique
    private boolean bwb$lastHadBackpack = false;
    @Unique
    private BackpackTier bwb$lastTier = null;
    @Unique
    private BackpackTier bwb$currentTier = null;
    @Unique
    private boolean bwb$renderingScaled = false;

    protected ScreenInventoryMixin(MenuAbstract menu) {
        super(menu);
    }

    @Unique
    private boolean bwb$hasBackpack() {
        if (this.mc == null || this.mc.thePlayer == null) return false;
        return ItemBackpack.isWearingBackpack(this.mc.thePlayer);
    }

    @Unique
    private BackpackTier bwb$getCurrentTier() {
        if (this.mc == null || this.mc.thePlayer == null) return null;
        ItemBackpack backpack = ItemBackpack.getWornBackpack(this.mc.thePlayer);
        return backpack != null ? backpack.getTier() : null;
    }

    @Unique
    private void bwb$refreshBackpack() {
        MenuInventory menu = (MenuInventory) this.inventorySlots;
        BackpackMenuCapable backpackMenu = (BackpackMenuCapable) menu;
        backpackMenu.betterWithBags$refreshBackpackSlots();

        if (!bwb$hasBackpack()) {
            this.bwb$backpackRows = 0;
            this.bwb$currentTier = null;
        } else {
            this.bwb$backpackRows = backpackMenu.betterWithBags$getBackpackRows();
            this.bwb$currentTier = bwb$getCurrentTier();
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bwb$adjustHeight(CallbackInfo ci) {
        this.bwb$lastHadBackpack = bwb$hasBackpack();
        this.bwb$lastTier = bwb$getCurrentTier();
        bwb$refreshBackpack();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwb$checkBackpackChange(CallbackInfo ci) {
        boolean hasBackpackNow = bwb$hasBackpack();
        BackpackTier tierNow = bwb$getCurrentTier();
        if (hasBackpackNow != this.bwb$lastHadBackpack || tierNow != this.bwb$lastTier) {
            this.bwb$lastHadBackpack = hasBackpackNow;
            this.bwb$lastTier = tierNow;
            bwb$refreshBackpack();
        }
    }

    @Unique
    private int bwb$getContentWidth() {
        if ((Object) this instanceof ScreenInventoryCreative) {
            return this.xSize;
        }
        return this.bwb$currentTier != null
            ? BackpackPanelLayout.getContentWidth(this.bwb$currentTier) : BackpackPanelLayout.PANEL_WIDTH;
    }

    @Unique
    private int bwb$getContentHeight() {
        return BackpackPanelLayout.getContentHeight(this.bwb$backpackRows);
    }

    @Unique
    private boolean bwb$contentOverflowsCurrentPosition() {
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;
        return guiLeft < SCREEN_MARGIN ||
            guiLeft + bwb$getContentWidth() > this.width - SCREEN_MARGIN ||
            guiTop < SCREEN_MARGIN ||
            guiTop + bwb$getContentHeight() > this.height - SCREEN_MARGIN;
    }

    @Override
    public boolean betterWithBags$hasResponsiveLayout() {
        return this.bwb$backpackRows > 0 && this.bwb$currentTier != null &&
            bwb$contentOverflowsCurrentPosition();
    }

    @Override
    public float betterWithBags$getGuiScale() {
        float horizontalScale = (this.width - SCREEN_MARGIN * 2) / (float) bwb$getContentWidth();
        float verticalScale = (this.height - SCREEN_MARGIN * 2) / (float) bwb$getContentHeight();
        return Math.min(1.0F, Math.min(horizontalScale, verticalScale));
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
        if (this.bwb$backpackRows > 0 && this.bwb$currentTier != null) {
            BackpackInventoryPanel.draw(this, this.bwb$currentTier, ItemBackpack.getWornBackpackStack(this.mc.thePlayer));
        }
    }
}
