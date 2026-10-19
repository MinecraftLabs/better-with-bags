package com.sajmonoriginal.betterwithbags.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.component.HudComponentArmorBar;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(value = HudComponentArmorBar.class, remap = false)
public abstract class HudComponentArmorBarMixin {

    @WrapOperation(
        method = "render",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I")
    )
    private int bwb$keepBackpackIconStable(Random random, int bound, Operation<Integer> original,
                                           @Local(ordinal = 0) ItemStack armorStack) {
        if (armorStack != null && armorStack.getItem() instanceof ItemBackpack) {
            return 0;
        }
        return original.call(random, bound);
    }
}
