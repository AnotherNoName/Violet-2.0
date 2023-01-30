/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraftforge.fml.relauncher.FMLLaunchHandler
 */
package me.ninethousand.violet.loader.launch.mixin;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.ninethousand.violet.loader.ModInitializer;
import me.ninethousand.violet.loader.launch.Loader;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin
implements IMixinConfigPlugin {
    private List<String> mixins = new ArrayList<String>();

    @Override
    public void onLoad(String mixinPackage) {
        try {
            Launch.classLoader.addURL(Loader.getLoader().getRefmap().toURI().toURL());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Loader.getLoader().getMixins().forEach(mixin -> {
            this.mixins.add((String)mixin);
            if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
                try {
                    Launch.classLoader.loadClass(mixin);
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public String getRefMapperConfig() {
        try {
            ModInitializer.LOADER_LOGGER.info("Setting up refmap config");
            return Loader.getLoader().getRefmap().toURI().toURL().toString();
        }
        catch (MalformedURLException e) {
            ModInitializer.LOADER_LOGGER.info("Failed to setup refmap config");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return this.mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}

