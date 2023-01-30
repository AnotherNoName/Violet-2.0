//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.ninethousand.violet.client.impl.managers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.concurrent.atomic.AtomicReference;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.impl.commands.FriendCommand;
import me.ninethousand.violet.client.impl.events.ChatEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import me.ninethousand.violet.client.impl.managers.MessageManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;

public class CommandManager
implements Manager {
    private final String PREFIX = ";";
    private final CommandDispatcher<Object> dispatcher = new CommandDispatcher();
    private static CommandManager instance;

    private CommandManager() {
        new FriendCommand().populate(this.dispatcher);
        EventManager.get().register(this);
    }

    @Listener
    public void listen(ChatEvent.Outgoing event) {
        if (!event.getMessage().startsWith(";")) {
            return;
        }
        event.cancel();
        ParseResults<Object> parse = this.dispatcher.parse(event.getMessage().substring(1), (Object)this);
        int result = 0;
        try {
            result = this.dispatcher.execute(parse);
        }
        catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        if (result == 0) {
            String commandName = event.getMessage().split(" ")[0];
            AtomicReference<String> chatMessage = new AtomicReference<String>("Command " + commandName + " did not successfully execute.");
            this.dispatcher.getCompletionSuggestions(parse).thenAccept(suggestions -> {
                if (!suggestions.isEmpty()) {
                    String suggestion = suggestions.getList().get(0).apply(event.getMessage());
                    chatMessage.set((String)chatMessage.get() + " Did you mean '" + suggestion.substring(0, suggestion.length() - 1) + "'?");
                }
            });
            String msg = new MessageBuilder().text("Failed to parse command!").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
            MessageManager.get().chatNotify(msg, this.hashCode());
        }
        Constants.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
    }

    public String getPrefix() {
        return ";";
    }

    public static CommandManager get() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }
}

