package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBackpack extends ItemModelStandard {

    private static final String PLACED_GEOMETRY_ID = "geometry.bag.placed";
    private static final String TEXTURE_PATH = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/entity/";

    private static StaticEntityModel placedModel;

    private final BackpackTier tier;
    private final String texture;

    public ItemModelPlacedBackpack(Item blockItem, BackpackTier tier) {
        super(blockItem, false);
        this.tier = tier;
        this.texture = TEXTURE_PATH + "backpack_placed_" + tier.getName() + ".png";
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/backpack_" + tier.getName());
    }

    private static StaticEntityModel getPlacedModel() {
        if (placedModel == null) {
            placedModel = EntityGeometryMojangData.Cache.getModel(PLACED_GEOMETRY_ID, 0);
        }
        return placedModel;
    }

    @Override
    public void renderGui(TessellatorGeneral tessellator, Entity entity, ItemStack itemStack, int x, int y, byte layerFlags, float alpha) {
        StaticEntityModel model = getPlacedModel();
        if (model == null) {
            super.renderGui(tessellator, entity, itemStack, x, y, layerFlags, alpha);
            return;
        }

        float brightness = 1.0F;

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

        GLRenderer.pushFrame();

        GLRenderer.modelM4f().translate(x - 2, y + 3, -3.0f);
        GLRenderer.modelM4f().scale(10.0f, 10.0f, 10.0f);
        GLRenderer.modelM4f().translate(1.0f, 0.5f, 1.0f);
        GLRenderer.modelM4f().scale(1.0f, 1.0f, -1.0f);
        GLRenderer.modelM4f().rotateX((float)Math.toRadians(210.0f));
        GLRenderer.modelM4f().rotateY((float)Math.toRadians(45.0f));
        GLRenderer.setColor4f(brightness, brightness, brightness, alpha);
        GLRenderer.modelM4f().rotateY((float)Math.toRadians(-90.0f));

        float modelScale = 1.0f / 16.0f;
        GLRenderer.modelM4f().scale(modelScale, modelScale, modelScale);
        GLRenderer.modelM4f().translate(0.0f, -13.5f, 0.0f);

        GLRenderer.disableState(State.CULL_FACE);

        Minecraft.getMinecraft().textureManager.loadTexture(this.texture).bind();
        model.render();

        GLRenderer.enableState(State.CULL_FACE);

        GLRenderer.popFrame();

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);
    }
}
