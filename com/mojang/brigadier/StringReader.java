/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class StringReader
implements ImmutableStringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '\"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';
    private final String string;
    private int cursor;

    public StringReader(StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return this.string;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getRemainingLength() {
        return this.string.length() - this.cursor;
    }

    @Override
    public int getTotalLength() {
        return this.string.length();
    }

    @Override
    public int getCursor() {
        return this.cursor;
    }

    @Override
    public String getRead() {
        return this.string.substring(0, this.cursor);
    }

    @Override
    public String getRemaining() {
        return this.string.substring(this.cursor);
    }

    @Override
    public boolean canRead(int length) {
        return this.cursor + length <= this.string.length();
    }

    @Override
    public boolean canRead() {
        return this.canRead(1);
    }

    @Override
    public char peek() {
        return this.string.charAt(this.cursor);
    }

    @Override
    public char peek(int offset) {
        return this.string.charAt(this.cursor + offset);
    }

    public char read() {
        return this.string.charAt(this.cursor++);
    }

    public void skip() {
        ++this.cursor;
    }

    public static boolean isAllowedNumber(char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == '\"' || c == '\'';
    }

    public void skipWhitespace() {
        while (this.canRead() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
    }

    public int readInt() throws CommandSyntaxException {
        int start = this.cursor;
        while (this.canRead() && StringReader.isAllowedNumber(this.peek())) {
            this.skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
        }
        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException ex) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
        }
    }

    public long readLong() throws CommandSyntaxException {
        int start = this.cursor;
        while (this.canRead() && StringReader.isAllowedNumber(this.peek())) {
            this.skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
        }
        try {
            return Long.parseLong(number);
        }
        catch (NumberFormatException ex) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number);
        }
    }

    public double readDouble() throws CommandSyntaxException {
        int start = this.cursor;
        while (this.canRead() && StringReader.isAllowedNumber(this.peek())) {
            this.skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
        }
        try {
            return Double.parseDouble(number);
        }
        catch (NumberFormatException ex) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number);
        }
    }

    public float readFloat() throws CommandSyntaxException {
        int start = this.cursor;
        while (this.canRead() && StringReader.isAllowedNumber(this.peek())) {
            this.skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
        }
        try {
            return Float.parseFloat(number);
        }
        catch (NumberFormatException ex) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
        }
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        int start = this.cursor;
        while (this.canRead() && StringReader.isAllowedInUnquotedString(this.peek())) {
            this.skip();
        }
        return this.string.substring(start, this.cursor);
    }

    public String readQuotedString() throws CommandSyntaxException {
        if (!this.canRead()) {
            return "";
        }
        char next = this.peek();
        if (!StringReader.isQuotedStringStart(next)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
        }
        this.skip();
        return this.readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (this.canRead()) {
            char c = this.read();
            if (escaped) {
                if (c == terminator || c == '\\') {
                    result.append(c);
                    escaped = false;
                    continue;
                }
                this.setCursor(this.getCursor() - 1);
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
            }
            if (c == '\\') {
                escaped = true;
                continue;
            }
            if (c == terminator) {
                return result.toString();
            }
            result.append(c);
        }
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }

    public String readString() throws CommandSyntaxException {
        if (!this.canRead()) {
            return "";
        }
        char next = this.peek();
        if (StringReader.isQuotedStringStart(next)) {
            this.skip();
            return this.readStringUntil(next);
        }
        return this.readUnquotedString();
    }

    public boolean readBoolean() throws CommandSyntaxException {
        int start = this.cursor;
        String value = this.readString();
        if (value.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
        }
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        this.cursor = start;
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
    }

    public void expect(char c) throws CommandSyntaxException {
        if (!this.canRead() || this.peek() != c) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
        }
        this.skip();
    }
}

