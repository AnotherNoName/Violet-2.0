/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.Bind;
import me.ninethousand.violet.client.util.misc.GUITracker;
import org.lwjgl.input.Keyboard;

public class BindComponent
extends ToggleableComponent {
    private Bind bind;

    public BindComponent(int x, int y, int width, int height, Bind bind) {
        super(x, y, width, height);
        this.bind = bind;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int key;
        super.draw(mouseX, mouseY);
        if (this.bind.isGuiTyping() && (key = GUITracker.keyDown) != 0) {
            if (key == 28) {
                this.bind.setGuiTyping(false);
            } else {
                if (key == 14 || key == 211) {
                    key = 0;
                }
                this.bind.setKey(key);
                this.bind.setGuiTyping(false);
            }
        }
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        FontManager.get().drawString("Bind", (float)this.x + (float)(2 * this.width) / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight("Bind") / 2.0f, this.getTheme().getText());
        FontManager.get().drawString(this.bind.isGuiTyping() ? "..." : Keyboard.getKeyName((int)this.bind.getKey()), this.x + this.width - FontManager.get().getWidth(this.bind.isGuiTyping() ? "..." : Keyboard.getKeyName((int)this.bind.getKey())) - 3, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.bind.isGuiTyping() ? "..." : Keyboard.getKeyName((int)this.bind.getKey())) / 2.0f, this.getTheme().getText());
    }

    @Override
    public void onLeftClick() {
        this.bind.setGuiTyping(!this.bind.isGuiTyping());
    }
}

