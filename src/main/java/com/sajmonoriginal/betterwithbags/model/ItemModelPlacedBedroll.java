package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.render.BedrollModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TextureManager;
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

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBedroll extends ItemModelStandard {

    public ItemModelPlacedBedroll(Item blockItem) {
        super(blockItem, false);
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/bedroll");
    }

    @Override
    public void renderGui(TessellatorGeneral tessellator, Entity entity, ItemStack itemStack, int x, int y, byte layerFlags, float alpha) {
        float brightness = 1.0F;
        TextureManager textureManager = Minecraft.getMinecraft().textureManager;
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

        GLRenderer.modelM4f().scale(0.5f, 0.5f, 0.5f);
        GLRenderer.modelM4f().translate(0.5f, 0.0f, 0.0f);

        GLRenderer.disableState(State.CULL_FACE);

        BedrollModel.render(tessellator, path -> textureManager.loadTexture(path).bind());

        GLRenderer.enableState(State.CULL_FACE);

        GLRenderer.popFrame();

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);
    }
}
