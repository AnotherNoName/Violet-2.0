/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;

public class ChatEvent
extends CancellableEvent {
    private String message;

    private ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Outgoing
    extends ChatEvent {
        public Outgoing(String message) {
            super(message);
        }
    }

    public static class Incoming
    extends ChatEvent {
        public Incoming(String message) {
            super(message);
        }
    }
}

