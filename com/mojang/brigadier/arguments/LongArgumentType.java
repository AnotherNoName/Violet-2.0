/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;

public class LongArgumentType
implements ArgumentType<Long> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");
    private final long minimum;
    private final long maximum;

    private LongArgumentType(long minimum, long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static LongArgumentType longArg() {
        return LongArgumentType.longArg(Long.MIN_VALUE);
    }

    public static LongArgumentType longArg(long min) {
        return LongArgumentType.longArg(min, Long.MAX_VALUE);
    }

    public static LongArgumentType longArg(long min, long max) {
        return new LongArgumentType(min, max);
    }

    public static long getLong(CommandContext<?> context, String name) {
        return context.getArgument(name, Long.TYPE);
    }

    public long getMinimum() {
        return this.minimum;
    }

    public long getMaximum() {
        return this.maximum;
    }

    @Override
    public Long parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        long result = reader.readLong();
        if (result < this.minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow().createWithContext(reader, result, this.minimum);
        }
        if (result > this.maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh().createWithContext(reader, result, this.maximum);
        }
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongArgumentType)) {
            return false;
        }
        LongArgumentType that = (LongArgumentType)o;
        return this.maximum == that.maximum && this.minimum == that.minimum;
    }

    public int hashCode() {
        return 31 * Long.hashCode(this.minimum) + Long.hashCode(this.maximum);
    }

    public String toString() {
        if (this.minimum == Long.MIN_VALUE && this.maximum == Long.MAX_VALUE) {
            return "longArg()";
        }
        if (this.maximum == Long.MAX_VALUE) {
            return "longArg(" + this.minimum + ")";
        }
        return "longArg(" + this.minimum + ", " + this.maximum + ")";
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

