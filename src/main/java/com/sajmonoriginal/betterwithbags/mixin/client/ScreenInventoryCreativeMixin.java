package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.gui.BackpackInventoryPanel;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventoryCreative.class, remap = false)
public abstract class ScreenInventoryCreativeMixin extends ScreenInventory {

    protected ScreenInventoryCreativeMixin(Player player) {
        super(player);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"))
    private void bwb$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(this.mc.thePlayer);
        if (backpackStack != null) {
            BackpackInventoryPanel.draw(this, ((ItemBackpack) backpackStack.getItem()).getTier(), backpackStack);
        }
    }
}
