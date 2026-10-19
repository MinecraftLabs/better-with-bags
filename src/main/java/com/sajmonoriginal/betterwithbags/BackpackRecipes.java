package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.recipe.RecipeEntryBackpackUpgrade;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.Map;

public class BackpackRecipes {

    public static void registerRecipes() {
        String modId = BetterWithBagsMod.MOD_ID;

        RecipeBuilder.Shaped(modId, "CLC", "LSL", "CLC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('S', Items.STRING)
            .create("backpack_small", BackpackItems.BACKPACK_SMALL.getDefaultStack());

        registerUpgrade("backpack_medium", BackpackItems.BACKPACK_SMALL, BackpackItems.BACKPACK_MEDIUM,
            new String[]{"CIC", "LBL", "CIC"},
            Map.of('L', Items.LEATHER, 'C', Items.CLOTH, 'I', Items.INGOT_IRON));

        registerUpgrade("backpack_large", BackpackItems.BACKPACK_MEDIUM, BackpackItems.BACKPACK_LARGE,
            new String[]{"CGC", "LBL", "CGC"},
            Map.of('L', Items.LEATHER, 'C', Items.CLOTH, 'G', Items.INGOT_GOLD));

        registerUpgrade("backpack_reinforced", BackpackItems.BACKPACK_LARGE, BackpackItems.BACKPACK_REINFORCED,
            new String[]{"DCD", "MBM", "DCD"},
            Map.of('D', Items.DIAMOND, 'C', Items.CLOTH, 'M', Blocks.MESH));

        registerUpgrade("backpack_nether", BackpackItems.BACKPACK_REINFORCED, BackpackItems.BACKPACK_NETHER,
            new String[]{"OCO", "MBM", "OCO"},
            Map.of('O', Items.OLIVINE, 'C', Items.NETHERCOAL, 'M', Blocks.MESH_GOLD));

        RecipeBuilder.Shaped(modId, "LWL", "LWL", "LLL")
            .addInput('L', Items.LEATHER)
            .addInput('W', Items.CLOTH)
            .create("bedroll", new ItemStack(BackpackItems.BEDROLL, 1));
    }

    @SuppressWarnings("unchecked")
    private static void registerUpgrade(String key, Item input, Item output, String[] shape, Map<Character, IItemConvertible> ingredients) {
        ItemStack anyInput = new ItemStack(input);
        anyInput.setMetadata(-1);

        RecipeSymbol[] symbols = new RecipeSymbol[9];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                char symbol = shape[y].charAt(x);
                ItemStack stack = symbol == 'B' ? anyInput : ingredients.get(symbol).getDefaultStack();
                symbols[x + y * 3] = new RecipeSymbol(symbol, stack);
            }
        }

        RecipeGroup<RecipeEntryCrafting<?, ?>> workbench = (RecipeGroup<RecipeEntryCrafting<?, ?>>) RecipeBuilder.getRecipeGroup(
            BetterWithBagsMod.MOD_ID, "workbench", new RecipeSymbol(Blocks.WORKBENCH.getDefaultStack()));
        workbench.register(key, new RecipeEntryBackpackUpgrade(3, 3, symbols, new ItemStack(output), true));
    }

    public static void registerRecipeTypes() {
        Registries.RECIPE_TYPES.register(RecipeEntryBackpackUpgrade.TYPE, RecipeEntryBackpackUpgrade.class);
    }

    public static void initNamespaces() {
        RecipeBuilder.initNameSpace(BetterWithBagsMod.MOD_ID);
    }
}
