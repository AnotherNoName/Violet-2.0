/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.ui.component.AbstractComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;

public class GuiNotifyComponent
extends AbstractComponent {
    private String message;
    private Animation animation;

    public GuiNotifyComponent(int x, int y, int width, int height, String message) {
        super(x, y, width, height);
        this.message = message;
        this.animation = new Animation();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Constants.renderer.fill(this.x, this.y, this.width, this.height - 2, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 150));
        Constants.renderer.fill(this.x, this.y + this.height - 2, this.width, 2.0, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        FontManager.get().drawString(this.message, (float)this.x + (float)this.width / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.message) / 2.0f, this.getTheme().getText());
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

