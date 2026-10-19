package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.render.BackpackGeometry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBackpack extends ItemModelPlacedBase {

    private final String texture;

    public ItemModelPlacedBackpack(Item blockItem, BackpackTier tier) {
        super(blockItem);
        this.texture = BackpackGeometry.getPlacedTexture(tier);
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/backpack_" + tier.getName());
    }

    @Override
    protected void renderPlacedModel(TessellatorGeneral tessellator) {
        BackpackGeometry.applyPlacedModelTransform();
        Minecraft.getMinecraft().textureManager.loadTexture(this.texture).bind();
        EntityGeometryMojangData.Cache.getModel(BackpackGeometry.PLACED, 0).render();
    }
}
