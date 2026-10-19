package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityItem.class, remap = false)
public abstract class EntityItemMixin extends Entity {

    @Shadow
    public ItemStack item;

    protected EntityItemMixin(World world) {
        super(world);
    }

    @WrapOperation(
        method = "tick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityItem;handleBuoyancy(Lnet/minecraft/core/block/Block;Lnet/minecraft/core/world/pos/TilePos;Lnet/minecraft/core/block/BlockLogicFluid;Z)V")
    )
    private void bwb$floatFireproofBackpackInLava(EntityItem self, Block<?> blockUnder, TilePos posUnder, BlockLogicFluid fluid,
                                                 boolean inFlowingLiquid, Operation<Void> original) {
        if (this.item != null && this.item.getItem() instanceof ItemBackpack
            && this.item.getItem().hasTag(ItemTags.IS_FIRE_PROOF)
            && this.world.isMaterialInBB(this.bb, Materials.LAVA)) {
            this.xd *= 0.98F;
            this.zd *= 0.98F;
            this.yd = Math.min(this.yd + 0.02, 0.1);
            return;
        }
        original.call(self, blockUnder, posUnder, fluid, inFlowingLiquid);
    }
}
