package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class ContainerInventoryMixin {
    @Shadow
    @Final
    public Player player;

    @ModifyReturnValue(method = "currentSlotLocked", at = @At("RETURN"))
    private boolean bwb$lockOpenBackpackSlot(boolean locked) {
        return locked || this.player != null && this.player.containerMenu instanceof MenuBackpack;
    }
}
