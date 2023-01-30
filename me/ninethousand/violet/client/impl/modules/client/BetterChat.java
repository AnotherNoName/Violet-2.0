//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer$EnumChatVisibility
 *  net.minecraft.util.math.MathHelper
 */
package me.ninethousand.violet.client.impl.modules.client;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.animation.Animation;
import me.ninethousand.violet.client.api.render.animation.motion.Motion;
import me.ninethousand.violet.client.api.render.misc.ColorUtil;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.RenderChatEvent;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

@Module.Manifest(value=Module.Category.CLIENT, description="Make chat look cool")
public class BetterChat
extends Module {
    @Setting(category="Font")
    private boolean customFont = true;
    @Setting(category="Background")
    private boolean customBackground = false;
    @Setting(category="Background")
    private Color customBackgroundColor = new Color(0x3A3A3A);
    @Setting(category="Background")
    private boolean customBackgroundAlpha = false;
    @Setting(category="Timestamp")
    private boolean timestamp = true;
    @Setting(category="Animation")
    private boolean animatedChat = true;
    @Setting(category="Animation")
    private ChatAnimation animationMode = ChatAnimation.Cubic;
    @Setting(category="Animation", min=1.0, max=1000.0)
    private int animationSpeed = 200;
    private Map<ChatLine, Animation> chatLineAnimateMap;
    private static BetterChat instance;

    @Override
    protected void onEnable() {
        this.chatLineAnimateMap = new ConcurrentHashMap<ChatLine, Animation>();
    }

    @Override
    protected void onDisable() {
        this.chatLineAnimateMap = null;
    }

    @Listener
    private void listen(RenderChatEvent event) {
        event.cancel();
        if (Constants.mc.gameSettings.chatVisibility == EntityPlayer.EnumChatVisibility.HIDDEN) {
            return;
        }
        int lineCount = event.getChatGui().getLineCount();
        int drawnLineCount = event.getDrawnChatLines().size();
        if (drawnLineCount <= 0) {
            return;
        }
        float opacity = Constants.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
        float scale = event.getChatGui().getChatScale();
        boolean open = event.getChatGui().getChatOpen();
        int k = MathHelper.ceil((float)((float)event.getChatGui().getChatWidth() / scale));
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)2.0f, (float)8.0f, (float)0.0f);
        GlStateManager.scale((float)scale, (float)scale, (float)1.0f);
        for (int i = 0; i + event.getScrollPos() < drawnLineCount && i < lineCount; ++i) {
            Animation animate;
            ChatLine chatline = event.getDrawnChatLines().get(i + event.getScrollPos());
            if (chatline == null) continue;
            if (this.chatLineAnimateMap == null) {
                this.chatLineAnimateMap = new ConcurrentHashMap<ChatLine, Animation>();
            }
            if (!this.chatLineAnimateMap.containsKey(chatline)) {
                animate = new Animation().setMotion(this.animationMode.getEasing()).setMin(-2.0f).setMax(100.0f).setReversed(false).setSpeed(this.animationSpeed);
                this.chatLineAnimateMap.put(chatline, animate);
            } else {
                animate = this.chatLineAnimateMap.get(chatline);
            }
            animate.update();
            int ticksExisted = event.getUpdateCounter() - chatline.getUpdatedCounter();
            if (ticksExisted >= 200 && !open) continue;
            double d0 = (double)ticksExisted / 200.0;
            d0 = 1.0 - d0;
            d0 *= 10.0;
            d0 = MathHelper.clamp((double)d0, (double)0.0, (double)1.0);
            d0 *= d0;
            int l1 = (int)(255.0 * d0);
            if (open) {
                l1 = 255;
            }
            if ((l1 = (int)((float)l1 * opacity)) <= 3) continue;
            String s = chatline.getChatComponent().getFormattedText();
            String timeStamp = this.timestamp ? DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) : "";
            int j2 = -i * (this.customFont ? FontManager.get().getHeight(s) + 2 : 9);
            int x = this.animatedChat ? (int)(-animate.getMax() + animate.getValue()) : -2;
            int width = k + 4;
            animate.setMax(width);
            Color backgroundColor = ColorUtil.getColorWithAlpha(this.customBackgroundColor, this.customBackgroundAlpha ? new Color(l1 / 2 << 24).getAlpha() : this.customBackgroundColor.getAlpha());
            Gui.drawRect((int)x, (int)(j2 - 9), (int)(x + width), (int)j2, (int)(this.customBackground ? backgroundColor.getRGB() : l1 / 2 << 24));
            GlStateManager.enableBlend();
            if (this.customFont) {
                FontManager.get().drawString(timeStamp, x + 2, j2 - 8, new Color(0xFFFFFF + (l1 << 24)));
                FontManager.get().drawString(s, x + 2 + FontManager.get().getWidth(timeStamp), j2 - 8, new Color(0xFFFFFF + (l1 << 24)));
            } else {
                Constants.mc.fontRendererObj.drawStringWithShadow(timeStamp, (float)(x + 2), (float)(j2 - 8), 0xFFFFFF + (l1 << 24));
                Constants.mc.fontRendererObj.drawStringWithShadow(s, (float)(x + 2 + Constants.mc.fontRendererObj.getStringWidth(timeStamp)), (float)(j2 - 8), 0xFFFFFF + (l1 << 24));
            }
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }

    public static BetterChat getInstance() {
        if (instance == null) {
            instance = new BetterChat();
        }
        return instance;
    }

    public static enum ChatAnimation {
        Linear(Motion.LINEAR),
        Quadratic(Motion.QUAD_IN_OUT),
        Cubic(Motion.CUBIC_IN_OUT),
        Quartic(Motion.QUARTIC_IN_OUT),
        Bounce(Motion.BOUNCE_IN),
        Circular(Motion.CIRC_IN_OUT),
        Elastic(Motion.ELASTIC_IN_OUT),
        Sine(Motion.SINE_IN_OUT);

        private Motion easing;

        private ChatAnimation(Motion easing) {
            this.easing = easing;
        }

        public Motion getEasing() {
            return this.easing;
        }
    }
}

