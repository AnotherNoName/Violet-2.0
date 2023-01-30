//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 */
package me.ninethousand.violet.client.util;

import java.util.List;
import me.ninethousand.violet.client.api.render.VioletRenderer;
import me.ninethousand.violet.client.mixin.ICPacketUseEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Constants {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final VioletRenderer renderer = new VioletRenderer();
    public static final float VANILLA_STEP_HEIGHT = 0.625f;

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(mc);
    }

    public static boolean nullCheck() {
        return Constants.mc.player == null || Constants.mc.world == null;
    }

    public static EntityPlayerSP player() {
        return Constants.mc.player;
    }

    public static WorldClient world() {
        return Constants.mc.world;
    }

    public static List<EntityPlayer> playerEntities() {
        return Constants.world().playerEntities;
    }

    public static List<Entity> loadedEntityList() {
        return Constants.world().loadedEntityList;
    }

    public static double getHealthPoints(EntityLivingBase entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static void sendPacket(Packet<?> packet) {
        Constants.player().connection.sendPacket(packet);
    }

    public static CPacketUseEntity attackEntityPacket(int id) {
        CPacketUseEntity cPacketUseEntity = new CPacketUseEntity();
        ((ICPacketUseEntity)cPacketUseEntity).setEntityId(id);
        ((ICPacketUseEntity)cPacketUseEntity).setAction(CPacketUseEntity.Action.ATTACK);
        return cPacketUseEntity;
    }

    private Constants() {
        throw new UnsupportedOperationException();
    }
}

