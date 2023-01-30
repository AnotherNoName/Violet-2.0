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

public class StringArgumentType
implements ArgumentType<String> {
    private final StringType type;

    private StringArgumentType(StringType type) {
        this.type = type;
    }

    public static StringArgumentType word() {
        return new StringArgumentType(StringType.SINGLE_WORD);
    }

    public static StringArgumentType string() {
        return new StringArgumentType(StringType.QUOTABLE_PHRASE);
    }

    public static StringArgumentType greedyString() {
        return new StringArgumentType(StringType.GREEDY_PHRASE);
    }

    public static String getString(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    public StringType getType() {
        return this.type;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        if (this.type == StringType.GREEDY_PHRASE) {
            String text = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return text;
        }
        if (this.type == StringType.SINGLE_WORD) {
            return reader.readUnquotedString();
        }
        return reader.readString();
    }

    public String toString() {
        return "string()";
    }

    @Override
    public Collection<String> getExamples() {
        return this.type.getExamples();
    }

    public static String escapeIfRequired(String input) {
        for (char c : input.toCharArray()) {
            if (StringReader.isAllowedInUnquotedString(c)) continue;
            return StringArgumentType.escape(input);
        }
        return input;
    }

    private static String escape(String input) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c == '\\' || c == '\"') {
                result.append('\\');
            }
            result.append(c);
        }
        result.append("\"");
        return result.toString();
    }

    public static enum StringType {
        SINGLE_WORD("word", "words_with_underscores"),
        QUOTABLE_PHRASE("\"quoted phrase\"", "word", "\"\""),
        GREEDY_PHRASE("word", "words with spaces", "\"and symbols\"");

        private final Collection<String> examples;

        private StringType(String ... examples) {
            this.examples = Arrays.asList(examples);
        }

        public Collection<String> getExamples() {
            return this.examples;
        }
    }
}

