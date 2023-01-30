/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.ui.component;

import java.awt.Rectangle;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.ninethousand.violet.client.api.render.gl.ScissorStack;
import me.ninethousand.violet.client.api.ui.component.AbstractComponent;
import me.ninethousand.violet.client.util.misc.GUITracker;

public class VerticalScissorComponent
extends AbstractComponent {
    protected int maxWidth;
    protected int maxHeight;
    protected int lastWidth;
    protected int lastHeight;
    protected int maxY;
    protected List<AbstractComponent> list = new LinkedList<AbstractComponent>();
    protected ScissorStack scissors = new ScissorStack();

    public VerticalScissorComponent(int x, int y, int width, int height, int maxWidth, int maxHeight) {
        super(x, y, width, height);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.lastWidth = maxWidth;
        this.lastHeight = maxHeight;
    }

    public void add(AbstractComponent component) {
        this.list.add(component);
    }

    public void sort(Comparator<? super AbstractComponent> comparator) {
        this.list.sort(comparator);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        AtomicInteger drawing = new AtomicInteger(0);
        this.list.forEach(c -> {
            Rectangle scissored = GUITracker.getScissor();
            GUITracker.addScissor(this.x, this.y, Math.min(this.maxWidth, this.lastWidth), Math.min(this.maxHeight, this.lastHeight));
            this.scissors.pushScissor(this.x, this.y, Math.min(this.maxWidth, this.lastWidth), Math.min(this.maxHeight, this.lastHeight));
            c.x = this.x;
            c.y = this.y + drawing.get();
            c.width = this.width;
            c.height = this.height;
            c.draw(mouseX, mouseY);
            drawing.getAndAdd(c.getHeight());
            this.scissors.popScissor();
            GUITracker.clearScissor();
        });
        this.lastHeight = drawing.get();
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getLastWidth() {
        return this.lastWidth;
    }

    public int getLastHeight() {
        return this.lastHeight;
    }
}

