//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.stream.JsonReader
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.input.Keyboard
 */
package me.ninethousand.violet.client.util.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.impl.managers.ModuleManager;
import me.ninethousand.violet.client.util.client.FriendUtil;
import me.ninethousand.violet.client.util.misc.Bind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Config {
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, "violet-rewrite");
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String currentConfig = "default";
    private static String currentTheme = "default";
    private static File configDir = null;
    private static File currentConfigDir = null;
    private static File themeDir = null;
    private static File currentThemeDir = null;

    public static void load() {
        Config.loadConfig("default");
        FriendUtil.init();
    }

    public static void save() {
        Config.saveConfig(currentConfig);
        FriendUtil.save();
    }

    public static void loadConfig(String name) {
        if (!DIRECTORY.exists() || DIRECTORY.isFile()) {
            DIRECTORY.delete();
            DIRECTORY.mkdir();
            return;
        }
        configDir = new File(DIRECTORY, "configs");
        if (!configDir.exists() || configDir.isFile()) {
            configDir.delete();
            configDir.mkdir();
        }
        if (!(currentConfigDir = new File(configDir, currentConfig = name)).exists() || currentConfigDir.isFile()) {
            currentConfigDir.delete();
            currentConfigDir.mkdir();
        }
        for (Module module : ModuleManager.get().getModules()) {
            File file;
            File categoryDir = new File(currentConfigDir, module.getCategory().name().toLowerCase());
            if (!categoryDir.exists() || !categoryDir.isDirectory()) {
                categoryDir.delete();
                categoryDir.mkdir();
            }
            if (!(file = new File(categoryDir, module.getName() + ".json")).exists()) {
                try {
                    file.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JsonElement element = null;
            try {
                element = new JsonParser().parse(new JsonReader((Reader)new FileReader(file)));
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (!(element instanceof JsonObject)) continue;
            JsonObject object = element.getAsJsonObject();
            for (SettingContainer<?> value : module.getSettingContainers()) {
                Config.fromJsonObject(value, object);
            }
            if (!object.has("Enabled")) continue;
            module.setEnabled(object.get("Enabled").getAsBoolean());
        }
    }

    public static void saveConfig(String name) {
        if (!DIRECTORY.exists() || DIRECTORY.isFile()) {
            DIRECTORY.delete();
            DIRECTORY.mkdir();
        }
        if (!(configDir = new File(DIRECTORY, "configs")).exists() || configDir.isFile()) {
            configDir.delete();
            configDir.mkdir();
        }
        if (!(currentConfigDir = new File(configDir, currentConfig = name)).exists() || currentConfigDir.isFile()) {
            currentConfigDir.delete();
            currentConfigDir.mkdir();
        }
        for (Module module : ModuleManager.get().getModules()) {
            File file;
            File categoryDir = new File(currentConfigDir, module.getCategory().name().toLowerCase());
            if (!categoryDir.exists() || !categoryDir.isDirectory()) {
                categoryDir.delete();
                categoryDir.mkdir();
            }
            if (!(file = new File(categoryDir, module.getName() + ".json")).exists()) {
                try {
                    file.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JsonObject object = new JsonObject();
            for (SettingContainer<?> value : module.getSettingContainers()) {
                object.add(value.getName(), Config.toJsonElement(value));
            }
            object.add("Enabled", (JsonElement)new JsonPrimitive(Boolean.valueOf(module.isEnabled())));
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(GSON.toJson((JsonElement)object));
                fw.flush();
                fw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadTheme(String name) {
        if (!DIRECTORY.exists() || DIRECTORY.isFile()) {
            DIRECTORY.delete();
            DIRECTORY.mkdir();
            return;
        }
        themeDir = new File(DIRECTORY, "themes");
        if (!themeDir.exists() || themeDir.isFile()) {
            themeDir.delete();
            themeDir.mkdir();
        }
        currentTheme = name;
        currentThemeDir = new File(themeDir, currentTheme);
    }

    private static JsonElement toJsonElement(SettingContainer<?> value) {
        if (value.getValue() instanceof Bind) {
            return new JsonPrimitive(Keyboard.getKeyName((int)((Bind)value.getValue()).getKey()));
        }
        if (value.getValue() instanceof Boolean) {
            return new JsonPrimitive((Boolean)value.getValue());
        }
        if (value.getValue() instanceof Color) {
            JsonObject object = new JsonObject();
            Color color = (Color)value.getValue();
            object.add("Hex Code", (JsonElement)new JsonPrimitive(String.format("#%06X", color.getRGB() & 0xFFFFFF)));
            object.add("Alpha", (JsonElement)new JsonPrimitive((Number)color.getAlpha()));
            return object;
        }
        if (value.getValue() instanceof Enum) {
            return new JsonPrimitive(value.getValue().toString());
        }
        if (value.getValue() instanceof Number) {
            return new JsonPrimitive((Number)value.getValue());
        }
        return JsonNull.INSTANCE;
    }

    private static void fromJsonObject(SettingContainer<?> value, JsonObject object) {
        if (!object.has(value.getName())) {
            return;
        }
        if (value.getValue() instanceof Bind) {
            ((Bind)value.getValue()).setKey(Keyboard.getKeyIndex((String)object.get(value.getName()).getAsString()));
        }
        if (value.getValue() instanceof Boolean) {
            value.setValue(object.get(value.getName()).getAsBoolean());
        }
        if (value.getValue() instanceof Color) {
            JsonObject object1 = object.get(value.getName()).getAsJsonObject();
            Color rgb = Color.decode(object1.get("Hex Code").getAsString());
            value.setValue(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), object1.get("Alpha").getAsInt()));
        }
        if (value.getValue() instanceof Double) {
            value.setValue(object.get(value.getName()).getAsDouble());
        }
        if (value.getValue() instanceof Enum) {
            Enum[] enumArray;
            SettingContainer<?> enumValue = value;
            String enumName = object.get(value.getName()).getAsString();
            for (Enum e : enumArray = (Enum[])((Enum)enumValue.getValue()).getDeclaringClass().getEnumConstants()) {
                if (!e.name().equalsIgnoreCase(enumName)) continue;
                enumValue.setValue(e);
                break;
            }
        }
        if (value.getValue() instanceof Float) {
            value.setValue(Float.valueOf(object.get(value.getName()).getAsFloat()));
        }
        if (value.getValue() instanceof Integer) {
            value.setValue(object.get(value.getName()).getAsInt());
        }
    }

    private Config() {
        throw new UnsupportedOperationException();
    }
}

