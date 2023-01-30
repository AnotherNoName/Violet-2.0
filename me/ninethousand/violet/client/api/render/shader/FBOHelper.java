//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.shader.Framebuffer
 */
package me.ninethousand.violet.client.api.render.shader;

import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class FBOHelper {
    private Framebuffer fbo;
    private ScaledResolution currentSR;
    private ScaledResolution previousSR;

    public void bind(ScaledResolution scaledResolution) {
        this.currentSR = scaledResolution;
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        this.setup();
        this.fbo.bindFramebuffer(true);
    }

    public void unbind() {
        Constants.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        this.previousSR = this.currentSR;
    }

    public int getFramebufferID() {
        return this.fbo.framebufferTexture;
    }

    private void setup() {
        if (this.fbo == null || this.scaleChanged()) {
            this.fbo = new Framebuffer(Constants.mc.displayWidth, Constants.mc.displayHeight, true);
        }
        this.fbo.framebufferClear();
    }

    private boolean scaleChanged() {
        return this.previousSR == null || this.previousSR.getScaleFactor() != this.currentSR.getScaleFactor() || this.previousSR.getScaledWidth() != this.currentSR.getScaledWidth() || this.previousSR.getScaledHeight() != this.currentSR.getScaledHeight();
    }
}

