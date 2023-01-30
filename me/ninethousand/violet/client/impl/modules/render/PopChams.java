//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 */
package me.ninethousand.violet.client.impl.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.misc.RenderMathUtil;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.RenderLivingModelEvent;
import me.ninethousand.violet.client.impl.events.TotemPopEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.EntityPopCham;
import me.ninethousand.violet.client.util.minecraft.EntityUtil;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@Module.Manifest(value=Module.Category.RENDER, description="Etikahack inspired")
public class PopChams
extends Module {
    @Setting
    private Color color = new Color(-1131345008, true);
    @Setting(min=0.0, max=10000.0)
    private int time = 1000;
    @Setting
    private boolean rise = true;
    @Setting(min=0.0, max=10.0)
    private float height = 5.0f;
    private Map<EntityPopCham, Timer> ghosts;
    private static PopChams instance;

    @Override
    protected void onEnable() {
        if (Constants.nullCheck()) {
            return;
        }
    }

    @Override
    protected void onDisable() {
        this.ghosts = null;
    }

    @Listener
    public void listen(TotemPopEvent event) {
        if (Constants.nullCheck()) {
            return;
        }
        if (this.ghosts == null) {
            this.ghosts = new HashMap<EntityPopCham, Timer>();
        }
        EntityPopCham ghost = new EntityPopCham((World)Constants.mc.world, new GameProfile(Constants.mc.player.getUniqueID(), ""), event.getPlayer());
        ghost.copyLocationAndAnglesFrom((Entity)event.getPlayer());
        ghost.limbSwingAmount = event.getPlayer().limbSwingAmount;
        ghost.setAlwaysRenderNameTag(false);
        this.ghosts.put(ghost, new Timer());
    }

    @Listener
    private void listen(Render3DEvent event) {
        if (Constants.nullCheck() || this.ghosts == null) {
            return;
        }
        for (Map.Entry<EntityPopCham, Timer> entry : this.ghosts.entrySet()) {
            RenderPlayer render = new RenderPlayer(Constants.mc.getRenderManager());
            Vec3d interpolatedRenderPos = EntityUtil.getInterpolatedRenderPos((Entity)entry.getKey(), event.getPartialTicks());
            GL11.glPushMatrix();
            float boost = this.rise ? this.height * entry.getValue().getTime() / (float)this.time : 0.0f;
            GlStateManager.translate((double)interpolatedRenderPos.xCoord, (double)(interpolatedRenderPos.yCoord + (double)boost), (double)interpolatedRenderPos.zCoord);
            GlStateManager.rotate((float)(-entry.getKey().rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
            if (render != null) {
                render.doRender((AbstractClientPlayer)entry.getKey(), 0.0, 0.0, 0.0, entry.getKey().rotationYaw, 1.0f);
            }
            GL11.glPopMatrix();
        }
        this.ghosts.entrySet().removeIf(e -> ((Timer)e.getValue()).passed(this.time));
    }

    @Listener
    private void listen(RenderLivingModelEvent event) {
        if (Constants.nullCheck() || !(event.getEntity() instanceof EntityPopCham)) {
            return;
        }
        event.cancel();
        float alphaMultiplier = RenderMathUtil.clamp(1.0f - this.ghosts.get(event.getEntity()).getTime() / (float)this.time, 0.0f, 1.0f);
        GlStateManager.pushMatrix();
        Constants.renderer.getRenderer().setup3D();
        GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f * alphaMultiplier));
        event.invokeRenderModel();
        Constants.renderer.getRenderer().release3D();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        Constants.renderer.getRenderer().setupLine().lineWidth(2.0f);
        GlStateManager.color((float)((float)this.color.getRed() / 255.0f), (float)((float)this.color.getGreen() / 255.0f), (float)((float)this.color.getBlue() / 255.0f), (float)((float)this.color.getAlpha() / 255.0f * alphaMultiplier));
        event.invokeRenderModel();
        Constants.renderer.getRenderer().resetLineWidth().releaseLine();
        GlStateManager.popMatrix();
    }

    public static PopChams getInstance() {
        if (instance == null) {
            instance = new PopChams();
        }
        return instance;
    }
}

