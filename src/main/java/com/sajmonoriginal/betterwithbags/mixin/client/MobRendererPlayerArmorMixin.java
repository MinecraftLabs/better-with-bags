package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.HelmetVariant;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemMinersHelmet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerArmorMixin extends MobRenderer<Player> {

    protected MobRendererPlayerArmorMixin(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Inject(method = "prepareArmor(Lnet/minecraft/core/entity/player/Player;IF)Z", at = @At("HEAD"), cancellable = true)
    private void skipCustomArmorRender(Player player, int layer, float partialTick, CallbackInfoReturnable<Boolean> cir) {
        if (layer == 2) {
            ItemStack chestSlot = player.inventory.armorInventory[2];
            if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
                cir.setReturnValue(false);
            }
        }

        if (layer == 0) {
            HelmetVariant variant = ItemMinersHelmet.getHelmetVariant(player);
            if (variant != null && variant.isAmbient()) {
                cir.setReturnValue(false);
            }
        }
    }
}
