//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingAttackEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.ninethousand.violet.client.impl.managers;

import me.ninethousand.violet.client.api.event.handler.EventHandler;
import me.ninethousand.violet.client.api.event.handler.EventHandlerImpl;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.impl.events.ChatEvent;
import me.ninethousand.violet.client.impl.events.EntityDamagedEvent;
import me.ninethousand.violet.client.impl.events.PacketEvent;
import me.ninethousand.violet.client.impl.events.Render2DEvent;
import me.ninethousand.violet.client.impl.events.Render3DEvent;
import me.ninethousand.violet.client.impl.events.TotemPopEvent;
import me.ninethousand.violet.client.mixin.ICPacketChatMessage;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventManager
implements Manager {
    private final EventHandler eventHandler = new EventHandlerImpl();
    private static EventManager instance;

    private EventManager() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.register(this);
    }

    public void register(Object object) {
        this.eventHandler.register(object);
    }

    public void unregister(Object object) {
        this.eventHandler.unregister(object);
    }

    public void register(Class<?> clazz) {
        this.eventHandler.register(clazz);
    }

    public void unregister(Class<?> clazz) {
        this.eventHandler.unregister(clazz);
    }

    public boolean isRegistered(Object object) {
        return this.eventHandler.isRegistered(object);
    }

    public boolean isRegistered(Class<?> clazz) {
        return this.eventHandler.isRegistered(clazz);
    }

    public void attach(EventHandler handler) {
        this.eventHandler.attach(handler);
    }

    public void detach(EventHandler handler) {
        this.eventHandler.detach(handler);
    }

    public boolean isAttached(EventHandler handler) {
        return this.eventHandler.isAttached(handler);
    }

    public <T> void post(T object) {
        this.eventHandler.post(object);
    }

    @SubscribeEvent
    public void listen(RenderGameOverlayEvent.Text event) {
        if (Constants.nullCheck()) {
            return;
        }
        Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), Constants.getScaledResolution());
        this.post(render2DEvent);
    }

    @SubscribeEvent
    public void listen(RenderWorldLastEvent event) {
        if (Constants.nullCheck()) {
            return;
        }
        Render3DEvent render3DEvent = new Render3DEvent(event.getPartialTicks());
        this.post(render3DEvent);
    }

    @SubscribeEvent
    public void listen(ClientChatReceivedEvent event) {
        ChatEvent.Incoming incoming = new ChatEvent.Incoming(event.getMessage().getFormattedText());
        this.post(incoming);
        if (incoming.isCancelled()) {
            event.setCanceled(true);
        } else {
            event.setMessage((ITextComponent)new TextComponentString(incoming.getMessage()));
        }
    }

    @SubscribeEvent
    public void listen(LivingAttackEvent event) {
        EntityDamagedEvent entityDamagedEvent = new EntityDamagedEvent(event.getEntity(), event.getSource(), event.getAmount());
        this.post(entityDamagedEvent);
        if (entityDamagedEvent.isCancelled()) {
            event.setCanceled(true);
        }
    }

    @Listener
    public void listen(PacketEvent.Write event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage cPacketChatMessage = (CPacketChatMessage)event.getPacket();
            ChatEvent.Outgoing outgoing = new ChatEvent.Outgoing(cPacketChatMessage.getMessage());
            this.post(outgoing);
            if (outgoing.isCancelled()) {
                event.cancel();
            } else {
                ((ICPacketChatMessage)cPacketChatMessage).setMessage(outgoing.getMessage());
            }
        }
    }

    @Listener
    public void listen(PacketEvent.Read event) {
        SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35 && packet.getEntity((World)Constants.mc.world) instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)packet.getEntity((World)Constants.mc.world);
            this.post(new TotemPopEvent(player));
        }
    }

    public static EventManager get() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }
}

