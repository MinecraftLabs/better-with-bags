package com.sajmonoriginal.betterwithbags.mixin;

import com.sajmonoriginal.betterwithbags.gui.ContainerBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class ContainerInventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Inject(method = "currentSlotLocked", at = @At("HEAD"), cancellable = true)
    private void bwb$lockOpenBackpackSlot(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null && this.player.containerMenu instanceof ContainerBackpack) {
            cir.setReturnValue(true);
        }
    }
}
