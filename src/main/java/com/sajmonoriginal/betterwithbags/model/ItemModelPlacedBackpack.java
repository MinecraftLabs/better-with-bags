package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBackpack extends ItemModelPlacedBase {

    private static final String PLACED_GEOMETRY_ID = "geometry.bag.placed";
    private static final String TEXTURE_PATH = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/entity/";

    private final String texture;

    public ItemModelPlacedBackpack(Item blockItem, BackpackTier tier) {
        super(blockItem);
        this.texture = TEXTURE_PATH + "backpack_placed_" + tier.getName() + ".png";
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/backpack_" + tier.getName());
    }

    @Override
    protected void renderPlacedModel(TessellatorGeneral tessellator) {
        float modelScale = 1.0f / 16.0f;
        GLRenderer.modelM4f().scale(modelScale, modelScale, modelScale);
        GLRenderer.modelM4f().translate(0.0f, -13.5f, 0.0f);

        StaticEntityModel model = EntityGeometryMojangData.Cache.getModel(PLACED_GEOMETRY_ID, 0);
        Minecraft.getMinecraft().textureManager.loadTexture(this.texture).bind();
        model.render();
    }
}
