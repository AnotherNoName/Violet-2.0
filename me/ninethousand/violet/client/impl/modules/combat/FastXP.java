//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.modules.combat;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.RotationUpdateEvent;
import me.ninethousand.violet.client.util.Constants;
import me.ninethousand.violet.client.util.minecraft.BlockUtil;
import me.ninethousand.violet.client.util.minecraft.rotation.Rotation;
import me.ninethousand.violet.client.util.minecraft.rotation.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Manifest(value=Module.Category.COMBAT, description="Throw xp faster")
public class FastXP
extends Module {
    @Setting(name="FootXP")
    private boolean feetXp = false;
    private static FastXP instance;

    @Listener(priority=1000)
    private void listen(RotationUpdateEvent event) {
        if (this.feetXp) {
            event.cancel();
            Vec3d feet = BlockUtil.blockPosToVec(new BlockPos((Entity)Constants.mc.player));
            RotationUtil.rotateToTargetVecStepped(feet, 20.0f, Rotation.Rotate.PACKET);
        }
    }

    public static FastXP getInstance() {
        if (instance == null) {
            instance = new FastXP();
        }
        return instance;
    }
}

