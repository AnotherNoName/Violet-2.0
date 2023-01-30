/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.tree;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class CommandNode<S>
implements Comparable<CommandNode<S>> {
    private final Map<String, CommandNode<S>> children = new LinkedHashMap<String, CommandNode<S>>();
    private final Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap<String, LiteralCommandNode<S>>();
    private final Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap();
    private final Predicate<S> requirement;
    private final CommandNode<S> redirect;
    private final RedirectModifier<S> modifier;
    private final boolean forks;
    private Command<S> command;

    protected CommandNode(Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
        this.command = command;
        this.requirement = requirement;
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public Collection<CommandNode<S>> getChildren() {
        return this.children.values();
    }

    public CommandNode<S> getChild(String name) {
        return this.children.get(name);
    }

    public CommandNode<S> getRedirect() {
        return this.redirect;
    }

    public RedirectModifier<S> getRedirectModifier() {
        return this.modifier;
    }

    public boolean canUse(S source) {
        return this.requirement.test(source);
    }

    public void addChild(CommandNode<S> node) {
        if (node instanceof RootCommandNode) {
            throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
        }
        CommandNode<S> child = this.children.get(node.getName());
        if (child != null) {
            if (node.getCommand() != null) {
                child.command = node.getCommand();
            }
            for (CommandNode<S> grandchild : node.getChildren()) {
                child.addChild(grandchild);
            }
        } else {
            this.children.put(node.getName(), node);
            if (node instanceof LiteralCommandNode) {
                this.literals.put(node.getName(), (LiteralCommandNode)node);
            } else if (node instanceof ArgumentCommandNode) {
                this.arguments.put(node.getName(), (ArgumentCommandNode)node);
            }
        }
    }

    public void findAmbiguities(AmbiguityConsumer<S> consumer) {
        HashSet<String> matches = new HashSet<String>();
        for (CommandNode<S> child : this.children.values()) {
            for (CommandNode<S> sibling : this.children.values()) {
                if (child == sibling) continue;
                for (String input : child.getExamples()) {
                    if (!sibling.isValidInput(input)) continue;
                    matches.add(input);
                }
                if (matches.size() <= 0) continue;
                consumer.ambiguous(this, child, sibling, matches);
                matches = new HashSet();
            }
            child.findAmbiguities(consumer);
        }
    }

    protected abstract boolean isValidInput(String var1);

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandNode)) {
            return false;
        }
        CommandNode that = (CommandNode)o;
        if (!this.children.equals(that.children)) {
            return false;
        }
        return !(this.command != null ? !this.command.equals(that.command) : that.command != null);
    }

    public int hashCode() {
        return 31 * this.children.hashCode() + (this.command != null ? this.command.hashCode() : 0);
    }

    public Predicate<S> getRequirement() {
        return this.requirement;
    }

    public abstract String getName();

    public abstract String getUsageText();

    public abstract void parse(StringReader var1, CommandContextBuilder<S> var2) throws CommandSyntaxException;

    public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) throws CommandSyntaxException;

    public abstract ArgumentBuilder<S, ?> createBuilder();

    protected abstract String getSortedKey();

    public Collection<? extends CommandNode<S>> getRelevantNodes(StringReader input) {
        if (this.literals.size() > 0) {
            int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') {
                input.skip();
            }
            String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            LiteralCommandNode<S> literal = this.literals.get(text);
            if (literal != null) {
                return Collections.singleton(literal);
            }
            return this.arguments.values();
        }
        return this.arguments.values();
    }

    @Override
    public int compareTo(CommandNode<S> o) {
        if (this instanceof LiteralCommandNode == o instanceof LiteralCommandNode) {
            return this.getSortedKey().compareTo(o.getSortedKey());
        }
        return o instanceof LiteralCommandNode ? 1 : -1;
    }

    public boolean isFork() {
        return this.forks;
    }

    public abstract Collection<String> getExamples();
}

