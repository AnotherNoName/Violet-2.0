/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DynamicNCommandExceptionType
implements CommandExceptionType {
    private final Function function;

    public DynamicNCommandExceptionType(Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object a, Object ... args) {
        return new CommandSyntaxException(this, this.function.apply(args));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object ... args) {
        return new CommandSyntaxException(this, this.function.apply(args), reader.getString(), reader.getCursor());
    }

    public static interface Function {
        public Message apply(Object[] var1);
    }
}

