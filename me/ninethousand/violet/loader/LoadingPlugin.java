/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin$MCVersion
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin$Name
 */
package me.ninethousand.violet.loader;

import java.util.Map;
import javax.annotation.Nullable;
import me.ninethousand.violet.client.Violet;
import me.ninethousand.violet.loader.ModInitializer;
import me.ninethousand.violet.loader.launch.Loader;
import me.ninethousand.violet.loader.launch.mixin.MixinConfigLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value="1.12.2")
@IFMLLoadingPlugin.Name(value="violet")
public class LoadingPlugin
implements IFMLLoadingPlugin {
    public LoadingPlugin() {
        ModInitializer.LOADER_LOGGER.info("Launching, Mode : {}", (Object)ModInitializer.ENV);
        switch (ModInitializer.ENV) {
            case DEV: {
                this.devLoad();
                break;
            }
            case CLIENT: {
                this.clientLoad();
            }
        }
    }

    private void devLoad() {
        Loader.getLoader().setDefaultLaunchClass(Violet.class);
        MixinConfigLoader.getDevConfig().loadConfig();
    }

    private void clientLoad() {
        try {
            Loader.getLoader().launch("https://drive.google.com/uc?export=download&id=1dUm4TmiX4WgKcN1tMaU-j8i-Y59O5_gt");
        }
        catch (Exception e) {
            e.printStackTrace();
            ModInitializer.LOADER_LOGGER.info("Failed to launch");
        }
    }

    public String[] getASMTransformerClass() {
        return null;
    }

    public String getModContainerClass() {
        return null;
    }

    @Nullable
    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public static void message(String message) {
        System.out.println("[Violet Core] " + message);
    }
}

