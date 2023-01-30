//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.render.misc.Pair;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.RenderLivingModelEvent;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

@Module.Manifest(value=Module.Category.RENDER, description="Look like x8 and whale")
public class ChingChongHat
extends Module {
    @Setting
    private Color color = new Color(-1131345008, true);
    @Setting
    private boolean rainbow = true;
    private static ChingChongHat instance;

    @Listener
    private void listen(RenderLivingModelEvent event) {
        if (Constants.nullCheck() || !(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        Vec3d position = new Vec3d(0.0, 0.0, 0.0);
        ArrayList<Vec3d> vertices = Constants.renderer.get3DArcVertices(position, 0.5, new Pair<Float, Float>(Float.valueOf(-180.0f), Float.valueOf(180.0f)), 25);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        ModelRenderer head = ((ModelBiped)event.getRenderLivingBase().getMainModel()).bipedHead;
        GlStateManager.translate((float)(head.rotationPointX * event.getScaleFactor()), (float)(head.rotationPointY * event.getScaleFactor()), (float)(head.rotationPointZ * event.getScaleFactor()));
        if (head.rotateAngleZ != 0.0f) {
            GlStateManager.rotate((float)(head.rotateAngleZ * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        if (head.rotateAngleY != 0.0f) {
            GlStateManager.rotate((float)(head.rotateAngleY * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (head.rotateAngleX != 0.0f) {
            GlStateManager.rotate((float)(head.rotateAngleX * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        GlStateManager.translate((double)0.0, (double)-0.5, (double)0.0);
        if (event.getEntity().isSneaking()) {
            GlStateManager.translate((float)0.0f, (float)0.27f, (float)0.0f);
        }
        if (this.rainbow) {
            Constants.renderer.getRenderer().setup2D().start(6).addVertex(position.addVector(0.0, -0.25, 0.0), Color.WHITE).execute(() -> {
                float startingHue = ColorUtil.getCycle();
                for (int i = 0; i < vertices.size(); ++i) {
                    float newHue = (float)i / (float)vertices.size() + startingHue;
                    Constants.renderer.getRenderer().addVertex((Vec3d)vertices.get(i), ColorUtil.createColor(newHue, 1.0f, 1.0f));
                }
            }).finish().release2D();
        } else {
            Constants.renderer.getRenderer().setup2D().start(6).addVertex(position.addVector(0.0, -0.25, 0.0), this.color).addVertices(vertices, this.color).finish().release2D();
        }
        GlStateManager.popMatrix();
    }

    public static ChingChongHat getInstance() {
        if (instance == null) {
            instance = new ChingChongHat();
        }
        return instance;
    }
}

