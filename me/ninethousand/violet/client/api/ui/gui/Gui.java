//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.ninethousand.violet.client.api.ui.gui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import me.ninethousand.violet.client.api.ui.component.AbstractComponent;
import me.ninethousand.violet.client.api.ui.theme.Theme;
import me.ninethousand.violet.client.impl.managers.ThemeManager;
import me.ninethousand.violet.client.util.misc.GUITracker;
import net.minecraft.client.gui.GuiScreen;

public abstract class Gui
extends GuiScreen {
    protected int componentWidth;
    protected int componentHeight;
    protected List<AbstractComponent> components = new LinkedList<AbstractComponent>();
    protected Theme theme;

    public Gui(int componentWidth, int componentHeight) {
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;
    }

    public void initGui() {
        super.initGui();
        this.theme = ThemeManager.get().getClickGuiTheme();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GUITracker.activeGui = this;
        this.components.forEach(c -> c.draw(mouseX, mouseY));
        GUITracker.updateMousePos(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            GUITracker.updateLeftClick();
        }
        if (mouseButton == 1) {
            GUITracker.updateRightClick();
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            GUITracker.updateMouseState();
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        GUITracker.updateKeyState(keyCode);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        GUITracker.activeGui = null;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}

