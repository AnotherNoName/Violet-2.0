//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.ninethousand.violet.client.impl.modules.player;

import java.awt.Color;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.render.VioletRenderer;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.PlayerDamageBlockEvent;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.managers.RotationManager;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.AngleUtil;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

@Module.Manifest(value=Module.Category.PLAYER, description="Packets make blocks go bye bye")
public class PacketMine
extends Module {
    @Setting
    private Rotation.Rotate rotate = Rotation.Rotate.NONE;
    @Setting
    private Color color = new Color(-762246256, true);
    private BlockPos currentBlock = null;
    private EnumFacing currentDirection = EnumFacing.UP;
    private static PacketMine instance;

    @Listener
    private void listen(UpdateEvent event) {
        Block block;
        if (this.currentBlock != null && (block = Constants.mc.world.getBlockState(this.currentBlock).getBlock()) == Blocks.AIR) {
            this.currentBlock = null;
        }
        if (this.currentBlock != null) {
            Rotation rotation = AngleUtil.calculateAngles(BlockUtil.getCenter(this.currentBlock));
            RotationManager.get().addRotation(new Rotation(rotation.getYaw(), rotation.getPitch(), this.rotate));
        }
    }

    @Listener
    private void listen(PlayerDamageBlockEvent event) {
        if (Constants.mc.world.getBlockState(event.pos()).getBlock() == Blocks.BEDROCK || Constants.nullCheck()) {
            return;
        }
        this.currentBlock = event.pos();
        Constants.mc.player.swingArm(EnumHand.MAIN_HAND);
        this.currentBlock = event.pos();
        this.currentDirection = event.direction();
        Constants.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.currentBlock, this.currentDirection));
        Constants.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.currentBlock, this.currentDirection));
        event.cancel();
    }

    @Listener
    private void listen(Render3DEvent event) {
        if (this.currentBlock != null) {
            Constants.renderer.renderBlockPos(this.currentBlock, this.color, VioletRenderer.BoxMode.FilledBounded);
        }
    }

    public static PacketMine getInstance() {
        if (instance == null) {
            instance = new PacketMine();
        }
        return instance;
    }
}

