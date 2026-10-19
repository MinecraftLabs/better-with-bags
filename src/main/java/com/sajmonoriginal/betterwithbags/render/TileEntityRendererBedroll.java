package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.util.helper.Side;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBedroll extends TileEntityRenderer<TileEntityBedroll> {

    @Override
    public void doRender(TessellatorGeneral tessellator, TileEntityBedroll tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity.isFoot()) {
            return;
        }

        int direction = tileEntity.getDirection();
        Side footToHead = BlockLogicBed.footToHeadMap[direction];

        GLRenderer.pushFrame();

        GLRenderer.modelM4f().translate((float) x + 0.5f - footToHead.offsetX(), (float) y, (float) z + 0.5f - footToHead.offsetZ());
        GLRenderer.modelM4f().rotateY((float) Math.toRadians((4 - direction) % 4 * 90f));
        GLRenderer.modelM4f().translate(-0.5f, 0.0f, -0.5f);

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.setColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GLRenderer.disableState(State.CULL_FACE);

        BedrollModel.render(tessellator, this::bindTexture);

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);
        GLRenderer.popFrame();
    }
}
