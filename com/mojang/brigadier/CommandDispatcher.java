/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandDispatcher<S> {
    public static final String ARGUMENT_SEPARATOR = " ";
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";
    private final RootCommandNode<S> root;
    private final Predicate<CommandNode<S>> hasCommand = new Predicate<CommandNode<S>>(){

        @Override
        public boolean test(CommandNode<S> input) {
            return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand));
        }
    };
    private ResultConsumer<S> consumer = (c, s, r) -> {};

    public CommandDispatcher(RootCommandNode<S> root) {
        this.root = root;
    }

    public CommandDispatcher() {
        this(new RootCommandNode());
    }

    public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command) {
        CommandNode build = command.build();
        this.root.addChild(build);
        return build;
    }

    public void setConsumer(ResultConsumer<S> consumer) {
        this.consumer = consumer;
    }

    public int execute(String input, S source) throws CommandSyntaxException {
        return this.execute(new StringReader(input), source);
    }

    public int execute(StringReader input, S source) throws CommandSyntaxException {
        ParseResults<S> parse = this.parse(input, source);
        return this.execute(parse);
    }

    public int execute(ParseResults<S> parse) throws CommandSyntaxException {
        if (parse.getReader().canRead()) {
            if (parse.getExceptions().size() == 1) {
                throw parse.getExceptions().values().iterator().next();
            }
            if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            }
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
        }
        int result = 0;
        int successfulForks = 0;
        boolean forked = false;
        boolean foundCommand = false;
        String command = parse.getReader().getString();
        CommandContext<S> original = parse.getContext().build(command);
        List<CommandContext<S>> contexts = Collections.singletonList(original);
        ArrayList<CommandContext<S>> next = null;
        while (contexts != null) {
            int size = contexts.size();
            for (int i = 0; i < size; ++i) {
                CommandContext<S> context = contexts.get(i);
                CommandContext<S> child = context.getChild();
                if (child != null) {
                    forked |= context.isForked();
                    if (!child.hasNodes()) continue;
                    foundCommand = true;
                    RedirectModifier<S> modifier = context.getRedirectModifier();
                    if (modifier == null) {
                        if (next == null) {
                            next = new ArrayList<CommandContext<S>>(1);
                        }
                        next.add(child.copyFor(context.getSource()));
                        continue;
                    }
                    try {
                        Collection<S> results = modifier.apply(context);
                        if (results.isEmpty()) continue;
                        if (next == null) {
                            next = new ArrayList(results.size());
                        }
                        for (S source : results) {
                            next.add(child.copyFor(source));
                        }
                        continue;
                    }
                    catch (CommandSyntaxException ex) {
                        this.consumer.onCommandComplete(context, false, 0);
                        if (forked) continue;
                        throw ex;
                    }
                }
                if (context.getCommand() == null) continue;
                foundCommand = true;
                try {
                    int value = context.getCommand().run(context);
                    result += value;
                    this.consumer.onCommandComplete(context, true, value);
                    ++successfulForks;
                    continue;
                }
                catch (CommandSyntaxException ex) {
                    this.consumer.onCommandComplete(context, false, 0);
                    if (forked) continue;
                    throw ex;
                }
            }
            contexts = next;
            next = null;
        }
        if (!foundCommand) {
            this.consumer.onCommandComplete(original, false, 0);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }
        return forked ? successfulForks : result;
    }

    public ParseResults<S> parse(String command, S source) {
        return this.parse(new StringReader(command), source);
    }

    public ParseResults<S> parse(StringReader command, S source) {
        CommandContextBuilder<S> context = new CommandContextBuilder<S>(this, source, this.root, command.getCursor());
        return this.parseNodes(this.root, command, context);
    }

    private ParseResults<S> parseNodes(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar) {
        S source = contextSoFar.getSource();
        LinkedHashMap<CommandNode<S>, CommandSyntaxException> errors = null;
        ArrayList<ParseResults<S>> potentials = null;
        int cursor = originalReader.getCursor();
        for (CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) continue;
            CommandContextBuilder<S> context = contextSoFar.copy();
            StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                }
                catch (RuntimeException ex) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                }
                if (reader.canRead() && reader.peek() != ' ') {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                }
            }
            catch (CommandSyntaxException ex) {
                if (errors == null) {
                    errors = new LinkedHashMap<CommandNode<S>, CommandSyntaxException>();
                }
                errors.put(child, ex);
                reader.setCursor(cursor);
                continue;
            }
            context.withCommand(child.getCommand());
            if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
                reader.skip();
                if (child.getRedirect() != null) {
                    CommandContextBuilder<S> childContext = new CommandContextBuilder<S>(this, source, child.getRedirect(), reader.getCursor());
                    ParseResults<S> parse = this.parseNodes(child.getRedirect(), reader, childContext);
                    context.withChild(parse.getContext());
                    return new ParseResults<S>(context, parse.getReader(), parse.getExceptions());
                }
                ParseResults<S> parse = this.parseNodes(child, reader, context);
                if (potentials == null) {
                    potentials = new ArrayList(1);
                }
                potentials.add(parse);
                continue;
            }
            if (potentials == null) {
                potentials = new ArrayList<ParseResults<S>>(1);
            }
            potentials.add(new ParseResults<S>(context, reader, Collections.emptyMap()));
        }
        if (potentials != null) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return (ParseResults)potentials.get(0);
        }
        return new ParseResults<S>(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }

    public String[] getAllUsage(CommandNode<S> node, S source, boolean restricted) {
        ArrayList<String> result = new ArrayList<String>();
        this.getAllUsage(node, source, result, "", restricted);
        return result.toArray(new String[result.size()]);
    }

    private void getAllUsage(CommandNode<S> node, S source, ArrayList<String> result, String prefix, boolean restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }
        if (node.getCommand() != null) {
            result.add(prefix);
        }
        if (node.getRedirect() != null) {
            String redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
            result.add(prefix.isEmpty() ? node.getUsageText() + ARGUMENT_SEPARATOR + redirect : prefix + ARGUMENT_SEPARATOR + redirect);
        } else if (!node.getChildren().isEmpty()) {
            for (CommandNode<S> child : node.getChildren()) {
                this.getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText(), restricted);
            }
        }
    }

    public Map<CommandNode<S>, String> getSmartUsage(CommandNode<S> node, S source) {
        LinkedHashMap<CommandNode<S>, String> result = new LinkedHashMap<CommandNode<S>, String>();
        boolean optional = node.getCommand() != null;
        for (CommandNode<S> child : node.getChildren()) {
            String usage = this.getSmartUsage(child, source, optional, false);
            if (usage == null) continue;
            result.put(child, usage);
        }
        return result;
    }

    private String getSmartUsage(CommandNode<S> node, S source, boolean optional, boolean deep) {
        String close;
        if (!node.canUse(source)) {
            return null;
        }
        String self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        boolean childOptional = node.getCommand() != null;
        String open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        String string = close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;
        if (!deep) {
            if (node.getRedirect() != null) {
                String redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
                return self + ARGUMENT_SEPARATOR + redirect;
            }
            Collection children = node.getChildren().stream().filter(c -> c.canUse(source)).collect(Collectors.toList());
            if (children.size() == 1) {
                String usage = this.getSmartUsage((CommandNode)children.iterator().next(), source, childOptional, childOptional);
                if (usage != null) {
                    return self + ARGUMENT_SEPARATOR + usage;
                }
            } else if (children.size() > 1) {
                LinkedHashSet<String> childUsage = new LinkedHashSet<String>();
                for (CommandNode child : children) {
                    String usage = this.getSmartUsage(child, source, childOptional, true);
                    if (usage == null) continue;
                    childUsage.add(usage);
                }
                if (childUsage.size() == 1) {
                    String usage = (String)childUsage.iterator().next();
                    return self + ARGUMENT_SEPARATOR + (childOptional ? USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE : usage);
                }
                if (childUsage.size() > 1) {
                    StringBuilder builder = new StringBuilder(open);
                    int count = 0;
                    for (CommandNode child : children) {
                        if (count > 0) {
                            builder.append(USAGE_OR);
                        }
                        builder.append(child.getUsageText());
                        ++count;
                    }
                    if (count > 0) {
                        builder.append(close);
                        return self + ARGUMENT_SEPARATOR + builder.toString();
                    }
                }
            }
        }
        return self;
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse) {
        return this.getCompletionSuggestions(parse, parse.getReader().getTotalLength());
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse, int cursor) {
        CommandContextBuilder<S> context = parse.getContext();
        SuggestionContext<S> nodeBeforeCursor = context.findSuggestionContext(cursor);
        CommandNode parent = nodeBeforeCursor.parent;
        int start = Math.min(nodeBeforeCursor.startPos, cursor);
        String fullInput = parse.getReader().getString();
        String truncatedInput = fullInput.substring(0, cursor);
        String truncatedInputLowerCase = truncatedInput.toLowerCase(Locale.ROOT);
        CompletableFuture[] futures = new CompletableFuture[parent.getChildren().size()];
        int i = 0;
        for (CommandNode<S> node : parent.getChildren()) {
            CompletableFuture<Suggestions> future = Suggestions.empty();
            try {
                future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, truncatedInputLowerCase, start));
            }
            catch (CommandSyntaxException commandSyntaxException) {
                // empty catch block
            }
            futures[i++] = future;
        }
        CompletableFuture<Suggestions> result = new CompletableFuture<Suggestions>();
        CompletableFuture.allOf(futures).thenRun(() -> {
            ArrayList<Suggestions> suggestions = new ArrayList<Suggestions>();
            for (CompletableFuture future : futures) {
                suggestions.add((Suggestions)future.join());
            }
            result.complete(Suggestions.merge(fullInput, suggestions));
        });
        return result;
    }

    public RootCommandNode<S> getRoot() {
        return this.root;
    }

    public Collection<String> getPath(CommandNode<S> target) {
        ArrayList<List<CommandNode<S>>> nodes = new ArrayList<List<CommandNode<S>>>();
        this.addPaths(this.root, nodes, new ArrayList<CommandNode<S>>());
        for (List list : nodes) {
            if (list.get(list.size() - 1) != target) continue;
            ArrayList<String> result = new ArrayList<String>(list.size());
            for (CommandNode node : list) {
                if (node == this.root) continue;
                result.add(node.getName());
            }
            return result;
        }
        return Collections.emptyList();
    }

    public CommandNode<S> findNode(Collection<String> path) {
        CommandNode node = this.root;
        for (String name : path) {
            if ((node = node.getChild(name)) != null) continue;
            return null;
        }
        return node;
    }

    public void findAmbiguities(AmbiguityConsumer<S> consumer) {
        this.root.findAmbiguities(consumer);
    }

    private void addPaths(CommandNode<S> node, List<List<CommandNode<S>>> result, List<CommandNode<S>> parents) {
        ArrayList<CommandNode<S>> current = new ArrayList<CommandNode<S>>(parents);
        current.add(node);
        result.add(current);
        for (CommandNode<S> child : node.getChildren()) {
            this.addPaths(child, result, current);
        }
    }
}

