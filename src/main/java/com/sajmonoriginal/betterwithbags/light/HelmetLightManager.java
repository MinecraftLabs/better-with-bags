package com.sajmonoriginal.betterwithbags.light;

import com.sajmonoriginal.betterwithbags.item.HelmetVariant;
import com.sajmonoriginal.betterwithbags.item.ItemMinersHelmet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.item.ItemStack;

public class HelmetLightManager {
    private static final double LIGHT_RANGE = 11.625;
    private static final double SPOT_EXPONENT = 3.0;
    private static final double AMBIENT_SPILL = 0.15;

    private static boolean hasLight = false;
    private static boolean isAmbient = false;
    private static int lightLevel = 0;
    private static double playerX, playerY, playerZ;
    private static double lookDirX, lookDirY, lookDirZ;

    private static int prevBlockX = Integer.MIN_VALUE;
    private static int prevBlockY = Integer.MIN_VALUE;
    private static int prevBlockZ = Integer.MIN_VALUE;
    private static double dirtyLookX, dirtyLookY, dirtyLookZ;
    private static int ticksSinceLastDirty = 0;
    private static final double DIR_THRESHOLD = 0.05;
    private static final int DIR_DIRTY_COOLDOWN = 2;

    public static void updateWorld() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.currentWorld == null || mc.renderGlobal == null) return;

        boolean hadLight = hasLight;

        hasLight = false;
        if (mc.thePlayer != null) {
            ItemStack helmet = mc.thePlayer.inventory.armorInventory[3];
            if (helmet != null && helmet.getItem() instanceof ItemMinersHelmet) {
                HelmetVariant variant = ((ItemMinersHelmet) helmet.getItem()).getVariant();
                hasLight = true;
                isAmbient = variant.isAmbient();
                lightLevel = variant.getLightLevel();
                playerX = mc.thePlayer.x;
                playerY = mc.thePlayer.y;
                playerZ = mc.thePlayer.z;
                double yawRad = Math.toRadians(mc.thePlayer.yRot);
                double pitchRad = Math.toRadians(mc.thePlayer.xRot);
                double cosPitch = Math.cos(pitchRad);
                lookDirX = -Math.sin(yawRad) * cosPitch;
                lookDirY = -Math.sin(pitchRad);
                lookDirZ = Math.cos(yawRad) * cosPitch;

                if (variant.isAmbient()) {
                    spawnCandleFlameParticles(mc, yawRad, pitchRad);
                }
            }
        }

        ticksSinceLastDirty++;

        if (hasLight) {
            int bx = (int) Math.floor(playerX);
            int by = (int) Math.floor(playerY);
            int bz = (int) Math.floor(playerZ);

            int skyLight = mc.currentWorld.getSavedLightValue(LightLayer.Sky, bx, by, bz);
            if (skyLight >= lightLevel && !hadLight) {
                prevBlockX = bx;
                prevBlockY = by;
                prevBlockZ = bz;
                dirtyLookX = lookDirX;
                dirtyLookY = lookDirY;
                dirtyLookZ = lookDirZ;
                return;
            }

            boolean moved = bx != prevBlockX || by != prevBlockY || bz != prevBlockZ;

            boolean dirChanged = false;
            if (!isAmbient && ticksSinceLastDirty >= DIR_DIRTY_COOLDOWN) {
                dirChanged = Math.abs(lookDirX - dirtyLookX) > DIR_THRESHOLD
                    || Math.abs(lookDirY - dirtyLookY) > DIR_THRESHOLD
                    || Math.abs(lookDirZ - dirtyLookZ) > DIR_THRESHOLD;
            }

            if (moved || dirChanged || !hadLight) {
                int r = (int) LIGHT_RANGE;
                if (hadLight && moved) {
                    mc.renderGlobal.markDirty(
                        prevBlockX - r, prevBlockY - r, prevBlockZ - r,
                        prevBlockX + r, prevBlockY + r, prevBlockZ + r
                    );
                }
                mc.renderGlobal.markDirty(
                    bx - r, by - r, bz - r,
                    bx + r, by + r, bz + r
                );
                prevBlockX = bx;
                prevBlockY = by;
                prevBlockZ = bz;
                dirtyLookX = lookDirX;
                dirtyLookY = lookDirY;
                dirtyLookZ = lookDirZ;
                ticksSinceLastDirty = 0;
            }
        } else if (hadLight) {
            int r = (int) LIGHT_RANGE;
            mc.renderGlobal.markDirty(
                prevBlockX - r, prevBlockY - r, prevBlockZ - r,
                prevBlockX + r, prevBlockY + r, prevBlockZ + r
            );
            prevBlockX = Integer.MIN_VALUE;
            prevBlockY = Integer.MIN_VALUE;
            prevBlockZ = Integer.MIN_VALUE;
        }
    }

    public static int calculateDynamicLight(int x, int y, int z, int lightmapCoord) {
        if (!hasLight) return lightmapCoord;

        double dx = x + 0.5 - playerX;
        double dy = y + 0.5 - playerY;
        double dz = z + 0.5 - playerZ;
        double distSq = dx * dx + dy * dy + dz * dz;
        double rangeSq = LIGHT_RANGE * LIGHT_RANGE;

        if (distSq >= rangeSq) return lightmapCoord;

        double dist = Math.sqrt(distSq);
        double distFalloff = 1.0 - dist / LIGHT_RANGE;
        double dynamicLevel;

        if (isAmbient) {
            dynamicLevel = lightLevel * distFalloff;
        } else {
            double angularFactor;
            if (dist < 0.5) {
                angularFactor = 1.0;
            } else {
                double invDist = 1.0 / dist;
                double dot = (dx * lookDirX + dy * lookDirY + dz * lookDirZ) * invDist;
                if (dot <= 0) {
                    angularFactor = AMBIENT_SPILL;
                } else {
                    angularFactor = AMBIENT_SPILL + (1.0 - AMBIENT_SPILL) * Math.pow(dot, SPOT_EXPONENT);
                }
            }
            dynamicLevel = lightLevel * distFalloff * angularFactor;
        }

        int skylight = lightmapCoord >> 20;
        int blocklight = (lightmapCoord >> 4) & 0xFFFF;

        if (blocklight < dynamicLevel) {
            return LightmapHelper.getLightmapCoord(skylight, (int) dynamicLevel);
        }

        return lightmapCoord;
    }

    public static int getDynamicLightAt(double x, double y, double z) {
        if (!hasLight) return 0;

        double dx = x - playerX;
        double dy = y - playerY;
        double dz = z - playerZ;
        double distSq = dx * dx + dy * dy + dz * dz;

        if (distSq >= LIGHT_RANGE * LIGHT_RANGE) return 0;

        double dist = Math.sqrt(distSq);
        double distFalloff = 1.0 - dist / LIGHT_RANGE;
        double dynamicLevel;

        if (isAmbient) {
            dynamicLevel = lightLevel * distFalloff;
        } else {
            double angularFactor;
            if (dist < 0.5) {
                angularFactor = 1.0;
            } else {
                double invDist = 1.0 / dist;
                double dot = (dx * lookDirX + dy * lookDirY + dz * lookDirZ) * invDist;
                if (dot <= 0) {
                    angularFactor = AMBIENT_SPILL;
                } else {
                    angularFactor = AMBIENT_SPILL + (1.0 - AMBIENT_SPILL) * Math.pow(dot, SPOT_EXPONENT);
                }
            }
            dynamicLevel = lightLevel * distFalloff * angularFactor;
        }

        return (int) dynamicLevel;
    }

    private static void spawnCandleFlameParticles(Minecraft mc, double yawRad, double pitchRad) {
        if (mc.currentWorld.rand.nextInt(3) != 0) return;

        // player.y = feet + 1.62 (eye level). Wick tip is ~0.35 above eyes, ~0.27 forward.
        double wickUp = 0.35;
        double wickForward = 0.27;

        double cosP = Math.cos(pitchRad);
        double sinP = Math.sin(pitchRad);
        double ry = wickUp * cosP + wickForward * sinP;
        double rf = -wickUp * sinP + wickForward * cosP;

        double sinY = Math.sin(yawRad);
        double cosY = Math.cos(yawRad);

        double px = mc.thePlayer.x - rf * sinY;
        double py = mc.thePlayer.y + ry;
        double pz = mc.thePlayer.z + rf * cosY;

        mc.currentWorld.spawnParticle("flame", px, py, pz, 0.0, 0.0, 0.0, 0);
        if (mc.currentWorld.rand.nextInt(2) == 0) {
            mc.currentWorld.spawnParticle("smoke", px, py, pz, 0.0, 0.0, 0.0, 0);
        }
    }

    public static void onWorldUnload() {
        hasLight = false;
        lightLevel = 0;
        prevBlockX = Integer.MIN_VALUE;
        prevBlockY = Integer.MIN_VALUE;
        prevBlockZ = Integer.MIN_VALUE;
    }
}
