/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.managers;

import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.api.ui.theme.Theme;

public class ThemeManager
implements Manager {
    private Theme clickGuiTheme = Theme.DARK_VIOLET_THEME;
    private static ThemeManager instance;

    private ThemeManager() {
    }

    public Theme getClickGuiTheme() {
        return this.clickGuiTheme;
    }

    public void setClickGuiTheme(Theme clickGuiTheme) {
        this.clickGuiTheme = clickGuiTheme;
    }

    public static ThemeManager get() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }
}

