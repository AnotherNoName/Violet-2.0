/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraft.launchwrapper.LaunchClassLoader
 */
package me.ninethousand.violet.loader.launch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import me.ninethousand.violet.loader.ModInitializer;
import me.ninethousand.violet.loader.launch.LaunchClass;
import me.ninethousand.violet.loader.launch.mixin.MixinConfigLoader;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class Loader {
    private String mixinConfig;
    private String mixinRefmap;
    private String launchClass;
    private Class<? extends LaunchClass> defaultLaunchClass;
    private byte[] mixinBytes;
    private byte[] refmapBytes;
    private static Loader loader;

    public void launch(String url) throws IOException {
        this.loadClasses(new URL(url).openStream());
        this.loadMixins();
    }

    private void setupManifestValues(Manifest manifest) {
        for (Map.Entry<Object, Object> objectObjectEntry : manifest.getMainAttributes().entrySet()) {
            if (objectObjectEntry.getKey().toString().equals("Mixin-Config")) {
                this.mixinConfig = objectObjectEntry.getValue().toString();
                continue;
            }
            if (objectObjectEntry.getKey().toString().equals("Mixin-Refmap")) {
                this.mixinRefmap = objectObjectEntry.getValue().toString();
                continue;
            }
            if (!objectObjectEntry.getKey().toString().equals("LaunchClass")) continue;
            this.launchClass = objectObjectEntry.getValue().toString();
        }
    }

    private void loadClasses(InputStream inputStream) {
        ModInitializer.LOADER_LOGGER.info("Loading classes...");
        try {
            ZipEntry entry;
            Field resourceCacheField = LaunchClassLoader.class.getDeclaredField("resourceCache");
            resourceCacheField.setAccessible(true);
            Map cache = (Map)resourceCacheField.get(Launch.classLoader);
            JarInputStream jar = new JarInputStream(inputStream);
            this.setupManifestValues(jar.getManifest());
            while ((entry = jar.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    cache.put(name.replace('/', '.').replace(".class", ""), this.getBytes(jar));
                    continue;
                }
                if (name.contains(this.mixinConfig)) {
                    this.mixinBytes = this.getBytes(jar);
                    cache.put(this.mixinConfig, this.mixinBytes);
                    continue;
                }
                if (!name.contains(this.mixinRefmap)) continue;
                this.refmapBytes = this.getBytes(jar);
                cache.put(this.mixinRefmap, this.refmapBytes);
            }
            jar.close();
            resourceCacheField.set(Launch.classLoader, cache);
        }
        catch (Exception e) {
            ModInitializer.LOADER_LOGGER.info("Error loading");
            e.printStackTrace();
        }
    }

    public LaunchClass getLaunchClass() {
        try {
            Class<LaunchClass> clazz = Objects.nonNull(this.defaultLaunchClass) ? this.defaultLaunchClass : Class.forName(this.launchClass);
            return clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDefaultLaunchClass(Class<? extends LaunchClass> defaultLaunchClass) {
        this.defaultLaunchClass = defaultLaunchClass;
    }

    private void loadMixins() {
        MixinConfigLoader.getLoaderConfig().loadConfig();
    }

    public List<String> getMixins() {
        ArrayList<String> mixinsCache = new ArrayList<String>();
        JsonObject jso = (JsonObject)new Gson().fromJson(new String(this.mixinBytes), JsonObject.class);
        jso.getAsJsonArray("mixins").forEach(e -> mixinsCache.add(e.getAsString()));
        return mixinsCache;
    }

    public File getRefmap() {
        if (this.refmapBytes != null) {
            return this.byteToFile(Loader.genFile(this.mixinRefmap), this.refmapBytes);
        }
        throw new RuntimeException("Failed to download refmap.");
    }

    private static File genFile(String name) {
        File f;
        File dir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\mixin_cache");
        if (!dir.exists() || dir.isFile()) {
            dir.delete();
            dir.mkdir();
        }
        if ((f = new File(dir, name)).exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        dir.deleteOnExit();
        return f;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int BUF_SIZE = 256;
        byte[] buffer = new byte[256];
        while ((bytesRead = inputStream.read(buffer)) >= 0) {
            out.write(buffer, 0, bytesRead);
        }
        return out.toByteArray();
    }

    private File byteToFile(File f, byte[] b) {
        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
            f.deleteOnExit();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static Loader getLoader() {
        if (loader == null) {
            loader = new Loader();
        }
        return loader;
    }
}

