/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.animation.motion;

import me.ninethousand.violet.client.api.render.animation.motion.Motion;

abstract class BackMotion
implements Motion {
    public static final float DEFAULT_OVERSHOOT = 1.70158f;
    private float overshoot;

    public BackMotion() {
        this(1.70158f);
    }

    public BackMotion(float overshoot) {
        this.overshoot = overshoot;
    }

    public float getOvershoot() {
        return this.overshoot;
    }

    public void setOvershoot(float overshoot) {
        this.overshoot = overshoot;
    }

    static class InOut
    extends BackMotion {
        public InOut() {
        }

        public InOut(float overshoot) {
            super(overshoot);
        }

        @Override
        public float ease(float t, float b, float c, float d) {
            float f;
            float s = this.getOvershoot();
            t /= d / 2.0f;
            if (f < 1.0f) {
                s = (float)((double)s * 1.525);
                return c / 2.0f * (t * t * ((s + 1.0f) * t - s)) + b;
            }
            s = (float)((double)s * 1.525);
            return c / 2.0f * ((t -= 2.0f) * t * ((s + 1.0f) * t + s) + 2.0f) + b;
        }
    }

    static class Out
    extends BackMotion {
        public Out() {
        }

        public Out(float overshoot) {
            super(overshoot);
        }

        @Override
        public float ease(float t, float b, float c, float d) {
            float s = this.getOvershoot();
            t = t / d - 1.0f;
            return c * (t * t * ((s + 1.0f) * t + s) + 1.0f) + b;
        }
    }

    static class In
    extends BackMotion {
        public In() {
        }

        public In(float overshoot) {
            super(overshoot);
        }

        @Override
        public float ease(float t, float b, float c, float d) {
            float s = this.getOvershoot();
            return c * (t /= d) * t * ((s + 1.0f) * t - s) + b;
        }
    }
}

