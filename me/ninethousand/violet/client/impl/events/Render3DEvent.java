/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;

public class Render3DEvent
extends CancellableEvent {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

