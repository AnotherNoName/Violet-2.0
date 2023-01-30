//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.mixin;

import com.mojang.authlib.GameProfile;
import me.ninethousand.violet.client.impl.events.MotionUpdateEvent;
import me.ninethousand.violet.client.impl.events.PlayerMoveEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP
extends AbstractClientPlayer {
    @Shadow
    protected Minecraft mc;
    @Shadow
    private boolean prevOnGround;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private boolean autoJumpEnabled;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private boolean serverSneakState;

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    public void onUpdateMovingPlayer(CallbackInfo info) {
        MotionUpdateEvent motionUpdateEvent = new MotionUpdateEvent();
        EventManager.get().post(motionUpdateEvent);
        if (motionUpdateEvent.isCancelled()) {
            boolean sneakUpdate;
            info.cancel();
            ++this.positionUpdateTicks;
            boolean sprintUpdate = this.isSprinting();
            if (sprintUpdate != this.serverSprintState) {
                if (sprintUpdate) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_SPRINTING));
                } else {
                    this.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.STOP_SPRINTING));
                }
                this.serverSprintState = sprintUpdate;
            }
            if ((sneakUpdate = this.isSneaking()) != this.serverSneakState) {
                if (sneakUpdate) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_SNEAKING));
                } else {
                    this.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.STOP_SNEAKING));
                }
                this.serverSneakState = sneakUpdate;
            }
            if (this.isCurrentViewEntity()) {
                boolean rotationUpdate;
                boolean movementUpdate = StrictMath.pow(motionUpdateEvent.getX() - this.lastReportedPosX, 2.0) + StrictMath.pow(motionUpdateEvent.getY() - this.lastReportedPosY, 2.0) + StrictMath.pow(motionUpdateEvent.getZ() - this.lastReportedPosZ, 2.0) > 9.0E-4 || this.positionUpdateTicks >= 20;
                boolean bl = rotationUpdate = (double)(motionUpdateEvent.getYaw() - this.lastReportedYaw) != 0.0 || (double)(motionUpdateEvent.getPitch() - this.lastReportedPitch) != 0.0;
                if (this.isRiding()) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(this.motionX, -999.0, this.motionZ, motionUpdateEvent.getYaw(), motionUpdateEvent.getPitch(), motionUpdateEvent.getOnGround()));
                    movementUpdate = false;
                } else if (movementUpdate && rotationUpdate) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(motionUpdateEvent.getX(), motionUpdateEvent.getY(), motionUpdateEvent.getZ(), motionUpdateEvent.getYaw(), motionUpdateEvent.getPitch(), motionUpdateEvent.getOnGround()));
                } else if (movementUpdate) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(motionUpdateEvent.getX(), motionUpdateEvent.getY(), motionUpdateEvent.getZ(), motionUpdateEvent.getOnGround()));
                } else if (rotationUpdate) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(motionUpdateEvent.getYaw(), motionUpdateEvent.getPitch(), motionUpdateEvent.getOnGround()));
                } else if (this.prevOnGround != motionUpdateEvent.getOnGround()) {
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer(motionUpdateEvent.getOnGround()));
                }
                if (movementUpdate) {
                    this.lastReportedPosX = motionUpdateEvent.getX();
                    this.lastReportedPosY = motionUpdateEvent.getY();
                    this.lastReportedPosZ = motionUpdateEvent.getZ();
                    this.positionUpdateTicks = 0;
                }
                if (rotationUpdate) {
                    this.lastReportedYaw = motionUpdateEvent.getYaw();
                    this.lastReportedPitch = motionUpdateEvent.getPitch();
                }
                this.prevOnGround = motionUpdateEvent.getOnGround();
                this.autoJumpEnabled = this.mc.gameSettings.autoJump;
            }
        }
    }

    @Redirect(method={"move"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer instance, MoverType moverType, double x, double y, double z) {
        if (instance == this.mc.player) {
            PlayerMoveEvent.Pre preMoveEvent = new PlayerMoveEvent.Pre(moverType, x, y, z);
            EventManager.get().post(preMoveEvent);
            double beforeX = this.posX;
            double beforeY = this.posY;
            double beforeZ = this.posZ;
            super.moveEntity(preMoveEvent.getType(), preMoveEvent.getMotionX(), preMoveEvent.getMotionY(), preMoveEvent.getMotionZ());
            PlayerMoveEvent.Post postMoveEvent = new PlayerMoveEvent.Post(this.posX - beforeX, this.posY - beforeY, this.posZ - beforeZ);
            EventManager.get().post(postMoveEvent);
        } else {
            super.moveEntity(moverType, x, y, z);
        }
    }
}

