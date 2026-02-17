package com.sajmonoriginal.betterwithbags.mixin;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BedrollSleepTracker;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Entity {

    @Shadow
    public ContainerInventory inventory;

    @Shadow
    public abstract void dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean flag);

    protected PlayerMixin(World world) {
        super(world);
    }

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void bwb$applyBackpackSpeedPenalty(CallbackInfo ci) {
        Player self = (Player) (Object) this;

        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(self);
        if (backpackStack == null) return;

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        if (!backpack.getTier().hasSpeedPenalty()) return;

        int occupied = ItemBackpack.getOccupiedSlotCount(backpackStack);
        int capacity = backpack.getCapacity();
        if (occupied <= 0 || capacity <= 0) return;

        float fillRatio = (float) occupied / capacity;
        float maxPenalty = 1.0f - backpack.getTier().getSpeedMultiplier();
        this.speed *= (1.0f - fillRatio * maxPenalty);
    }

    @Redirect(
        method = "sleepInBedAt",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;sendTranslatedChatMessage(Ljava/lang/String;)V")
    )
    private void bwb$redirectSpawnMessage(Player self, String key) {
        if (!BedrollSleepTracker.isSleepingInBedroll(self)) {
            self.sendTranslatedChatMessage(key);
        }
    }

    @Redirect(
        method = "sleepInBedAt",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;setPlayerSpawnCoordinate(Lnet/minecraft/core/world/chunk/ChunkCoordinates;)V")
    )
    private void bwb$redirectSpawnSet(Player self, ChunkCoordinates coords) {
        if (!BedrollSleepTracker.isSleepingInBedroll(self)) {
            self.setPlayerSpawnCoordinate(coords);
        }
    }

    @Inject(method = "wakeUpPlayer", at = @At("HEAD"))
    private void bwb$clearBedrollSleepFlag(boolean flag1, boolean flag2, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        BedrollSleepTracker.clearSleepingInBedroll(self);
    }

    @Inject(method = "setPlayerSleeping", at = @At("TAIL"))
    private void bwb$adjustBedrollSleepPosition(int x, int y, int z, CallbackInfo ci) {
        if (this.world != null && this.world.isBlockLoaded(x, y, z)) {
            net.minecraft.core.block.Block<?> block = this.world.getBlock(x, y, z);
            if (block != null && block.getLogic() instanceof com.sajmonoriginal.betterwithbags.block.BlockLogicBedroll) {
                // Bed surface: y + 0.5625 → sleep Y: y + 0.9375
                // Bedroll surface: y + 0.1875 → sleep Y: y + 0.5625 (0.375 lower)
                this.setPos(this.x, (double) y + 0.5625, this.z);
            }
        }
    }


}
