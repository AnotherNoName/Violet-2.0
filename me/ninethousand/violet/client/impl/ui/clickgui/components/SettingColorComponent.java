/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import java.awt.Color;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.api.ui.component.VerticalComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SliderComponent;
import me.ninethousand.violet.client.util.Constants;

public class SettingColorComponent
extends ToggleableComponent {
    private SettingContainer<Color> setting;
    private VerticalComponent sliders;
    private SliderComponent hueSlider;
    private SliderComponent satSlider;
    private SliderComponent briSlider;
    private SliderComponent alphaSlider;

    public SettingColorComponent(int x, int y, int width, int height, SettingContainer<Color> setting) {
        super(x, y, width, height);
        this.setting = setting;
        this.sliders = new VerticalComponent(x, y + height, width, height, width, 0);
        int drawY = y + height;
        int moduleHeight = height - 2;
        this.hueSlider = new SliderComponent(x, drawY, width, moduleHeight, "Hue", 0.0, 120.0, 100.0, 1);
        this.satSlider = new SliderComponent(x, drawY, width, moduleHeight, "Saturation", 0.0, 120.0, 100.0, 1);
        this.briSlider = new SliderComponent(x, drawY, width, moduleHeight, "Brightness", 0.0, 120.0, 100.0, 1);
        this.alphaSlider = new SliderComponent(x, drawY, width, moduleHeight, "Alpha", 0.0, 120.0, 255.0, 1);
        this.sliders.add(this.hueSlider);
        this.sliders.add(this.satSlider);
        this.sliders.add(this.briSlider);
        this.sliders.add(this.alphaSlider);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        FontManager.get().drawString(this.setting.getName(), (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getName()) / 2.0f, this.getTheme().getText());
        if (!this.setting.isOpen()) {
            return;
        }
        this.hueSlider.setValue(ColorUtil.getHue(this.setting.getValue()) * 100.0f);
        this.satSlider.setValue(ColorUtil.getSat(this.setting.getValue()) * 100.0f);
        this.briSlider.setValue(ColorUtil.getBright(this.setting.getValue()) * 100.0f);
        this.alphaSlider.setValue(this.setting.getValue().getAlpha());
        this.sliders.setX(this.x);
        this.sliders.setY(this.y + this.height);
        this.sliders.draw(mouseX, mouseY);
        this.setHeight(this.height + this.sliders.getLastHeight());
        this.setting.setValue(ColorUtil.getColorWithAlpha(ColorUtil.createColor((float)(this.hueSlider.getValue() / 100.0), (float)(this.satSlider.getValue() / 100.0), (float)(this.briSlider.getValue() / 100.0)), (int)this.alphaSlider.getValue()));
    }

    @Override
    public void onRightClick() {
        this.setting.setOpen(!this.setting.isOpen());
    }
}

