package com.sajmonoriginal.betterwithbags.mixin;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BagScreens;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixinBagScreens implements BagScreens {
    @Override
    public void betterWithBags$displayBackpackScreen(ItemStack backpackStack) {
    }

    @Override
    public void betterWithBags$displayBackpackBlockScreen(TileEntityBackpack tileEntity) {
    }

    @Override
    public void betterWithBags$closeBackpackScreen() {
    }
}
