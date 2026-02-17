package com.sajmonoriginal.betterwithbags.item;

public enum HelmetVariant {
    LEATHER_LAMP("leather", "lamp", false, 11, 12.0),
    LEATHER_LAMP_ALT("leather_alt", "lamp", false, 11, 12.0),
    LEATHER_CANDLE("leather_candle", "candle", true, 10, 10.0),
    LEATHER_CANDLE_ALT("leather_candle_alt", "candle", true, 10, 10.0),
    BRICK_LAMP("brick", "lamp", false, 13, 15.0),
    BRICK_LAMP_ALT("brick_alt", "lamp", false, 13, 15.0),
    BRICK_CANDLE("brick_candle", "candle", true, 12, 12.0),
    BRICK_CANDLE_ALT("brick_candle_alt", "candle", true, 12, 12.0),
    GOLD_LAMP("gold", "lamp", false, 14, 18.0),
    GOLD_LAMP_ALT("gold_alt", "lamp", false, 14, 18.0),
    GOLD_CANDLE("gold_candle", "candle", true, 13, 14.0),
    GOLD_CANDLE_ALT("gold_candle_alt", "candle", true, 13, 14.0);

    private final String material;
    private final String lightType;
    private final boolean ambient;
    private final int lightLevel;
    private final double lightRange;

    HelmetVariant(String material, String lightType, boolean ambient, int lightLevel, double lightRange) {
        this.material = material;
        this.lightType = lightType;
        this.ambient = ambient;
        this.lightLevel = lightLevel;
        this.lightRange = lightRange;
    }

    public String getMaterial() { return material; }
    public String getLightType() { return lightType; }
    public boolean isAmbient() { return ambient; }
    public int getLightLevel() { return lightLevel; }
    public double getLightRange() { return lightRange; }
    
    public String getArmorTextureName() {
        return "miners_helmet_" + material;
    }
    
    public String getGlowTexturePath() {
        return "/assets/betterwithbags/textures/armor/" + getArmorTextureName() + "_1_glow.png";
    }
}
