package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
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
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Direction;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBackpack extends TileEntityRenderer<TileEntityBackpack> {

    private static final String TEXTURE_PATH = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/entity/";
    private static final String PLACED_GEOMETRY_ID = "geometry.bag.placed";
    private static final String BEDROLL_GEOMETRY_ID = "geometry.bag.bedroll";
    private static final String BEDROLL_TEXTURE = TEXTURE_PATH + "bedroll_attachment.png";

    private String getTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_placed_" + tier.getName() + ".png";
    }

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
            Block<?> block = tileEntity.worldObj.getBlockType(tileEntity.tilePos);
            int blockId = block == null ? 0 : block.id();
            BackpackTier blockTier = BackpackBlocks.getTierForBlockId(blockId);
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

        float scale = 1.0f / 16.0f;
        GLRenderer.modelM4f().translate((float) x + 0.5f, (float) y, (float) z + 0.5f);
        GLRenderer.modelM4f().scale(scale, scale, scale);
        GLRenderer.modelM4f().translate(0.0f, -13.5f, 0.0f);
        GLRenderer.modelM4f().rotateY((float)Math.toRadians(rotation));

        GLRenderer.enableState(State.BLEND);
        GLRenderer.disableState(State.CULL_FACE);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.setColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        bindTexture(getTexture(tier));
        EntityGeometryMojangData.Cache.getModel(PLACED_GEOMETRY_ID, 0).render();

        if (tileEntity.hasAttachment()) {
            bindTexture(BEDROLL_TEXTURE);
            EntityGeometryMojangData.Cache.getModel(BEDROLL_GEOMETRY_ID, 0).render();
        }

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);

        GLRenderer.popFrame();
    }
}
