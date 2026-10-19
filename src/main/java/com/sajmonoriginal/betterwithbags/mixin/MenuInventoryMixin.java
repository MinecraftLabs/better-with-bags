package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.sajmonoriginal.betterwithbags.gui.WornBackpackContainer;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.mixin.accessor.MenuAbstractAccessor;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpackAttachment;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = MenuInventory.class, remap = false)
public abstract class MenuInventoryMixin implements BackpackMenuCapable {
    @Shadow
    public ContainerInventory inventory;

    @Unique
    private static final int STANDARD_HEIGHT = 166;
    @Unique
    private static final int ROW_HEIGHT = 18;
    @Unique
    private static final int EXTENSION_TITLE_HEIGHT = 14;
    @Unique
    private static final int HIDDEN_Y = -9999;
    @Unique
    private static final int HOTBAR_SLOTS_START = 36;

    @Unique
    private boolean bwb$hasBackpackSlots = false;

    @Unique
    private int bwb$backpackSlotStartIndex = -1;

    @Unique
    private int bwb$backpackRows = 0;

    @Unique
    private List<SlotBackpack> bwb$backpackSlots = new ArrayList<>();

    @Unique
    private SlotBackpackAttachment bwb$attachmentSlot = null;

    @Unique
    private WornBackpackContainer bwb$backpackContainer = null;

    @Override
    public boolean betterWithBags$hasBackpackSlots() {
        return this.bwb$hasBackpackSlots;
    }

    @Override
    public int betterWithBags$getBackpackSlotStartIndex() {
        return this.bwb$backpackSlotStartIndex;
    }

    @Override
    public int betterWithBags$getBackpackRows() {
        return this.bwb$backpackRows;
    }

    public int getHotbarSlotId(int number) {
        return HOTBAR_SLOTS_START + number - 1;
    }

    @Inject(method = "<init>(Lnet/minecraft/core/player/inventory/container/ContainerInventory;)V",
            at = @At("RETURN"))
    private void bwb$addBackpackSlotsInConstructor(ContainerInventory inventory, CallbackInfo ci) {
        MenuInventory self = (MenuInventory) (Object) this;
        this.bwb$backpackSlotStartIndex = self.slots.size();

        bwb$backpackContainer = new WornBackpackContainer(inventory);

        int maxCapacity = BackpackTier.REINFORCED.getSlots();
        for (int i = 0; i < maxCapacity; i++) {
            SlotBackpack slot = new SlotBackpack(bwb$backpackContainer, inventory, i, HIDDEN_Y, HIDDEN_Y);
            bwb$backpackSlots.add(slot);
            ((MenuAbstractAccessor) this).invokeAddSlot(slot);
        }

        bwb$attachmentSlot = new SlotBackpackAttachment(inventory, HIDDEN_Y, HIDDEN_Y);
        ((MenuAbstractAccessor) this).invokeAddSlot(bwb$attachmentSlot);
    }

    @Override
    public void betterWithBags$refreshBackpackSlots() {
        if (inventory == null || inventory.player == null) {
            hideBackpackSlots();
            return;
        }

        ItemStack chestSlot = inventory.player.inventory.armorInventory[HumanArmorShape.CHEST.getSlotIndex()];
        boolean hasBackpack = chestSlot != null && chestSlot.getItem() instanceof ItemBackpack;

        if (!hasBackpack) {
            hideBackpackSlots();
            return;
        }

        ItemBackpack backpack = (ItemBackpack) chestSlot.getItem();
        BackpackTier tier = backpack.getTier();
        int capacity = tier.getSlots();
        int rows = tier.getRows();

        this.bwb$hasBackpackSlots = true;
        this.bwb$backpackRows = rows;

        for (int i = 0; i < bwb$backpackSlots.size(); i++) {
            SlotBackpack slot = bwb$backpackSlots.get(i);
            if (i < capacity) {
                int row = i / 9;
                int col = i % 9;
                slot.x = 8 + col * 18;
                slot.y = STANDARD_HEIGHT + EXTENSION_TITLE_HEIGHT + 1 + row * ROW_HEIGHT;
            } else {
                slot.x = HIDDEN_Y;
                slot.y = HIDDEN_Y;
            }
        }

        if (bwb$attachmentSlot != null && bwb$attachmentSlot.isActive()) {
            bwb$attachmentSlot.x = 178;
            bwb$attachmentSlot.y = STANDARD_HEIGHT + EXTENSION_TITLE_HEIGHT + 1;
        } else if (bwb$attachmentSlot != null) {
            bwb$attachmentSlot.x = HIDDEN_Y;
            bwb$attachmentSlot.y = HIDDEN_Y;
        }
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"))
    private void bwb$flushBackpackContainer(Container container, CallbackInfo ci) {
        if (bwb$backpackContainer != null) {
            bwb$backpackContainer.flush();
        }

        betterWithBags$refreshBackpackSlots();
    }

    @Unique
    private void hideBackpackSlots() {
        this.bwb$hasBackpackSlots = false;
        this.bwb$backpackRows = 0;

        for (SlotBackpack slot : bwb$backpackSlots) {
            slot.x = HIDDEN_Y;
            slot.y = HIDDEN_Y;
        }

        if (bwb$attachmentSlot != null) {
            bwb$attachmentSlot.x = HIDDEN_Y;
            bwb$attachmentSlot.y = HIDDEN_Y;
        }
    }

    @ModifyReturnValue(method = "getTargetSlots", at = @At("RETURN"))
    private IntList bwb$addBackpackTargets(IntList original, InventoryAction action, Slot slot, int target, Player player) {
        betterWithBags$refreshBackpackSlots();
        if (original == null || !bwb$hasBackpackSlots || bwb$backpackSlotStartIndex < 0) {
            return original;
        }
        if (slot.index < 9 || slot.index > 44) {
            return original;
        }
        for (int i = 0; i < original.size(); i++) {
            int index = original.getInt(i);
            if (index < 9 || index > 44) {
                return original;
            }
        }

        IntList targets = new IntArrayList();
        int capacity = bwb$backpackRows * 9;
        for (int i = 0; i < capacity; i++) {
            targets.add(bwb$backpackSlotStartIndex + i);
        }
        targets.addAll(original);
        return targets;
    }
}
