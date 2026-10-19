package com.sajmonoriginal.betterwithbags.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveScreens;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, remap = false)
public abstract class WorldRendererMixin {

    @WrapOperation(
        method = "updateCameraAndRender",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Screen;render(IIF)V")
    )
    private void bwb$renderScaledScreen(Screen screen, int mouseX, int mouseY, float partialTick, Operation<Void> original) {
        ResponsiveBackpackScreen layout = ResponsiveScreens.getScaled(screen);
        if (layout == null) {
            original.call(screen, mouseX, mouseY, partialTick);
            return;
        }

        screen.renderBackground();
        GLRenderer.pushFrame();
        ResponsiveScreens.applyTransform(screen, layout);
        layout.betterWithBags$setRenderingScaled(true);
        try {
            original.call(screen, ResponsiveScreens.toContentX(screen, mouseX), ResponsiveScreens.toContentY(screen, mouseY), partialTick);
        } finally {
            layout.betterWithBags$setRenderingScaled(false);
            GLRenderer.popFrame();
        }
    }
}
