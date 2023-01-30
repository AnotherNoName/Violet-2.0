//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 */
package me.ninethousand.violet.client.util.misc;

import java.awt.Rectangle;
import me.ninethousand.violet.client.api.ui.gui.Gui;
import net.minecraft.util.math.Vec2f;

public class GUITracker {
    public static Gui activeGui = null;
    public static int mouseX;
    public static int mouseY;
    public static int keyDown;
    public static boolean leftClicked;
    public static boolean leftDown;
    public static boolean rightClicked;
    public static Rectangle scissor;

    public static boolean mouseOver(double x, double y, double width, double height) {
        return GUITracker.mouseOverUnstrict((int)x, (int)y, (int)x + (int)width, (int)x + (int)height);
    }

    public static boolean mouseOverUnstrict(int minX, int minY, int maxX, int maxY) {
        return mouseX >= minX && mouseY >= minY && mouseX <= maxX && mouseY <= maxY;
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY) {
        return mouseX >= minX && mouseY >= minY && mouseX <= maxX && mouseY <= maxY && GUITracker.mouseOverScissor();
    }

    public static boolean mouseOver(float minX, float minY, float maxX, float maxY) {
        return (float)mouseX >= minX && (float)mouseY >= minY && (float)mouseX <= maxX && (float)mouseY <= maxY && GUITracker.mouseOverScissor();
    }

    public static boolean mouseOver(Vec2f topLeft, Vec2f bottomRight) {
        return (float)mouseX >= topLeft.x && (float)mouseY >= topLeft.y && (float)mouseX <= bottomRight.x && (float)mouseY <= bottomRight.y && GUITracker.mouseOverScissor();
    }

    public static boolean mouseOver(Rectangle rectangle) {
        return mouseX >= rectangle.x && mouseY >= rectangle.y && mouseX <= rectangle.x + rectangle.width && mouseY <= rectangle.y + rectangle.height && GUITracker.mouseOverScissor();
    }

    private static boolean mouseOverScissor() {
        if (scissor == null) {
            return true;
        }
        return mouseX >= GUITracker.scissor.x && mouseY >= GUITracker.scissor.y && mouseX <= GUITracker.scissor.x + GUITracker.scissor.width && mouseY <= GUITracker.scissor.y + GUITracker.scissor.height;
    }

    public static void addScissor(int x, int y, int width, int height) {
        scissor = new Rectangle(x, y, width, height);
    }

    public static void clearScissor() {
        scissor = null;
    }

    public static Rectangle getScissor() {
        return scissor;
    }

    public static void updateMousePos(int mouseXPos, int mouseYPos) {
        mouseX = mouseXPos;
        mouseY = mouseYPos;
        leftClicked = false;
        rightClicked = false;
        keyDown = 0;
    }

    public static void updateLeftClick() {
        leftClicked = true;
        leftDown = true;
    }

    public static void updateRightClick() {
        rightClicked = true;
    }

    public static void updateMouseState() {
        leftDown = false;
    }

    public static void updateKeyState(int key) {
        keyDown = key;
    }
}

