/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.loader.launch.mixin;

import me.ninethousand.violet.loader.ModInitializer;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class MixinConfigLoader {
    private final String configName;
    private final String[] configFiles;
    private static MixinConfigLoader LOADER_CONFIG;
    private static MixinConfigLoader DEV_CONFIG;

    public MixinConfigLoader(String configName, String ... configFiles) {
        this.configName = configName;
        this.configFiles = configFiles;
    }

    public void loadConfig() {
        ModInitializer.LOADER_LOGGER.info("Loading Mixin Config: " + this.configName);
        MixinBootstrap.init();
        Mixins.addConfigurations(this.configFiles);
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    public static MixinConfigLoader getLoaderConfig() {
        if (LOADER_CONFIG == null) {
            LOADER_CONFIG = new MixinConfigLoader("Loader", "mixins.loader.json");
        }
        return LOADER_CONFIG;
    }

    public static MixinConfigLoader getDevConfig() {
        if (DEV_CONFIG == null) {
            DEV_CONFIG = new MixinConfigLoader("Dev", "mixins.violet.json");
        }
        return DEV_CONFIG;
    }
}

