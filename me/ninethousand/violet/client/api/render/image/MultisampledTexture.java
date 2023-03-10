//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL32
 */
package me.ninethousand.violet.client.api.render.image;

import me.ninethousand.violet.client.api.render.image.Texture;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

public class MultisampledTexture
extends Texture {
    private int samples;

    public MultisampledTexture(int samples, int width, int height) {
        super(width, height);
        this.samples = samples;
        this.init();
    }

    @Override
    public void init() {
        this.id = TextureUtil.glGenTextures();
        this.bind();
        GL32.glTexImage2DMultisample((int)37120, (int)this.samples, (int)6408, (int)this.width, (int)this.height, (boolean)true);
        this.unbind();
    }

    @Override
    public void bind() {
        GL11.glBindTexture((int)37120, (int)this.id);
    }

    @Override
    public void unbind() {
        GL11.glBindTexture((int)37120, (int)0);
    }
}

