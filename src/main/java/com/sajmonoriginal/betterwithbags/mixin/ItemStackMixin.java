package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    @ModifyReturnValue(method = "getItemDamageForDisplay", at = @At("RETURN"))
    private int bwb$showFreeBackpackSlots(int damage) {
        ItemStack self = (ItemStack) (Object) this;
        return self.getItem() instanceof ItemBackpack ? ItemBackpack.getFreeSlotCount(self) : damage;
    }
}
