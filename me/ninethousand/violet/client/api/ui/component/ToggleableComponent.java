/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.ui.component;

import me.ninethousand.violet.client.api.ui.component.AbstractComponent;
import me.ninethousand.violet.client.util.misc.GUITracker;

public class ToggleableComponent
extends AbstractComponent {
    public ToggleableComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ToggleableComponent(int x, int y, int width, int height, AbstractComponent parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.mouseOver()) {
            if (GUITracker.leftClicked) {
                this.onLeftClick();
            }
            if (GUITracker.rightClicked) {
                this.onRightClick();
            }
        }
    }

    public void onLeftClick() {
    }

    public void onRightClick() {
    }
}

