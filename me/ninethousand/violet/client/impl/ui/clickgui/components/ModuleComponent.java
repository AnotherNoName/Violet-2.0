/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.ICanvas;
import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.render.animation.motion.Motion;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.api.ui.component.VerticalComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.impl.ui.clickgui.ClickGuiScreen;
import me.ninethousand.violet.client.impl.ui.clickgui.components.BindComponent;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SettingBooleanComponent;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SettingCanvasComponent;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SettingColorComponent;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SettingEnumComponent;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SettingNumberComponent;
import me.ninethousand.violet.client.impl.ui.clickgui.components.SettingStringComponent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;

public class ModuleComponent
extends ToggleableComponent {
    private Module module;
    private VerticalComponent components;
    private Animation settingsAnimation;

    public ModuleComponent(int x, int y, int width, int height, Module module) {
        super(x, y, width, height);
        this.module = module;
        int maxHeight = 100;
        this.settingsAnimation = new Animation().setMin(0.0f).setMax(maxHeight).setSpeed(50.0f).setMotion(Motion.QUAD_IN).setReversed(!module.isOpen());
        this.components = new VerticalComponent(x, y + height, width, height, width, maxHeight);
        int drawY = y + height;
        int moduleHeight = height - 2;
        this.components.add(new BindComponent(x, drawY, width, moduleHeight, module.getBind()));
        this.components.add(new SettingCategory(x, drawY += moduleHeight, width, moduleHeight));
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        this.settingsAnimation.update();
        if (this.mouseOver() && !this.module.getDescription().isEmpty()) {
            ClickGuiScreen.getInstance().setNotificationMessage(this.module.getName() + " - " + this.module.getDescription());
        }
        Constants.renderer.fill(this.x, this.y, this.width, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x, this.y + this.height - 1, this.width, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        FontManager.get().drawString(this.module.getName(), (float)this.x + (float)this.width / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.module.getName()) / 2.0f, this.module.isEnabled() ? this.getTheme().getAccent() : this.getTheme().getText());
        if (this.module.isOpen()) {
            this.components.setX(this.x);
            this.components.setY(this.y + this.height);
            this.components.draw(mouseX, mouseY);
            this.setHeight(this.height + this.components.getLastHeight());
        }
    }

    @Override
    public void onLeftClick() {
        this.module.toggle();
        String message = new MessageBuilder().text(this.module.getName() + " " + (this.module.isEnabled() ? "enabled" : "disabled")).format(ChatFormatting.BOLD.toString()).append().getAsString();
        ClickGuiScreen.getInstance().setNotificationMessage(message);
    }

    @Override
    public void onRightClick() {
        this.module.toggleOpen();
    }

    private class SettingCategory
    extends ToggleableComponent {
        private final Map<String, VerticalComponent> settingsWindows;
        private int windowIndex;
        private Map.Entry<String, VerticalComponent> current;

        public SettingCategory(int x, int y, int width, int height) {
            super(x, y, width, height);
            this.windowIndex = 0;
            this.settingsWindows = new HashMap<String, VerticalComponent>();
            ModuleComponent.this.module.getSettingContainers().stream().map(SettingContainer::getCategory).distinct().forEach(category -> {
                VerticalComponent component = new VerticalComponent(x, y + height, width, height, width, 100);
                int drawY = y + height;
                int moduleHeight = height;
                for (int i = 0; i < ModuleComponent.this.module.getSettingContainers().size(); ++i) {
                    if (!ModuleComponent.this.module.getSettingContainers().get(i).getCategory().equals(category)) continue;
                    ToggleableComponent subComponent = null;
                    if (ModuleComponent.this.module.getSettingContainers().get(i).getValue() instanceof Boolean) {
                        subComponent = new SettingBooleanComponent(x, drawY, width, moduleHeight, ModuleComponent.this.module.getSettingContainers().get(i));
                        component.add(subComponent);
                    }
                    if (ModuleComponent.this.module.getSettingContainers().get(i).getValue() instanceof Number) {
                        subComponent = new SettingNumberComponent(x, drawY, width, moduleHeight, ModuleComponent.this.module.getSettingContainers().get(i));
                        component.add(subComponent);
                    }
                    if (ModuleComponent.this.module.getSettingContainers().get(i).getValue() instanceof Enum) {
                        subComponent = new SettingEnumComponent(x, drawY, width, moduleHeight, ModuleComponent.this.module.getSettingContainers().get(i));
                        component.add(subComponent);
                    }
                    if (ModuleComponent.this.module.getSettingContainers().get(i).getValue() instanceof ICanvas) {
                        subComponent = new SettingCanvasComponent(x, drawY, width, moduleHeight, ModuleComponent.this.module.getSettingContainers().get(i));
                        component.add(subComponent);
                    }
                    if (ModuleComponent.this.module.getSettingContainers().get(i).getValue() instanceof String) {
                        subComponent = new SettingStringComponent(x, drawY, width, moduleHeight, ModuleComponent.this.module.getSettingContainers().get(i));
                        component.add(subComponent);
                    }
                    if (ModuleComponent.this.module.getSettingContainers().get(i).getValue() instanceof Color) {
                        subComponent = new SettingColorComponent(x, drawY, width, moduleHeight, ModuleComponent.this.module.getSettingContainers().get(i));
                        component.add(subComponent);
                    }
                    if (subComponent == null) continue;
                    drawY += subComponent.getHeight();
                }
                if (component.getList().isEmpty()) {
                    return;
                }
                this.settingsWindows.put((String)category, component);
            });
            try {
                Object[] arr = this.settingsWindows.entrySet().toArray();
                this.current = (Map.Entry)arr[0];
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        @Override
        public void draw(int mouseX, int mouseY) {
            super.draw(mouseX, mouseY);
            if (this.current == null) {
                this.setHeight(0);
                return;
            }
            if (this.settingsWindows.size() > 1) {
                Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
                Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
                Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
                FontManager.get().drawCenteredString("Page: " + this.current.getKey(), (float)this.x + (float)this.width / 2.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.current.getKey()) / 2.0f, this.getTheme().getText());
            } else {
                this.setHeight(0);
            }
            this.current.getValue().setX(this.x);
            this.current.getValue().setY(this.y + this.height);
            this.current.getValue().draw(mouseX, mouseY);
            this.setHeight(this.height + this.current.getValue().getLastHeight());
        }

        @Override
        public void onLeftClick() {
            Object[] arr = this.settingsWindows.entrySet().toArray();
            if (++this.windowIndex >= arr.length) {
                this.windowIndex = 0;
            }
            this.current = (Map.Entry)arr[this.windowIndex];
        }

        @Override
        public void onRightClick() {
            Object[] arr = this.settingsWindows.entrySet().toArray();
            if (--this.windowIndex < 0) {
                this.windowIndex = arr.length - 1;
            }
            this.current = (Map.Entry)arr[this.windowIndex];
        }
    }
}

