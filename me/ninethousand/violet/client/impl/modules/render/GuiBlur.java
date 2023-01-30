//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiConfirmOpenLink
 *  net.minecraft.client.gui.GuiControls
 *  net.minecraft.client.gui.GuiCustomizeSkin
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreenOptionsSounds
 *  net.minecraft.client.gui.GuiVideoSettings
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.GuiModList
 */
package me.ninethousand.violet.client.impl.modules.render;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.modules.client.ClickGui;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

@Module.Manifest(value=Module.Category.RENDER, description="Makes you need glasses")
public class GuiBlur
extends Module {
    private static GuiBlur instance;

    @Override
    public void onDisable() {
        if (Constants.mc.world != null) {
            Constants.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Listener
    public void listen(UpdateEvent event) {
        if (!Constants.nullCheck()) {
            if (ClickGui.getInstance().isEnabled() || Constants.mc.currentScreen instanceof GuiContainer || Constants.mc.currentScreen instanceof GuiChat || Constants.mc.currentScreen instanceof GuiConfirmOpenLink || Constants.mc.currentScreen instanceof GuiEditSign || Constants.mc.currentScreen instanceof GuiGameOver || Constants.mc.currentScreen instanceof GuiOptions || Constants.mc.currentScreen instanceof GuiIngameMenu || Constants.mc.currentScreen instanceof GuiVideoSettings || Constants.mc.currentScreen instanceof GuiScreenOptionsSounds || Constants.mc.currentScreen instanceof GuiControls || Constants.mc.currentScreen instanceof GuiCustomizeSkin || Constants.mc.currentScreen instanceof GuiModList) {
                if (OpenGlHelper.shadersSupported && Constants.mc.getRenderViewEntity() instanceof EntityPlayer) {
                    if (Constants.mc.entityRenderer.getShaderGroup() != null) {
                        Constants.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                    try {
                        Constants.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Constants.mc.entityRenderer.getShaderGroup() != null && Constants.mc.currentScreen == null) {
                    Constants.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            } else if (Constants.mc.entityRenderer.getShaderGroup() != null) {
                Constants.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }

    public static GuiBlur getInstance() {
        if (instance == null) {
            instance = new GuiBlur();
        }
        return instance;
    }
}

