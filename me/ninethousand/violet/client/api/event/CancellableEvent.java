/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.event;

public class CancellableEvent {
    private boolean cancelled = false;

    protected CancellableEvent() {
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}

