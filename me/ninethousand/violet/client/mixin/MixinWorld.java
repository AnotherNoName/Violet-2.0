/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package me.ninethousand.violet.client.mixin;

import me.ninethousand.violet.client.impl.events.SpawnParticleEvent;
import me.ninethousand.violet.client.impl.managers.EventManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={World.class})
public abstract class MixinWorld {
    @Inject(method={"spawnParticle(IZDDDDDD[I)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void spawnParticleHead(int particleID, boolean ignoreRange, double xCood, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters, CallbackInfo ci) {
        SpawnParticleEvent spawnParticleEvent = new SpawnParticleEvent(particleID, new Vec3d(xCood, yCoord, zCoord));
        EventManager.get().post(spawnParticleEvent);
        if (spawnParticleEvent.isCancelled()) {
            ci.cancel();
        }
    }
}

