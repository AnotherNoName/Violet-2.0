/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumHand
 */
package me.ninethousand.violet.client.impl.events;

import net.minecraft.util.EnumHand;

public class SwingHandEvent {
    private EnumHand hand;

    public SwingHandEvent(EnumHand hand) {
        this.hand = hand;
    }

    public EnumHand getHand() {
        return this.hand;
    }

    public void setHand(EnumHand hand) {
        this.hand = hand;
    }
}

