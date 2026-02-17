package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.HelmetVariant;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemMinersHelmet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Environment(EnvType.CLIENT)
@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {

    private static final String TEXTURE_PATH = "/assets/betterwithbags/textures/entity/";
    private static final String ARMOR_TEXTURE_PATH = "/assets/betterwithbags/textures/armor/";
    private static final String GEOMETRY_ID = "geometry.humanoid.armor.bag";
    private static final String BEDROLL_GEOMETRY_ID = "geometry.bag.bedroll";
    private static final String CANDLE_HELMET_GEOMETRY_ID = "geometry.miners_helmet.candle";
    private static final String BEDROLL_TEXTURE = TEXTURE_PATH + "bedroll_attachment.png";
    private static final float GLOW_ALPHA = 0.6f;
    private static final float SCALE = 0.0625f;

    @Shadow
    private ModelBiped modelBipedMain;

    @Unique
    private static StaticEntityModel bwb$model;

    @Unique
    private static StaticEntityModel bwb$modelBedroll;

    @Unique
    private static boolean bwb$bedrollTextureChecked = false;

    @Unique
    private static boolean bwb$bedrollTextureValid = false;

    @Unique
    private static ModelBiped bwb$helmetGlowModel;

    @Unique
    private static StaticEntityModel bwb$candleHelmetModel;

    protected MobRendererPlayerMixin(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Unique
    private ModelBiped bwb$getHelmetGlowModel() {
        if (bwb$helmetGlowModel == null) {
            bwb$helmetGlowModel = new ModelBiped(1.0f);
        }
        return bwb$helmetGlowModel;
    }

    @Unique
    private String bwb$getGlowTexture(Player player) {
        ItemStack helmet = player.inventory.armorInventory[3];
        if (helmet != null && helmet.getItem() instanceof ItemMinersHelmet) {
            return ((ItemMinersHelmet) helmet.getItem()).getVariant().getGlowTexturePath();
        }
        return "/assets/betterwithbags/textures/armor/miners_helmet_leather_1_glow.png";
    }

    @Unique
    private StaticEntityModel bwb$getModel() {
        if (bwb$model == null) {
            bwb$model = EntityGeometryMojangData.Cache.getModel(GEOMETRY_ID, 0);
        }
        return bwb$model;
    }

    @Unique
    private StaticEntityModel bwb$getBedrollModel() {
        if (bwb$modelBedroll == null) {
            bwb$modelBedroll = EntityGeometryMojangData.Cache.getModel(BEDROLL_GEOMETRY_ID, 0);
        }
        return bwb$modelBedroll;
    }

    @Unique
    private StaticEntityModel bwb$getCandleHelmetModel() {
        if (bwb$candleHelmetModel == null) {
            bwb$candleHelmetModel = EntityGeometryMojangData.Cache.getModel(CANDLE_HELMET_GEOMETRY_ID, 0);
        }
        return bwb$candleHelmetModel;
    }

    @Unique
    private String bwb$getTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_" + tier.getName() + ".png";
    }

    @Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V", at = @At("TAIL"))
    private void renderBackpackOnBack(Player player, float partialTick, CallbackInfo ci) {
        ItemStack chestSlot = player.inventory.armorInventory[2];
        if (chestSlot == null || !(chestSlot.getItem() instanceof ItemBackpack)) {
            return;
        }

        ItemBackpack backpackItem = (ItemBackpack) chestSlot.getItem();
        BackpackTier tier = backpackItem.getTier();
        boolean hasBedroll = ItemBackpack.hasAttachment(chestSlot);

        GL11.glPushMatrix();

        float bodyYaw = modelBipedMain.body.yRot;
        float bodyPitch = modelBipedMain.body.xRot;
        GL11.glRotatef(bodyYaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(bodyPitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

        float s = 0.0625F;
        GL11.glScalef(s, s, s);
        GL11.glTranslatef(0.0F, 24.6F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float brightness = player.getBrightness(partialTick);
        GL11.glColor4f(brightness, brightness, brightness, 1.0F);

        this.renderDispatcher.textureManager.loadTexture(bwb$getTexture(tier)).bind();
        bwb$getModel().render(Tessellator.instance);

        if (hasBedroll) {
            if (!bwb$bedrollTextureChecked) {
                bwb$bedrollTextureChecked = true;
                try {
                    InputStream stream = MobRendererPlayerMixin.class.getResourceAsStream(BEDROLL_TEXTURE);
                    if (stream != null) {
                        BufferedImage testImg = ImageIO.read(stream);
                        bwb$bedrollTextureValid = (testImg != null);
                        stream.close();
                    }
                } catch (Exception e) {
                    bwb$bedrollTextureValid = false;
                }
            }
            if (bwb$bedrollTextureValid) {
                try {
                    StaticEntityModel bedroll = bwb$getBedrollModel();
                    if (bedroll != null) {
                        this.renderDispatcher.textureManager.loadTexture(BEDROLL_TEXTURE).bind();
                        bedroll.render(Tessellator.instance);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }

    @Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V", at = @At("TAIL"))
    private void bwb$renderHelmetGlow(Player player, float partialTick, CallbackInfo ci) {
        if (!ItemMinersHelmet.isWearingHelmet(player)) {
            return;
        }

        ModelBiped glowModel = bwb$getHelmetGlowModel();

        glowModel.hair.yRot = modelBipedMain.head.yRot;
        glowModel.hair.xRot = modelBipedMain.head.xRot;
        glowModel.hair.x = modelBipedMain.head.x;
        glowModel.hair.y = modelBipedMain.head.y;
        glowModel.hair.z = modelBipedMain.head.z;
        glowModel.head.visible = false;
        glowModel.hair.visible = true;
        glowModel.body.visible = false;
        glowModel.armRight.visible = false;
        glowModel.armLeft.visible = false;
        glowModel.legRight.visible = false;
        glowModel.legLeft.visible = false;

        GL11.glPushMatrix();

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDepthMask(false);

        this.renderDispatcher.textureManager.loadTexture(bwb$getGlowTexture(player)).bind();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, GLOW_ALPHA);
        glowModel.hair.render(SCALE);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        GL11.glPopMatrix();
    }

    @Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V", at = @At("TAIL"))
    private void bwb$renderHelmetCandle(Player player, float partialTick, CallbackInfo ci) {
        HelmetVariant variant = ItemMinersHelmet.getHelmetVariant(player);
        if (variant == null || !variant.isAmbient()) {
            return;
        }

        StaticEntityModel candleModel = bwb$getCandleHelmetModel();
        if (candleModel == null) {
            return;
        }

        GL11.glPushMatrix();

        GL11.glTranslatef(
            modelBipedMain.head.x * SCALE,
            modelBipedMain.head.y * SCALE,
            modelBipedMain.head.z * SCALE
        );

        float headYaw = modelBipedMain.head.yRot;
        float headPitch = modelBipedMain.head.xRot;
        GL11.glRotatef(headYaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(headPitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

        float s = 0.0625F;
        GL11.glScalef(s, s, s);
        GL11.glTranslatef(0.0F, 24.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-1.0f, -1.0f);
        float brightness = player.getBrightness(partialTick);
        GL11.glColor4f(brightness, brightness, brightness, 1.0F);

        String armorTexture = ARMOR_TEXTURE_PATH + variant.getArmorTextureName() + "_1.png";
        this.renderDispatcher.textureManager.loadTexture(armorTexture).bind();
        candleModel.render(Tessellator.instance);

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }
}
