package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BagScreens;
import com.sajmonoriginal.betterwithbags.gui.ScreenBackpack;
import com.sajmonoriginal.betterwithbags.gui.ScreenBackpackBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerLocal.class, remap = false)
public abstract class PlayerLocalMixinBagScreens extends Player implements BagScreens {
    protected PlayerLocalMixinBagScreens(World world) {
        super(world);
    }

    @Shadow
    protected Minecraft mc;

    @Override
    public void betterWithBags$displayBackpackScreen(ItemStack backpackStack) {
        this.mc.displayScreen(new ScreenBackpack(this.inventory, backpackStack));
    }

    @Override
    public void betterWithBags$displayBackpackBlockScreen(TileEntityBackpack tileEntity) {
        this.mc.displayScreen(new ScreenBackpackBlock(this.inventory, tileEntity));
    }

    @Override
    public void betterWithBags$closeBackpackScreen() {
        this.closeScreen();
    }
}
