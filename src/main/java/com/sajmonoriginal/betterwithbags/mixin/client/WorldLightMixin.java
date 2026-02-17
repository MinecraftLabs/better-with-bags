package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.light.HelmetLightManager;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, remap = false)
public class WorldLightMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwb$onWorldTick(CallbackInfo ci) {
        HelmetLightManager.updateWorld();
    }
}
