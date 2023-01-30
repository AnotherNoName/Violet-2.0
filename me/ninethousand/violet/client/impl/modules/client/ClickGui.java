//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.ninethousand.violet.client.impl.modules.client;

import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.ui.clickgui.ClickGuiScreen;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.gui.GuiScreen;

@Module.Manifest(value=Module.Category.CLIENT, key=24, description="PK Gui")
public class ClickGui
extends Module {
    @Setting
    private boolean blur = true;
    private static ClickGui instance;

    @Override
    protected void onEnable() {
        if (Constants.nullCheck()) {
            this.toggle();
            return;
        }
        Constants.mc.displayGuiScreen((GuiScreen)ClickGuiScreen.getInstance());
    }

    @Override
    protected void onDisable() {
        if (Constants.nullCheck()) {
            return;
        }
        Constants.mc.player.closeScreen();
    }

    public boolean isBlur() {
        return this.blur;
    }

    public static ClickGui getInstance() {
        if (instance == null) {
            instance = new ClickGui();
        }
        return instance;
    }
}

