//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.util.minecraft;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.ninethousand.violet.client.api.render.vector.VectorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static BlockPos getEntityPos(Entity entity) {
        return new BlockPos(Math.floor(entity.posX), Math.floor(entity.posY + 0.25), Math.floor(entity.posZ));
    }

    public static float getHealth(EntityLivingBase entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static ChatFormatting getPlayerHPColor(float hp) {
        if (hp > 18.0f) {
            return ChatFormatting.GREEN;
        }
        if (hp > 16.0f) {
            return ChatFormatting.DARK_GREEN;
        }
        if (hp > 12.0f) {
            return ChatFormatting.YELLOW;
        }
        if (hp > 16.0f) {
            return ChatFormatting.RED;
        }
        return ChatFormatting.DARK_RED;
    }

    public static boolean isPlayerMoving() {
        return EntityUtil.mc.gameSettings.keyBindForward.isKeyDown() || EntityUtil.mc.gameSettings.keyBindLeft.isKeyDown() || EntityUtil.mc.gameSettings.keyBindBack.isKeyDown() || EntityUtil.mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static double getPlayerSpeed() {
        return VectorUtil.length(new Vec2f((float)EntityUtil.mc.player.motionX, (float)EntityUtil.mc.player.motionZ));
    }

    public static double getPlayerSpeedMs() {
        return EntityUtil.getPlayerSpeed() * 72.0;
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.xCoord - from.xCoord;
        double difY = (to.yCoord - from.yCoord) * -1.0;
        double difZ = to.zCoord - from.zCoord;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static Vec2f getInstantLookVec(Vec3d looking, Vec3d target) {
        double difX = target.xCoord - looking.xCoord;
        double difY = (target.yCoord - looking.yCoord) * -1.0;
        double difZ = target.zCoord - looking.zCoord;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new Vec2f((float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist))), (float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)));
    }

    private static float updateRotation(float current, float target, float max) {
        float f = MathHelper.wrapDegrees((float)(target - current));
        if (f > max) {
            f = max;
        }
        if (f < -max) {
            f = -max;
        }
        return current + f;
    }

    public static Vec3d getEyePos(Entity entity) {
        return new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
    }

    public static Vec3d getCenterPos(Entity entity) {
        return new Vec3d(entity.posX, entity.posY + (double)entity.height / 2.0, entity.posZ);
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
        return EntityUtil.getInterpolatedPos(entity, ticks).subtract(Minecraft.getMinecraft().getRenderManager().viewerPosX, Minecraft.getMinecraft().getRenderManager().viewerPosY, Minecraft.getMinecraft().getRenderManager().viewerPosZ);
    }

    public static Vec3d getInterpolatedPos(Entity entity, double ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(EntityUtil.getLastTickPos(entity, ticks, ticks, ticks));
    }

    public static Vec3d getLastTickPos(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
}

