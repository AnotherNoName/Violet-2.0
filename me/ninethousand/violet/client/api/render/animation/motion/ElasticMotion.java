/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.animation.motion;

import me.ninethousand.violet.client.api.render.animation.motion.Motion;

abstract class ElasticMotion
implements Motion {
    private float amplitude;
    private float period;

    public ElasticMotion(float amplitude, float period) {
        this.amplitude = amplitude;
        this.period = period;
    }

    public ElasticMotion() {
        this(-1.0f, 0.0f);
    }

    public float getPeriod() {
        return this.period;
    }

    public void setPeriod(float period) {
        this.period = period;
    }

    public float getAmplitude() {
        return this.amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    static class InOut
    extends ElasticMotion {
        public InOut(float amplitude, float period) {
            super(amplitude, period);
        }

        public InOut() {
        }

        @Override
        public float ease(float t, float b, float c, float d) {
            float a = this.getAmplitude();
            float p = this.getPeriod();
            if (t == 0.0f) {
                return b;
            }
            if ((t /= d / 2.0f) == 2.0f) {
                return b + c;
            }
            if (p == 0.0f) {
                p = d * 0.45000002f;
            }
            float s = 0.0f;
            if (a < Math.abs(c)) {
                a = c;
                s = p / 4.0f;
            } else {
                s = p / ((float)Math.PI * 2) * (float)Math.asin(c / a);
            }
            if (t < 1.0f) {
                return -0.5f * (a * (float)Math.pow(2.0, 10.0f * (t -= 1.0f)) * (float)Math.sin((double)(t * d - s) * (Math.PI * 2) / (double)p)) + b;
            }
            return a * (float)Math.pow(2.0, -10.0f * (t -= 1.0f)) * (float)Math.sin((double)(t * d - s) * (Math.PI * 2) / (double)p) * 0.5f + c + b;
        }
    }

    static class Out
    extends ElasticMotion {
        public Out(float amplitude, float period) {
            super(amplitude, period);
        }

        public Out() {
        }

        @Override
        public float ease(float t, float b, float c, float d) {
            float a = this.getAmplitude();
            float p = this.getPeriod();
            if (t == 0.0f) {
                return b;
            }
            if ((t /= d) == 1.0f) {
                return b + c;
            }
            if (p == 0.0f) {
                p = d * 0.3f;
            }
            float s = 0.0f;
            if (a < Math.abs(c)) {
                a = c;
                s = p / 4.0f;
            } else {
                s = p / ((float)Math.PI * 2) * (float)Math.asin(c / a);
            }
            return a * (float)Math.pow(2.0, -10.0f * t) * (float)Math.sin((double)(t * d - s) * (Math.PI * 2) / (double)p) + c + b;
        }
    }

    static class In
    extends ElasticMotion {
        public In(float amplitude, float period) {
            super(amplitude, period);
        }

        public In() {
        }

        @Override
        public float ease(float t, float b, float c, float d) {
            float a = this.getAmplitude();
            float p = this.getPeriod();
            if (t == 0.0f) {
                return b;
            }
            if ((t /= d) == 1.0f) {
                return b + c;
            }
            if (p == 0.0f) {
                p = d * 0.3f;
            }
            float s = 0.0f;
            if (a < Math.abs(c)) {
                a = c;
                s = p / 4.0f;
            } else {
                s = p / ((float)Math.PI * 2) * (float)Math.asin(c / a);
            }
            return -(a * (float)Math.pow(2.0, 10.0f * (t -= 1.0f)) * (float)Math.sin((double)(t * d - s) * (Math.PI * 2) / (double)p)) + b;
        }
    }
}

