package com.sajmonoriginal.betterwithbags.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.renderer.GLRenderer;

@Environment(EnvType.CLIENT)
public final class ResponsiveScreens {

    private ResponsiveScreens() {
    }

    public static ResponsiveBackpackScreen getScaled(Screen screen) {
        if (screen instanceof ResponsiveBackpackScreen && ((ResponsiveBackpackScreen) screen).betterWithBags$hasResponsiveLayout()) {
            return (ResponsiveBackpackScreen) screen;
        }
        return null;
    }

    public static void applyTransform(Screen screen, ResponsiveBackpackScreen layout) {
        float scale = layout.betterWithBags$getGuiScale();
        GLRenderer.modelM4f()
            .translate(screen.width / 2.0F, screen.height / 2.0F, 0.0F)
            .scale(scale, scale, 1.0F)
            .translate(-layout.betterWithBags$getContentCenterX(), -layout.betterWithBags$getContentCenterY(), 0.0F);
    }

    public static int toContentX(Screen screen, int mouseX) {
        ResponsiveBackpackScreen layout = getScaled(screen);
        return layout == null ? mouseX
            : Math.round(layout.betterWithBags$getContentCenterX() + (mouseX - screen.width / 2.0F) / layout.betterWithBags$getGuiScale());
    }

    public static int toContentY(Screen screen, int mouseY) {
        ResponsiveBackpackScreen layout = getScaled(screen);
        return layout == null ? mouseY
            : Math.round(layout.betterWithBags$getContentCenterY() + (mouseY - screen.height / 2.0F) / layout.betterWithBags$getGuiScale());
    }
}
