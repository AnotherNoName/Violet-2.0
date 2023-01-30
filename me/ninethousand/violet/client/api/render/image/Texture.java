/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.image;

public abstract class Texture {
    protected int id = -1;
    protected int width;
    protected int height;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected abstract void init();

    public abstract void bind();

    public abstract void unbind();

    public int getId() {
        return this.id;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

