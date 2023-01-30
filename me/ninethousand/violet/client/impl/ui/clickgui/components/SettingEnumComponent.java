/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;

public class SettingEnumComponent
extends ToggleableComponent {
    private SettingContainer<Enum<?>> setting;

    public SettingEnumComponent(int x, int y, int width, int height, SettingContainer<Enum<?>> setting) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        FontManager.get().drawString(this.setting.getName(), (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getName()) / 2.0f, this.getTheme().getText());
        FontManager.get().drawString(this.setting.getValue().name(), this.x + this.width - FontManager.get().getWidth(this.setting.getValue().name()) - 3, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getValue().name()) / 2.0f, this.getTheme().getText());
    }

    @Override
    public void onLeftClick() {
        this.increase();
    }

    @Override
    public void onRightClick() {
        this.decrease();
    }

    private <E extends Enum<E>> void increase() {
        Enum[] values = (Enum[])this.setting.getValue().getDeclaringClass().getEnumConstants();
        int ordinal = this.setting.getValue().ordinal();
        this.setting.setValue(values[ordinal + 1 >= values.length ? 0 : ordinal + 1]);
    }

    private <E extends Enum<E>> void decrease() {
        Enum[] values = (Enum[])this.setting.getValue().getDeclaringClass().getEnumConstants();
        int ordinal = this.setting.getValue().ordinal();
        this.setting.setValue(values[ordinal - 1 < 0 ? values.length - 1 : ordinal - 1]);
    }
}

