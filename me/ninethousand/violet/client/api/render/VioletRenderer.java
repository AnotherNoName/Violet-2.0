//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.ninethousand.violet.client.api.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.render.RenderWrapper;
import me.ninethousand.violet.client.api.render.gl.VertexRenderer;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.render.misc.Pair;
import me.ninethousand.violet.client.api.render.vector.Vector2d;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class VioletRenderer {
    private final VertexRenderer renderer;

    public VioletRenderer() {
        this.renderer = RenderWrapper.bufferedRenderer;
    }

    public VioletRenderer(VertexRenderer renderer) {
        this.renderer = renderer;
    }

    public VertexRenderer getRenderer() {
        return this.renderer;
    }

    public VioletRenderer fill(double x, double y, double width, double height, Color color) {
        this.renderer.setup2D().translate(x, y).start(8).addVertex(0.0, 0.0, color).addVertex(width, 0.0, color).addVertex(0.0, height, color).addVertex(width, height, color).finish().release2D();
        return this;
    }

    public VioletRenderer fill(Vector2d top, Vector2d bottom, Color color) {
        return this.fill(top.x, top.y, bottom.x - top.x, bottom.y - top.y, color);
    }

    public VioletRenderer fill(Vec2f top, Vec2f bottom, Color color) {
        return this.fill(top.x, top.y, bottom.x - top.x, bottom.y - top.y, color);
    }

    public VioletRenderer outline(double x, double y, double width, double height, Color color, float lineWidth) {
        this.line(x, y, x + width, y, color, lineWidth);
        this.line(x, y, x, y + height, color, lineWidth);
        this.line(x + width, y, x + width, y + height, color, lineWidth);
        this.line(x, y + height, x + width, y + height, color, lineWidth);
        return this;
    }

    public VioletRenderer outline(Vector2d top, Vector2d bottom, Color color, float lineWidth) {
        return this.outline(top.x, top.y, bottom.x - top.x, bottom.y - top.y, color, lineWidth);
    }

    public VioletRenderer outline(Vec2f top, Vec2f bottom, Color color, float lineWidth) {
        return this.outline(top.x, top.y, bottom.x - top.x, bottom.y - top.y, color, lineWidth);
    }

    public VioletRenderer grad(double x, double y, double width, double height, Color start, Color end, boolean horizontal) {
        this.renderer.setup2D().translate(x, y).start(8).addVertex(0.0, 0.0, start).addVertex(width, 0.0, horizontal ? end : start).addVertex(0.0, height, horizontal ? start : end).addVertex(width, height, end).finish().release2D();
        return this;
    }

    public VioletRenderer grad(Vec2f top, Vec2f bottom, Color start, Color end, boolean horizontal) {
        return this.grad(top.x, top.y, bottom.x - top.x, bottom.y - top.y, start, end, horizontal);
    }

    public VioletRenderer gradOutline(double x, double y, double width, double height, Color start, Color end, boolean horizontal, float lineWidth) {
        this.renderer.setup2D().translate(x, y).lineWidth(lineWidth).polygon(1032, 6913).start(8).addVertex(0.0, 0.0, start).addVertex(width, 0.0, horizontal ? end : start).addVertex(0.0, height, horizontal ? start : end).addVertex(width, height, end).finish().polygon(1032, 6914).resetLineWidth().release2D();
        return this;
    }

    public VioletRenderer rounded(double x, double y, double width, double height, Color color, int segments, double radius) {
        Vector2d top = new Vector2d(x, y);
        Vector2d vertex1 = top.add(width, 0.0);
        Vector2d vertex2 = top.add(0.0, height);
        Vector2d bottom = top.add(width, height);
        this.filledArc(top.add(radius), radius, new Pair<Float, Float>(Float.valueOf(-90.0f), Float.valueOf(0.0f)), segments, color);
        this.filledArc(vertex1.add(-radius, radius), radius, new Pair<Float, Float>(Float.valueOf(0.0f), Float.valueOf(90.0f)), segments, color);
        this.filledArc(bottom.subtract(radius), radius, new Pair<Float, Float>(Float.valueOf(90.0f), Float.valueOf(180.0f)), segments, color);
        this.filledArc(vertex2.add(radius, -radius), radius, new Pair<Float, Float>(Float.valueOf(180.0f), Float.valueOf(270.0f)), segments, color);
        this.fill(top.add(radius, 0.0), vertex1.add(-radius, radius), color);
        this.fill(top.add(0.0, radius), bottom.subtract(0.0, radius), color);
        this.fill(vertex2.add(radius, -radius), bottom.subtract(radius, 0.0), color);
        return this;
    }

    public VioletRenderer rounded(Vector2d top, Vector2d bottom, Color color, int segments, double radius) {
        return this.rounded(top.x, top.y, bottom.x - top.x, bottom.y - top.y, color, segments, radius);
    }

    public VioletRenderer rounded(Vec2f top, Vec2f bottom, Color color, int segments, double radius) {
        return this.rounded(top.x, top.y, bottom.x - top.x, bottom.y - top.y, color, segments, radius);
    }

    public VioletRenderer roundedOutline(double x, double y, double width, double height, Color color, int segments, double radius, float linewidth) {
        Vector2d top = new Vector2d(x, y);
        Vector2d vertex1 = top.add(width, 0.0);
        Vector2d vertex2 = top.add(0.0, height);
        Vector2d bottom = top.add(width, height);
        this.outlineArc(top.add(radius), radius, new Pair<Float, Float>(Float.valueOf(-90.0f), Float.valueOf(0.0f)), segments, color, linewidth);
        this.outlineArc(vertex1.add(-radius, radius), radius, new Pair<Float, Float>(Float.valueOf(0.0f), Float.valueOf(90.0f)), segments, color, linewidth);
        this.outlineArc(bottom.subtract(radius), radius, new Pair<Float, Float>(Float.valueOf(90.0f), Float.valueOf(180.0f)), segments, color, linewidth);
        this.outlineArc(vertex2.add(radius, -radius), radius, new Pair<Float, Float>(Float.valueOf(180.0f), Float.valueOf(270.0f)), segments, color, linewidth);
        this.line(top.add(radius, 0.0), vertex1.subtract(radius, 0.0), color, linewidth);
        this.line(top.add(0.0, radius), vertex2.subtract(0.0, radius), color, linewidth);
        this.line(vertex1.add(0.0, radius), bottom.subtract(0.0, radius), color, linewidth);
        this.line(vertex2.add(radius, 0.0), bottom.subtract(radius, 0.0), color, linewidth);
        return this;
    }

    public VioletRenderer hueWheel(double x, double y, double radius) {
        this.rainbowArc(new Vector2d(x, y), radius, new Pair<Float, Float>(Float.valueOf(-180.0f), Float.valueOf(180.0f)), 100, ColorUtil.getRainbow(-1.0f, 0.0f, 0.0f));
        return this;
    }

    public VioletRenderer line(double startX, double startY, double endX, double endY, Color color, float lineWidth) {
        this.renderer.setup2D().lineWidth(lineWidth).start(1).addVertex(startX, startY, color).addVertex(endX, endY, color).finish().resetLineWidth().release2D();
        return this;
    }

    public VioletRenderer line(Vector2d start, Vector2d end, Color color, float lineWidth) {
        return this.line(start.x, start.y, end.x, end.y, color, lineWidth);
    }

    public VioletRenderer filledArc(Vector2d centre, double radius, Pair<Float, Float> angleRange, int segments, Color color) {
        return this.triangleFan(centre.x, centre.y, this.getArcVertices(centre.x, centre.y, radius, angleRange, segments), color);
    }

    public VioletRenderer rainbowArc(Vector2d centre, double radius, Pair<Float, Float> angleRange, int segments) {
        return this.rainbowFan(centre.x, centre.y, this.getArcVertices(centre.x, centre.y, radius, angleRange, segments));
    }

    public VioletRenderer rainbowArc(Vector2d centre, double radius, Pair<Float, Float> angleRange, int segments, Color color) {
        return this.rainbowFan(centre.x, centre.y, this.getArcVertices(centre.x, centre.y, radius, angleRange, segments), color);
    }

    public VioletRenderer outlineArc(Vector2d centre, double radius, Pair<Float, Float> angleRange, int segments, Color color, float linewidth) {
        return this.lineStrip(centre.x, centre.y, this.getArcVertices(centre.x, centre.y, radius, angleRange, segments), color, linewidth);
    }

    public VioletRenderer triangleFan(double x, double y, List<Vector2d> vertices, Color color) {
        this.renderer.setup2D().start(6).addVertex(x, y, color).addVertices(vertices.stream().map(Vector2d::to3D).collect(Collectors.toList()), color).finish().release2D();
        return this;
    }

    public VioletRenderer rainbowFan(double x, double y, List<Vector2d> vertices) {
        this.renderer.setup2D().start(6).addVertex(x, y, Color.WHITE).execute(() -> {
            for (int i = 0; i < vertices.size(); ++i) {
                this.renderer.addVertex((Vector2d)vertices.get(i), ColorUtil.createColor((float)i / (float)vertices.size(), 1.0f, 1.0f));
            }
        }).finish().release2D();
        return this;
    }

    public VioletRenderer rainbowFan(double x, double y, List<Vector2d> vertices, Color color) {
        this.renderer.setup2D().start(6).addVertex(x, y, Color.WHITE).execute(() -> {
            for (int i = 0; i < vertices.size(); ++i) {
                float newHue = (float)i / (float)vertices.size() + ColorUtil.getHue(color);
                this.renderer.addVertex((Vector2d)vertices.get(i), ColorUtil.createColor(newHue, 1.0f, 1.0f));
            }
        }).finish().release2D();
        return this;
    }

    public VioletRenderer lineStrip(double x, double y, List<Vector2d> vertices, Color color, float lineWidth) {
        this.renderer.setup2D().lineWidth(lineWidth).start(3).addVertices(vertices.stream().map(Vector2d::to3D).collect(Collectors.toList()), color).finish().resetLineWidth().release2D();
        return this;
    }

    public VioletRenderer image(double posX, double posY, double width, double height, Color color) {
        GL11.glPushAttrib((int)1);
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)0.0);
        GlStateManager.enableBlend();
        GlStateManager.shadeModel((int)7425);
        GlStateManager.enableAlpha();
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glBegin((int)7);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3d((double)0.0, (double)height, (double)0.0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3d((double)width, (double)height, (double)0.0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3d((double)width, (double)0.0, (double)0.0);
        GL11.glEnd();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7424);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        return this;
    }

    public VioletRenderer image(Vec2f topLeft, Vec2f bottomRight, Color color) {
        double posX = topLeft.x;
        double posY = topLeft.y;
        double width = bottomRight.x - topLeft.x;
        double height = bottomRight.y - topLeft.y;
        GL11.glPushAttrib((int)1);
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)0.0);
        GlStateManager.enableBlend();
        GlStateManager.shadeModel((int)7425);
        GlStateManager.enableAlpha();
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glBegin((int)7);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3d((double)0.0, (double)height, (double)0.0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3d((double)width, (double)height, (double)0.0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3d((double)width, (double)0.0, (double)0.0);
        GL11.glEnd();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7424);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        return this;
    }

    public VioletRenderer downArrow(double x, double y, double width, double height, Color color, float lineWidth) {
        return this.line(x, y, x + width / 2.0, y + height, color, lineWidth).line(x + width / 2.0, y + height, x + width, y, color, lineWidth);
    }

    public VioletRenderer renderBlockPos(BlockPos blockPos, Color color, BoxMode mode) {
        AxisAlignedBB bb = new AxisAlignedBB((double)blockPos.getX() - Constants.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - Constants.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - Constants.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - Constants.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - Constants.mc.getRenderManager().viewerPosY, (double)(blockPos.getZ() + 1) - Constants.mc.getRenderManager().viewerPosZ);
        switch (mode) {
            case Filled: {
                return this.filledAABB(bb, 0.0, 0.0, 0.0, color);
            }
            case Bounded: {
                return this.boundedAABB(bb, 0.0, 0.0, 0.0, color);
            }
            case FilledBounded: {
                this.filledAABB(bb, 0.0, 0.0, 0.0, color);
                return this.boundedAABB(bb, 0.0, 0.0, 0.0, color);
            }
        }
        return this;
    }

    public VioletRenderer filledAABB(AxisAlignedBB bb, double height, double length, double width, Color color) {
        this.renderer.setup3D().start(5).addFilledBoxVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color).finish().release3D();
        return this;
    }

    public VioletRenderer boundedAABB(AxisAlignedBB bb, double height, double length, double width, Color color) {
        this.renderer.setup3D().lineWidth(2.0f).start(3).addBoundingBoxVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color).finish().resetLineWidth().release3D();
        return this;
    }

    public VioletRenderer flatPipe(AxisAlignedBB bb, int segments, Color color) {
        ArrayList<Vec3d> vertices = this.get3DArcVertices(bb.getCenter().addVector(0.0, (bb.maxX - bb.minX) / 2.0, 0.0), (bb.maxX - bb.minX) / 2.0, new Pair<Float, Float>(Float.valueOf(-180.0f), Float.valueOf(180.0f)), segments);
        this.renderer.setup2D().start(6).addVertex(bb.getCenter().addVector(0.0, (bb.maxX - bb.minX) / 2.0, 0.0), color).addVertices(vertices, color).finish().release2D();
        return this;
    }

    public VioletRenderer depthBoundedAABB(AxisAlignedBB bb, double height, double length, double width, Color color) {
        this.renderer.setup3D().execute(GlStateManager::enableDepth).lineWidth(2.0f).start(3).addBoundingBoxVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color).finish().resetLineWidth().release3D();
        return this;
    }

    public VioletRenderer drawEntityOnScreen(int posX, int posY, int scale, float mouseY, Entity entityLivingBase) {
        RenderManager renderManager = Constants.mc.getRenderManager();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, (float)50.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-((float)Math.atan(mouseY / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.0f);
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.doRenderEntity(entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        return this;
    }

    public AxisAlignedBB interpolate(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - Constants.mc.getRenderManager().viewerPosX, bb.minY - Constants.mc.getRenderManager().viewerPosY, bb.minZ - Constants.mc.getRenderManager().viewerPosZ, bb.maxX - Constants.mc.getRenderManager().viewerPosX, bb.maxY - Constants.mc.getRenderManager().viewerPosY, bb.maxZ - Constants.mc.getRenderManager().viewerPosZ);
    }

    public Vec3d interpolate(Vec3d vec3d) {
        return vec3d.subtract(Constants.mc.getRenderManager().viewerPosX, Constants.mc.getRenderManager().viewerPosY, Constants.mc.getRenderManager().viewerPosZ);
    }

    public ArrayList<Vector2d> getArcVertices(double x, double y, double radius, Pair<Float, Float> angleRange, int segments) {
        double range = Math.max(((Float)angleRange.a).floatValue(), ((Float)angleRange.b).floatValue()) - Math.min(((Float)angleRange.a).floatValue(), ((Float)angleRange.b).floatValue());
        double seg = this.calculateSegments(segments, radius, range);
        double segAngle = range / seg;
        ArrayList<Vector2d> vectors = new ArrayList<Vector2d>();
        int i = 0;
        while ((double)i < seg + 1.0) {
            double angle = Math.toRadians((double)i * segAngle + (double)((Float)angleRange.a).floatValue());
            vectors.add(new Vector2d(Math.sin(angle) * radius + x, -Math.cos(angle) * radius + y));
            ++i;
        }
        return vectors;
    }

    public double calculateSegments(int segmentsIn, double radius, double range) {
        if (segmentsIn != 0) {
            return segmentsIn;
        }
        double segments = radius * 0.5 * Math.PI * (range / 360.0);
        return Math.max(segments, 16.0);
    }

    public ArrayList<Vec3d> get3DArcVertices(Vec3d pos, double radius, Pair<Float, Float> angleRange, int segments) {
        double range = Math.max(((Float)angleRange.a).floatValue(), ((Float)angleRange.b).floatValue()) - Math.min(((Float)angleRange.a).floatValue(), ((Float)angleRange.b).floatValue());
        double seg = this.calculateSegments(segments, radius, range);
        double segAngle = range / seg;
        ArrayList<Vec3d> vectors = new ArrayList<Vec3d>();
        int i = 0;
        while ((double)i < seg + 1.0) {
            double angle = Math.toRadians((double)i * segAngle + (double)((Float)angleRange.a).floatValue());
            vectors.add(new Vec3d(Math.sin(angle) * radius + pos.xCoord, pos.yCoord, -Math.cos(angle) * radius + pos.zCoord));
            ++i;
        }
        return vectors;
    }

    public static enum BoxMode {
        Filled,
        Bounded,
        FilledBounded;

    }
}

