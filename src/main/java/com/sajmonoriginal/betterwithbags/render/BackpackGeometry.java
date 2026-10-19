package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.renderer.GLRenderer;

@Environment(EnvType.CLIENT)
public final class BackpackGeometry {
    public static final String WORN = "geometry.humanoid.armor.bag";
    public static final String PLACED = "geometry.bag.placed";
    public static final String BEDROLL = "geometry.bag.bedroll";

    private static final String TEXTURE_PATH = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/entity/";
    public static final String BEDROLL_TEXTURE = TEXTURE_PATH + "bedroll_attachment.png";

    private BackpackGeometry() {
    }

    public static String getWornTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_" + tier.getName() + ".png";
    }

    public static String getPlacedTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_placed_" + tier.getName() + ".png";
    }

    public static void applyPlacedModelTransform() {
        float scale = 1.0f / 16.0f;
        GLRenderer.modelM4f().scale(scale, scale, scale);
        GLRenderer.modelM4f().translate(0.0f, -13.5f, 0.0f);
    }
}
