package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.render.BedrollModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBedroll extends ItemModelPlacedBase {

    public ItemModelPlacedBedroll(Item blockItem) {
        super(blockItem);
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/bedroll");
    }

    @Override
    protected void renderPlacedModel(TessellatorGeneral tessellator) {
        GLRenderer.modelM4f().scale(0.5f, 0.5f, 0.5f);
        GLRenderer.modelM4f().translate(0.5f, 0.0f, 0.0f);

        TextureManager textureManager = Minecraft.getMinecraft().textureManager;
        BedrollModel.render(tessellator, path -> textureManager.loadTexture(path).bind());
    }
}
