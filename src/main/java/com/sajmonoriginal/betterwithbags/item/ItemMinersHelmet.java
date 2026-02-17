package com.sajmonoriginal.betterwithbags.item;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;

public class ItemMinersHelmet extends Item implements IArmorItem {
    private final HelmetVariant variant;

    public ItemMinersHelmet(String name, String textureKey, int id, HelmetVariant variant) {
        super(name, textureKey, id);
        this.maxStackSize = 1;
        this.variant = variant;
    }

    public HelmetVariant getVariant() {
        return variant;
    }

    @Override
    public ArmorMaterial getArmorMaterial() {
        switch (variant) {
            case LEATHER_LAMP:
                return BackpackArmorMaterial.MINERS_HELMET_LEATHER;
            case LEATHER_LAMP_ALT:
                return BackpackArmorMaterial.MINERS_HELMET_LEATHER_ALT;
            case LEATHER_CANDLE:
                return BackpackArmorMaterial.MINERS_HELMET_LEATHER_CANDLE;
            case LEATHER_CANDLE_ALT:
                return BackpackArmorMaterial.MINERS_HELMET_LEATHER_CANDLE_ALT;
            case BRICK_LAMP:
                return BackpackArmorMaterial.MINERS_HELMET_BRICK;
            case BRICK_LAMP_ALT:
                return BackpackArmorMaterial.MINERS_HELMET_BRICK_ALT;
            case BRICK_CANDLE:
                return BackpackArmorMaterial.MINERS_HELMET_BRICK_CANDLE;
            case BRICK_CANDLE_ALT:
                return BackpackArmorMaterial.MINERS_HELMET_BRICK_CANDLE_ALT;
            case GOLD_LAMP:
                return BackpackArmorMaterial.MINERS_HELMET_GOLD;
            case GOLD_LAMP_ALT:
                return BackpackArmorMaterial.MINERS_HELMET_GOLD_ALT;
            case GOLD_CANDLE:
                return BackpackArmorMaterial.MINERS_HELMET_GOLD_CANDLE;
            case GOLD_CANDLE_ALT:
                return BackpackArmorMaterial.MINERS_HELMET_GOLD_CANDLE_ALT;
            default:
                return BackpackArmorMaterial.MINERS_HELMET_LEATHER;
        }
    }

    @Override
    public int armorPieceProtection() {
        return 2;
    }

    @Override
    public int getArmorPiece() {
        return 3;
    }

    public static boolean isWearingHelmet(Player player) {
        ItemStack helmet = player.inventory.armorInventory[3];
        return helmet != null && helmet.getItem() instanceof ItemMinersHelmet;
    }

    public static HelmetVariant getHelmetVariant(Player player) {
        ItemStack helmet = player.inventory.armorInventory[3];
        if (helmet != null && helmet.getItem() instanceof ItemMinersHelmet) {
            return ((ItemMinersHelmet) helmet.getItem()).getVariant();
        }
        return null;
    }
}
