package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.render.BackpackGeometry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {

    @Unique
    private static final int BWB_ANIM_LAYER = 5;

    @Shadow
    protected abstract StaticEntityModel setupAnimations(Player player, StaticEntityModel model, float partialTick, int layer);

    protected MobRendererPlayerMixin(float shadowSize) {
        super(shadowSize);
    }

    @Inject(method = "renderAdditional(Lnet/minecraft/client/render/tessellator/TessellatorGeneral;Lnet/minecraft/core/entity/player/Player;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;getHeldObject()Lnet/minecraft/core/world/ICarriable;"))
    private void bwb$renderBackpackOnBack(TessellatorGeneral tessellator, Player player, float partialTick, CallbackInfo ci) {
        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(player);
        if (backpackStack == null) {
            return;
        }

        BackpackTier tier = ItemBackpack.getBackpackTier(backpackStack);
        bwb$renderPart(player, partialTick, BackpackGeometry.WORN, BackpackGeometry.getWornTexture(tier));
        if (ItemBackpack.hasAttachment(backpackStack)) {
            bwb$renderPart(player, partialTick, BackpackGeometry.BEDROLL, BackpackGeometry.BEDROLL_TEXTURE);
        }
    }

    @Unique
    private void bwb$renderPart(Player player, float partialTick, String geometryId, String texture) {
        StaticEntityModel model = this.setupAnimations(player, EntityGeometryMojangData.Cache.getModel(geometryId, 0), partialTick, BWB_ANIM_LAYER);
        if (model != null) {
            this.renderDispatcher.textureManager.loadTexture(texture).bind();
            model.render();
        }
    }
}
