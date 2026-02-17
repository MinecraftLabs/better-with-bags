package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.light.HelmetLightManager;
import net.minecraft.core.world.chunk.ChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkCache.class, remap = false)
public class ChunkCacheLightMixin {

    @Inject(method = "getLightmapCoord(IIII)I", at = @At("RETURN"), cancellable = true)
    private void bwb$addHelmetLight(int x, int y, int z, int blockLightValue, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(HelmetLightManager.calculateDynamicLight(x, y, z, cir.getReturnValue()));
    }
}
