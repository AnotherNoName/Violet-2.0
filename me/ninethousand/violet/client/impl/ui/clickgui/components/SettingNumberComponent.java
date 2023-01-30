/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.render.misc.RenderMathUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.GUITracker;
import me.ninethousand.violet.client.util.misc.MathUtil;
import net.minecraft.util.math.Vec2f;

public class SettingNumberComponent
extends ToggleableComponent {
    private SettingContainer<Number> setting;
    private double sliderProgress;

    public SettingNumberComponent(int x, int y, int width, int height, SettingContainer<Number> setting) {
        super(x, y, width, height);
        this.setting = setting;
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
        FontManager.get().drawString(this.setting.getName(), (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.setting.getName()) / 2.0f, this.getTheme().getText());
        FontManager.get().drawString(String.valueOf(this.setting.getValue()), this.x + this.width - FontManager.get().getWidth(String.valueOf(this.setting.getValue())) - 3, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(String.valueOf(this.setting.getValue())) / 2.0f, this.getTheme().getText());
    }

    private void setSettingValue(double percentage) {
        Number value = this.setting.getValue();
        double range = this.setting.getMax() - this.setting.getMin();
        if (value.getClass() == Integer.class) {
            this.setting.setValue((int)RenderMathUtil.clamp(MathUtil.round(percentage * range, this.setting.getDp()), this.setting.getMin(), this.setting.getMax()));
            this.sliderProgress = (double)((Integer)this.setting.getValue()).intValue() / (this.setting.getMax() - this.setting.getMin());
        } else if (value.getClass() == Float.class) {
            this.setting.setValue(Float.valueOf((float)RenderMathUtil.clamp(MathUtil.round(percentage * range, this.setting.getDp()), this.setting.getMin(), this.setting.getMax())));
            this.sliderProgress = (double)((Float)this.setting.getValue()).floatValue() / (this.setting.getMax() - this.setting.getMin());
        } else if (value.getClass() == Double.class) {
            this.setting.setValue(RenderMathUtil.clamp(MathUtil.round(percentage * range, this.setting.getDp()), this.setting.getMin(), this.setting.getMax()));
            this.sliderProgress = (Double)this.setting.getValue() / (this.setting.getMax() - this.setting.getMin());
        } else if (value.getClass() == Long.class) {
            this.setting.setValue((long)RenderMathUtil.clamp(MathUtil.round(percentage * range, this.setting.getDp()), this.setting.getMin(), this.setting.getMax()));
            this.sliderProgress = (double)((Long)this.setting.getValue()).longValue() / (this.setting.getMax() - this.setting.getMin());
        }
    }

    private void setSettingDefaultValue() {
        Number value = this.setting.getValue();
        double range = this.setting.getMax() - this.setting.getMin();
        if (value.getClass() == Integer.class) {
            this.sliderProgress = (double)((Integer)this.setting.getValue()).intValue() / (this.setting.getMax() - this.setting.getMin());
        } else if (value.getClass() == Float.class) {
            this.sliderProgress = (double)((Float)this.setting.getValue()).floatValue() / (this.setting.getMax() - this.setting.getMin());
        } else if (value.getClass() == Double.class) {
            this.sliderProgress = (Double)this.setting.getValue() / (this.setting.getMax() - this.setting.getMin());
        } else if (value.getClass() == Long.class) {
            this.sliderProgress = (double)((Long)this.setting.getValue()).longValue() / (this.setting.getMax() - this.setting.getMin());
        }
    }
}

