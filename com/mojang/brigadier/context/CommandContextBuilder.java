/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.context;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.tree.CommandNode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandContextBuilder<S> {
    private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap();
    private final CommandNode<S> rootNode;
    private final List<ParsedCommandNode<S>> nodes = new ArrayList<ParsedCommandNode<S>>();
    private final CommandDispatcher<S> dispatcher;
    private S source;
    private Command<S> command;
    private CommandContextBuilder<S> child;
    private StringRange range;
    private RedirectModifier<S> modifier = null;
    private boolean forks;

    public CommandContextBuilder(CommandDispatcher<S> dispatcher, S source, CommandNode<S> rootNode, int start) {
        this.rootNode = rootNode;
        this.dispatcher = dispatcher;
        this.source = source;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder<S> withSource(S source) {
        this.source = source;
        return this;
    }

    public S getSource() {
        return this.source;
    }

    public CommandNode<S> getRootNode() {
        return this.rootNode;
    }

    public CommandContextBuilder<S> withArgument(String name, ParsedArgument<S, ?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public Map<String, ParsedArgument<S, ?>> getArguments() {
        return this.arguments;
    }

    public CommandContextBuilder<S> withCommand(Command<S> command) {
        this.command = command;
        return this;
    }

    public CommandContextBuilder<S> withNode(CommandNode<S> node, StringRange range) {
        this.nodes.add(new ParsedCommandNode<S>(node, range));
        this.range = StringRange.encompassing(this.range, range);
        this.modifier = node.getRedirectModifier();
        this.forks = node.isFork();
        return this;
    }

    public CommandContextBuilder<S> copy() {
        CommandContextBuilder<S> copy = new CommandContextBuilder<S>(this.dispatcher, this.source, this.rootNode, this.range.getStart());
        copy.command = this.command;
        copy.arguments.putAll(this.arguments);
        copy.nodes.addAll(this.nodes);
        copy.child = this.child;
        copy.range = this.range;
        copy.forks = this.forks;
        return copy;
    }

    public CommandContextBuilder<S> withChild(CommandContextBuilder<S> child) {
        this.child = child;
        return this;
    }

    public CommandContextBuilder<S> getChild() {
        return this.child;
    }

    public CommandContextBuilder<S> getLastChild() {
        CommandContextBuilder<S> result = this;
        while (result.getChild() != null) {
            result = result.getChild();
        }
        return result;
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public List<ParsedCommandNode<S>> getNodes() {
        return this.nodes;
    }

    public CommandContext<S> build(String input) {
        return new CommandContext<S>(this.source, input, this.arguments, this.command, this.rootNode, this.nodes, this.range, this.child == null ? null : this.child.build(input), this.modifier, this.forks);
    }

    public CommandDispatcher<S> getDispatcher() {
        return this.dispatcher;
    }

    public StringRange getRange() {
        return this.range;
    }

    public SuggestionContext<S> findSuggestionContext(int cursor) {
        if (this.range.getStart() <= cursor) {
            if (this.range.getEnd() < cursor) {
                if (this.child != null) {
                    return this.child.findSuggestionContext(cursor);
                }
                if (!this.nodes.isEmpty()) {
                    ParsedCommandNode<S> last = this.nodes.get(this.nodes.size() - 1);
                    return new SuggestionContext<S>(last.getNode(), last.getRange().getEnd() + 1);
                }
                return new SuggestionContext<S>(this.rootNode, this.range.getStart());
            }
            CommandNode<S> prev = this.rootNode;
            for (ParsedCommandNode<S> node : this.nodes) {
                StringRange nodeRange = node.getRange();
                if (nodeRange.getStart() <= cursor && cursor <= nodeRange.getEnd()) {
                    return new SuggestionContext<S>(prev, nodeRange.getStart());
                }
                prev = node.getNode();
            }
            if (prev == null) {
                throw new IllegalStateException("Can't find node before cursor");
            }
            return new SuggestionContext<S>(prev, this.range.getStart());
        }
        throw new IllegalStateException("Can't find node before cursor");
    }
}

