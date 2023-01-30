//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package me.ninethousand.violet.client.impl.ui.clickgui.components;

import java.util.List;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.render.animation.motion.Motion;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.ui.component.ToggleableComponent;
import me.ninethousand.violet.client.api.ui.component.VerticalScissorComponent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.impl.managers.ModuleManager;
import me.ninethousand.violet.client.impl.ui.clickgui.components.ModuleComponent;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.renderer.GlStateManager;

public class CategoryComponent
extends ToggleableComponent {
    private Module.Category category;
    private VerticalScissorComponent modules;
    private Animation modulesAnimation;

    public CategoryComponent(int x, int y, int width, int height, Module.Category category) {
        super(x, y, width, height);
        this.category = category;
        int maxHeight = 100;
        this.modulesAnimation = new Animation().setMin(0.0f).setMax(maxHeight).setSpeed(50.0f).setMotion(Motion.QUAD_IN).setReversed(!category.isOpen());
        this.modules = new VerticalScissorComponent(x, y + height, width, height, width, maxHeight);
        List moduleList = ModuleManager.get().getModules().stream().filter(module -> module.getCategory().equals((Object)category)).collect(Collectors.toList());
        int drawY = y + height;
        int moduleHeight = height - 2;
        for (int i = 0; i < moduleList.size(); ++i) {
            this.modules.add(new ModuleComponent(x, drawY, width, moduleHeight, (Module)moduleList.get(i)));
            drawY += moduleHeight;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        this.modulesAnimation.update();
        Constants.renderer.fill(this.x, this.y, this.width, this.height - 2, ColorUtil.getColorWithAlpha(this.getTheme().getBackground(), 190));
        Constants.renderer.fill(this.x, this.y + this.height - 2, this.width, 2.0, ColorUtil.getColorWithAlpha(this.getTheme().getAccent(), 180));
        FontManager.get().drawString(this.category.name(), (float)this.x + (float)this.width / 24.0f, (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.category.name()) / 2.0f, this.getTheme().getText());
        double arrowX = this.x + this.width - this.height;
        double arrowY = (float)this.y + (float)this.height / 2.0f - (float)FontManager.get().getHeight(this.category.name()) / 2.0f - 2.0f;
        double arrowWidth = this.height - 8;
        double arrowHeight = FontManager.get().getHeight(this.category.name());
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)(arrowX + arrowWidth / 2.0), (double)(arrowY + arrowHeight / 2.0), (double)0.0);
        GlStateManager.rotate((float)(-180.0f + this.modulesAnimation.getValue() / this.modulesAnimation.getMax() * 90.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        Constants.renderer.downArrow(-arrowWidth / 2.0, -arrowHeight / 2.0, arrowWidth, arrowHeight, this.getTheme().getText(), 3.0f);
        GlStateManager.popMatrix();
        this.modules.setX(this.x);
        this.modules.setY(this.y + this.height);
        this.modules.setMaxHeight((int)this.modulesAnimation.getValue());
        this.modules.draw(mouseX, mouseY);
        this.modulesAnimation.setMax(this.modules.getLastHeight()).setSpeed((float)this.modules.getLastHeight() * 1.5f);
    }

    @Override
    public void onRightClick() {
        this.modulesAnimation.setReversed(!this.category.toggleOpen());
    }
}

