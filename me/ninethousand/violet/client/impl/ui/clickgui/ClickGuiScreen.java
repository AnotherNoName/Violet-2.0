//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.util.ResourceLocation
 */
package me.ninethousand.violet.client.impl.ui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.render.animation.motion.Motion;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.render.misc.RenderMathUtil;
import me.ninethousand.violet.client.api.ui.gui.Gui;
import me.ninethousand.violet.client.impl.events.RenderChatEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.impl.modules.client.ClickGui;
import me.ninethousand.violet.client.impl.ui.clickgui.ModuleScreen;
import me.ninethousand.violet.client.impl.ui.clickgui.components.GuiNotifyComponent;
import me.ninethousand.violet.client.util.misc.ListUtils;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class ClickGuiScreen
extends Gui {
    private Animation openAnimation;
    private GuiNotifyComponent activeNotification;
    private Timer notificationTimer;
    private Gui activeScreen;
    private final LinkedList<Gui> screens = new LinkedList();
    private int currentScreenIndex = 0;
    private static ClickGuiScreen instance;

    public ClickGuiScreen() {
        super(130, 18);
        this.screens.addAll(Arrays.asList(new ModuleScreen(this.componentWidth, this.componentHeight)));
        this.activeScreen = this.screens.get(this.currentScreenIndex);
    }

    public void setNotificationMessage(String message) {
        if (message != null && !message.isEmpty()) {
            int notificationHeight = 50;
            int notificationWidth = 240;
            this.activeNotification = new GuiNotifyComponent(this.width - notificationWidth, this.height - notificationHeight - 50, notificationWidth, notificationHeight, message);
            this.notificationTimer = new Timer();
        }
    }

    public void setActiveScreen(Gui screen) {
        if (this.activeScreen == screen) {
            return;
        }
        this.activeScreen.onGuiClosed();
        this.activeScreen = screen;
        this.activeScreen.initGui();
    }

    @Override
    public void initGui() {
        super.initGui();
        EventManager.get().register((Object)this);
        this.openAnimation = new Animation().setMin(0.0f).setMax(255.0f).setSpeed(230.0f).setMotion(Motion.LINEAR);
        this.activeScreen.initGui();
    }

    @Listener
    private void listen(RenderChatEvent event) {
        event.cancel();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.openAnimation.update();
        this.activeScreen.drawScreen(mouseX, mouseY, partialTicks);
        this.notifications();
        this.watermark();
        if (ClickGui.getInstance().isBlur()) {
            this.blur();
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        EventManager.get().unregister((Object)this);
        this.unblur();
        ClickGui.getInstance().setEnabled(false);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 203) {
            this.currentScreenIndex = ListUtils.getPrevIndex(this.screens, this.currentScreenIndex);
        } else if (keyCode == 205) {
            this.currentScreenIndex = ListUtils.getNextIndex(this.screens, this.currentScreenIndex);
        }
        this.setActiveScreen(this.screens.get(this.currentScreenIndex));
    }

    private void blur() {
        if (OpenGlHelper.shadersSupported) {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    private void unblur() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    private void watermark() {
        FontManager.get().drawString("Violet v2.0", 2.0f, this.height - FontManager.get().getHeight("Violet v2.0") - 3, ColorUtil.getColorWithAlpha(new Color(0xB4B4B4), RenderMathUtil.clamp((int)this.openAnimation.getValue(), 20, 255)));
    }

    private void notifications() {
        long notificationDelay = 1000L;
        if (this.notificationTimer == null || this.notificationTimer.passed(notificationDelay)) {
            this.activeNotification = null;
            this.notificationTimer = null;
            return;
        }
        this.activeNotification.draw(0, 0);
    }

    public static ClickGuiScreen getInstance() {
        if (instance == null) {
            instance = new ClickGuiScreen();
        }
        return instance;
    }
}

