//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.managers;

import java.util.TreeMap;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.impl.events.MotionUpdateEvent;
import me.ninethousand.violet.client.impl.events.PacketEvent;
import me.ninethousand.violet.client.impl.events.RotationRenderEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import me.ninethousand.violet.client.mixin.ICPacketPlayer;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.AngleUtil;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationManager
implements Manager {
    private final TreeMap<Integer, Rotation> rotationMap = new TreeMap();
    private final Rotation serverRotation = new Rotation(Float.NaN, Float.NaN);
    private final Rotation lastSentRotation = new Rotation(Float.NaN, Float.NaN);
    private Rotation renderedRotation;
    private static RotationManager instance;

    private RotationManager() {
        EventManager.get().register(this);
    }

    @Listener
    private void listen(PacketEvent.Write event) {
        CPacketPlayer packet;
        if (event.getPacket() instanceof CPacketPlayer && ((ICPacketPlayer)(packet = (CPacketPlayer)event.getPacket())).isRotating()) {
            this.serverRotation.setYaw(packet.getYaw(0.0f));
            this.serverRotation.setPitch(packet.getPitch(0.0f));
        }
    }

    @Listener
    private void listen(MotionUpdateEvent event) {
        if (!this.rotationMap.isEmpty()) {
            Rotation rotation = this.rotationMap.lastEntry().getValue();
            event.cancel();
            event.setOnGround(Constants.mc.player.onGround);
            event.setX(Constants.mc.player.posX);
            event.setY(Constants.mc.player.getEntityBoundingBox().minY);
            event.setZ(Constants.mc.player.posZ);
            rotation = this.update(rotation);
            event.setYaw(rotation.getYaw());
            event.setPitch(rotation.getPitch());
            this.lastSentRotation.setYaw(rotation.getYaw());
            this.lastSentRotation.setPitch(rotation.getPitch());
            this.renderedRotation = rotation;
            this.rotationMap.clear();
        } else if (this.isBackAtNormal(this.getRealRotation(), 0.0f)) {
            Rotation real = this.getRealRotation();
            Rotation rotation = new Rotation(real.getYaw(), real.getPitch(), Rotation.Rotate.PACKET, 10.0f);
            event.cancel();
            event.setOnGround(Constants.mc.player.onGround);
            event.setX(Constants.mc.player.posX);
            event.setY(Constants.mc.player.getEntityBoundingBox().minY);
            event.setZ(Constants.mc.player.posZ);
            rotation = this.update(rotation);
            event.setYaw(rotation.getYaw());
            event.setPitch(rotation.getPitch());
            this.lastSentRotation.setYaw(rotation.getYaw());
            this.lastSentRotation.setPitch(rotation.getPitch());
            this.renderedRotation = rotation;
            this.rotationMap.clear();
        } else {
            this.renderedRotation = null;
        }
    }

    @Listener
    public void onRotationRenderEvent(RotationRenderEvent event) {
        if (this.renderedRotation != null) {
            event.cancel();
            event.setYaw(this.renderedRotation.getYaw());
            event.setPitch(this.renderedRotation.getPitch());
        }
    }

    private Rotation update(Rotation rotation) {
        if (!rotation.isInstant()) {
            int pitchFactor;
            float yawDifference = MathHelper.wrapDegrees((float)(rotation.getYaw() - this.serverRotation.getYaw()));
            float pitchDifference = rotation.getPitch() - this.serverRotation.getPitch();
            int yawFactor = yawDifference < 0.0f ? -1 : 1;
            int n = pitchFactor = pitchDifference < 0.0f ? -1 : 1;
            if (Math.sqrt(yawDifference * yawDifference) > (double)rotation.getStep()) {
                yawDifference = (float)yawFactor * rotation.getStep();
            }
            if (Math.sqrt(pitchDifference * pitchDifference) > (double)rotation.getStep()) {
                pitchDifference = (float)pitchFactor * rotation.getStep();
            }
            rotation.setYaw(this.serverRotation.getYaw() + yawDifference);
            rotation.setPitch(this.serverRotation.getPitch() + pitchDifference);
        }
        return rotation;
    }

    public Rotation getRealRotation() {
        return new Rotation(Constants.mc.player.rotationYaw, Constants.mc.player.rotationPitch);
    }

    public boolean isBackAtNormal(Rotation rotation, float error) {
        float yawDifference = MathHelper.wrapDegrees((float)(rotation.getYaw() - this.lastSentRotation.getYaw()));
        float pitchDifference = rotation.getPitch() - this.lastSentRotation.getPitch();
        return yawDifference <= error && pitchDifference <= error;
    }

    public boolean hasRotated(Rotation rotation, float error) {
        float yawDifference = MathHelper.wrapDegrees((float)(rotation.getYaw() - this.serverRotation.getYaw()));
        float pitchDifference = rotation.getPitch() - this.serverRotation.getPitch();
        return yawDifference <= error && pitchDifference <= error;
    }

    public boolean hasRotated(Vec3d to, float error) {
        Rotation rotation = AngleUtil.calculateAngles(to);
        float yawDifference = MathHelper.wrapDegrees((float)(rotation.getYaw() - this.serverRotation.getYaw()));
        float pitchDifference = rotation.getPitch() - this.serverRotation.getPitch();
        return yawDifference <= error && pitchDifference <= error;
    }

    public void addRotation(Vec3d to) {
        Rotation rotation = AngleUtil.calculateAngles(to);
        this.addRotation(new Rotation(rotation.getYaw(), rotation.getPitch(), Rotation.Rotate.PACKET));
    }

    public void addRotation(Rotation rotation) {
        this.addRotation(rotation, Integer.MAX_VALUE);
    }

    public void addRotation(Vec3d rotation, int priority) {
        this.addRotation(AngleUtil.calculateAngles(rotation), priority);
    }

    public void addRotation(Rotation rotation, int priority) {
        if (rotation.getRotation() == Rotation.Rotate.NONE) {
            return;
        }
        this.rotationMap.put(priority, rotation);
    }

    public Rotation getServerRotation() {
        return this.serverRotation;
    }

    public static RotationManager get() {
        if (instance == null) {
            instance = new RotationManager();
        }
        return instance;
    }
}

