//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.util.minecraft;

import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AngleUtil {
    public static Rotation calculateAngles(Vec3d to) {
        float yaw = (float)(Math.toDegrees(Math.atan2(to.subtract((Vec3d)Constants.mc.player.getPositionEyes((float)1.0f)).zCoord, to.subtract((Vec3d)Constants.mc.player.getPositionEyes((float)1.0f)).xCoord)) - 90.0);
        float pitch = (float)Math.toDegrees(-Math.atan2(to.subtract((Vec3d)Constants.mc.player.getPositionEyes((float)1.0f)).yCoord, Math.hypot(to.subtract((Vec3d)Constants.mc.player.getPositionEyes((float)1.0f)).xCoord, to.subtract((Vec3d)Constants.mc.player.getPositionEyes((float)1.0f)).zCoord)));
        return new Rotation(MathHelper.wrapDegrees((float)yaw), MathHelper.wrapDegrees((float)pitch));
    }

    public static Vec3d getVectorForRotation(Rotation rotation) {
        float yawCos = MathHelper.cos((float)(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI));
        float yawSin = MathHelper.sin((float)(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI));
        float pitchCos = -MathHelper.cos((float)(-rotation.getPitch() * ((float)Math.PI / 180)));
        float pitchSin = MathHelper.sin((float)(-rotation.getPitch() * ((float)Math.PI / 180)));
        return new Vec3d((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
    }
}

