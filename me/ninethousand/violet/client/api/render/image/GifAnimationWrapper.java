/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.image;

import org.newdawn.slick.Animation;

public class GifAnimationWrapper {
    private final String name;
    private final Animation animation;

    public GifAnimationWrapper(String name, Animation animation) {
        this.name = name;
        this.animation = animation;
    }

    public int getCurrentFrameTexID() {
        if (this.animation == null) {
            return 0;
        }
        this.animation.updateNoDraw();
        return this.animation.getCurrentFrame().getTexture().getTextureID();
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public String getName() {
        return this.name;
    }
}

