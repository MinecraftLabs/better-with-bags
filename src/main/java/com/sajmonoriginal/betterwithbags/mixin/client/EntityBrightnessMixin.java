package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.light.HelmetLightManager;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, remap = false)
public class EntityBrightnessMixin {

    @Shadow
    public World world;

    @Shadow
    public double x;

    @Shadow
    public double y;

    @Shadow
    public double z;

    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private void bwb$boostHelmetBrightness(float partialTick, CallbackInfoReturnable<Float> cir) {
        int dynamicLevel = HelmetLightManager.getDynamicLightAt(this.x, this.y, this.z);
        if (dynamicLevel > 0 && this.world != null) {
            float helmetBrightness = this.world.worldType.getBrightnessRamp()[dynamicLevel];
            if (helmetBrightness > cir.getReturnValue()) {
                cir.setReturnValue(helmetBrightness);
            }
        }
    }

    @Inject(method = "getLightmapCoord", at = @At("RETURN"), cancellable = true)
    private void bwb$boostHelmetLightmapCoord(float partialTick, CallbackInfoReturnable<Integer> cir) {
        int dynamicLevel = HelmetLightManager.getDynamicLightAt(this.x, this.y, this.z);
        if (dynamicLevel > 0) {
            int coord = cir.getReturnValue();
            int skylight = coord >> 20;
            int blocklight = (coord >> 4) & 0xFFFF;
            if (blocklight < dynamicLevel) {
                cir.setReturnValue((skylight << 20) | (dynamicLevel << 4));
            }
        }
    }
}
