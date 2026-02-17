package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.light.HelmetLightManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftWorldMixin {
    @Shadow
    public WorldClient currentWorld;

    @Inject(method = "changeWorld(Lnet/minecraft/client/world/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void bwb$onWorldChange(WorldClient world, String loadingMessage, CallbackInfo ci) {
        if (this.currentWorld != null && world == null) {
            HelmetLightManager.onWorldUnload();
        }
    }
}
