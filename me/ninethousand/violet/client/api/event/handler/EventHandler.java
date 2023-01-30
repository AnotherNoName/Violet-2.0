/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.event.handler;

public interface EventHandler {
    public void register(Object var1);

    public void unregister(Object var1);

    public void register(Class<?> var1);

    public void unregister(Class<?> var1);

    public boolean isRegistered(Object var1);

    public boolean isRegistered(Class<?> var1);

    public void attach(EventHandler var1);

    public void detach(EventHandler var1);

    public boolean isAttached(EventHandler var1);

    public <T> void post(T var1);
}

