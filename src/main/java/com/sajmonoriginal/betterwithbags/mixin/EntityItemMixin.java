package com.sajmonoriginal.betterwithbags.mixin;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, remap = false)
public abstract class EntityItemMixin extends Entity {

    @Shadow
    public ItemStack item;

    protected EntityItemMixin(World world) {
        super(world);
    }

    @Inject(method = "handleBuoyancy", at = @At("HEAD"), cancellable = true)
    private void bwb$floatFireproofBackpackInLava(CallbackInfo ci) {
        if (this.item == null || !(this.item.getItem() instanceof ItemBackpack)) {
            return;
        }
        if (!((ItemBackpack) this.item.getItem()).getTier().isFireResistant()) {
            return;
        }
        if (!this.world.isMaterialInBB(this.bb, Materials.LAVA)) {
            return;
        }

        this.xd *= 0.98F;
        this.zd *= 0.98F;
        this.yd = Math.min(this.yd + 0.02, 0.1);
        ci.cancel();
    }
}
