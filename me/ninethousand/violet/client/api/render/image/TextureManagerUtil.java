//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.util.ResourceLocation
 */
package me.ninethousand.violet.client.api.render.image;

import me.ninethousand.violet.client.api.render.image.EfficientTexture;
import me.ninethousand.violet.client.mixin.ITextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class TextureManagerUtil {
    public static ResourceLocation getEfficientTextureResourceLocation(TextureManager manager, String name, EfficientTexture texture) {
        Integer integer = ((ITextureManager)manager).getMapTextureCounters().get(name);
        integer = integer == null ? Integer.valueOf(1) : Integer.valueOf(integer + 1);
        ((ITextureManager)manager).getMapTextureCounters().put(name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        manager.loadTexture(resourcelocation, (ITextureObject)texture);
        return resourcelocation;
    }
}

