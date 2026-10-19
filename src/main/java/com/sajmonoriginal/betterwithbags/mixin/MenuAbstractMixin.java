package com.sajmonoriginal.betterwithbags.mixin;

import com.sajmonoriginal.betterwithbags.gui.ContainerBackpack;
import com.sajmonoriginal.betterwithbags.gui.ContainerBackpackBlock;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MenuAbstract.class, remap = false)
public abstract class MenuAbstractMixin {
    @Shadow
    public abstract Slot getSlot(int i);

    @Inject(method = "getFreeSpace", at = @At("HEAD"), cancellable = true)
    private void bwb$countOnlyAcceptingSlots(ItemStack item, IntList slots, CallbackInfoReturnable<Integer> cir) {
        if (!bwb$hasBackpackSlots()) {
            return;
        }

        int space = 0;
        int max = item.getMaxStackSize();
        for (int i = 0; i < slots.size(); i++) {
            Slot slot = this.getSlot(slots.getInt(i));
            if (slot == null || !slot.mayPlace(item)) {
                continue;
            }
            ItemStack stackInSlot = slot.getItemStack();
            if (stackInSlot == null) {
                cir.setReturnValue(max);
                return;
            }
            if (item.canStackWith(stackInSlot)) {
                space += stackInSlot.getMaxStackSize() - stackInSlot.stackSize;
            }
            if (space >= max) {
                cir.setReturnValue(space);
                return;
            }
        }
        cir.setReturnValue(space);
    }

    @Unique
    private boolean bwb$hasBackpackSlots() {
        Object self = this;
        if (self instanceof ContainerBackpack || self instanceof ContainerBackpackBlock) {
            return true;
        }
        return self instanceof BackpackMenuCapable && ((BackpackMenuCapable) self).betterWithBags$hasBackpackSlots();
    }
}
