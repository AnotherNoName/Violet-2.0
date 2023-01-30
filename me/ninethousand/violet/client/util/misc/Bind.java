/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.ninethousand.violet.client.util.misc;

import org.lwjgl.input.Keyboard;

public class Bind {
    private int key;
    private Mode mode = Mode.PRESS;
    private boolean lastDown = false;
    private boolean guiTyping = false;
    public static final Bind DEFAULT = new Bind(0);

    public Bind(int key) {
        this.key = key;
    }

    public boolean getOutput() {
        return this.mode == Mode.PRESS ? this.isPressed() : this.isReleased();
    }

    public boolean isPressed() {
        if (this.key == 0) {
            return false;
        }
        boolean down = this.isDown();
        boolean result = down && !this.lastDown;
        this.lastDown = down;
        return result;
    }

    public boolean isDown() {
        if (this.key == 0) {
            return false;
        }
        return Keyboard.isKeyDown((int)this.key);
    }

    public boolean isReleased() {
        if (this.key == 0) {
            return false;
        }
        boolean down = this.isDown();
        boolean result = !down && this.lastDown;
        this.lastDown = down;
        return result;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isGuiTyping() {
        return this.guiTyping;
    }

    public void setGuiTyping(boolean guiTyping) {
        this.guiTyping = guiTyping;
    }

    public static enum Mode {
        PRESS,
        RELEASE;

    }
}

