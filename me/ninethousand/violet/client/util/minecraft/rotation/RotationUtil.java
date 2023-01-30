//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.util.minecraft.rotation;

import me.ninethousand.violet.client.impl.managers.RotationManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.AngleUtil;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    public static boolean rotateToTargetVecStepped(Vec3d targetVec, float maxStep, Rotation.Rotate rotateType) {
        if (targetVec == null || maxStep <= 0.0f || rotateType == Rotation.Rotate.NONE) {
            return false;
        }
        Rotation rotation = AngleUtil.calculateAngles(targetVec);
        Rotation serverRotation = RotationManager.get().getServerRotation();
        rotation.setYaw(serverRotation.getYaw() + MathHelper.wrapDegrees((float)(rotation.getYaw() - serverRotation.getYaw())));
        rotation.setPitch(serverRotation.getPitch() + (rotation.getPitch() - serverRotation.getPitch()));
        if (rotateType.equals((Object)Rotation.Rotate.CLIENT)) {
            Constants.mc.player.rotationYaw = rotation.getYaw();
            Constants.mc.player.rotationYawHead = rotation.getYaw();
            Constants.mc.player.rotationPitch = rotation.getPitch();
        }
        RotationManager.get().addRotation(rotation, Integer.MAX_VALUE);
        return true;
    }

    private RotationUtil() {
        throw new UnsupportedOperationException();
    }
}

