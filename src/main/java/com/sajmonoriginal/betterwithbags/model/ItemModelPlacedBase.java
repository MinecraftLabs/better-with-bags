package com.sajmonoriginal.betterwithbags.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

@Environment(EnvType.CLIENT)
public abstract class ItemModelPlacedBase extends ItemModelStandard {

    protected ItemModelPlacedBase(Item item) {
        super(item, false);
    }

    protected abstract void renderPlacedModel(TessellatorGeneral tessellator);

    @Override
    public void renderGui(TessellatorGeneral tessellator, Entity entity, ItemStack itemStack, int x, int y, byte layerFlags, float alpha) {
        GLRenderer.enableState(State.BLEND);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, alpha);
        GLRenderer.pushFrame();

        GLRenderer.modelM4f().translate(x - 2, y + 3, -3.0f);
        GLRenderer.modelM4f().scale(10.0f, 10.0f, 10.0f);
        GLRenderer.modelM4f().translate(1.0f, 0.5f, 1.0f);
        GLRenderer.modelM4f().scale(1.0f, 1.0f, -1.0f);
        GLRenderer.modelM4f().rotateX((float) Math.toRadians(210.0f));
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(45.0f));
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(-90.0f));

        GLRenderer.disableState(State.CULL_FACE);
        renderPlacedModel(tessellator);
        GLRenderer.enableState(State.CULL_FACE);

        GLRenderer.popFrame();
        GLRenderer.disableState(State.BLEND);
    }
}
