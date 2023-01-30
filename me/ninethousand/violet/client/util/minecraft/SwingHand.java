//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.util.EnumHand
 */
package me.ninethousand.violet.client.util.minecraft;

import me.ninethousand.violet.client.util.Constants;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public enum SwingHand {
    Mainhand(EnumHand.MAIN_HAND),
    Offhand(EnumHand.OFF_HAND),
    None(null);

    private final EnumHand hand;

    private SwingHand(EnumHand hand) {
        this.hand = hand;
    }

    public void swing() {
        this.swing(false);
    }

    public void swing(boolean packet) {
        if (this.hand != null) {
            if (packet) {
                Constants.sendPacket(new CPacketAnimation(this.hand));
            } else {
                Constants.player().swingArm(this.hand);
            }
        }
    }
}

