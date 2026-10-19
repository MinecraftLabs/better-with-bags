package com.sajmonoriginal.betterwithbags.mixin;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    @Inject(method = "getItemDamageForDisplay", at = @At("HEAD"), cancellable = true)
    private void bwb$showFreeBackpackSlots(CallbackInfoReturnable<Integer> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (self.getItem() instanceof ItemBackpack) {
            cir.setReturnValue(ItemBackpack.getFreeSlotCount(self));
        }
    }
}
