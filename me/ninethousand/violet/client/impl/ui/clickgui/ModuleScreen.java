//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.ninethousand.violet.client.impl.ui.clickgui;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.RenderWrapper;
import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.render.animation.motion.Motion;
import me.ninethousand.violet.client.api.render.vector.Vector2d;
import me.ninethousand.violet.client.api.ui.component.AbstractComponent;
import me.ninethousand.violet.client.api.ui.gui.Gui;
import me.ninethousand.violet.client.impl.ui.clickgui.components.CategoryComponent;
import org.lwjgl.input.Mouse;

public class ModuleScreen
extends Gui {
    private Animation modulesAnimation;
    private final Map<AbstractComponent, Vector2d> positions = new HashMap<AbstractComponent, Vector2d>();

    public ModuleScreen(int componentWidth, int componentHeight) {
        super(componentWidth, componentHeight);
        int spacing = 10;
        int x = (RenderWrapper.scaledResolution().getScaledWidth() - Module.Category.values().length * (componentWidth + spacing)) / 2 - 10;
        int y = 10;
        for (Module.Category category : Module.Category.values()) {
            CategoryComponent component = new CategoryComponent(x, y, componentWidth, componentHeight, category);
            this.components.add(component);
            this.positions.put(component, new Vector2d(x, y));
            x += componentWidth + spacing;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        float maximumX = ((Float)this.positions.values().stream().sorted(Comparator.comparingDouble(vec -> vec.x)).map(vec -> Float.valueOf((float)vec.x)).sorted(Collections.reverseOrder()).collect(Collectors.toList()).get(0)).floatValue();
        this.modulesAnimation = new Animation().setMin(0.0f).setMax(maximumX).setSpeed(maximumX).setMotion(Motion.BACK_OUT);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.modulesAnimation.update();
        int scroll = Mouse.getDWheel();
        int boost = 0;
        if (scroll < 0) {
            boost = -8;
        } else if (scroll > 0) {
            boost = 8;
        }
        int finalBoost = boost;
        this.components.forEach(c -> {
            c.setX((int)(this.positions.get((Object)c).x - (double)this.modulesAnimation.getMax() + (double)this.modulesAnimation.getValue()));
            c.setY(c.getY() + finalBoost);
        });
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

