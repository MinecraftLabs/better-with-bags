package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventoryCreative.class, remap = false)
public abstract class ScreenInventoryCreativeMixin extends ScreenInventory {

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
    private boolean bwb$creative$lastHadBackpack = false;
    @Unique
    private BackpackTier bwb$creative$lastTier = null;
    @Unique
    private boolean bwb$creative$renderingResponsive = false;
    @Unique
    private boolean bwb$creative$transformingClick = false;

    protected ScreenInventoryCreativeMixin(net.minecraft.core.entity.player.Player player) {
        super(player);
    }

    @Unique
    private boolean bwb$creative$hasBackpack() {
        if (this.mc == null || this.mc.thePlayer == null) return false;
        return ItemBackpack.isWearingBackpack(this.mc.thePlayer);
    }

    @Unique
    private BackpackTier bwb$creative$getCurrentTier() {
        if (this.mc == null || this.mc.thePlayer == null) return null;
        ItemBackpack backpack = ItemBackpack.getWornBackpack(this.mc.thePlayer);
        return backpack != null ? backpack.getTier() : null;
    }

    @Unique
    private void bwb$creative$refreshBackpack() {
        if (this.inventorySlots instanceof MenuInventory) {
            MenuInventory menu = (MenuInventory) this.inventorySlots;
            BackpackMenuCapable backpackMenu = (BackpackMenuCapable) menu;
            backpackMenu.betterWithBags$refreshBackpackSlots();
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bwb$creative$onInit(CallbackInfo ci) {
        this.bwb$creative$lastHadBackpack = bwb$creative$hasBackpack();
        this.bwb$creative$lastTier = bwb$creative$getCurrentTier();
        bwb$creative$refreshBackpack();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwb$creative$onTick(CallbackInfo ci) {
        boolean hasBackpackNow = bwb$creative$hasBackpack();
        BackpackTier tierNow = bwb$creative$getCurrentTier();
        if (hasBackpackNow != this.bwb$creative$lastHadBackpack || tierNow != this.bwb$creative$lastTier) {
            this.bwb$creative$lastHadBackpack = hasBackpackNow;
            this.bwb$creative$lastTier = tierNow;
            bwb$creative$refreshBackpack();
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void bwb$creative$renderResponsive(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ResponsiveBackpackScreen responsive = (ResponsiveBackpackScreen) this;
        if (this.bwb$creative$renderingResponsive || !responsive.betterWithBags$hasResponsiveLayout()) {
            return;
        }

        BackpackTier tier = bwb$creative$getCurrentTier();
        if (tier == null) {
            return;
        }

        this.renderBackground();
        GLRenderer.pushFrame();
        int contentHeight = STANDARD_HEIGHT + EXTENSION_TITLE_HEIGHT + tier.getRows() * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        float horizontalScale = (this.width - 16) / (float) this.xSize;
        float verticalScale = (this.height - 16) / (float) contentHeight;
        float scale = Math.min(1.0F, Math.min(horizontalScale, verticalScale));
        int guiLeft = (this.width - this.xSize) / 2;
        int contentCenterX = guiLeft + this.xSize / 2;
        int guiTop = (this.height - this.ySize) / 2;
        int contentCenterY = guiTop + contentHeight / 2;
        GLRenderer.modelM4f()
            .translate(this.width / 2, this.height / 2, 0.0F)
            .scale(scale, scale, 1.0F)
            .translate(-contentCenterX, -contentCenterY, 0.0F);
        this.bwb$creative$renderingResponsive = true;
        ResponsiveBackpackScreen.RENDERING_RESPONSIVE.set(true);
        try {
            ((ScreenInventoryCreative) (Object) this).render(
                responsive.betterWithBags$transformMouseX(mouseX),
                responsive.betterWithBags$transformMouseY(mouseY),
                partialTicks);
        } finally {
            ResponsiveBackpackScreen.RENDERING_RESPONSIVE.set(false);
            this.bwb$creative$renderingResponsive = false;
            GLRenderer.popFrame();
        }
        ci.cancel();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void bwb$creative$transformMouseClick(int mouseX, int mouseY, int button, CallbackInfo ci) {
        ResponsiveBackpackScreen responsive = (ResponsiveBackpackScreen) this;
        if (this.bwb$creative$transformingClick || !responsive.betterWithBags$hasResponsiveLayout()) {
            return;
        }

        this.bwb$creative$transformingClick = true;
        ResponsiveBackpackScreen.MOUSE_ALREADY_TRANSFORMED.set(true);
        try {
            ((ScreenInventoryCreative) (Object) this).mouseClicked(
                responsive.betterWithBags$transformMouseX(mouseX),
                responsive.betterWithBags$transformMouseY(mouseY),
                button);
        } finally {
            ResponsiveBackpackScreen.MOUSE_ALREADY_TRANSFORMED.set(false);
            this.bwb$creative$transformingClick = false;
        }
        ci.cancel();
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"))
    private void bwb$creative$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        BackpackTier tier = bwb$creative$getCurrentTier();
        if (tier == null) return;

        int rows = tier.getRows();
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int panelY = guiTop + STANDARD_HEIGHT - EXTENSION_OVERLAP;

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture(EXTENSION_TEXTURE_BASE + tier.getName() + ".png").bind();

        int panelHeight = EXTENSION_OVERLAP + EXTENSION_TITLE_HEIGHT + rows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        this.drawTexturedModalRect(guiLeft, panelY, 0, 0, EXTENSION_WIDTH, panelHeight);

        if (tier == BackpackTier.LARGE || tier == BackpackTier.REINFORCED || tier == BackpackTier.NETHER) {
            this.drawTexturedModalRect(guiLeft + 174, panelY + 13, 174, 13, 36, 26);
        }

        ItemStack wornStack = ItemBackpack.getWornBackpackStack(this.mc.thePlayer);
        String title = wornStack != null ? wornStack.getDisplayName() : "";
        this.fontRenderer.render(title, guiLeft + 8, panelY + EXTENSION_OVERLAP + 4).setColor(0xFFFFFF).call();
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
