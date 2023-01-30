//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.layers.LayerArmorBase
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 */
package me.ninethousand.violet.client.impl.modules.render;

import java.io.File;
import javax.imageio.ImageIO;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.image.EfficientTexture;
import me.ninethousand.violet.client.api.render.shader.shaders.ImageShader;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.RenderArmorEvent;
import me.ninethousand.violet.client.mixin.ILayerArmorBase;
import me.ninethousand.violet.client.mixin.IRenderLivingBase;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

@Module.Manifest(value=Module.Category.RENDER, description="PK awesome shader")
public class ShaderESP
extends Module {
    private ImageShader shader;
    private EfficientTexture efficientTexture;
    private static ShaderESP instance;

    @Override
    protected void onEnable() {
        try {
            this.efficientTexture = new EfficientTexture(ImageIO.read(new File("D:\\Violet\\encrypt\\vliolet2.2v2\\olke.png")));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    protected void onDisable() {
        this.shader = null;
    }

    @Listener
    private void listen(Render3DEvent event) {
        if (this.shader == null) {
            this.shader = ImageShader.getInstance();
        }
        GlStateManager.matrixMode((int)5889);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.pushMatrix();
        this.shader.startDraw(event.getPartialTicks());
        for (Entity e : Constants.mc.world.loadedEntityList) {
            Render renderObject;
            if (e == Constants.mc.getRenderViewEntity() || (renderObject = Constants.mc.getRenderManager().getEntityRenderObject(e)) == null) continue;
            Vec3d interpolatedRenderPos = EntityUtil.getInterpolatedRenderPos(e, event.getPartialTicks());
            if (!(renderObject instanceof RenderLivingBase) || !(e instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer)e;
            ((IRenderLivingBase)renderObject).getLayers().stream().filter(layerRenderer -> layerRenderer instanceof LayerArmorBase).forEach(layerRenderer -> {
                ((ILayerArmorBase)layerRenderer).invokeRenderArmorLayer((EntityLivingBase)player, player.limbSwing, player.limbSwingAmount, event.getPartialTicks(), player.ticksExisted, player.rotationYaw, player.rotationPitch, 0.0625f, EntityEquipmentSlot.HEAD);
                ((ILayerArmorBase)layerRenderer).invokeRenderArmorLayer((EntityLivingBase)player, player.limbSwing, player.limbSwingAmount, event.getPartialTicks(), player.ticksExisted, player.rotationYaw, player.rotationPitch, 0.0625f, EntityEquipmentSlot.CHEST);
                ((ILayerArmorBase)layerRenderer).invokeRenderArmorLayer((EntityLivingBase)player, player.limbSwing, player.limbSwingAmount, event.getPartialTicks(), player.ticksExisted, player.rotationYaw, player.rotationPitch, 0.0625f, EntityEquipmentSlot.LEGS);
                ((ILayerArmorBase)layerRenderer).invokeRenderArmorLayer((EntityLivingBase)player, player.limbSwing, player.limbSwingAmount, event.getPartialTicks(), player.ticksExisted, player.rotationYaw, player.rotationPitch, 0.0625f, EntityEquipmentSlot.FEET);
            });
        }
        GL13.glActiveTexture((int)33985);
        GL11.glBindTexture((int)3553, (int)this.efficientTexture.getGlTextureId());
        this.shader.stopDraw();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
    }

    @Listener
    private void listen(RenderArmorEvent event) {
        event.cancel();
    }

    private void doRender(Entity entity, float partialTicks) {
        Render renderObject = Constants.mc.getRenderManager().getEntityRenderObject(entity);
        if (renderObject == null) {
            return;
        }
        Vec3d interpolatedRenderPos = EntityUtil.getInterpolatedRenderPos(entity, partialTicks);
        renderObject.doRender(entity, interpolatedRenderPos.xCoord, interpolatedRenderPos.yCoord, interpolatedRenderPos.zCoord, entity.rotationYaw, partialTicks);
    }

    public static ShaderESP getInstance() {
        if (instance == null) {
            instance = new ShaderESP();
        }
        return instance;
    }
}

