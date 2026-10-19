package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.tessellator.TessellatorGeneral;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class BedrollModel {

    private static final String TEX_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/block/bedroll/";

    private static final String HEAD_TOP = TEX_BASE + "head_top.png";
    private static final String HEAD_BOTTOM = TEX_BASE + "head_bottom.png";
    private static final String HEAD_SIDE = TEX_BASE + "head_side.png";
    private static final String HEAD_FRONT = TEX_BASE + "head_front.png";

    private static final String FOOT_TOP = TEX_BASE + "foot_top.png";
    private static final String FOOT_BOTTOM = TEX_BASE + "foot_bottom.png";
    private static final String FOOT_SIDE = TEX_BASE + "foot_side.png";
    private static final String FOOT_FRONT = TEX_BASE + "foot_front.png";

    private static final float H = 4f / 16f;
    private static final float SIDE_V0 = 12f / 16f;
    private static final float SIDE_V1 = 1f;

    private BedrollModel() {
    }

    public static void render(TessellatorGeneral t, Consumer<String> bindTexture) {
        renderHeadBlock(t, bindTexture);
        renderFootBlock(t, bindTexture);
    }

    private static void renderHeadBlock(TessellatorGeneral t, Consumer<String> bindTexture) {
        bindTexture.accept(HEAD_TOP);
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 0, 0, 0);
        t.addVertexWithUV(0, H, 1, 1, 0);
        t.addVertexWithUV(1, H, 1, 1, 1);
        t.addVertexWithUV(1, H, 0, 0, 1);
        t.draw();

        bindTexture.accept(HEAD_BOTTOM);
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 0, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(0, 0, 1, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();

        bindTexture.accept(HEAD_FRONT);
        t.startDrawingQuads();
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(1, H, 0, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 0, 1, SIDE_V1);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.draw();

        bindTexture.accept(HEAD_SIDE);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 1, 1, SIDE_V0);
        t.draw();

        bindTexture.accept(HEAD_SIDE);
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 1, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 0, 0, SIDE_V0);
        t.draw();
    }

    private static void renderFootBlock(TessellatorGeneral t, Consumer<String> bindTexture) {
        bindTexture.accept(FOOT_TOP);
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 1, 0, 0);
        t.addVertexWithUV(0, H, 2, 1, 0);
        t.addVertexWithUV(1, H, 2, 1, 1);
        t.addVertexWithUV(1, H, 1, 0, 1);
        t.draw();

        bindTexture.accept(FOOT_BOTTOM);
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 2, 1, 1);
        t.addVertexWithUV(0, 0, 2, 1, 0);
        t.addVertexWithUV(0, 0, 1, 0, 0);
        t.draw();

        bindTexture.accept(FOOT_FRONT);
        t.startDrawingQuads();
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(1, H, 2, 0, SIDE_V0);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 2, 0, SIDE_V1);
        t.draw();

        bindTexture.accept(FOOT_SIDE);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 1, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.draw();

        bindTexture.accept(FOOT_SIDE);
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 1, 0, SIDE_V0);
        t.draw();
    }
}
