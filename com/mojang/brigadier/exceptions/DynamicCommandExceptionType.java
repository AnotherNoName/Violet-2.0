/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;

public class DynamicCommandExceptionType
implements CommandExceptionType {
    private final Function<Object, Message> function;

    public DynamicCommandExceptionType(Function<Object, Message> function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object arg) {
        return new CommandSyntaxException(this, this.function.apply(arg));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object arg) {
        return new CommandSyntaxException(this, this.function.apply(arg), reader.getString(), reader.getCursor());
    }
}

