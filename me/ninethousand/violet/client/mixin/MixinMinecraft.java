//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.UpdateEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    public EntityPlayerSP player;
    @Shadow
    public WorldClient world;

    @Inject(method={"runTick"}, at={@At(value="HEAD")})
    public void runTickHead(CallbackInfo ci) {
        if (this.player != null && this.world != null) {
            UpdateEvent event = new UpdateEvent();
            EventManager.get().post(event);
        }
    }
}

