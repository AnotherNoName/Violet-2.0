//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL11
 */
package me.ninethousand.violet.client.api.render.gl;

import java.awt.Rectangle;
import java.util.LinkedList;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ScissorStack {
    private final LinkedList<Rectangle> scissorStack = new LinkedList();

    public void pushScissor(int x, int y, int width, int height) {
        Rectangle scissor;
        ScaledResolution resolution = new ScaledResolution(Constants.mc);
        int sx = x * resolution.getScaleFactor();
        int sy = (resolution.getScaledHeight() - (y + height)) * resolution.getScaleFactor();
        int sWidth = (x + width - x) * resolution.getScaleFactor();
        int sHeight = (y + height - y) * resolution.getScaleFactor();
        if (!this.scissorStack.isEmpty()) {
            GL11.glDisable((int)3089);
            Rectangle last = this.scissorStack.getLast();
            int nx = Math.max(sx, last.x);
            int ny = Math.max(sy, last.y);
            int hDiff = sx - nx;
            int nWidth = Math.min(Math.min(last.width + (last.x - sx), last.width), sWidth + hDiff);
            int diff = sy - ny;
            int nHeight = Math.min(Math.min(last.height + (last.y - sy), last.height), hDiff + diff);
            scissor = new Rectangle(nx, ny, nWidth, nHeight);
        } else {
            scissor = new Rectangle(sx, sy, sWidth, sHeight);
        }
        GL11.glEnable((int)3089);
        if (scissor.width > 0 && scissor.height > 0) {
            GL11.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.width, (int)scissor.height);
        } else {
            GL11.glScissor((int)0, (int)0, (int)0, (int)0);
        }
        this.scissorStack.add(scissor);
    }

    public void popScissor() {
        if (!this.scissorStack.isEmpty()) {
            GL11.glDisable((int)3089);
            this.scissorStack.removeLast();
            if (!this.scissorStack.isEmpty()) {
                Rectangle scissor = this.scissorStack.getLast();
                GL11.glEnable((int)3089);
                GL11.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.width, (int)scissor.height);
            }
        }
    }
}

