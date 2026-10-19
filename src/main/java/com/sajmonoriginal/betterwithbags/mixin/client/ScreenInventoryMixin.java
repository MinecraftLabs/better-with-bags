package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.item.ItemStack;
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
    private static final String EXTENSION_TEXTURE_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/gui/backpack_extension_";
    @Unique
    private static final int STANDARD_HEIGHT = 166;
    @Unique
    private static final int ROW_HEIGHT = 18;
    @Unique
    private static final int EXTENSION_WIDTH = 176;
    @Unique
    private static final int EXTENSION_OVERLAP = 3;
    @Unique
    private static final int EXTENSION_TITLE_HEIGHT = 14;
    @Unique
    private static final int EXTENSION_BOTTOM_HEIGHT = 7;
    @Unique
    private static final int SCREEN_MARGIN = 8;
    @Unique
    private static final int ATTACHMENT_PANEL_WIDTH = 210;

    @Unique
    private int bwb$backpackRows = 0;
    @Unique
    private boolean bwb$lastHadBackpack = false;
    @Unique
    private BackpackTier bwb$lastTier = null;
    @Unique
    private BackpackTier bwb$currentTier = null;
    @Unique
    private boolean bwb$renderingResponsive = false;

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

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void bwb$renderResponsive(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if ((Object) this instanceof ScreenInventoryCreative ||
            this.bwb$renderingResponsive || !betterWithBags$hasResponsiveLayout()) {
            return;
        }

        this.renderBackground();
        GLRenderer.pushFrame();
        float scale = bwb$getGuiScale();
        GLRenderer.modelM4f()
            .translate(this.width / 2, this.height / 2, 0.0F)
            .scale(scale, scale, 1.0F)
            .translate(-bwb$getContentCenterX(), -bwb$getContentCenterY(), 0.0F);
        this.bwb$renderingResponsive = true;
        try {
            ((ScreenInventory) (Object) this).render(
                betterWithBags$transformMouseX(mouseX),
                betterWithBags$transformMouseY(mouseY),
                partialTicks);
        } finally {
            this.bwb$renderingResponsive = false;
            GLRenderer.popFrame();
        }
        ci.cancel();
    }

    @Unique
    private int bwb$getContentWidth() {
        if ((Object) this instanceof ScreenInventoryCreative) {
            return this.xSize;
        }
        return this.bwb$currentTier != null && this.bwb$currentTier.supportsAttachment()
            ? ATTACHMENT_PANEL_WIDTH : EXTENSION_WIDTH;
    }

    @Unique
    private int bwb$getContentHeight() {
        return STANDARD_HEIGHT + EXTENSION_TITLE_HEIGHT + this.bwb$backpackRows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
    }

    @Unique
    private int bwb$getContentCenterX() {
        int guiLeft = (this.width - this.xSize) / 2;
        return guiLeft + bwb$getContentWidth() / 2;
    }

    @Unique
    private int bwb$getContentCenterY() {
        int guiTop = (this.height - this.ySize) / 2;
        return guiTop + bwb$getContentHeight() / 2;
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

    @Unique
    private float bwb$getGuiScale() {
        float horizontalScale = (this.width - SCREEN_MARGIN * 2) / (float) bwb$getContentWidth();
        float verticalScale = (this.height - SCREEN_MARGIN * 2) / (float) bwb$getContentHeight();
        return Math.min(1.0F, Math.min(horizontalScale, verticalScale));
    }

    @Override
    public boolean betterWithBags$hasResponsiveLayout() {
        return this.bwb$backpackRows > 0 && this.bwb$currentTier != null &&
            bwb$contentOverflowsCurrentPosition();
    }

    @Override
    public boolean betterWithBags$isRenderingResponsive() {
        return this.bwb$renderingResponsive;
    }

    @Override
    public int betterWithBags$transformMouseX(int mouseX) {
        return Math.round(bwb$getContentCenterX() + (mouseX - this.width / 2) / bwb$getGuiScale());
    }

    @Override
    public int betterWithBags$transformMouseY(int mouseY) {
        return Math.round(bwb$getContentCenterY() + (mouseY - this.height / 2) / bwb$getGuiScale());
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"))
    private void bwb$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        if (this.bwb$backpackRows <= 0 || this.bwb$currentTier == null) {
            return;
        }

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int panelY = guiTop + STANDARD_HEIGHT - EXTENSION_OVERLAP;

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        String extTexture = EXTENSION_TEXTURE_BASE + this.bwb$currentTier.getName() + ".png";
        this.mc.textureManager.loadTexture(extTexture).bind();

        int panelHeight = EXTENSION_OVERLAP + EXTENSION_TITLE_HEIGHT + this.bwb$backpackRows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        this.drawTexturedModalRect(guiLeft, panelY, 0, 0, EXTENSION_WIDTH, panelHeight);

        if (this.bwb$currentTier.supportsAttachment()) {
            this.drawTexturedModalRect(guiLeft + 174, panelY + 13, 174, 13, 36, 26);
        }

        ItemStack wornStack = ItemBackpack.getWornBackpackStack(this.mc.thePlayer);
        String title = wornStack != null ? wornStack.getDisplayName() : "";
        this.fontRenderer.render(title, guiLeft + 8, panelY + EXTENSION_OVERLAP + 4).setColor(0xFFFFFF).call();
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
