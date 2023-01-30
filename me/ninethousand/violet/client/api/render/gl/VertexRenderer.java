//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.ninethousand.violet.client.api.render.gl;

import java.awt.Color;
import java.util.List;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.render.vector.Vector2d;
import me.ninethousand.violet.client.api.render.vector.VectorUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class VertexRenderer {
    private final Tessellator tessellator;
    private final BufferBuilder buffer;
    private final boolean useBuffer;

    public VertexRenderer(boolean useBuffer) {
        this.useBuffer = useBuffer;
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.getBuffer();
    }

    public VertexRenderer setup2D() {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.shadeModel((int)7425);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GlStateManager.disableCull();
        return this;
    }

    public VertexRenderer release2D() {
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel((int)7424);
        GL11.glDisable((int)2848);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        return this;
    }

    public VertexRenderer setup3D() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        return this;
    }

    public VertexRenderer release3D() {
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        return this;
    }

    public VertexRenderer setupLine() {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.glPolygonMode((int)1032, (int)6913);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        return this;
    }

    public VertexRenderer releaseLine() {
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.glPolygonMode((int)1032, (int)6914);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        return this;
    }

    public VertexRenderer prepare() {
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7425);
        return this;
    }

    public VertexRenderer restore() {
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel((int)7424);
        return this;
    }

    public VertexRenderer translate(Vec3d translation) {
        GlStateManager.translate((double)translation.xCoord, (double)translation.yCoord, (double)translation.zCoord);
        return this;
    }

    public VertexRenderer translate(Vector2d translation) {
        return this.translate(translation.to3D());
    }

    public VertexRenderer translate(Vec2f translation) {
        return this.translate(VectorUtil.to3D(translation));
    }

    public VertexRenderer translate(double x, double y, double z) {
        return this.translate(new Vec3d(x, y, z));
    }

    public VertexRenderer translate(double x, double y) {
        return this.translate(new Vec3d(x, y, 0.0));
    }

    public VertexRenderer polygon(int i, int j) {
        GL11.glPolygonMode((int)i, (int)j);
        return this;
    }

    public VertexRenderer blend(int i, int j) {
        GL11.glBlendFunc((int)i, (int)j);
        return this;
    }

    public VertexRenderer execute(Runnable r) {
        r.run();
        return this;
    }

    public VertexRenderer start(int mode) {
        if (this.useBuffer) {
            this.buffer.begin(mode, DefaultVertexFormats.POSITION_COLOR);
        } else {
            GL11.glBegin((int)mode);
        }
        return this;
    }

    public VertexRenderer lineWidth(float lineWidth) {
        GL11.glLineWidth((float)lineWidth);
        return this;
    }

    public VertexRenderer resetLineWidth() {
        GL11.glLineWidth((float)1.0f);
        return this;
    }

    public VertexRenderer addVertex(Vec3d pos, Color color) {
        if (this.useBuffer) {
            this.buffer.pos(pos.xCoord, pos.yCoord, pos.zCoord).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        } else {
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex3d((double)pos.xCoord, (double)pos.yCoord, (double)pos.zCoord);
        }
        return this;
    }

    public VertexRenderer addVertex(Vector2d position, Color color) {
        return this.addVertex(position.to3D(), color);
    }

    public VertexRenderer addVertex(Vec2f position, Color color) {
        return this.addVertex(VectorUtil.to3D(position), color);
    }

    public VertexRenderer addVertex(Vector2d position, double z, Color color) {
        return this.addVertex(position.to3D(z), color);
    }

    public VertexRenderer addVertex(Vec2f position, double z, Color color) {
        return this.addVertex(VectorUtil.to3D(position, z), color);
    }

    public VertexRenderer addVertex(double x, double y, double z, Color color) {
        return this.addVertex(new Vec3d(x, y, z), color);
    }

    public VertexRenderer addVertex(double x, double y, Color color) {
        return this.addVertex(new Vec3d(x, y, 0.0), color);
    }

    public VertexRenderer addVertices(List<Vec3d> vertices, Color color) {
        vertices.forEach(v -> this.addVertex((Vec3d)v, color));
        return this;
    }

    public VertexRenderer addFilledBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(minX, minY, maxZ, color);
        this.addVertex(minX, maxY, minZ, color);
        this.addVertex(minX, maxY, maxZ, color);
        this.addVertex(minX, maxY, maxZ, color);
        this.addVertex(minX, minY, maxZ, color);
        this.addVertex(maxX, maxY, maxZ, color);
        this.addVertex(maxX, minY, maxZ, color);
        this.addVertex(maxX, minY, maxZ, color);
        this.addVertex(maxX, minY, minZ, color);
        this.addVertex(maxX, maxY, maxZ, color);
        this.addVertex(maxX, maxY, minZ, color);
        this.addVertex(maxX, maxY, minZ, color);
        this.addVertex(maxX, minY, minZ, color);
        this.addVertex(minX, maxY, minZ, color);
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(maxX, minY, minZ, color);
        this.addVertex(minX, minY, maxZ, color);
        this.addVertex(maxX, minY, maxZ, color);
        this.addVertex(maxX, minY, maxZ, color);
        this.addVertex(minX, maxY, minZ, color);
        this.addVertex(minX, maxY, minZ, color);
        this.addVertex(minX, maxY, maxZ, color);
        this.addVertex(maxX, maxY, minZ, color);
        this.addVertex(maxX, maxY, maxZ, color);
        this.addVertex(maxX, maxY, maxZ, color);
        this.addVertex(maxX, maxY, maxZ, color);
        return this;
    }

    public VertexRenderer addBoundingBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        this.addVertex(minX, minY, minZ, ColorUtil.CLEAR);
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(maxX, minY, minZ, color);
        this.addVertex(maxX, minY, maxZ, color);
        this.addVertex(minX, minY, maxZ, color);
        this.addVertex(minX, minY, minZ, color);
        this.addVertex(minX, maxY, minZ, color);
        this.addVertex(maxX, maxY, minZ, color);
        this.addVertex(maxX, maxY, maxZ, color);
        this.addVertex(minX, maxY, maxZ, color);
        this.addVertex(minX, maxY, minZ, color);
        this.addVertex(minX, maxY, maxZ, ColorUtil.CLEAR);
        this.addVertex(minX, minY, maxZ, color);
        this.addVertex(maxX, maxY, maxZ, ColorUtil.CLEAR);
        this.addVertex(maxX, minY, maxZ, color);
        this.addVertex(maxX, maxY, minZ, ColorUtil.CLEAR);
        this.addVertex(maxX, minY, minZ, color);
        this.addVertex(maxX, minY, minZ, ColorUtil.CLEAR);
        return this;
    }

    public VertexRenderer finish() {
        if (this.useBuffer) {
            this.tessellator.draw();
        } else {
            GL11.glEnd();
        }
        return this;
    }
}

