/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.setting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Setting {
    public String name() default "";

    public String description() default "";

    public String category() default "Default";

    public double min() default 0.0;

    public double max() default 10.0;

    public int dp() default 1;
}

