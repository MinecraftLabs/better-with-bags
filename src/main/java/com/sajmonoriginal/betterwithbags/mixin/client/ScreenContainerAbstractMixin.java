package com.sajmonoriginal.betterwithbags.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpackAttachment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenContainerAbstract.class, remap = false)
public abstract class ScreenContainerAbstractMixin {

    @Shadow
    public abstract Slot getSlotAtPosition(int mouseX, int mouseY);

    @ModifyReturnValue(method = "getSlotId", at = @At("RETURN"))
    private int bwb$allowBackpackSlotClicks(int slotId, int mouseX, int mouseY) {
        if (slotId == -999) {
            Slot slot = getSlotAtPosition(mouseX, mouseY);
            if (slot instanceof SlotBackpack || slot instanceof SlotBackpackAttachment) {
                return slot.index;
            }
        }
        return slotId;
    }
}
