/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.GUITracker;
import org.lwjgl.input.Keyboard;

public class SettingStringComponent
extends ToggleableComponent {
    private SettingContainer<String> setting;

    public SettingStringComponent(int x, int y, int width, int height, SettingContainer<String> setting) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        if (this.setting.isTyping()) {
            int key = GUITracker.keyDown;
            if (key == 28) {
                this.setting.setTyping(false);
            } else {
                this.typeKey(key);
            }
        }
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        FontManager.get().drawString(this.setting.getName(), (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getName()) / 2.0f, this.getTheme().getText());
        FontManager.get().drawString(this.setting.getValue(), this.x + this.width - FontManager.get().getWidth(this.setting.getValue()) - 3, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getValue()) / 2.0f, this.setting.isTyping() ? this.getTheme().getAccent() : this.getTheme().getText());
    }

    @Override
    public void onLeftClick() {
        this.setting.setTyping(!this.setting.isTyping());
    }

    private void typeKey(int key) {
        if (this.setting.isTyping() && key != 0) {
            if (key == 28 || key == 1) {
                this.setting.setTyping(false);
                return;
            }
            if (key == 14) {
                if (!this.setting.getValue().isEmpty()) {
                    this.setting.setValue(this.setting.getValue().substring(0, this.setting.getValue().length() - 1));
                }
                return;
            }
            boolean shift = Keyboard.isKeyDown((int)42);
            if (key == 57) {
                this.setting.setValue(this.setting.getValue() + " ");
            } else if (key == 52) {
                this.setting.setValue(this.setting.getValue() + (shift ? ">" : "."));
            } else if (key == 51) {
                this.setting.setValue(this.setting.getValue() + (shift ? "<" : ","));
            } else if (key == 39) {
                this.setting.setValue(this.setting.getValue() + (shift ? ":" : ";"));
            } else if (key == 41) {
                this.setting.setValue(this.setting.getValue() + (shift ? "~" : "`"));
            } else if (key == 40) {
                this.setting.setValue(this.setting.getValue() + (shift ? "\"" : "'"));
            } else if (key == 26) {
                this.setting.setValue(this.setting.getValue() + (shift ? "{" : "["));
            } else if (key == 27) {
                this.setting.setValue(this.setting.getValue() + (shift ? "}" : "]"));
            } else if (key == 12) {
                this.setting.setValue(this.setting.getValue() + (shift ? "_" : "-"));
            } else if (key == 13) {
                this.setting.setValue(this.setting.getValue() + (shift ? "+" : "="));
            } else if (key == 43) {
                this.setting.setValue(this.setting.getValue() + (shift ? "|" : "\\"));
            } else if (key == 53) {
                this.setting.setValue(this.setting.getValue() + (shift ? "?" : "/"));
            } else if (key == 2 && shift) {
                this.setting.setValue(this.setting.getValue() + "!");
            } else if (key == 3 && shift) {
                this.setting.setValue(this.setting.getValue() + "@");
            } else if (key == 4 && shift) {
                this.setting.setValue(this.setting.getValue() + "#");
            } else if (key == 5 && shift) {
                this.setting.setValue(this.setting.getValue() + "$");
            } else if (key == 6 && shift) {
                this.setting.setValue(this.setting.getValue() + "%");
            } else if (key == 7 && shift) {
                this.setting.setValue(this.setting.getValue() + "^");
            } else if (key == 8 && shift) {
                this.setting.setValue(this.setting.getValue() + "&");
            } else if (key == 9 && shift) {
                this.setting.setValue(this.setting.getValue() + "*");
            } else if (key == 10 && shift) {
                this.setting.setValue(this.setting.getValue() + "(");
            } else if (key == 11 && shift) {
                this.setting.setValue(this.setting.getValue() + ")");
            } else if (key != 58 && key != 15 && key != 211 && key != 29 && key != 42 && key != 56 && key != 219 && key != 157 && key != 54 && key != 184 && key != 220) {
                this.setting.setValue(this.setting.getValue() + (shift ? Keyboard.getKeyName((int)key).toUpperCase() : Keyboard.getKeyName((int)key).toLowerCase()).charAt(0));
            }
        }
    }
}

