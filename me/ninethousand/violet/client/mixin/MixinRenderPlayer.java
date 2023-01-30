//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.RenderPlayer
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.RotationRenderEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    private float renderPitch;
    private float renderYaw;
    private float renderHeadYaw;
    private float prevRenderHeadYaw;
    private float prevRenderPitch;
    private float prevRenderYawOffset;
    private float prevPrevRenderYawOffset;

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    private void doRenderPre(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Constants.mc.player.equals((Object)entity)) {
            this.prevRenderHeadYaw = entity.prevRotationYawHead;
            this.prevRenderPitch = entity.prevRotationPitch;
            this.renderPitch = entity.rotationPitch;
            this.renderYaw = entity.rotationYaw;
            this.renderHeadYaw = entity.rotationYawHead;
            this.prevPrevRenderYawOffset = entity.prevRenderYawOffset;
            this.prevRenderYawOffset = entity.renderYawOffset;
            RotationRenderEvent rotationRenderEvent = new RotationRenderEvent();
            EventManager.get().post(rotationRenderEvent);
            if (rotationRenderEvent.isCancelled()) {
                entity.rotationYaw = rotationRenderEvent.getYaw();
                entity.rotationYawHead = rotationRenderEvent.getYaw();
                entity.prevRotationYawHead = rotationRenderEvent.getYaw();
                entity.prevRenderYawOffset = rotationRenderEvent.getYaw();
                entity.renderYawOffset = rotationRenderEvent.getYaw();
                entity.rotationPitch = rotationRenderEvent.getPitch();
                entity.prevRotationPitch = rotationRenderEvent.getPitch();
            }
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    private void doRenderPost(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Constants.mc.player.equals((Object)entity)) {
            entity.rotationPitch = this.renderPitch;
            entity.rotationYaw = this.renderYaw;
            entity.rotationYawHead = this.renderHeadYaw;
            entity.prevRotationYawHead = this.prevRenderHeadYaw;
            entity.prevRotationPitch = this.prevRenderPitch;
            entity.renderYawOffset = this.prevRenderYawOffset;
            entity.prevRenderYawOffset = this.prevPrevRenderYawOffset;
        }
    }
}

