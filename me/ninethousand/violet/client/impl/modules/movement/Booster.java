//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.MobEffects
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 */
package me.ninethousand.violet.client.impl.modules.movement;

import java.util.List;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.LagbackEvent;
import me.ninethousand.violet.client.impl.events.PlayerMoveEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.misc.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

@Module.Manifest(value=Module.Category.MOVEMENT, description="Gives the player the maximum velocity")
public class Booster
extends Module {
    @Setting(min=0.1, max=50.0)
    private float speed = 20.0f;
    @Setting
    private boolean strictSpeed = false;
    @Setting
    private boolean autoJump = false;
    @Setting
    private boolean lagback = false;
    @Setting(min=1.0, max=1000.0)
    private int lagbackPause = 60;
    private Timer lagbackTimer = new Timer();
    private static Booster instance;

    @Listener
    public void listen(PlayerMoveEvent.Pre event) {
        if (Constants.mc.player.isSneaking() || Constants.mc.player.isInWater() || Constants.mc.player.isInLava()) {
            return;
        }
        float forward = Constants.mc.player.movementInput.field_192832_b;
        float strafe = Constants.mc.player.movementInput.moveStrafe;
        float yaw = Constants.mc.player.rotationYaw + 90.0f;
        if (forward == 0.0f && strafe == 0.0f) {
            return;
        }
        if (forward != 0.0f && strafe != 0.0f) {
            yaw = strafe > 0.0f ? (yaw += -45.0f * forward) : (yaw += 45.0f * forward);
            strafe = 0.0f;
        }
        float maxMotion = this.speed / 72.0f;
        if (this.lagback && !this.lagbackTimer.passed(this.lagbackPause) || this.strictSpeed) {
            maxMotion = 0.2873f;
        }
        if (Constants.mc.player.isPotionActive(MobEffects.SPEED)) {
            maxMotion = (float)((double)maxMotion * (1.0 + 0.2 * (double)(Constants.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1)));
        }
        float maxForward = maxMotion * forward;
        float maxStrafe = maxMotion * strafe;
        double sin = Math.sin(Math.toRadians(yaw));
        double cos = Math.cos(Math.toRadians(yaw));
        double calculatedMotionX = (double)maxForward * cos + (double)maxStrafe * sin;
        double calculatedMotionZ = (double)maxForward * sin - (double)maxStrafe * cos;
        event.setMotionX(calculatedMotionX);
        event.setMotionZ(calculatedMotionZ);
        if (!this.autoJump || !Constants.mc.player.onGround) {
            return;
        }
        Vec2f vec2f = Constants.mc.player.movementInput.getMoveVector();
        if (vec2f.x != 0.0f || vec2f.y != 0.0f) {
            IBlockState iblockstate1;
            Vec3d vec3d = new Vec3d(Constants.mc.player.posX, Constants.mc.player.getEntityBoundingBox().minY, Constants.mc.player.posZ);
            double d0 = Constants.mc.player.posX + event.getMotionX();
            double d1 = Constants.mc.player.posZ + event.getMotionZ();
            Vec3d vec3d1 = new Vec3d(d0, Constants.mc.player.getEntityBoundingBox().minY, d1);
            Vec3d vec3d2 = new Vec3d(event.getMotionX(), 0.0, event.getMotionZ());
            float f = Constants.mc.player.getAIMoveSpeed();
            float f1 = (float)vec3d2.lengthSquared();
            if (f1 <= 0.001f) {
                float f2 = f * vec2f.x;
                float f3 = f * vec2f.y;
                float f4 = MathHelper.sin((float)(Constants.mc.player.rotationYaw * ((float)Math.PI / 180)));
                float f5 = MathHelper.cos((float)(Constants.mc.player.rotationYaw * ((float)Math.PI / 180)));
                vec3d2 = new Vec3d((double)(f2 * f5 - f3 * f4), vec3d2.yCoord, (double)(f3 * f5 + f2 * f4));
                f1 = (float)vec3d2.lengthSquared();
                if (f1 <= 0.001f) {
                    return;
                }
            }
            float f12 = (float)MathHelper.fastInvSqrt((double)f1);
            Vec3d vec3d12 = vec3d2.scale((double)f12);
            BlockPos blockpos = new BlockPos(Constants.mc.player.posX, Constants.mc.player.getEntityBoundingBox().maxY, Constants.mc.player.posZ);
            IBlockState iblockstate = Constants.mc.player.world.getBlockState(blockpos);
            if (iblockstate.getCollisionBoundingBox((IBlockAccess)Constants.mc.player.world, blockpos) == null && (iblockstate1 = Constants.mc.player.world.getBlockState(blockpos = blockpos.up())).getCollisionBoundingBox((IBlockAccess)Constants.mc.player.world, blockpos) == null) {
                float f14;
                float f7 = 1.2f;
                if (Constants.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    f7 += (float)(Constants.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75f;
                }
                float f8 = Math.max(Constants.mc.player.getAIMoveSpeed() * 7.0f, 1.0f / f12);
                Vec3d vec3d4 = vec3d1.add(vec3d12.scale((double)f8));
                float f9 = Constants.mc.player.width;
                float f10 = Constants.mc.player.height;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(vec3d, vec3d4.addVector(0.0, (double)f10, 0.0)).expand((double)f9, 0.0, (double)f9);
                Vec3d lvt_19_1_ = vec3d.addVector(0.0, (double)0.51f, 0.0);
                vec3d4 = vec3d4.addVector(0.0, (double)0.51f, 0.0);
                Vec3d vec3d5 = vec3d12.crossProduct(new Vec3d(0.0, 1.0, 0.0));
                Vec3d vec3d6 = vec3d5.scale((double)(f9 * 0.5f));
                Vec3d vec3d7 = lvt_19_1_.subtract(vec3d6);
                Vec3d vec3d8 = vec3d4.subtract(vec3d6);
                Vec3d vec3d9 = lvt_19_1_.add(vec3d6);
                Vec3d vec3d10 = vec3d4.add(vec3d6);
                List list = Constants.mc.player.world.getCollisionBoxes((Entity)Constants.mc.player, axisalignedbb);
                if (!list.isEmpty()) {
                    // empty if block
                }
                float f11 = Float.MIN_VALUE;
                for (AxisAlignedBB axisalignedbb2 : list) {
                    if (!axisalignedbb2.intersects(vec3d7, vec3d8) && !axisalignedbb2.intersects(vec3d9, vec3d10)) continue;
                    f11 = (float)axisalignedbb2.maxY;
                    Vec3d vec3d11 = axisalignedbb2.getCenter();
                    BlockPos blockpos1 = new BlockPos(vec3d11);
                    int i = 1;
                    while (!((float)i >= f7)) {
                        IBlockState iblockstate3;
                        BlockPos blockpos2 = blockpos1.up(i);
                        IBlockState iblockstate2 = Constants.mc.player.world.getBlockState(blockpos2);
                        AxisAlignedBB axisalignedbb1 = iblockstate2.getCollisionBoundingBox((IBlockAccess)Constants.mc.player.world, blockpos2);
                        if (axisalignedbb1 != null && (double)(f11 = (float)axisalignedbb1.maxY + (float)blockpos2.getY()) - Constants.mc.player.getEntityBoundingBox().minY > (double)f7) {
                            return;
                        }
                        if (i > 1 && (iblockstate3 = Constants.mc.player.world.getBlockState(blockpos = blockpos.up())).getCollisionBoundingBox((IBlockAccess)Constants.mc.player.world, blockpos) != null) {
                            return;
                        }
                        ++i;
                    }
                    break block0;
                }
                if (f11 != Float.MIN_VALUE && (f14 = (float)((double)f11 - Constants.mc.player.getEntityBoundingBox().minY)) > 0.5f && f14 <= f7) {
                    float boost = 0.372f;
                    event.setMotionY(boost);
                    Constants.mc.player.motionY = boost;
                }
            }
        }
    }

    @Listener
    public void listen(LagbackEvent event) {
        this.lagbackTimer.reset();
    }

    public static Booster getInstance() {
        if (instance == null) {
            instance = new Booster();
        }
        return instance;
    }
}

