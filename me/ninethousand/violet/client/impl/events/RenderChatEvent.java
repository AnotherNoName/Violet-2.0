/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.GuiNewChat
 */
package me.ninethousand.violet.client.impl.events;

import java.util.List;
import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;

public class RenderChatEvent
extends CancellableEvent {
    private final GuiNewChat chatGui;
    private final int updateCounter;
    private final List<String> sentMessages;
    private final List<ChatLine> chatLines;
    private final List<ChatLine> drawnChatLines;
    private int scrollPos;
    private boolean isScrolled;

    public RenderChatEvent(GuiNewChat chatGui, int updateCounter, List<String> sentMessages, List<ChatLine> chatLines, List<ChatLine> drawnChatLines, int scrollPos, boolean isScrolled) {
        this.chatGui = chatGui;
        this.updateCounter = updateCounter;
        this.sentMessages = sentMessages;
        this.chatLines = chatLines;
        this.drawnChatLines = drawnChatLines;
        this.scrollPos = scrollPos;
        this.isScrolled = isScrolled;
    }

    public GuiNewChat getChatGui() {
        return this.chatGui;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public List<String> getSentMessages() {
        return this.sentMessages;
    }

    public List<ChatLine> getChatLines() {
        return this.chatLines;
    }

    public List<ChatLine> getDrawnChatLines() {
        return this.drawnChatLines;
    }

    public int getScrollPos() {
        return this.scrollPos;
    }

    public boolean isScrolled() {
        return this.isScrolled;
    }

    public void setScrollPos(int scrollPos) {
        this.scrollPos = scrollPos;
    }

    public void setScrolled(boolean scrolled) {
        this.isScrolled = scrolled;
    }
}

