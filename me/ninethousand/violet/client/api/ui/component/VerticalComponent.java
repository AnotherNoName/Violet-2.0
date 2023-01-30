/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.ui.component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.ninethousand.violet.client.api.ui.component.AbstractComponent;

public class VerticalComponent
extends AbstractComponent {
    protected int maxWidth;
    protected int maxHeight;
    protected int lastWidth;
    protected int lastHeight;
    protected List<AbstractComponent> list = new LinkedList<AbstractComponent>();

    public VerticalComponent(int x, int y, int width, int height, int maxWidth, int maxHeight) {
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
            c.x = this.x;
            c.y = this.y + drawing.get();
            c.width = this.width;
            c.height = this.height;
            c.draw(mouseX, mouseY);
            drawing.getAndAdd(c.getHeight());
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

    public List<AbstractComponent> getList() {
        return this.list;
    }
}

