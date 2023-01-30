/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.image;

import me.ninethousand.violet.client.api.render.image.EfficientTexture;

public class NameableImage {
    private final String name;
    private final EfficientTexture texture;

    public NameableImage(EfficientTexture texture, String name) {
        this.name = name;
        this.texture = texture;
    }

    public String getName() {
        return this.name;
    }

    public EfficientTexture getTexture() {
        return this.texture;
    }
}

