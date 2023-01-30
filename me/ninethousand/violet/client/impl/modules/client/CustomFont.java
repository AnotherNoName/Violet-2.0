/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.ninethousand.violet.client.impl.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;

@Module.Manifest(value=Module.Category.CLIENT, description="Use custom fonts")
public class CustomFont
extends Module {
    @Setting
    private FontStyle style = FontStyle.Normal;
    private static CustomFont instance;

    @Override
    protected void onEnable() {
        FontManager.get().setCustomFont(true);
    }

    @Override
    protected void onDisable() {
        FontManager.get().setCustomFont(false);
    }

    public String getStyleFormat() {
        switch (this.style) {
            case Bold: {
                return MessageBuilder.createFormat(ChatFormatting.BOLD);
            }
            case Italic: {
                return MessageBuilder.createFormat(ChatFormatting.ITALIC);
            }
            case BoldItalic: {
                return MessageBuilder.createFormat(ChatFormatting.BOLD, ChatFormatting.ITALIC);
            }
        }
        return MessageBuilder.createFormat(new ChatFormatting[0]);
    }

    public static CustomFont getInstance() {
        if (instance == null) {
            instance = new CustomFont();
        }
        return instance;
    }

    private static enum FontStyle {
        Normal,
        Bold,
        Italic,
        BoldItalic;

    }
}

