//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumParticleTypes
 */
package me.ninethousand.violet.client.impl.modules.render;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.RenderArmorEvent;
import me.ninethousand.violet.client.impl.events.SpawnParticleEvent;
import net.minecraft.util.EnumParticleTypes;

@Module.Manifest(value=Module.Category.RENDER, description="Modify what the game renders")
public class NoRender
extends Module {
    @Setting
    private boolean noExplosions = true;
    @Setting
    private boolean noArmor = true;
    private static NoRender instance;

    @Listener
    private void listen(SpawnParticleEvent event) {
        if (event.getParticleId() == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() || event.getParticleId() == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() || event.getParticleId() == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() || event.getParticleId() == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && this.noExplosions) {
            event.cancel();
        }
    }

    @Listener
    private void listen(RenderArmorEvent event) {
        if (this.noArmor) {
            event.cancel();
        }
    }

    public static NoRender getInstance() {
        if (instance == null) {
            instance = new NoRender();
        }
        return instance;
    }
}

