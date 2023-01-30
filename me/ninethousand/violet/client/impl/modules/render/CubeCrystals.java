//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.impl.modules.render;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.ICanvas;
import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.RenderCrystalModelBaseEvent;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

@Module.Manifest(value=Module.Category.RENDER, description="GS++ is shit")
public class CubeCrystals
extends Module {
    @Setting
    private boolean rubiks = true;
    @Setting
    private boolean randomAxis = true;
    @Setting
    private Color color = new Color(-1131345008, true);
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
        if (this.viewerCrystal == null) {
            this.viewerCrystal = new EntityEnderCrystal((World)Constants.mc.world);
        }
        this.viewerCrystal.setShowBottom(false);
        Constants.renderer.drawEntityOnScreen(d.width / 2, d.height - d.height / 10, (int)((double)d.height / 3.5), 0.0f, (Entity)this.viewerCrystal);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    };
    private Vec3d rotationDirection;
    private Animation rotationAnimation;
    private Random random;
    private static CubeCrystals instance;

    @Override
    protected void onEnable() {
        this.rotationDirection = new Vec3d(1.0, 0.0, 0.0);
        this.resetAnimation();
        this.random = new Random();
    }

    @Listener
    private void listen(RenderCrystalModelBaseEvent event) {
        if (Constants.nullCheck()) {
            return;
        }
        event.cancel();
        if (!event.getModelRenderer().boxName.equalsIgnoreCase("cube")) {
            return;
        }
        if (!this.rubiks) {
            event.getModelRenderer().render(event.getJ());
            return;
        }
        HashMap<Vec3i, AxisAlignedBB> map = new HashMap<Vec3i, AxisAlignedBB>();
        double partition = 0.3333333333333333;
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                for (int z = 0; z < 3; ++z) {
                    AxisAlignedBB bb = new AxisAlignedBB(-0.5 + (double)x * partition, -0.5 + (double)y * partition, -0.5 + (double)z * partition, -0.5 + (double)(x + 1) * partition, -0.5 + (double)(y + 1) * partition, -0.5 + (double)(z + 1) * partition);
                    map.put(new Vec3i(x, y, z), bb);
                }
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.translate((float)event.getModelRenderer().offsetX, (float)event.getModelRenderer().offsetY, (float)event.getModelRenderer().offsetZ);
        GlStateManager.scale((double)0.5, (double)0.5, (double)0.5);
        List bbs = map.entrySet().stream().sorted(Comparator.comparingInt(e -> {
            if (this.rotationDirection.xCoord > 0.0) {
                return ((Vec3i)e.getKey()).getX();
            }
            if (this.rotationDirection.yCoord > 0.0) {
                return ((Vec3i)e.getKey()).getY();
            }
            return ((Vec3i)e.getKey()).getZ();
        })).map(e -> (AxisAlignedBB)e.getValue()).collect(Collectors.toList());
        for (int i = 0; i < bbs.size(); ++i) {
            this.doRubiksRotation(i);
            Constants.renderer.depthBoundedAABB((AxisAlignedBB)bbs.get(i), 0.0, 0.0, 0.0, this.color);
        }
        GlStateManager.popMatrix();
    }

    private void resetAnimation() {
        this.rotationAnimation = new Animation().setMax(90.0f).setMin(0.0f).setSpeed(120.0f);
    }

    private void doRubiksRotation(int i) {
        if (i != 18) {
            return;
        }
        this.rotationAnimation.update();
        GlStateManager.rotate((float)this.rotationAnimation.getValue(), (float)((float)this.rotationDirection.xCoord), (float)((float)this.rotationDirection.yCoord), (float)((float)this.rotationDirection.zCoord));
        if (this.rotationAnimation.getValue() >= 90.0f) {
            this.rotationDirection = this.randomAxis ? this.getRandomRotation() : (this.rotationDirection.xCoord != 0.0 ? new Vec3d(0.0, 1.0, 0.0) : (this.rotationDirection.yCoord != 0.0 ? new Vec3d(0.0, 0.0, 1.0) : new Vec3d(1.0, 0.0, 0.0)));
            this.resetAnimation();
        }
    }

    private Vec3d getRandomRotation() {
        int axis = this.random.nextInt(3);
        switch (axis) {
            case 0: {
                return new Vec3d(1.0, 0.0, 0.0);
            }
            case 1: {
                return new Vec3d(0.0, 1.0, 0.0);
            }
        }
        return new Vec3d(0.0, 0.0, 1.0);
    }

    public static CubeCrystals getInstance() {
        if (instance == null) {
            instance = new CubeCrystals();
        }
        return instance;
    }
}

