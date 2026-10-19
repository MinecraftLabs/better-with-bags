package com.sajmonoriginal.betterwithbags.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sajmonoriginal.betterwithbags.gui.ResponsiveScreens;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(value = Screen.class, remap = false)
public abstract class ScreenMixin {

    @ModifyVariable(method = "updateEvents", at = @At("STORE"), ordinal = 0)
    private int bwb$scaleMouseX(int mouseX) {
        return ResponsiveScreens.toContentX((Screen) (Object) this, mouseX);
    }

    @ModifyVariable(method = "updateEvents", at = @At("STORE"), ordinal = 1)
    private int bwb$scaleMouseY(int mouseY) {
        return ResponsiveScreens.toContentY((Screen) (Object) this, mouseY);
    }

    @WrapOperation(
        method = "guiSpecificControllerInput",
        at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Screen;mouseClicked(III)V"),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Screen;mouseReleased(III)V")
        }
    )
    private void bwb$scaleControllerCursor(Screen screen, int x, int y, int button, Operation<Void> original) {
        original.call(screen, ResponsiveScreens.toContentX(screen, x), ResponsiveScreens.toContentY(screen, y), button);
    }
}
