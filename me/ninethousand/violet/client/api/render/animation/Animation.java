/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.Sys
 */
package me.ninethousand.violet.client.api.render.animation;

import me.ninethousand.violet.client.api.render.animation.motion.Motion;
import me.ninethousand.violet.client.api.render.misc.RenderMathUtil;
import org.lwjgl.Sys;

public class Animation {
    private float value = 0.0f;
    private float min = 0.0f;
    private float max = 1.0f;
    private float speed = 50.0f;
    private float time;
    private boolean reversed = false;
    private Motion motion;
    private long lastFrame = this.getTime();
    private long deltaTime;

    public Animation() {
        this.motion = Motion.LINEAR;
    }

    public Animation update() {
        long currentTime = this.getTime();
        this.deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        if (this.reversed) {
            if (this.time > this.min) {
                this.time -= (float)this.deltaTime * 0.001f * this.speed;
            }
        } else if (this.time < this.max) {
            this.time += (float)this.deltaTime * 0.001f * this.speed;
        }
        this.time = RenderMathUtil.clamp(this.time, this.min, this.max);
        this.value = this.motion.ease(this.time, this.min, this.max, this.max);
        return this;
    }

    public float getValue() {
        return this.value;
    }

    public Animation setValue(float value) {
        this.value = value;
        return this;
    }

    public float getMin() {
        return this.min;
    }

    public Animation setMin(float min) {
        this.min = min;
        if (this.value < min) {
            this.value = min;
        }
        return this;
    }

    public float getMax() {
        return this.max;
    }

    public Animation setMax(float max) {
        this.max = max;
        if (this.value > max) {
            this.value = max;
        }
        return this;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Animation setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public boolean isReversed() {
        return this.reversed;
    }

    public Animation setReversed(boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    public Motion getMotion() {
        return this.motion;
    }

    public Animation setMotion(Motion motion) {
        this.motion = motion;
        return this;
    }

    private long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
}

