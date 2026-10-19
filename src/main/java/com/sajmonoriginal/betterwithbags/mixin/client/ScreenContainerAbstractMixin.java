package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.slot.SlotBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpackAttachment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenContainerAbstract.class, remap = false)
public abstract class ScreenContainerAbstractMixin {

    @Shadow
    public abstract Slot getSlotAtPosition(int mouseX, int mouseY);

    @Inject(method = "getSlotId", at = @At("RETURN"), cancellable = true)
    private void bwb$allowBackpackSlotClicks(int mouseX, int mouseY, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() == -999) {
            Slot slot = getSlotAtPosition(mouseX, mouseY);
            if (slot instanceof SlotBackpack || slot instanceof SlotBackpackAttachment) {
                cir.setReturnValue(slot.index);
            }
        }
    }
}
