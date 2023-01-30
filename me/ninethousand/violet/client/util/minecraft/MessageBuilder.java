/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.TextComponentString
 */
package me.ninethousand.violet.client.util.minecraft;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;

public class MessageBuilder {
    private final StringBuilder message = new StringBuilder();
    private String text;
    private String format;

    public MessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder format(String format) {
        this.format = format;
        return this;
    }

    public MessageBuilder append() {
        if (this.text != null) {
            if (this.format != null) {
                this.message.append(this.format);
            }
            this.message.append(this.text);
            this.message.append(MessageBuilder.createFormat(ChatFormatting.RESET));
        }
        this.text = null;
        this.format = null;
        return this;
    }

    public TextComponentString getAsTextComponent() {
        return new TextComponentString(this.message.toString());
    }

    public String getAsString() {
        return this.message.toString();
    }

    public static String createFormat(ChatFormatting ... formats) {
        StringBuilder format = new StringBuilder();
        for (ChatFormatting chatFormatting : formats) {
            format.append(chatFormatting.toString());
        }
        return format.toString();
    }
}

