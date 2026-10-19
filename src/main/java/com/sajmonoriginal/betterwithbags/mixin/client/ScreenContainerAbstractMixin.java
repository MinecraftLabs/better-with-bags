package com.sajmonoriginal.betterwithbags.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpackAttachment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenContainerAbstract.class, remap = false)
public abstract class ScreenContainerAbstractMixin {
    @Unique
    private static final int BWB_OUTSIDE_SLOT_ID = -999;

    @Shadow
    public abstract Slot getSlotAtPosition(int mouseX, int mouseY);

    @WrapOperation(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/container/ScreenContainerAbstract;renderBackground()V")
    )
    private void bwb$keepBackgroundUnscaled(ScreenContainerAbstract screen, Operation<Void> original) {
        if (!(screen instanceof ResponsiveBackpackScreen) || !((ResponsiveBackpackScreen) screen).betterWithBags$isRenderingScaled()) {
            original.call(screen);
        }
    }

    @ModifyReturnValue(method = "getSlotId", at = @At("RETURN"))
    private int bwb$allowBackpackSlotClicks(int slotId, int mouseX, int mouseY) {
        if (slotId == BWB_OUTSIDE_SLOT_ID) {
            Slot slot = getSlotAtPosition(mouseX, mouseY);
            if (slot instanceof SlotBackpack || slot instanceof SlotBackpackAttachment) {
                return slot.index;
            }
        }
        return slotId;
    }
}
