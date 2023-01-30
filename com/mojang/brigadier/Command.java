/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface Command<S> {
    public static final int SINGLE_SUCCESS = 1;

    public int run(CommandContext<S> var1) throws CommandSyntaxException;
}

