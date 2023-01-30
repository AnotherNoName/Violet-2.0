//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.GuiNewChat
 */
package me.ninethousand.violet.client.mixin;

import java.util.List;
import me.ninethousand.violet.client.impl.events.RenderChatEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiNewChat.class})
public class MixinGuiNewChat {
    @Shadow
    @Final
    private List<String> sentMessages;
    @Shadow
    @Final
    private List<ChatLine> chatLines;
    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;
    @Shadow
    private int scrollPos;
    @Shadow
    private boolean isScrolled;

    @Inject(method={"drawChat"}, at={@At(value="HEAD")}, cancellable=true)
    public void drawChat(int updateCounter, CallbackInfo ci) {
        RenderChatEvent renderChatEvent = new RenderChatEvent((GuiNewChat)this, updateCounter, this.sentMessages, this.chatLines, this.drawnChatLines, this.scrollPos, this.isScrolled);
        EventManager.get().post(renderChatEvent);
        if (renderChatEvent.isCancelled()) {
            ci.cancel();
        }
    }
}

