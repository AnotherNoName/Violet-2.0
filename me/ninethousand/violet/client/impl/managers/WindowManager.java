//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  org.lwjgl.opengl.Display
 */
package me.ninethousand.violet.client.impl.managers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.imageio.ImageIO;
import me.ninethousand.violet.client.api.manager.Manager;
import net.minecraft.util.Util;
import org.lwjgl.opengl.Display;

public class WindowManager
implements Manager {
    private static WindowManager instance;

    private WindowManager() {
        this.setTitle("Violet 2.0".toUpperCase() + " | LEAN.CLUB | LEANGOD.CC | REAPSENSE.GOD V3");
        this.setIcon();
    }

    public void setTitle(String title) {
        Display.setTitle((String)title);
    }

    public void setIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = this.getClass().getResourceAsStream("/assets/minecraft/violet/icons/lean16x.png");
                 InputStream inputStream32x = this.getClass().getResourceAsStream("/assets/minecraft/violet/icons/lean32x.png");){
                ByteBuffer[] icons = new ByteBuffer[]{this.readImageToBuffer(inputStream16x), this.readImageToBuffer(inputStream32x)};
                Display.setIcon((ByteBuffer[])icons);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        Arrays.stream(aint).map(i -> i << 8 | i >> 24 & 0xFF).forEach(bytebuffer::putInt);
        bytebuffer.flip();
        return bytebuffer;
    }

    public static WindowManager get() {
        if (instance == null) {
            instance = new WindowManager();
        }
        return instance;
    }
}

