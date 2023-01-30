//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.ninethousand.violet.client.api.render.misc;

import java.io.IOException;
import me.ninethousand.violet.client.api.render.image.GifConverter;
import me.ninethousand.violet.client.api.render.image.GifImage;
import me.ninethousand.violet.client.api.ui.theme.Theme;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ResourceUtil {
    public static void bindThemeResources(Theme theme) {
        if (theme.getBackgroundGif() != null) {
            GL11.glBindTexture((int)3553, (int)theme.getBackgroundGif().getDynamicTexture().getGlTextureId());
        } else if (theme.getBackgroundImage() != null) {
            Constants.mc.getTextureManager().bindTexture(theme.getBackgroundImage());
        }
    }

    public static GifImage getGif(String name) {
        GifImage gif = null;
        try {
            gif = GifConverter.readGifImage(ResourceUtil.class.getResourceAsStream("/assets/minecraft/violet/gif/" + name + ".gif"), name + ".gif");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return gif;
    }

    public static ResourceLocation getIcon(String iconName) {
        return new ResourceLocation("violet/icons/" + iconName + ".png");
    }

    public static ResourceLocation getBackground(String backgroundName) {
        return new ResourceLocation("violet/backgrounds/" + backgroundName + ".png");
    }

    private ResourceUtil() {
        throw new UnsupportedOperationException();
    }
}

