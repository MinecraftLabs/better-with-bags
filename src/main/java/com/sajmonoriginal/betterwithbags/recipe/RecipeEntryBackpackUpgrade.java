package com.sajmonoriginal.betterwithbags.recipe;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeCraftingShapedJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import java.lang.reflect.Type;

public class RecipeEntryBackpackUpgrade extends RecipeEntryCraftingShaped implements HasJsonAdapter {
    public static final String TYPE = BetterWithBagsMod.MOD_ID + ":crafting/backpack_upgrade";

    public RecipeEntryBackpackUpgrade() {
        super();
    }

    public RecipeEntryBackpackUpgrade(int width, int height, RecipeSymbol[] input, ItemStack output, boolean allowMirrored) {
        super(width, height, input, output, false, allowMirrored);
    }

    @Override
    public boolean matches(ContainerCrafting crafting) {
        if (!super.matches(crafting)) {
            return false;
        }
        int capacity = ((ItemBackpack) getOutput().getItem()).getCapacity();
        return ItemBackpack.getOccupiedSlotCount(findBackpack(crafting)) <= capacity;
    }

    @Override
    public ItemStack getCraftingResult(ContainerCrafting crafting) {
        ItemStack result = super.getCraftingResult(crafting);
        ItemStack source = findBackpack(crafting);
        if (source != null) {
            ItemBackpack.transferContents(source, result);
        }
        return result;
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new Adapter();
    }

    private static ItemStack findBackpack(ContainerCrafting crafting) {
        for (int i = 0; i < crafting.getContainerSize(); i++) {
            ItemStack stack = crafting.getItem(i);
            if (stack != null && stack.getItem() instanceof ItemBackpack) {
                return stack;
            }
        }
        return null;
    }

    public static class Adapter extends RecipeCraftingShapedJsonAdapter {
        @Override
        public RecipeEntryCraftingShaped deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RecipeEntryCraftingShaped shaped = super.deserialize(json, typeOfT, context);
            RecipeEntryBackpackUpgrade recipe = new RecipeEntryBackpackUpgrade(
                shaped.recipeWidth, shaped.recipeHeight, shaped.getInput(), shaped.getOutput(), shaped.allowMirrored);
            recipe.consolidationGroup = shaped.consolidationGroup;
            return recipe;
        }
    }
}
