/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.ninethousand.violet.client.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ninethousand.violet.client.api.command.VioletCommand;
import me.ninethousand.violet.client.impl.managers.MessageManager;
import me.ninethousand.violet.client.impl.managers.MultiThreadManager;
import me.ninethousand.violet.client.util.client.FriendUtil;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;
import me.ninethousand.violet.client.util.misc.NameUtil;

public class FriendCommand
implements VioletCommand {
    @Override
    public void populate(CommandDispatcher<Object> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)LiteralArgumentBuilder.literal("friend").then(((RequiredArgumentBuilder)RequiredArgumentBuilder.argument("command", StringArgumentType.string()).then(RequiredArgumentBuilder.argument("name", StringArgumentType.string()).executes(c -> {
            String command = StringArgumentType.getString(c, "command").toLowerCase();
            String name = StringArgumentType.getString(c, "name");
            switch (command) {
                case "add": {
                    switch (FriendUtil.addFriend(name)) {
                        case FAIL: {
                            String msg = new MessageBuilder().text("Could not find player: '" + name + "'.").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
                            MessageManager.get().chatNotify(msg, this.hashCode());
                            return 2;
                        }
                        case SUCCESS: {
                            String msg = new MessageBuilder().text("Added player: '" + NameUtil.getNameFromUUID(NameUtil.getUUIDFromName(name)) + "' as a friend.").format(MessageBuilder.createFormat(ChatFormatting.GREEN)).append().getAsString();
                            MessageManager.get().chatNotify(msg, this.hashCode());
                            return 1;
                        }
                        case NO_CHANGE: {
                            String msg = new MessageBuilder().text("Player: '" + NameUtil.getNameFromUUID(NameUtil.getUUIDFromName(name)) + "' is already a friend.").format(MessageBuilder.createFormat(ChatFormatting.GREEN)).append().getAsString();
                            MessageManager.get().chatNotify(msg, this.hashCode());
                            return 1;
                        }
                    }
                }
                case "del": {
                    switch (FriendUtil.removeFriend(name)) {
                        case FAIL: {
                            String msg = new MessageBuilder().text("Could not find player: '" + name + "'.").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
                            MessageManager.get().chatNotify(msg, this.hashCode());
                            return 2;
                        }
                        case SUCCESS: {
                            String msg = new MessageBuilder().text("Player: '" + NameUtil.getNameFromUUID(NameUtil.getUUIDFromName(name)) + "' is no longer a friend.").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
                            MessageManager.get().chatNotify(msg, this.hashCode());
                            return 1;
                        }
                        case NO_CHANGE: {
                            String msg = new MessageBuilder().text("Player: '" + NameUtil.getNameFromUUID(NameUtil.getUUIDFromName(name)) + "' is already not a friend.").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
                            MessageManager.get().chatNotify(msg, this.hashCode());
                            return 1;
                        }
                    }
                }
            }
            String msg = new MessageBuilder().text("Could not recognize parameter: '" + command + "'.").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
            MessageManager.get().chatNotify(msg, this.hashCode());
            return 2;
        }))).executes(c -> {
            String command = StringArgumentType.getString(c, "command");
            if (command.equalsIgnoreCase("list")) {
                StringBuilder builder = new StringBuilder().append("\n").append("Friend list:").append("\n");
                FriendUtil.FRIENDS.forEach(uuid -> builder.append(NameUtil.getNameFromUUID(uuid)).append(", "));
                String msg = new MessageBuilder().text(builder.toString()).format(MessageBuilder.createFormat(ChatFormatting.GREEN)).append().getAsString();
                MessageManager.get().chatNotify(msg, this.hashCode());
                return 1;
            }
            if (command.equalsIgnoreCase("import")) {
                MultiThreadManager.get().addTask(FriendUtil::importFutureFriendList);
            }
            return 2;
        })));
    }
}

