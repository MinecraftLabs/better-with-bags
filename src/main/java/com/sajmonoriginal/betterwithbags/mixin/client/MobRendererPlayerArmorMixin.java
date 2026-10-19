package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerArmorMixin extends MobRenderer<Player> {

    protected MobRendererPlayerArmorMixin(float shadowSize) {
        super(shadowSize);
    }

    @Inject(method = "getAndSetupModelForLayer(Lnet/minecraft/core/entity/player/Player;FFI)Lorg/useless/dragonfly/models/entity/StaticEntityModel;", at = @At("HEAD"), cancellable = true)
    private void bwb$skipChestArmorUnderBackpack(Player player, float limbSwing, float partialTick, int layer, CallbackInfoReturnable<StaticEntityModel> cir) {
        if (layer == HumanArmorShape.CHEST.ordinal() + 1 && ItemBackpack.isWearingBackpack(player)) {
            cir.setReturnValue(null);
        }
    }
}
