//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.impl.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.ICanvas;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.RenderCrystalModelEvent;
import me.ninethousand.violet.client.impl.events.RenderLivingModelEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.EntityPopCham;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@Module.Manifest(value=Module.Category.RENDER, description="Makes entities look gamer")
public class Chams
extends Module {
    @Setting
    private boolean players = true;
    @Setting
    private boolean crystals = true;
    @Setting
    private Style style = Style.Both;
    @Setting
    private Color color = new Color(-1131345008, true);
    @Setting
    private boolean fillThroughWalls = true;
    @Setting
    private boolean linesThroughWalls = true;
    @Setting
    private boolean texture = true;
    private EntityOtherPlayerMP viewerPlayer;
    private EntityEnderCrystal viewerCrystal;
    @Setting
    private ICanvas canvas = d -> {
        if (Constants.nullCheck()) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (this.viewerPlayer == null) {
            this.viewerPlayer = new EntityOtherPlayerMP((World)Constants.mc.world, new GameProfile(Constants.mc.player.getUniqueID(), "Chams"));
        }
        if (this.viewerCrystal == null) {
            this.viewerCrystal = new EntityEnderCrystal((World)Constants.mc.world);
        }
        this.viewerPlayer.rotationYaw = 0.0f;
        this.viewerPlayer.rotationPitch = 0.0f;
        this.viewerPlayer.rotationYawHead = 0.0f;
        this.viewerCrystal.setShowBottom(false);
        Constants.renderer.drawEntityOnScreen(d.width / 4, d.height - d.height / 10, (int)((double)d.height / 2.5), 0.0f, (Entity)this.viewerPlayer);
        Constants.renderer.drawEntityOnScreen(3 * d.width / 4, d.height - d.height / 10, (int)((double)d.height / 3.5), 0.0f, (Entity)this.viewerCrystal);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    };
    private final ResourceLocation GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static Chams instance;

    @Listener
    private void listen(RenderLivingModelEvent event) {
        if (Constants.nullCheck() || !this.shouldRender((Entity)event.getEntity())) {
            return;
        }
        event.cancel();
        if (this.style == Style.Fill || this.style == Style.Both) {
            GlStateManager.pushMatrix();
            Constants.renderer.getRenderer().setup3D();
            if (!this.fillThroughWalls) {
                GlStateManager.enableDepth();
            }
            if (this.texture) {
                GlStateManager.enableTexture2D();
            }
            GlStateManager.disableLighting();
            GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f));
            event.invokeRenderModel();
            Constants.renderer.getRenderer().release3D();
            GlStateManager.popMatrix();
        }
        if (this.style == Style.Outline || this.style == Style.Both) {
            GlStateManager.pushMatrix();
            Constants.renderer.getRenderer().setupLine().lineWidth(2.0f);
            if (!this.linesThroughWalls) {
                GlStateManager.enableDepth();
            }
            GlStateManager.disableLighting();
            GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f));
            event.invokeRenderModel();
            Constants.renderer.getRenderer().resetLineWidth().releaseLine();
            GlStateManager.popMatrix();
        }
        if (this.style == Style.Texture) {
            GlStateManager.pushMatrix();
            event.invokeRenderModel();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            Constants.renderer.getRenderer().setup3D();
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f));
            GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
            Constants.mc.getTextureManager().bindTexture(this.GLINT);
            ModelBiped model = (ModelBiped)event.getRenderLivingBase().getMainModel();
            model.setRotationAngles(event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor(), (Entity)event.getEntity());
            GlStateManager.pushMatrix();
            if (model.isChild) {
                float f = 2.0f;
                GlStateManager.scale((float)0.75f, (float)0.75f, (float)0.75f);
                GlStateManager.translate((float)0.0f, (float)(16.0f * event.getScaleFactor()), (float)0.0f);
                model.bipedHead.render(event.getScaleFactor() * 1.05f);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
                GlStateManager.translate((float)0.0f, (float)(24.0f * event.getScaleFactor()), (float)0.0f);
                model.bipedBody.render(event.getScaleFactor());
                model.bipedRightArm.render(event.getScaleFactor());
                model.bipedLeftArm.render(event.getScaleFactor());
                model.bipedRightLeg.render(event.getScaleFactor());
                model.bipedLeftLeg.render(event.getScaleFactor());
                model.bipedHeadwear.render(event.getScaleFactor());
            } else {
                if (event.getEntity().isSneaking()) {
                    GlStateManager.translate((float)0.0f, (float)0.2f, (float)0.0f);
                }
                model.bipedHead.render(event.getScaleFactor());
                model.bipedBody.render(event.getScaleFactor());
                model.bipedRightArm.render(event.getScaleFactor());
                model.bipedLeftArm.render(event.getScaleFactor());
                model.bipedRightLeg.render(event.getScaleFactor());
                model.bipedLeftLeg.render(event.getScaleFactor());
                model.bipedHeadwear.render(event.getScaleFactor());
            }
            GlStateManager.popMatrix();
            Constants.renderer.getRenderer().release3D();
            GlStateManager.popMatrix();
        }
    }

    @Listener
    private void listen(RenderCrystalModelEvent event) {
        if (Constants.nullCheck() || !this.shouldRender(event.getEntity())) {
            return;
        }
        event.cancel();
        if (this.style == Style.Fill || this.style == Style.Both) {
            GlStateManager.pushMatrix();
            Constants.renderer.getRenderer().setup3D();
            if (!this.fillThroughWalls) {
                GlStateManager.enableDepth();
            }
            if (this.texture) {
                GlStateManager.enableTexture2D();
            }
            GlStateManager.disableLighting();
            GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f));
            event.renderModel();
            Constants.renderer.getRenderer().release3D();
            GlStateManager.popMatrix();
        }
        if (this.style == Style.Outline || this.style == Style.Both) {
            GlStateManager.pushMatrix();
            Constants.renderer.getRenderer().setupLine().lineWidth(2.0f);
            if (!this.linesThroughWalls) {
                GlStateManager.enableDepth();
            }
            GlStateManager.disableLighting();
            GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f));
            event.renderModel();
            Constants.renderer.getRenderer().resetLineWidth().releaseLine();
            GlStateManager.popMatrix();
        }
        if (this.style == Style.Texture) {
            GlStateManager.pushMatrix();
            event.renderModel();
            GlStateManager.popMatrix();
        }
    }

    private boolean shouldRender(Entity entity) {
        return !(entity instanceof EntityPopCham) && (entity instanceof EntityPlayer && this.players || entity instanceof EntityEnderCrystal && this.crystals);
    }

    public static Chams getInstance() {
        if (instance == null) {
            instance = new Chams();
        }
        return instance;
    }

    private static enum Style {
        Fill,
        Outline,
        Both,
        Texture;

    }
}

