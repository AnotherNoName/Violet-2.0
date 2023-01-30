//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.Vec2f
 */
package me.ninethousand.violet.client.api.ui.component;

import me.ninethousand.violet.client.api.ui.theme.Theme;
import me.ninethousand.violet.client.impl.managers.ThemeManager;
import me.ninethousand.violet.client.util.misc.GUITracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec2f;

public abstract class AbstractComponent {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected AbstractComponent parent;

    public AbstractComponent() {
        this(0, 0, 0, 0);
    }

    public AbstractComponent(int x, int y, int width, int height) {
        this(x, y, width, height, null);
    }

    public AbstractComponent(int x, int y, int width, int height, AbstractComponent parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public abstract void draw(int var1, int var2);

    protected Vec2f getTopCorner() {
        return new Vec2f((float)this.x, (float)this.y);
    }

    protected Vec2f getBottomCorner() {
        return new Vec2f((float)(this.x + this.width), (float)(this.y + this.height));
    }

    protected boolean mouseOver() {
        return GUITracker.mouseOver(this.getTopCorner(), this.getBottomCorner());
    }

    protected Theme getTheme() {
        return GUITracker.activeGui == null ? GUITracker.activeGui.getTheme() : ThemeManager.get().getClickGuiTheme();
    }

    protected void playClickSoundEffect() {
        Minecraft.getMinecraft().getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AbstractComponent getParent() {
        return this.parent;
    }

    public void setParent(AbstractComponent parent) {
        this.parent = parent;
    }
}

