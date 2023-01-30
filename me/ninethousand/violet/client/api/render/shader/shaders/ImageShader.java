//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL20
 */
package me.ninethousand.violet.client.api.render.shader.shaders;

import java.awt.Color;
import me.ninethousand.violet.client.api.render.shader.FrameBufferShader;
import org.lwjgl.opengl.GL20;

public class ImageShader
extends FrameBufferShader {
    private static ImageShader instance;

    public static ImageShader getInstance() {
        if (instance == null) {
            instance = new ImageShader("test");
        }
        return instance;
    }

    public ImageShader(String name) {
        super(name);
    }

    public void stopDraw() {
        super.stopDraw(Color.WHITE, Color.WHITE, 1.0f, 1.0f, 1, 1, false);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("radius");
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("sampler");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1i((int)this.getUniform("texture"), (int)0);
        GL20.glUniform2f((int)this.getUniform("texelSize"), (float)(1.0f / (float)this.mc.displayWidth * (this.radius * this.quality)), (float)(1.0f / (float)this.mc.displayHeight * (this.radius * this.quality)));
        GL20.glUniform1f((int)this.getUniform("radius"), (float)this.radius);
        GL20.glUniform2f((int)this.getUniform("resolution"), (float)this.mc.displayWidth, (float)this.mc.displayHeight);
        GL20.glUniform1f((int)this.getUniform("time"), (float)((float)(System.currentTimeMillis() - this.startTime) / 1000.0f));
        GL20.glUniform1i((int)this.getUniform("sampler"), (int)1);
    }
}

