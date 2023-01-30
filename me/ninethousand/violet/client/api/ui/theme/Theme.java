/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package me.ninethousand.violet.client.api.ui.theme;

import java.awt.Color;
import me.ninethousand.violet.client.api.render.image.GifImage;
import net.minecraft.util.ResourceLocation;

public class Theme {
    private Color accent = Color.WHITE;
    private Color background = Color.WHITE;
    private Color outline = Color.WHITE;
    private Color enabled = Color.WHITE;
    private Color disabled = Color.WHITE;
    private Color icon = Color.WHITE;
    private Color text = Color.WHITE;
    private ResourceLocation backgroundImage = null;
    private GifImage backgroundGif;
    public static Theme LIGHT_BLUE_THEME = new Theme().setAccent(new Color(4236732)).setEnabled(new Color(4107831)).setDisabled(new Color(11417399)).setBackground(new Color(0xFFFFFF)).setIcon(new Color(0x3C3C3C)).setText(new Color(0x3C3C3C)).setOutline(new Color(0xFFFFFF));
    public static Theme LIGHT_VIOLET_THEME = new Theme().setAccent(new Color(7880892)).setEnabled(new Color(4107831)).setDisabled(new Color(11417399)).setBackground(new Color(0xFFFFFF)).setIcon(new Color(0x3C3C3C)).setText(new Color(0x3C3C3C)).setOutline(new Color(0xFFFFFF));
    public static Theme DARK_VIOLET_THEME = new Theme().setAccent(new Color(7880892)).setEnabled(new Color(3479370)).setDisabled(new Color(11417399)).setBackground(new Color(0x414141)).setIcon(new Color(0xFFFFFF)).setText(new Color(0xFFFFFF)).setOutline(new Color(7880892));

    public Color getAccent() {
        return this.accent;
    }

    public Theme setAccent(Color accent) {
        this.accent = accent;
        return this;
    }

    public Color getBackground() {
        return this.background;
    }

    public Theme setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Color getOutline() {
        return this.outline;
    }

    public Theme setOutline(Color outline) {
        this.outline = outline;
        return this;
    }

    public Color getEnabled() {
        return this.enabled;
    }

    public Theme setEnabled(Color enabled) {
        this.enabled = enabled;
        return this;
    }

    public Color getDisabled() {
        return this.disabled;
    }

    public Theme setDisabled(Color disabled) {
        this.disabled = disabled;
        return this;
    }

    public Color getIcon() {
        return this.icon;
    }

    public Theme setIcon(Color icon) {
        this.icon = icon;
        return this;
    }

    public Color getText() {
        return this.text;
    }

    public Theme setText(Color text) {
        this.text = text;
        return this;
    }

    public GifImage getBackgroundGif() {
        return this.backgroundGif;
    }

    public Theme setBackgroundGif(GifImage backgroundGif) {
        this.backgroundGif = backgroundGif;
        return this;
    }

    public ResourceLocation getBackgroundImage() {
        return this.backgroundImage;
    }

    public Theme setBackgroundImage(ResourceLocation backgroundImage) {
        this.backgroundImage = backgroundImage;
        return this;
    }
}

