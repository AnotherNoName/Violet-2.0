/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package me.ninethousand.violet.loader;

import me.ninethousand.violet.loader.launch.LaunchClass;
import me.ninethousand.violet.loader.launch.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid="violet", name="Violet", version="2.0")
public class ModInitializer {
    public static final String MOD_ID = "violet";
    public static final String MOD_NAME = "Violet";
    public static final String VERSION = "2.0";
    public static final LaunchEnvironment ENV = LaunchEnvironment.DEV;
    public static final Logger LOADER_LOGGER = LogManager.getLogger((String)"violet-loader");
    private LaunchClass launchClass = Loader.getLoader().getLaunchClass();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Hello moon");
        if (this.launchClass != null) {
            this.launchClass.onPreInit(event);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (this.launchClass != null) {
            this.launchClass.onInit(event);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (this.launchClass != null) {
            this.launchClass.onPostInit(event);
        }
    }

    public static enum LaunchEnvironment {
        DEV,
        CLIENT;

    }
}

