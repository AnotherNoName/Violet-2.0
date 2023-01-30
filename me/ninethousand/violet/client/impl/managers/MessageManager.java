//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package me.ninethousand.violet.client.impl.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.ninethousand.violet.client.Violet;
import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class MessageManager
implements Manager {
    private static MessageManager instance;

    public void log(String message) {
        Violet.CLIENT_LOGGER.info(message);
    }

    public void chatNotify(String message, int id) {
        if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) {
            return;
        }
        TextComponentString textComponent = new MessageBuilder().text("[Violet] ").format(MessageBuilder.createFormat(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD)).append().text(message).format(MessageBuilder.createFormat(ChatFormatting.WHITE)).append().getAsTextComponent();
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)textComponent, id);
    }

    public static MessageManager get() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }
}

