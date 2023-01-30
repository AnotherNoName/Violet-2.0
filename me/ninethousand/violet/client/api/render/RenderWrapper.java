/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.ninethousand.violet.client.api.render;

import me.ninethousand.violet.client.api.render.gl.VertexRenderer;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.gui.ScaledResolution;

public class RenderWrapper {
    public static VertexRenderer unbufferedRenderer = new VertexRenderer(false);
    public static VertexRenderer bufferedRenderer = new VertexRenderer(true);

    public static ScaledResolution scaledResolution() {
        return new ScaledResolution(Constants.mc);
    }

    private RenderWrapper() {
        throw new UnsupportedOperationException();
    }
}

