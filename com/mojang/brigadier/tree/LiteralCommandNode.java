/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class LiteralCommandNode<S>
extends CommandNode<S> {
    private final String literal;
    private final String literalLowerCase;

    public LiteralCommandNode(String literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
        super(command, requirement, redirect, modifier, forks);
        this.literal = literal;
        this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override
    public String getName() {
        return this.literal;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        int start = reader.getCursor();
        int end = this.parse(reader);
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, this.literal);
    }

    private int parse(StringReader reader) {
        int start = reader.getCursor();
        if (reader.canRead(this.literal.length())) {
            int end = start + this.literal.length();
            if (reader.getString().substring(start, end).equals(this.literal)) {
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                }
                reader.setCursor(start);
            }
        }
        return -1;
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (this.literalLowerCase.startsWith(builder.getRemainingLowerCase())) {
            return builder.suggest(this.literal).buildFuture();
        }
        return Suggestions.empty();
    }

    @Override
    public boolean isValidInput(String input) {
        return this.parse(new StringReader(input)) > -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LiteralCommandNode)) {
            return false;
        }
        LiteralCommandNode that = (LiteralCommandNode)o;
        if (!this.literal.equals(that.literal)) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public String getUsageText() {
        return this.literal;
    }

    @Override
    public int hashCode() {
        int result = this.literal.hashCode();
        result = 31 * result + super.hashCode();
        return result;
    }

    public LiteralArgumentBuilder<S> createBuilder() {
        LiteralArgumentBuilder builder = LiteralArgumentBuilder.literal(this.literal);
        builder.requires(this.getRequirement());
        builder.forward(this.getRedirect(), this.getRedirectModifier(), this.isFork());
        if (this.getCommand() != null) {
            builder.executes(this.getCommand());
        }
        return builder;
    }

    @Override
    protected String getSortedKey() {
        return this.literal;
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singleton(this.literal);
    }

    public String toString() {
        return "<literal " + this.literal + ">";
    }
}

