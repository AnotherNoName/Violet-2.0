/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.misc;

import java.awt.Color;
import me.ninethousand.violet.client.api.render.misc.RenderMathUtil;

public class ColorUtil {
    public static final Color CLEAR = new Color(0, true);

    public static Color getColorWithAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), RenderMathUtil.clamp(alpha, 0, 255));
    }

    public static Color getRainbow(float hue, float saturation, float brightness) {
        if (hue < 0.0f) {
            hue = ColorUtil.getCycle();
        }
        if (saturation < 0.0f) {
            ColorUtil.getCycle();
        }
        if (brightness < 0.0f) {
            ColorUtil.getCycle();
        }
        System.out.println(ColorUtil.getCycle());
        return new Color(Color.HSBtoRGB(hue, saturation, brightness));
    }

    public static float getCycle() {
        return (float)(System.currentTimeMillis() % 7500L) / 7500.0f;
    }

    public static Color createColor(float h, float s, float b) {
        return new Color(Color.HSBtoRGB(h, s, b));
    }

    public static float getHue(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
    }

    public static float getSat(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[1];
    }

    public static float getBright(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[2];
    }

    private ColorUtil() {
        throw new UnsupportedOperationException();
    }
}

