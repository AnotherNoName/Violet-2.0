/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.MoverType
 */
package me.ninethousand.violet.client.impl.events;

import net.minecraft.entity.MoverType;

public class PlayerMoveEvent {
    private MoverType type;
    private double motionX;
    private double motionY;
    private double motionZ;

    public PlayerMoveEvent(MoverType type, double x, double y, double z) {
        this.type = type;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public MoverType getType() {
        return this.type;
    }

    public void setType(MoverType type) {
        this.type = type;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    public double getMotionY() {
        return this.motionY;
    }

    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }

    public static class Post {
        private final double movedX;
        private final double movedY;
        private final double movedZ;

        public Post(double movedX, double yChange, double movedZ) {
            this.movedX = movedX;
            this.movedY = yChange;
            this.movedZ = movedZ;
        }

        public double getMovedX() {
            return this.movedX;
        }

        public double getMovedY() {
            return this.movedY;
        }

        public double getMovedZ() {
            return this.movedZ;
        }
    }

    public static class Pre
    extends PlayerMoveEvent {
        public Pre(MoverType type, double x, double y, double z) {
            super(type, x, y, z);
        }
    }
}

