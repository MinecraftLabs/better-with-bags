package com.sajmonoriginal.betterwithbags.item;

public enum BackpackTier {
    SMALL("small", 9, 1.0f),
    MEDIUM("medium", 18, 1.0f),
    LARGE("large", 27, 0.90f),
    REINFORCED("reinforced", 36, 0.85f),
    NETHER("nether", 27, 1.0f);

    private final String name;
    private final int slots;
    private final float speedMultiplier;

    BackpackTier(String name, int slots, float speedMultiplier) {
        this.name = name;
        this.slots = slots;
        this.speedMultiplier = speedMultiplier;
    }

    public String getName() {
        return name;
    }

    public int getSlots() {
        return slots;
    }

    public int getRows() {
        return slots / 9;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean hasSpeedPenalty() {
        return speedMultiplier < 1.0f;
    }

    public boolean isFireResistant() {
        return this == NETHER;
    }

    public boolean supportsAttachment() {
        return this == LARGE || this == REINFORCED || this == NETHER;
    }

}
