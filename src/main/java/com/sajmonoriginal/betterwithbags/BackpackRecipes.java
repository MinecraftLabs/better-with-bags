package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.RecipeBuilder;

public class BackpackRecipes {

    public static void registerRecipes() {
        String modId = BetterWithBagsMod.MOD_ID;

        RecipeBuilder.Shaped(modId, "CLC", "LSL", "CLC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('S', Items.STRING)
            .create("backpack_small", ((ItemBackpack) BackpackItems.BACKPACK_SMALL).getDefaultStack());

        RecipeBuilder.Shaped(modId, "CIC", "LBL", "CIC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('I', Items.INGOT_IRON)
            .addInput('B', ((ItemBackpack) BackpackItems.BACKPACK_SMALL).getDefaultStack())
            .create("backpack_medium", ((ItemBackpack) BackpackItems.BACKPACK_MEDIUM).getDefaultStack());

        RecipeBuilder.Shaped(modId, "CGC", "LBL", "CGC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('G', Items.INGOT_GOLD)
            .addInput('B', ((ItemBackpack) BackpackItems.BACKPACK_MEDIUM).getDefaultStack())
            .create("backpack_large", ((ItemBackpack) BackpackItems.BACKPACK_LARGE).getDefaultStack());

        RecipeBuilder.Shaped(modId, "DCD", "MBM", "DCD")
            .addInput('D', Items.DIAMOND)
            .addInput('C', Items.CLOTH)
            .addInput('M', Blocks.MESH)
            .addInput('B', ((ItemBackpack) BackpackItems.BACKPACK_LARGE).getDefaultStack())
            .create("backpack_reinforced", ((ItemBackpack) BackpackItems.BACKPACK_REINFORCED).getDefaultStack());

        RecipeBuilder.Shaped(modId, "OCO", "MBM", "OCO")
            .addInput('O', Items.OLIVINE)
            .addInput('C', Items.NETHERCOAL)
            .addInput('M', Blocks.MESH_GOLD)
            .addInput('B', ((ItemBackpack) BackpackItems.BACKPACK_REINFORCED).getDefaultStack())
            .create("backpack_nether", ((ItemBackpack) BackpackItems.BACKPACK_NETHER).getDefaultStack());

        RecipeBuilder.Shaped(modId, "LWL", "LWL", "LLL")
            .addInput('L', Items.LEATHER)
            .addInput('W', Items.CLOTH)
            .create("bedroll", new ItemStack(BackpackItems.BEDROLL, 1));
    }

    public static void initNamespaces() {
        RecipeBuilder.initNameSpace(BetterWithBagsMod.MOD_ID);
    }
}
