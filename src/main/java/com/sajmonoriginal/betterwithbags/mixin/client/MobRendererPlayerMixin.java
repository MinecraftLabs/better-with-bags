package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
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
    private static final String TEXTURE_PATH = "/assets/betterwithbags/textures/entity/";
    @Unique
    private static final String GEOMETRY_ID = "geometry.humanoid.armor.bag";
    @Unique
    private static final String BEDROLL_GEOMETRY_ID = "geometry.bag.bedroll";
    @Unique
    private static final String BEDROLL_TEXTURE = TEXTURE_PATH + "bedroll_attachment.png";

    @Unique
    private static final int BWB_ANIM_LAYER = 5;

    @Shadow
    protected abstract StaticEntityModel setupAnimations(Player player, StaticEntityModel model, float partialTick, int layer);

    protected MobRendererPlayerMixin(float shadowSize) {
        super(shadowSize);
    }

    @Unique
    private String bwb$getTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_" + tier.getName() + ".png";
    }

    @Inject(method = "renderAdditional(Lnet/minecraft/client/render/tessellator/TessellatorGeneral;Lnet/minecraft/core/entity/player/Player;F)V",
            at = @At("TAIL"))
    private void bwb$renderBackpackOnBack(TessellatorGeneral tessellator, Player player, float partialTick, CallbackInfo ci) {
        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(player);
        if (backpackStack == null) {
            return;
        }

        BackpackTier tier = ((ItemBackpack) backpackStack.getItem()).getTier();
        bwb$renderPart(player, partialTick, GEOMETRY_ID, bwb$getTexture(tier));
        if (ItemBackpack.hasAttachment(backpackStack)) {
            bwb$renderPart(player, partialTick, BEDROLL_GEOMETRY_ID, BEDROLL_TEXTURE);
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
