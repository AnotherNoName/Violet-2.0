/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.render.misc.RenderMathUtil;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.GUITracker;
import me.ninethousand.violet.client.util.misc.MathUtil;
import net.minecraft.util.math.Vec2f;

public class SliderComponent
extends ToggleableComponent {
    private String name;
    private double min;
    private double value;
    private double max;
    private int dp;
    private double sliderProgress;

    public SliderComponent(int x, int y, int width, int height, String name, double min, double value, double max, int dp) {
        super(x, y, width, height);
        this.name = name;
        this.min = min;
        this.value = value;
        this.max = max;
        this.dp = dp;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        double sliderX = this.x + 3;
        double sliderY = this.y + 2;
        double sliderWidth = this.width - 5;
        double sliderHeight = this.height - 5;
        double sliderFill = -1.0;
        Constants.renderer.fill(sliderX, sliderY, sliderWidth, sliderHeight, this.getTheme().getBackground());
        if (GUITracker.mouseOver(new Vec2f((float)sliderX, (float)sliderY), new Vec2f((float)(sliderX + sliderWidth), (float)(sliderY + sliderHeight))) && GUITracker.leftDown) {
            sliderFill = RenderMathUtil.clamp((double)mouseX - sliderX, 0.0, sliderWidth);
        }
        if (sliderFill != -1.0) {
            this.setSettingValue(sliderFill / sliderWidth);
        } else {
            this.setSettingDefaultValue();
        }
        Constants.renderer.fill(sliderX, sliderY, this.sliderProgress * sliderWidth, sliderHeight, this.getTheme().getAccent());
        FontManager.get().drawString(this.name, (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.name) / 2.0f, this.getTheme().getText());
        FontManager.get().drawString(String.valueOf(this.value), this.x + this.width - FontManager.get().getWidth(String.valueOf(this.value)) - 3, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(String.valueOf(this.value)) / 2.0f, this.getTheme().getText());
    }

    private void setSettingValue(double percentage) {
        this.value = RenderMathUtil.clamp(MathUtil.round(percentage * (this.max - this.min), this.dp), this.min, this.max);
        this.sliderProgress = this.value / (this.max - this.min);
    }

    private void setSettingDefaultValue() {
        this.sliderProgress = this.value / (this.max - this.min);
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = RenderMathUtil.clamp(MathUtil.round(value, this.dp), this.min, this.max);
    }
}

