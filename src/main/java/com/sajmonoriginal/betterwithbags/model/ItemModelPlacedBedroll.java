package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
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

    private static final String TEX_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/block/bedroll/";

    private static final String HEAD_TOP = TEX_BASE + "foot_top.png";
    private static final String HEAD_BOTTOM = TEX_BASE + "foot_bottom.png";
    private static final String HEAD_SIDE = TEX_BASE + "foot_side.png";
    private static final String HEAD_FRONT = TEX_BASE + "foot_front.png";

    private static final String FOOT_TOP = TEX_BASE + "head_top.png";
    private static final String FOOT_BOTTOM = TEX_BASE + "head_bottom.png";
    private static final String FOOT_SIDE = TEX_BASE + "head_side.png";
    private static final String FOOT_FRONT = TEX_BASE + "head_front.png";

    private static final float H = 4f / 16f;
    private static final float SIDE_V0 = 12f / 16f;
    private static final float SIDE_V1 = 1f;

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

        renderHeadBlock(tessellator, textureManager);
        renderFootBlock(tessellator, textureManager);

        GLRenderer.enableState(State.CULL_FACE);

        GLRenderer.popFrame();

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);
    }

    private void renderHeadBlock(TessellatorGeneral t, TextureManager textureManager) {
        textureManager.loadTexture(HEAD_TOP).bind();
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 0, 0, 0);
        t.addVertexWithUV(0, H, 1, 1, 0);
        t.addVertexWithUV(1, H, 1, 1, 1);
        t.addVertexWithUV(1, H, 0, 0, 1);
        t.draw();

        textureManager.loadTexture(HEAD_BOTTOM).bind();
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 0, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(0, 0, 1, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();

        textureManager.loadTexture(HEAD_FRONT).bind();
        t.startDrawingQuads();
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(1, H, 0, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 0, 1, SIDE_V1);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.draw();

        textureManager.loadTexture(HEAD_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 1, 1, SIDE_V0);
        t.draw();

        textureManager.loadTexture(HEAD_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 1, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 0, 0, SIDE_V0);
        t.draw();
    }

    private void renderFootBlock(TessellatorGeneral t, TextureManager textureManager) {
        textureManager.loadTexture(FOOT_TOP).bind();
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 1, 0, 0);
        t.addVertexWithUV(0, H, 2, 1, 0);
        t.addVertexWithUV(1, H, 2, 1, 1);
        t.addVertexWithUV(1, H, 1, 0, 1);
        t.draw();

        textureManager.loadTexture(FOOT_BOTTOM).bind();
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 2, 1, 1);
        t.addVertexWithUV(0, 0, 2, 1, 0);
        t.addVertexWithUV(0, 0, 1, 0, 0);
        t.draw();

        textureManager.loadTexture(FOOT_FRONT).bind();
        t.startDrawingQuads();
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(1, H, 2, 0, SIDE_V0);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 2, 0, SIDE_V1);
        t.draw();

        textureManager.loadTexture(FOOT_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 1, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.draw();

        textureManager.loadTexture(FOOT_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 1, 0, SIDE_V0);
        t.draw();
    }
}
