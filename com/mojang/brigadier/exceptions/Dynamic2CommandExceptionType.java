/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class Dynamic2CommandExceptionType
implements CommandExceptionType {
    private final Function function;

    public Dynamic2CommandExceptionType(Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object a, Object b) {
        return new CommandSyntaxException(this, this.function.apply(a, b));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object a, Object b) {
        return new CommandSyntaxException(this, this.function.apply(a, b), reader.getString(), reader.getCursor());
    }

    public static interface Function {
        public Message apply(Object var1, Object var2);
    }
}

