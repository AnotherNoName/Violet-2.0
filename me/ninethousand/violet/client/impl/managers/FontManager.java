//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.managers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.api.render.font.CFontRenderer;
import me.ninethousand.violet.client.impl.modules.client.CustomFont;
import me.ninethousand.violet.client.util.Constants;

public class FontManager
implements Manager {
    private final CFontRenderer renderer;
    private String name = "Rubik";
    private float size = 18.0f;
    private boolean customFont = CustomFont.getInstance().isEnabled();
    private boolean shadow = false;
    private static final String DEFAULT_FONT = "Rubik";
    private static final float DEFAULT_SIZE = 18.0f;
    private static FontManager instance;

    private FontManager() {
        this.renderer = new CFontRenderer(this.getFontByName(this.name).deriveFont(this.size), true, true);
    }

    public void updateFont(String name, float size) {
        if (this.name.equals(name) && this.size == size) {
            return;
        }
        this.name = name;
        this.size = size;
        Font font = this.getFontByName(name).deriveFont(size);
        if (font != null) {
            this.renderer.setFont(font);
        }
    }

    public void updateFont(Font font) {
        if (font != null) {
            this.renderer.setFont(font);
        }
    }

    public float drawString(String text, float x, float y, Color color) {
        text = this.format(text);
        if (this.customFont) {
            if (this.shadow) {
                return this.renderer.drawStringWithShadow(text, x, y, color.getRGB());
            }
            return this.renderer.drawString(text, x, y, color.getRGB());
        }
        if (this.shadow) {
            return Constants.mc.fontRendererObj.drawStringWithShadow(text, x, y, color.getRGB());
        }
        return Constants.mc.fontRendererObj.drawString(text, (int)x, (int)y, color.getRGB());
    }

    public float drawCenteredString(String text, float x, float y, Color color) {
        text = this.format(text);
        if (this.customFont) {
            if (this.shadow) {
                return this.renderer.drawCenteredStringWithShadow(text, x, y, color.getRGB());
            }
            return this.renderer.drawCenteredString(text, x, y, color.getRGB());
        }
        if (this.shadow) {
            return Constants.mc.fontRendererObj.drawStringWithShadow(text, (float)((int)x) - (float)Constants.mc.fontRendererObj.getStringWidth(text) / 2.0f, (float)((int)y), color.getRGB());
        }
        return Constants.mc.fontRendererObj.drawString(text, (int)(x - (float)Constants.mc.fontRendererObj.getStringWidth(text) / 2.0f), (int)y, color.getRGB());
    }

    public int getWidth(String s) {
        s = this.format(s);
        if (this.customFont) {
            return this.renderer.getStringWidth(s);
        }
        return Constants.mc.fontRendererObj.getStringWidth(s);
    }

    public int getHeight(String s) {
        s = this.format(s);
        if (this.customFont) {
            return this.renderer.getStringHeight(s);
        }
        return Constants.mc.fontRendererObj.FONT_HEIGHT;
    }

    public int getHeight() {
        if (this.customFont) {
            return this.renderer.getHeight();
        }
        return Constants.mc.fontRendererObj.FONT_HEIGHT;
    }

    public Font getFont() {
        return this.renderer.getFont();
    }

    private Font getFontByName(String name) {
        try {
            return this.getFontFromInput("/assets/minecraft/violet/fonts/" + name + ".ttf");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(0, this.getClass().getResourceAsStream(path));
    }

    private String format(String text) {
        if (CustomFont.getInstance().isEnabled()) {
            String temp = text;
            text = CustomFont.getInstance().getStyleFormat() + temp;
        }
        return text;
    }

    public boolean isCustomFont() {
        return this.customFont;
    }

    public void setCustomFont(boolean customFont) {
        this.customFont = customFont;
    }

    public boolean isShadow() {
        return this.shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public static FontManager get() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }
}

