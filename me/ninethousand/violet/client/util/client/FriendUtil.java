//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.stream.JsonReader
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  javax.annotation.Nonnull
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.ninethousand.violet.client.util.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import me.ninethousand.violet.client.impl.managers.MessageManager;
import me.ninethousand.violet.client.util.client.Config;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;
import me.ninethousand.violet.client.util.misc.EnumChangeResult;
import me.ninethousand.violet.client.util.misc.NameUtil;
import net.minecraft.entity.player.EntityPlayer;

public class FriendUtil {
    private static final File JSON_FILE = new File(Config.DIRECTORY, "amigos.json");
    public static final List<UUID> FRIENDS = new ArrayList<UUID>();

    public static void init() {
        JsonArray jsonArray;
        if (!JSON_FILE.exists() || !JSON_FILE.isFile()) {
            try {
                JSON_FILE.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            jsonArray = new JsonParser().parse(new JsonReader((Reader)new FileReader(JSON_FILE))).getAsJsonArray();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        jsonArray.forEach(element -> FriendUtil.addFriend(UUID.fromString(element.getAsString())));
    }

    public static void save() {
        if (!JSON_FILE.exists() || !JSON_FILE.isFile()) {
            try {
                JSON_FILE.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonArray jsonArray = new JsonArray();
        FRIENDS.forEach(uuid -> jsonArray.add(uuid.toString()));
        try {
            FileWriter fileWriter = new FileWriter(JSON_FILE);
            fileWriter.write(Config.GSON.toJson((JsonElement)jsonArray));
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean importFutureFriendList() {
        JsonElement element;
        File file = new File(System.getProperty("user.home") + "\\Future\\friends.json");
        if (!file.exists()) {
            String msg = new MessageBuilder().text("Future friend file does not exist!").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
            MessageManager.get().chatNotify(msg, FriendUtil.class.hashCode());
            return false;
        }
        try {
            element = new JsonParser().parse(new JsonReader((Reader)new FileReader(file)));
        }
        catch (FileNotFoundException e) {
            return false;
        }
        for (JsonElement jsonElement : element.getAsJsonArray()) {
            if (!(jsonElement instanceof JsonObject)) continue;
            JsonObject object = jsonElement.getAsJsonObject();
            String msg = new MessageBuilder().text("Added Friend: " + object.get("friend-label").getAsString()).format(MessageBuilder.createFormat(ChatFormatting.GREEN)).append().getAsString();
            MessageManager.get().chatNotify(msg, FriendUtil.class.hashCode());
            FriendUtil.addFriend(object.get("friend-label").getAsString());
        }
        String msg = new MessageBuilder().text("Imported future friends!").format(MessageBuilder.createFormat(ChatFormatting.GREEN)).append().getAsString();
        MessageManager.get().chatNotify(msg, FriendUtil.class.hashCode());
        return true;
    }

    public static EnumChangeResult addFriend(String name) {
        UUID uuid = NameUtil.getUUIDFromName(name);
        if (uuid == null) {
            return EnumChangeResult.FAIL;
        }
        return FriendUtil.addFriend(uuid);
    }

    public static EnumChangeResult addFriend(@Nonnull UUID friend) {
        if (!FRIENDS.contains(friend)) {
            FRIENDS.add(friend);
            return EnumChangeResult.SUCCESS;
        }
        return EnumChangeResult.NO_CHANGE;
    }

    public static EnumChangeResult removeFriend(String name) {
        UUID uuid = NameUtil.getUUIDFromName(name);
        if (uuid == null) {
            return EnumChangeResult.FAIL;
        }
        return FriendUtil.removeFriend(uuid);
    }

    public static EnumChangeResult removeFriend(@Nonnull UUID friend) {
        return FRIENDS.remove(friend) ? EnumChangeResult.SUCCESS : EnumChangeResult.NO_CHANGE;
    }

    public static boolean toggleFriend(@Nonnull UUID friend) {
        if (FRIENDS.contains(friend)) {
            FriendUtil.removeFriend(friend);
            return false;
        }
        FriendUtil.addFriend(friend);
        return true;
    }

    public static boolean isFriend(EntityPlayer player) {
        for (UUID friend : FRIENDS) {
            if (!NameUtil.getNameFromUUID(friend).equals(player.getName())) continue;
            return true;
        }
        return false;
    }

    private FriendUtil() {
        throw new UnsupportedOperationException();
    }
}

