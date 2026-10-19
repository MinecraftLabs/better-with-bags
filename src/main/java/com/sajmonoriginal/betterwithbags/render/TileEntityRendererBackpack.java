package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.util.helper.Direction;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBackpack extends TileEntityRenderer<TileEntityBackpack> {

    private float getRotationFromMeta(int meta) {
        Direction dir = Direction.fromId(meta);
        if (dir == Direction.NORTH) return 180f;
        if (dir == Direction.SOUTH) return 0f;
        if (dir == Direction.WEST) return 90f;
        if (dir == Direction.EAST) return -90f;
        return 0f;
    }

    private BackpackTier getDisplayTier(TileEntityBackpack tileEntity) {
        if (tileEntity.worldObj != null) {
            BackpackTier blockTier = BackpackBlocks.getTierForBlock(tileEntity.worldObj.getBlockType(tileEntity.tilePos));
            if (blockTier != null) return blockTier;
        }
        return tileEntity.getTier();
    }

    @Override
    public void doRender(TessellatorGeneral tessellator, TileEntityBackpack tileEntity, double x, double y, double z, float partialTick) {
        BackpackTier tier = getDisplayTier(tileEntity);
        int meta = tileEntity.getBlockMeta();
        float rotation = getRotationFromMeta(meta);

        GLRenderer.pushFrame();

        GLRenderer.modelM4f().translate((float) x + 0.5f, (float) y, (float) z + 0.5f);
        BackpackGeometry.applyPlacedModelTransform();
        GLRenderer.modelM4f().rotateY((float)Math.toRadians(rotation));

        GLRenderer.enableState(State.BLEND);
        GLRenderer.disableState(State.CULL_FACE);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.setColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        bindTexture(BackpackGeometry.getPlacedTexture(tier));
        EntityGeometryMojangData.Cache.getModel(BackpackGeometry.PLACED, 0).render();

        if (tileEntity.hasAttachment()) {
            bindTexture(BackpackGeometry.BEDROLL_TEXTURE);
            EntityGeometryMojangData.Cache.getModel(BackpackGeometry.BEDROLL, 0).render();
        }

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);

        GLRenderer.popFrame();
    }
}
