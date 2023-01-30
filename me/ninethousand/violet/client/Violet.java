/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package me.ninethousand.violet.client;

import me.ninethousand.violet.client.impl.managers.CommandManager;
import me.ninethousand.violet.client.impl.managers.EventManager;
import me.ninethousand.violet.client.impl.managers.FontManager;
import me.ninethousand.violet.client.impl.managers.MessageManager;
import me.ninethousand.violet.client.impl.managers.ModuleManager;
import me.ninethousand.violet.client.impl.managers.MultiThreadManager;
import me.ninethousand.violet.client.impl.managers.RotationManager;
import me.ninethousand.violet.client.impl.managers.ThemeManager;
import me.ninethousand.violet.client.impl.managers.TranslationManager;
import me.ninethousand.violet.client.impl.managers.WindowManager;
import me.ninethousand.violet.client.util.client.Config;
import me.ninethousand.violet.loader.launch.LaunchClass;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Violet
implements LaunchClass {
    public static final String MOD_NAME = "Violet";
    public static final String VERSION = "2.0";
    public static final Logger CLIENT_LOGGER = LogManager.getLogger((String)"violet");
    private static Violet instance;

    public Violet() {
        CLIENT_LOGGER.info("Welcome to Violet v2.0");
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        CLIENT_LOGGER.info("Beginning initialisation process...");
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        Config.load();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Config.save()));
        MultiThreadManager.get();
        WindowManager.get();
        EventManager.get();
        CommandManager.get();
        ModuleManager.get();
        MessageManager.get();
        FontManager.get();
        ThemeManager.get();
        RotationManager.get();
        TranslationManager.get();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        CLIENT_LOGGER.info("Finishing initialisation process...");
        MultiThreadManager.get().startThread();
    }

    public static Violet getClientInstance() {
        if (instance == null) {
            instance = new Violet();
        }
        return instance;
    }
}

