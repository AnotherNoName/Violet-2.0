/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;

public class SettingBooleanComponent
extends ToggleableComponent {
    private SettingContainer<Boolean> setting;

    public SettingBooleanComponent(int x, int y, int width, int height, SettingContainer<Boolean> setting) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        FontManager.get().drawString(this.setting.getName(), (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getName()) / 2.0f, this.setting.getValue() != false ? this.getTheme().getAccent() : this.getTheme().getText());
    }

    @Override
    public void onLeftClick() {
        this.setting.setValue(this.setting.getValue() == false);
    }
}

