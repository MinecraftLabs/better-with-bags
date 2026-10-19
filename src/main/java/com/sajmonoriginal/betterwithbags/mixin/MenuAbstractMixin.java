package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpackBase;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MenuAbstract.class, remap = false)
public abstract class MenuAbstractMixin {

    @ModifyExpressionValue(
        method = "getFreeSpace",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/menu/MenuAbstract;getSlotFromList(Lit/unimi/dsi/fastutil/ints/IntList;I)Lnet/minecraft/core/player/inventory/slot/Slot;")
    )
    private Slot bwb$skipSlotsRefusingItem(Slot slot, ItemStack item, IntList slots) {
        return slot != null && bwb$hasBackpackSlots() && !slot.mayPlace(item) ? null : slot;
    }

    @Unique
    private boolean bwb$hasBackpackSlots() {
        Object self = this;
        return self instanceof MenuBackpackBase
            || self instanceof BackpackMenuCapable && ((BackpackMenuCapable) self).betterWithBags$hasBackpackSlots();
    }
}
