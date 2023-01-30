//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import java.awt.Dimension;
import me.ninethousand.violet.client.api.render.ICanvas;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.renderer.GlStateManager;

public class SettingCanvasComponent
extends ToggleableComponent {
    private SettingContainer<ICanvas> setting;

    public SettingCanvasComponent(int x, int y, int width, int height, SettingContainer<ICanvas> setting) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        this.setHeight((int)(0.6666667f * (float)this.width));
        ICanvas canvas = this.setting.getValue();
        Constants.renderer.fill(this.x, this.y, 1.0, this.height, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        Constants.renderer.fill(this.x + 1, this.y, this.width - 1, this.height - 1, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x + 1, this.y + this.height - 1, this.width - 1, 1.0, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 180));
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.x + 2), (float)(this.y + 1), (float)0.0f);
        canvas.paint(new Dimension(this.width - 4, this.height - 2));
        GlStateManager.popMatrix();
    }
}

