/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.event.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Listener {
    public int priority() default 0;
}

