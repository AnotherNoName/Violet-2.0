/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 */
package me.ninethousand.violet.client.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.ninethousand.violet.client.impl.events.PacketEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetworkManager.class})
public class MixinNetworkManager {
    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void sendPacketHead(Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Write packetWriteEvent = new PacketEvent.Write(packet);
        EventManager.get().post(packetWriteEvent);
        if (packetWriteEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"channelRead0*"}, at={@At(value="HEAD")}, cancellable=true)
    public void channelRead0Head(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Read packetReadEvent = new PacketEvent.Read(packet);
        EventManager.get().post(packetReadEvent);
        if (packetReadEvent.isCancelled()) {
            ci.cancel();
        }
    }
}

