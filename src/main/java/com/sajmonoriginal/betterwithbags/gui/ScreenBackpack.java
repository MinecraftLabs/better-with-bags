package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

@Environment(EnvType.CLIENT)
public class ScreenBackpack extends ScreenBackpackBase {
    private final MenuBackpack backpackMenu;

    public ScreenBackpack(ContainerInventory playerInventory, int slotIndex, boolean isArmor) {
        this(new MenuBackpack(playerInventory, slotIndex, isArmor));
    }

    private ScreenBackpack(MenuBackpack menu) {
        super(menu);
        this.backpackMenu = menu;
    }

    @Override
    protected String getTitle() {
        ItemStack backpackStack = backpackMenu.getBackpackStack();
        return backpackStack != null
            ? backpackStack.getDisplayName()
            : BackpackItems.getItemForTier(backpackMenu.getTier()).getDefaultStack().getDisplayName();
    }
}
