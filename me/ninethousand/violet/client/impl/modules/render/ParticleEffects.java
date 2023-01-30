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
import me.ninethousand.violet.client.impl.events.EntityDamagedEvent;
import me.ninethousand.violet.client.util.Constants;
import net.minecraft.util.EnumParticleTypes;

@Module.Manifest(value=Module.Category.RENDER, description="Render particles differently")
public class ParticleEffects
extends Module {
    @Setting
    private EnumParticleTypes hit = EnumParticleTypes.HEART;
    private static ParticleEffects instance;

    @Listener
    private void listen(EntityDamagedEvent event) {
        for (int i = 0; i < 20; ++i) {
            Constants.mc.effectRenderer.spawnEffectParticle(this.hit.getParticleID(), event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, this.getRandomVelocity(), this.getRandomVelocity(), this.getRandomVelocity(), new int[0]);
        }
    }

    private double getRandomVelocity() {
        return -1.0 + Math.random() * 2.0;
    }

    public static ParticleEffects getInstance() {
        if (instance == null) {
            instance = new ParticleEffects();
        }
        return instance;
    }
}

