package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBedroll extends TileEntityRenderer<TileEntityBedroll> {

    @Override
    public void doRender(TessellatorGeneral tessellator, TileEntityBedroll tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity.isFoot()) {
            return;
        }

        int direction = tileEntity.getDirection();

        float footDx, footDz;
        switch (direction) {
            case 0: footDx =  0; footDz = -1; break;
            case 1: footDx =  1; footDz =  0; break;
            case 2: footDx =  0; footDz =  1; break;
            case 3: footDx = -1; footDz =  0; break;
            default: footDx = 0; footDz = 0; break;
        }

        GLRenderer.pushFrame();

        GLRenderer.modelM4f().translate((float) x + 0.5f + footDx, (float) y, (float) z + 0.5f + footDz);
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(getRotation(direction)));
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

    private float getRotation(int direction) {
        switch (direction) {
            case 0: return 0f;
            case 1: return 270f;
            case 2: return 180f;
            case 3: return 90f;
            default: return 0f;
        }
    }
}
