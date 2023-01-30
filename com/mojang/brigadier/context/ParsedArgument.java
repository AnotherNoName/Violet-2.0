/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.context;

import com.mojang.brigadier.context.StringRange;
import java.util.Objects;

public class ParsedArgument<S, T> {
    private final StringRange range;
    private final T result;

    public ParsedArgument(int start, int end, T result) {
        this.range = StringRange.between(start, end);
        this.result = result;
    }

    public StringRange getRange() {
        return this.range;
    }

    public T getResult() {
        return this.result;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParsedArgument)) {
            return false;
        }
        ParsedArgument that = (ParsedArgument)o;
        return Objects.equals(this.range, that.range) && Objects.equals(this.result, that.result);
    }

    public int hashCode() {
        return Objects.hash(this.range, this.result);
    }
}

